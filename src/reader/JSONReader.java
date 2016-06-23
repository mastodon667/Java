package reader;

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
	
	public JSONReader(String path) {
		programme = parseProgramme(new JSONObject(read(path)));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	private Group parseProgramme(JSONObject domain) {
		JSONObject edj = domain.getJSONObject("opleiding");
		Group programme = new Group(edj.getString("naam"), "Opleiding", edj.getInt("min_studiepunten"), edj.getInt("max_studiepunten"), edj.getInt("fases"));
		parseCourses(programme, edj);
		
		JSONObject avod = edj.getJSONObject("avo");
		Group avo = new Group("Algemeen_Vormend", "AVO", avod.getInt("min_studiepunten"), avod.getInt("max_studiepunten"), edj.getInt("fases"));
		parseCourses(avo, avod);
		programme.addGroup(avo);
		
		JSONObject vsd = edj.getJSONObject("verdere_specialisatie");
		Group vs = new Group("Verdere", "Verdere_Specialisatie", vsd.getInt("min_studiepunten"), vsd.getInt("max_studiepunten"), edj.getInt("fases"));
		parseCourses(vs, vsd);
		programme.addGroup(vs);
		
		JSONArray bvds = edj.getJSONArray("bachelor_verbredend");
		for (int i = 0; i < bvds.length(); i++) {
			JSONObject bvd = bvds.getJSONObject(i);
			Group bv = new Group(bvd.getString("naam"), "Bachelor_Verbredend", 0, 0, edj.getInt("fases"));
			parseCourses(bv, bvd);
			programme.addGroup(bv);
		}
		
		JSONArray sds = edj.getJSONArray("specialisaties");
		for (int i = 0; i < sds.length(); i++) {
			JSONObject sd = sds.getJSONObject(i);
			Group s = new Group(sd.getString("naam"), "Specialisatie", 0, 0, edj.getInt("fases"));
			parseCourses(s, sd);
			programme.addGroup(s);
		}
		
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
		for (Object s : domain.getJSONArray("fases")) {
			stages.add((int)s);
		}
		return new Course(domain.getString("code"), domain.getString("naam"), domain.getInt("punten"), domain.getInt("semester"), stages);
	}
	
	public Group getProgramme() {
		return programme;
	}
}
