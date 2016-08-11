package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {

	private String name;
	private String group;
	private String type;
	private int min;
	private int max;
	private int stages;
	private ArrayList<Group> courseGroups;
	private ArrayList<Group> choiceGroups;
	private ArrayList<Course> mandatoryCourses;
	private ArrayList<Course> optionalCourses;
	
	public Group(String name, String group, String type, int min, int max, int stages) {
		this.name = name;
		this.group = group;
		this.type = type;
		this.min = min;
		this.max = max;
		this.stages = stages;
		courseGroups = new ArrayList<Group>();
		choiceGroups = new ArrayList<Group>();
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
		ArrayList<Group> groups = new ArrayList<Group>(courseGroups);
		groups.addAll(choiceGroups);
		return groups;
	}
	
	public int getStages() {
		return stages;
	}
	
	public int getMaxEcts() {
		return max;
	}
	
	public void addCourseGroup(Group group) {
		if (group != null)
			courseGroups.add(group);
	}
	
	public void addChoiceGroup(Group group) {
		if (group != null)
			choiceGroups.add(group);
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
		for (Group group : courseGroups) {
			if (group.update(course))
				return true;
		}
		for (Group group : choiceGroups) {
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
		for (Group group : courseGroups) {
			HashMap<String, Integer> distri = group.getDistribution();
			for (String name : distri.keySet()) {
				count += distri.get(name);
				distribution.put(name, distri.get(name));
			}
		}
		for (Group group : choiceGroups) {
			HashMap<String, Integer> distri = group.getDistribution();
			for (String name : distri.keySet()) {
				count += distri.get(name);
				distribution.put(name, distri.get(name));
			}
		}
		distribution.put(name, count);
		return distribution;
	}
	
	public int countFreeVariables() {
		int count = 0;
		for (Course course : mandatoryCourses)
			if (course.isFree())
				count++;
		for (Course course : optionalCourses)
			if (course.isFree())
				count++;
		for (Group group : choiceGroups)
			count += group.countFreeVariables();
		for (Group group : courseGroups)
			count += group.countFreeVariables();
		return count;
	}
	
	public HashMap<String, Integer> getAllMinEcts() {
		HashMap<String, Integer> min = new HashMap<String, Integer>();
		min.put(name, this.min);
		for (Group group : courseGroups) {
			HashMap<String, Integer> m = group.getAllMinEcts();
			for (String name : m.keySet()) {
				min.put(name, m.get(name));
			}
		}
		for (Group group : choiceGroups) {
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
		for (Group group : courseGroups) {
			HashMap<String, Integer> m = group.getAllMaxEcts();
			for (String name : m.keySet()) {
				max.put(name, m.get(name));
			}
		}
		for (Group group : choiceGroups) {
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
		for (Group group : courseGroups) {
			HashMap<String, Course> c = group.getAllCourses();
			for (String code : c.keySet())
				courses.put(code, c.get(code));
		}
		for (Group group : choiceGroups) {
			HashMap<String, Course> c = group.getAllCourses();
			for (String code : c.keySet())
				courses.put(code, c.get(code));
		}
		return courses;
	}
	
	public Group clone() {
		Group clone = new Group(name, group, type, min, max, stages);
		for (Course course : mandatoryCourses)
			clone.addMandatoryCourse(course.clone());
		for (Course course : optionalCourses)
			clone.addOptionalCourse(course.clone());
		for (Group group : courseGroups)
			clone.addCourseGroup(group.clone());
		for (Group group : choiceGroups)
			clone.addChoiceGroup(group.clone());
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
		for (Group group : courseGroups)
			s += group.printCourse();
		for (Group group : choiceGroups)
			s += group.printCourse();
		return s;
	}
	
	public String printMandatoryCourses() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.getCode() + ";";
		for (Group group : courseGroups)
			s += group.printMandatoryCourses();
		for (Group group : choiceGroups)
			s += group.printMandatoryCourses();
		return s;
	}
	
	public String printInStage() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printInStage();
		for (Course course : optionalCourses)
			s += course.printInStage();
		for (Group group : courseGroups)
			s += group.printInStage();
		for (Group group : choiceGroups)
			s += group.printInStage();
		return s;
	}
	
	public String printInGroup() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.getCode() + "," + group + ";";
		for (Course course : optionalCourses)
			s += course.getCode() + "," + group + ";";
		for (Group group : courseGroups)
			s += group.printInGroup();
		for (Group group : choiceGroups)
			s += group.printInGroup();
		return s;
	}
	
	public String printSelected() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printSelected();
		for (Course course : optionalCourses)
			s += course.printSelected();
		for (Group group : courseGroups)
			s += group.printSelected();
		for (Group group : choiceGroups)
			s += group.printSelected();
		return s;
	}
	
	public String printNotInterested() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printNotInterested();
		for (Course course : optionalCourses)
			s += course.printNotInterested();
		for (Group group : courseGroups)
			s += group.printNotInterested();
		for (Group group : choiceGroups)
			s += group.printNotInterested();
		return s;
	}
	
	public String printInSemester() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printInSemester();
		for (Course course : optionalCourses)
			s += course.printInSemester();
		for (Group group : courseGroups)
			s += group.printInSemester();
		for (Group group : choiceGroups)
			s += group.printInSemester();
		return s;
	}
	
	public String printMinEcts() {
		String s = "";
		if (max > 0)
			s += group + "->" + min + ";";
		for (Group group : courseGroups)
			s += group.printMinEcts();
		for (Group group : choiceGroups)
			s += group.printMinEcts();
		return s;
	}
	
	public String printMaxEcts() {
		String s = "";
		if (max > 0)
			s += group + "->" + max + ";";
		for (Group group : courseGroups)
			s += group.printMaxEcts();
		for (Group group : choiceGroups)
			s += group.printMaxEcts();
		return s;
	}
	
	public String printAmountOfEcts() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printAmountOfEcts();
		for (Course course : optionalCourses)
			s += course.printAmountOfEcts();
		for (Group group : courseGroups)
			s += group.printAmountOfEcts();
		for (Group group : choiceGroups)
			s += group.printAmountOfEcts();
		return s;
	}
	
	public String printGroup() {
		String s = "";
		s += group + ";";
		for (Group group : courseGroups)
			s += group.printGroup();
		for (Group group : choiceGroups)
			s += group.printGroup();
		return s;
	}
	
	public String printCourseGroup() {
		String s = "";
		if (type.equals("Opleiding") || type.equals("AlgemeenVormend") || type.equals("VerdereSpecialisatie"))
			s += group + ";";
		for (Group group : courseGroups)
			s += group.printCourseGroup();
		for (Group group : choiceGroups)
			s += group.printCourseGroup();
		return s;
	}
	
	public String printChoiceGroup() {
		String s = "";
		if (type.equals("Specialisatie") || type.equals("BachelorVerbredend"))
			s += group + ";";
		for (Group group : courseGroups)
			s += group.printChoiceGroup();
		for (Group group : choiceGroups)
			s += group.printChoiceGroup();
		return s;
	}
	
	public String printType(String type) {
		String s = "";
		if (type.equals(this.type))
			s += group + ";";
		for (Group group : courseGroups)
			s += group.printType(type);
		for (Group group : choiceGroups)
			s += group.printType(type);
		return s;
	}
	
	public String printLesson() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printLesson();
		for (Course course : optionalCourses)
			s += course.printLesson();
		for (Group group : courseGroups)
			s += group.printLesson();
		for (Group group : choiceGroups)
			s += group.printLesson();
		return s;
	}
	
	public String printHasLesson() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printHasLesson();
		for (Course course : optionalCourses)
			s += course.printHasLesson();
		for (Group group : courseGroups)
			s += group.printHasLesson();
		for (Group group : choiceGroups)
			s += group.printHasLesson();
		return s;
	}
	
	public String printStarts() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printStarts();
		for (Course course : optionalCourses)
			s += course.printStarts();
		for (Group group : courseGroups)
			s += group.printStarts();
		for (Group group : choiceGroups)
			s += group.printStarts();
		return s;
	}
	
	public String printEnds() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printEnds();
		for (Course course : optionalCourses)
			s += course.printEnds();
		for (Group group : courseGroups)
			s += group.printEnds();
		for (Group group : choiceGroups)
			s += group.printEnds();
		return s;
	}
	
	public String printConnected() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printConnected();
		for (Course course : optionalCourses)
			s += course.printConnected();
		for (Group group : courseGroups)
			s += group.printConnected();
		for (Group group : choiceGroups)
			s += group.printConnected();
		return s;
	}
	
	public String printSimultaneous() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printSimultaneous();
		for (Course course : optionalCourses)
			s += course.printSimultaneous();
		for (Group group : courseGroups)
			s += group.printSimultaneous();
		for (Group group : choiceGroups)
			s += group.printSimultaneous();
		return s;
	}
	
	public String printHasSimultaneous() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printHasSimultaneous();
		for (Course course : optionalCourses)
			s += course.printHasSimultaneous();
		for (Group group : courseGroups)
			s += group.printHasSimultaneous();
		for (Group group : choiceGroups)
			s += group.printHasSimultaneous();
		return s;
	}
	
	public String printHasCourse() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printHasCourse();
		for (Course course : optionalCourses)
			s += course.printHasCourse();
		for (Group group : courseGroups)
			s += group.printHasCourse();
		for (Group group : choiceGroups)
			s += group.printHasCourse();
		return s;
	}
	
	public String printCondition() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printCondition();
		for (Course course : optionalCourses)
			s += course.printCondition();
		for (Group group : courseGroups)
			s += group.printCondition();
		for (Group group : choiceGroups)
			s += group.printCondition();
		return s;
	}
	
	public String printHasCondition() {
		String s = "";
		for (Course course : mandatoryCourses)
			s += course.printHasCondition();
		for (Course course : optionalCourses)
			s += course.printHasCondition();
		for (Group group : courseGroups)
			s += group.printHasCondition();
		for (Group group : choiceGroups)
			s += group.printHasCondition();
		return s;
	}
}
