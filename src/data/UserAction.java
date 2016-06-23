package data;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAction extends Action {

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
				before.put(oCourse.getCode(), oCourse);
				after.put(nCourse.getCode(), nCourse);
				oList.add(oCourse.getCode());
				nList.add(nCourse.getCode());
			}
	}

	@Override
	public String toString() {
		String s = "";
		for (String code : newChoices) {
			s += "[" + code + "] ";
			Course t = after.get(code);
			if (t.getNotInterested()) {
				s += "Not Interested. ";
			}
			else if (t.getSelected() != Course.notSelected) {
				s += "Fase " + t.getSelected() + ". ";
			}
			else {
				s += "Deselected. ";
			}
		}
		return s;
	}


}
