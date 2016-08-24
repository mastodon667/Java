package handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import data.Course;

public class IDPTranslator {

	private ArrayList<Course> iCourses;
	private HashMap<String, Course> courses;
	private String[] symbols = {"<cf>", "<ct>", "=", "{", "}", " ", "\""};
	
	public IDPTranslator(HashMap<String, Course> courses) {
		iCourses = new ArrayList<Course>(courses.values());
		this.courses = new HashMap<String, Course>();
		init();
	}
	
	private void init() {
		for (Course course : iCourses)
			courses.put(course.getCode(), course.clone());
	}
	
	protected void filter(String structure) {
		for (String line : structure.split("\n")) {
			if (line.contains("Geselecteerd ") || line.contains("Geselecteerd<ct>"))
				updateSelected(line.replace("Geselecteerd", ""));
			else if (line.contains("GeenInteresse ") || line.contains("GeenInteresse<ct>"))
				updateNotInterested(line.replace("GeenInteresse", ""));
		}
	}
	
	private void updateSelected(String line) {
		line = filterLine(line);
		ArrayList<String> codes = new ArrayList<String>(courses.keySet());
		for (String item : line.split(";")) {
			if (!item.isEmpty()) {
				String[] items = item.split(",");
				courses.get(items[0]).setSelected(Integer.parseInt(items[1]));
				codes.remove(items[0]);
			}
		}
		for (String code : codes)
			courses.get(code).setSelected(Course.notSelected);
	}
	
	private void updateNotInterested(String line) {
		line = filterLine(line);
		ArrayList<String> codes = new ArrayList<String>(courses.keySet());
		for (String item : line.split(";")) {
			if (!item.isEmpty()) {
				courses.get(item).setNotInterested(true);
				codes.remove(item);
			}
		}
		for (String code : codes)
			courses.get(code).setNotInterested(false);
	}
	
	protected ArrayList<Course> getCourses() {
		ArrayList<Course> c = new ArrayList<Course>(courses.values()); 
		init();
		return c;
	}
	
	private String filterLine(String line) {
		for (String symbol : symbols)
			line = line.replace(symbol, "");
		return line;
	}
	
	protected ArrayList<String> filterUnsat(String structure) {
		TreeSet<String> unsatStructure = new TreeSet<String>();
		for (String line : structure.split("\n")) {
			if (line.contains("Geselecteerd<ct>"))
				for (String code : filterLine(line.replace("Geselecteerd", "")).split(";"))
					if(!code.isEmpty())
						unsatStructure.add(code.split(",")[0]);
			if (line.contains("GeenInteresse<ct>"))
				for (String code : filterLine(line.replace("GeenInteresse", "")).split(";"))
					if(!code.isEmpty())
						unsatStructure.add(code);
		}
		return new ArrayList<String>(unsatStructure);
	}
	
	protected ArrayList<String> filterConditions(String structure) {
		System.out.println(structure);
		ArrayList<String> conditions = new ArrayList<String>();
		for (String line : structure.split("\n")) {
			if (line.contains("Feedback<ct>") || line.contains("Feedback "))
				for (String code : filterLine(line.replace("Feedback", "")).split(";"))
					if(!code.isEmpty())
						conditions.add(code);
		}
		return conditions;
	}
}
