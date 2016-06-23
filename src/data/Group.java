package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {

	private String name;
	private String type;
	private int min;
	private int max;
	private int stages;
	private ArrayList<Group> groups;
	private ArrayList<Course> mandatoryCourses;
	private ArrayList<Course> optionalCourses;
	
	public Group(String name, String type, int min, int max, int stages) {
		this.name = name;
		this.type = type;
		this.min = min;
		this.max = max;
		this.stages = stages;
		groups = new ArrayList<Group>();
		mandatoryCourses = new ArrayList<Course>();
		optionalCourses = new ArrayList<Course>();
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Course> getMandatoryCourses() {
		return mandatoryCourses;
	}
	
	public ArrayList<Course> getOptionalCourses() {
		return optionalCourses;
	}
	
	public ArrayList<Group> getGroups() {
		return groups;
	}
	
	public int getStages() {
		return stages;
	}
	
	public int getMaxEcts() {
		return max;
	}
	
	public void addGroup(Group group) {
		if (group != null)
			groups.add(group);
	}
	
	public void addMandatoryCourse(Course course) {
		if (course != null)
			mandatoryCourses.add(course);
	}
	
	public void addOptionalCourse(Course course) {
		if (course != null)
			optionalCourses.add(course);
	}
	
	public boolean update(Course course) {
		for (Course c : mandatoryCourses) {
			if (course.getCode().equals(c.getCode())) {
				c.setNotInterested(course.getNotInterested());
				c.setSelected(course.getSelected());
				return true;
			}
		}
		for (Course c : optionalCourses) {
			if (course.getCode().equals(c.getCode())) {
				c.setNotInterested(course.getNotInterested());
				c.setSelected(course.getSelected());
				return true;
			}
		}
		for (Group group : groups) {
			if (group.update(course))
				return true;
		}
		return false;
	}
	
	public HashMap<String, Integer> getDistribution() {
		HashMap<String, Integer> distribution = new HashMap<String, Integer>();
		int count = 0;
		for (Course course : mandatoryCourses) {
			if (course.getSelected() != Course.notSelected)
				count += course.getEcts();
		}
		for (Course course : optionalCourses) {
			if (course.getSelected() != Course.notSelected)
				count += course.getEcts();
		}
		for (Group group : groups) {
			HashMap<String, Integer> distri = group.getDistribution();
			for (String name : distri.keySet()) {
				count += distri.get(name);
				distribution.put(name, distri.get(name));
			}
		}
		distribution.put(name, count);
		return distribution;
	}
	
	public HashMap<String, Integer> getAllMinEcts() {
		HashMap<String, Integer> min = new HashMap<String, Integer>();
		min.put(name, this.min);
		for (Group group : groups) {
			HashMap<String, Integer> m = group.getAllMinEcts();
			for (String name : m.keySet()) {
				min.put(name, m.get(name));
			}
		}
		return min;
	}
	
	public HashMap<String, Integer> getAllMaxEcts() {
		HashMap<String, Integer> max = new HashMap<String, Integer>();
		max.put(name, this.max);
		for (Group group : groups) {
			HashMap<String, Integer> m = group.getAllMaxEcts();
			for (String name : m.keySet()) {
				max.put(name, m.get(name));
			}
		}
		return max;
	}
	
	public HashMap<String, Course> getAllCourses() {
		HashMap<String, Course> courses = new HashMap<String, Course>();
		for (Course course : mandatoryCourses)
			courses.put(course.getCode(), course);
		for (Course course : optionalCourses)
			courses.put(course.getCode(), course);
		for (Group group : groups) {
			HashMap<String, Course> c = group.getAllCourses();
			for (String code : c.keySet())
				courses.put(code, c.get(code));
		}
		return courses;
	}
	
	public Group clone() {
		Group clone = new Group(name, type, min, max, stages);
		for (Course course : mandatoryCourses)
			clone.addMandatoryCourse(course.clone());
		for (Course course : optionalCourses)
			clone.addOptionalCourse(course.clone());
		for (Group group : groups)
			clone.addGroup(group.clone());
		return clone;
	}
	
	/**
	 * PRINT FUNCTIONS
	 */
	
	public String printCourse() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.getCode() + ";";
		for (Course course : optionalCourses)
			s += course.getCode() + ";";
		for (Group group : groups)
			s += group.printCourse();
		return s;
	}
	
	public String printIsType() {
		String s = "";
		s += name + "," + type + ";";
		for (Group group : groups)
			s+= group.printIsType();
		return s;
	}
	
	public String printMandatoryCourses() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.getCode() + "," + name + ";";
		for (Course course : optionalCourses)
			s += course.getCode() + "," + name + ";";
		for (Group group : groups)
			s += group.printMandatoryCourses();
		return s;
	}
	
	public String printInStage() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printInStage();
		for (Course course : optionalCourses)
			s += course.printInStage();
		for (Group group : groups)
			s += group.printInStage();
		return s;
	}
	
	public String printInGroup() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.getCode() + "," + name + ";";
		for (Course course : optionalCourses)
			s += course.getCode() + "," + name + ";";
		for (Group group : groups)
			s += group.printInGroup();
		return s;
	}
	
	public String printSelected() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printSelected();
		for (Course course : optionalCourses)
			s += course.printSelected();
		for (Group group : groups)
			s += group.printSelected();
		return s;
	}
	
	//TODO: FUNCTIONALITY TO BE DETERMINED
	public String printNotSelected() {
		String s = "";
		
		return s;
	}
	
	public String printNotInterested() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printNotInterested();
		for (Course course : optionalCourses)
			s += course.printNotInterested();
		for (Group group : groups)
			s += group.printNotInterested();
		return s;
	}
	
	public String printInSemester() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printInSemester();
		for (Course course : optionalCourses)
			s += course.printInSemester();
		for (Group group : groups)
			s += group.printInSemester();
		return s;
	}
	
	public String printMinEcts() {
		String s = "";
		s += name + "->" + min + ";";
		for (Group group : groups)
			s += group.printMinEcts();
		return s;
	}
	
	public String printMaxEcts() {
		String s = "";
		s += name + "->" + max + ";";
		for (Group group : groups)
			s += group.printMaxEcts();
		return s;
	}
	
	public String printAmountOfEcts() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printAmountOfEcts();
		for (Course course : optionalCourses)
			s += course.printAmountOfEcts();
		for (Group group : groups)
			s += group.printAmountOfEcts();
		return s;
	}
	
	public String printGroup() {
		String s = "";
		s += name + ";";
		for (Group group : groups)
			s += group.printGroup();
		return s;
	}
}
