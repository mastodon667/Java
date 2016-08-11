package data;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAction extends Action {

	private String text;
	private HashMap<String, Course> nCourses;
	private HashMap<String, Course> oCourses;
	private ArrayList<String> oldChoices;
	private ArrayList<String> newChoices;
	private ArrayList<String> oldPropagations;
	private ArrayList<String> newPropagations;
	private ArrayList<String> oldUnknowns;
	private ArrayList<String> newUnknowns;
	private HashMap<String, Course> before;
	private HashMap<String, Course> after;

	public UserAction(HashMap<String,Course> oChoices, HashMap<String,Course> nChoices, 
			HashMap<String,Course> oProps, HashMap<String,Course> nProps, 
			HashMap<String,Course> oUnknowns, HashMap<String,Course> nUnknowns) {
		setText(oChoices, nChoices);
		oCourses = new HashMap<String, Course>();
		for (Course course : oChoices.values())
			oCourses.put(course.getCode(), course);
		nCourses = new HashMap<String, Course>();
		for (Course course : nChoices.values())
			nCourses.put(course.getCode(), course.init());
		oldChoices = new ArrayList<String>();
		newChoices = new ArrayList<String>();
		oldPropagations = new ArrayList<String>();
		newPropagations = new ArrayList<String>();
		oldUnknowns = new ArrayList<String>();
		newUnknowns = new ArrayList<String>();
		before = new HashMap<String, Course>();
		after = new HashMap<String, Course>();
		setup(oChoices, nChoices, oProps, nProps, oUnknowns, nUnknowns);
	}

	private void setup(HashMap<String,Course> oChoices, HashMap<String,Course> nChoices, 
			HashMap<String,Course> oProps, HashMap<String,Course> nProps, 
			HashMap<String,Course> oUnknowns, HashMap<String,Course> nUnknowns) {
		for (String code : oChoices.keySet()) {
			put(oChoices.get(code), nChoices.get(code), oldChoices, newChoices);
			put(oChoices.get(code), nProps.get(code), oldChoices, newPropagations);
			put(oChoices.get(code), nUnknowns.get(code), oldChoices, newUnknowns);
		}
		for (String code : oProps.keySet()) {
			put(oProps.get(code), nChoices.get(code), oldPropagations, newChoices);
			put(oProps.get(code), nProps.get(code), oldPropagations, newPropagations);
			put(oProps.get(code), nUnknowns.get(code), oldPropagations, newUnknowns);
		}
		for (String code : oUnknowns.keySet()) {
			put(oUnknowns.get(code), nChoices.get(code), oldUnknowns, newChoices);
			put(oUnknowns.get(code), nProps.get(code), oldUnknowns, newPropagations);
			put(oUnknowns.get(code), nUnknowns.get(code), oldUnknowns, newUnknowns);
		}
	}

	private void put(Course oCourse, Course nCourse, ArrayList<String> oList, ArrayList<String> nList) {
		if (nCourse != null)
			if (!(oCourse.equals(nCourse))) {
				before.put(oCourse.getCode(), oCourse.clone());
				after.put(nCourse.getCode(), nCourse.clone());
				oList.add(oCourse.getCode());
				nList.add(nCourse.getCode());
			}
	}
	
	private void setText(HashMap<String, Course> oChoices, HashMap<String, Course> nChoices) {
		ArrayList<Course> courses = new ArrayList<Course>();
		for (String code : oChoices.keySet()) {
			if (nChoices.containsKey(code)) {
				Course oCourse = oChoices.get(code);
				Course nCourse = nChoices.get(code);
				if (!oCourse.equals(nCourse))
					courses.add(nCourse);
			}
			else 
				courses.add(oChoices.get(code).init());
		}
		for (String code : nChoices.keySet())
			if (!oChoices.containsKey(code))
				courses.add(nChoices.get(code));
		
		text = "";
		for (Course course : courses) {
			text += "[" + course.getCode() + "] ";
			if (course.getNotInterested()) {
				text += "Not Interested. ";
			}
			else if (course.getSelected() != Course.notSelected) {
				text += "Fase " + course.getSelected() + ". ";
			}
			else {
				text += "Deselected. ";
			}
		}
	}

	@Override
	public String toString() {
		return text;
	}
	
	public Course getOldCourse(String code) {
		return before.get(code);
	}

	public Course getNewCourse(String code) {
		return after.get(code);
	}
	
	public ArrayList<Course> getBefore() {
		return new ArrayList<Course>(before.values());
	}
	
	public ArrayList<String> getNewPropagations() {
		for (String code : oCourses.keySet())
			newPropagations.remove(code);
		return newPropagations;
	}
	
	public ArrayList<Course> getChoices() {
		ArrayList<Course> choices = new ArrayList<Course>();
		for (Course course : nCourses.values()) {
			if (oCourses.containsKey(course.getCode()))
				choices.add(oCourses.get(course.getCode()));
			else
				choices.add(course);
		}
		for (Course course : oCourses.values())
			if (!nCourses.containsKey(course.getCode()))
				choices.add(course);
		return choices;
	}
}
