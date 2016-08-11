package reader;

import global.Singleton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

import data.Course;
import data.Group;

public class JSONReader {

	private Group programme; 
	private ScheduleReader sReader;
	
	public JSONReader(Singleton s) {
		sReader = new ScheduleReader(s);
		programme = parseProgramme(new JSONObject(read(s.getJsonPath())));
	}
	
	private String read(String path) {
		String s = "";
		try {
			 BufferedReader br = new BufferedReader(new FileReader(path));
			 String line = br.readLine();
				while (line != null) {
					s += line;
					line = br.readLine();
				}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	private Group parseCourseGroup(JSONObject json, int stages) {
		Group group = new Group(json.getString("naam"), json.getString("groep"), json.getString("type"), 
				json.getInt("min_studiepunten"), json.getInt("max_studiepunten"), stages);
		parseCourses(group, json);
		return group;
	}
	
	private Group parseChoiceGroup(JSONObject json, int stages) {
		Group group = new Group(json.getString("naam"), json.getString("groep"), json.getString("type"), 
				0, 0, stages);
		parseCourses(group, json);
		return group;
	}
	
	private Group parseProgramme(JSONObject domain) {
		JSONObject json = domain.getJSONObject("opleiding");
		Group programme = new Group(json.getString("naam"), json.getString("groep"), json.getString("type"), 
				json.getInt("min_studiepunten"), json.getInt("max_studiepunten"), json.getInt("fases"));
		
		parseCourses(programme, json);
		
		for (Object groupV : json.getJSONArray("vakgroep"))
			programme.addCourseGroup(parseCourseGroup((JSONObject)groupV, programme.getStages()));
		
		for (Object groupK : json.getJSONArray("keuzegroep"))
			programme.addChoiceGroup(parseChoiceGroup((JSONObject)groupK, programme.getStages()));
		
		return programme;
	}
	
	private void parseCourses(Group group, JSONObject domain) {
		for (Object course : domain.getJSONObject("vakken").getJSONArray("verplicht")) {
			group.addMandatoryCourse(parseCourse((JSONObject)course));
		}
		for (Object course : domain.getJSONObject("vakken").getJSONArray("keuze")) {
			group.addOptionalCourse(parseCourse((JSONObject)course));
		}
	}
	
	private Course parseCourse(JSONObject domain) {
		ArrayList<Integer> stages = new ArrayList<Integer>();
		for (Object s : domain.getJSONArray("fases"))
			stages.add((int)s);
		Course course = new Course(domain.getString("code"), domain.getString("naam"), 
				domain.getInt("punten"), domain.getInt("semester"), stages,sReader.getSchedule(domain.getString("code")));
		if (domain.has("verbonden"))
			for (Object code : domain.getJSONArray("verbonden"))
				course.addConnection((String) code);
		if (domain.has("voorwaarde"))
			course.addCondition(1, domain.getString("voorwaarde"));
		if (domain.has("gelijktijdigheden")) {
			int count = 1;
			for (Object sim : domain.getJSONArray("gelijktijdigheden")) {
				String id = course.getCode() + "_sim" + count;
				for (Object code : (JSONArray)sim)
					course.addSimultaneousCourse(id, code.toString());
				count++;
			}
		}
		return course;
	}
	
	public Group getProgramme() {
		return programme;
	}
}
