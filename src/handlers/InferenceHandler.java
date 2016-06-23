package handlers;

import gui.SelectionPanelInterface;
import idp.IDPISP;
import java.util.ArrayList;
import java.util.HashMap;
import parser.IDPParser;
import automaton.Automaton;
import reader.JSONReader;
import data.Course;
import data.Group;
import data.InferenceAction;
import data.UserAction;

public class InferenceHandler {

	private HashMap<String, Course> cUserChoices;
	private HashMap<String, Course> bUserChoices;
	private Group iProgramme; //Initial instance of the programme
	private Group cProgramme; //Current instance of the programme
	private Group bProgramme; //Back-up instance of the programme
	private Automaton automaton;
	private IDPParser parser;
	private IDPTranslator translator;
	private IDPISP isp;

	public InferenceHandler(String path) {
		JSONReader reader = new JSONReader(path);
		parser = new IDPParser();
		isp = new IDPISP();
		cUserChoices = new HashMap<String, Course>();
		bUserChoices = new HashMap<String, Course>();
		iProgramme = reader.getProgramme();
		cProgramme = iProgramme.clone();
		bProgramme = iProgramme.clone();
		translator = new IDPTranslator(iProgramme.getAllCourses());
	}

	public Group getProgramme() {
		return cProgramme;
	}

	public ArrayList<String> getTerms() {
		return isp.getTerms();
	}

	public void propagate(Group programme) {
		String input = parser.parseISPStructure(programme);
		translator.filter(input);
		String output = isp.propagate(input);
		translator.filter(output);
		for (Course course : translator.getCourses())
			programme.update(course);
	}

	public void expand() {
		String input = parser.parseISPStructure(cProgramme);
		translator.filter(input);
		String output = isp.expand(input);
		translator.filter(output);
		for (Course course : translator.getCourses())
			cProgramme.update(course);
	}

	public void minimize(String term) {
		String input = parser.parseISPStructure(cProgramme);
		translator.filter(input);
		String output = isp.minimize(input, term);
		translator.filter(output);
		for (Course course : translator.getCourses())
			cProgramme.update(course);
	}

	private boolean sat() {
		Group programme = buildProgramme(new ArrayList<Course>(cUserChoices.values()));
		return isp.sat(parser.parseISPStructure(programme));
	}

	private ArrayList<HashMap<Course, Course>> calculateSolutions() {

		return null;
	}

	public InferenceAction createInferenceAction() {

		return null;
	}

	private Group buildProgramme(ArrayList<Course> choices) {
		Group programme = iProgramme.clone();
		for (Course course : choices)
			programme.update(course);
		return programme;
	}

	public void update(SelectionPanelInterface pnlSelection, ArrayList<Course> newChoices) {
		updateChoices(newChoices);
		if (sat())
			selectionStep(pnlSelection, newChoices);
		else 
			pnlSelection.showSolutionPopup(calculateSolutions());
	}

	private void selectionStep(SelectionPanelInterface pnlSelection, ArrayList<Course> newChoices) {
		Group programme = buildProgramme(new ArrayList<Course>(cUserChoices.values()));
		propagate(programme);

		HashMap<String, Course> oldPropagations = getPropagations(new ArrayList<String>(bUserChoices.keySet()), bProgramme);
		HashMap<String, Course> newUnknowns = getUnknowns(new ArrayList<String>(cUserChoices.keySet()), programme);

		ArrayList<String> changes = new ArrayList<String>();
		for (Course course : newChoices)
			changes.add(course.getCode());
		ArrayList<Course> before = new ArrayList<Course>();
		ArrayList<Course> after = new ArrayList<Course>();

		for (String code : newUnknowns.keySet()) {
			if (oldPropagations.containsKey(code))
				if (!changes.contains(code)) {
					before.add(oldPropagations.get(code));
					after.add(newUnknowns.get(code));
				}
		}
		if (before.isEmpty()) {
			HashMap<String, Course> newPropagations = getPropagations(new ArrayList<String>(cUserChoices.keySet()), programme);
			HashMap<String, Course> oldUnknowns = getUnknowns(new ArrayList<String>(bUserChoices.keySet()), bProgramme);
			pnlSelection.addAction(new UserAction(bUserChoices, cUserChoices, oldPropagations, newPropagations, oldUnknowns, newUnknowns));
			for (Course course : programme.getAllCourses().values())
				cProgramme.update(course);
			bUserChoices = new HashMap<String, Course>(cUserChoices);
			bProgramme = cProgramme.clone();
		}
		else 
			pnlSelection.showPropagationPopup(before, after);
	}

	private HashMap<String, Course> getPropagations(ArrayList<String> choices, Group programme) {
		HashMap<String, Course> propagations = new HashMap<String, Course>();
		for (Course course : programme.getAllCourses().values()) {
			if (!choices.contains(course.getCode()))
				if (course.getNotInterested())
					propagations.put(course.getCode(), course);
				else if (course.getSelected() != Course.notSelected)
					propagations.put(course.getCode(), course);
		}
		return propagations;
	}

	private HashMap<String, Course> getUnknowns(ArrayList<String> choices, Group programme) {
		HashMap<String, Course> unknowns = new HashMap<String, Course>();
		for (Course course : programme.getAllCourses().values()) {
			if (!choices.contains(course.getCode()))
				if (!course.getNotInterested())
					if (course.getSelected() == Course.notSelected)
						unknowns.put(course.getCode(), course);
		}
		return unknowns;
	}

	private void updateChoices(ArrayList<Course> newChoices) {
		for (Course choice : newChoices) {
			if (choice.getNotInterested())
				cUserChoices.put(choice.getCode(), choice.clone());
			else if (choice.getSelected() != Course.notSelected)
				cUserChoices.put(choice.getCode(), choice.clone());
			else
				cUserChoices.remove(choice.getCode());
		}
//		System.out.println("VOORGAANDE KEUZES");
//		for (Course c : bUserChoices.values())
//			System.out.println(c.getCode() + " " + c.getSelected() + " " + c.getNotInterested());
//		System.out.println("HUIDIGE KEUZES");
//		for (Course c : cUserChoices.values())
//			System.out.println(c.getCode() + " " + c.getSelected() + " " + c.getNotInterested());
//		System.out.println();
	}
}