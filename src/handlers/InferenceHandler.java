package handlers;

import global.Singleton;
import gui.SelectionPanelInterface;
import idp.IDPExplanation;
import idp.IDPISP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import parser.IDPParser;
import automaton.Automaton;
import reader.AutomatonReader;
import reader.JSONReader;
import data.Action;
import data.Course;
import data.Group;
import data.InferenceAction;
import data.Message;
import data.UserAction;

public class InferenceHandler extends Observable {

	private HashMap<String, Course> cUserChoices;
	private HashMap<String, Course> bUserChoices;
	private Group iProgramme; //Initial instance of the programme
	private Group cProgramme; //Current instance of the programme
	private Group bProgramme; //Back-up instance of the programme
	private Automaton automaton;
	private IDPParser parser;
	private IDPTranslator translator;
	private IDPExplanationTranslator explanationTranslator;
	private IDPISP isp;
	private IDPExplanation explanation;

	public InferenceHandler(Singleton s) {
		JSONReader reader = new JSONReader(s);
		parser = new IDPParser();
		isp = new IDPISP(s);
		explanation = new IDPExplanation(s);
		automaton = new AutomatonReader(s.getVariablesPath(), s.getAutomatonPath(), s).getAutomaton();
		cUserChoices = new HashMap<String, Course>();
		bUserChoices = new HashMap<String, Course>();
		iProgramme = reader.getProgramme();
		cProgramme = iProgramme.clone();
		bProgramme = iProgramme.clone();
		translator = new IDPTranslator(iProgramme.getAllCourses());
		explanationTranslator = new IDPExplanationTranslator(s);
	}

	public Group getProgramme() {
		return cProgramme;
	}

	public ArrayList<String> getTerms() {
		return isp.getTerms();
	}

	public void propagate(Group programme) {
		String input = parser.parseStructure(programme, "");
		translator.filter(input);
		String output = isp.propagate(input, programme.countFreeVariables());
		translator.filter(output);
		for (Course course : translator.getCourses())
			programme.update(course);
	}

	public void expand(SelectionPanelInterface pnlSelection) {
		String input = parser.parseStructure(cProgramme, "");
		translator.filter(input);
		String output = isp.expand(input, cProgramme.countFreeVariables());
		translator.filter(output);
		for (Course course : translator.getCourses())
			cProgramme.update(course);
		pnlSelection.addAction(new InferenceAction("Expansie", new ArrayList<Course>(bProgramme.getAllCourses().values()), new ArrayList<Course>(cProgramme.getAllCourses().values())));
		update();
		bProgramme = cProgramme.clone();
	}

	public void minimize(SelectionPanelInterface pnlSelection, String term) {
		String input = parser.parseStructure(cProgramme, term.toLowerCase());
		translator.filter(input);
		String output = isp.minimize(input, term, cProgramme.countFreeVariables());
		translator.filter(output);
		for (Course course : translator.getCourses())
			cProgramme.update(course);
		pnlSelection.addAction(new InferenceAction("Minimizatie (" + term + ")", new ArrayList<Course>(bProgramme.getAllCourses().values()), new ArrayList<Course>(cProgramme.getAllCourses().values())));
		update();
		bProgramme = cProgramme.clone();
	}

	private boolean sat() {
		Group programme = buildProgramme(new ArrayList<Course>(cUserChoices.values()));
		return isp.sat(parser.parseStructure(programme, ""), programme.countFreeVariables());
	}
	
	public void isConsistent(SelectionPanelInterface pnlSelection) {
		pnlSelection.showConfirmationPopup(isp.consistent(parser.parseStructure(cProgramme, "consistent"), cProgramme.countFreeVariables()));
	}

	private ArrayList<ArrayList<Course>> calculateSolutions() {
		ArrayList<ArrayList<Course>> solutions = new ArrayList<ArrayList<Course>>();
		ArrayList<HashMap<String, Character>> sols = automaton.calculateSolutions();
		for (HashMap<String, Character> sol : sols) {
			ArrayList<Course> solution = new ArrayList<Course>();
			for (String code : sol.keySet())
				solution.add(cUserChoices.get(code));
			solutions.add(solution);
		}
		return solutions;
	}
	
	private HashMap<String, Course> calculateUnsatStructure() {
		Group programme = buildProgramme(new ArrayList<Course>(cUserChoices.values()));
		HashMap<String, Course> unsatStructure = new HashMap<String, Course>();
		for (String code : translator.filterUnsat(isp.unsat(parser.parseStructure(programme, ""), programme.countFreeVariables())))
			unsatStructure.put(code, cUserChoices.get(code));
		return unsatStructure;
	}
	
	private ArrayList<String> findBrokenRules() {
		Group programme = buildProgramme(new ArrayList<Course>(cUserChoices.values()));
		return explanationTranslator.findBrokenRules(explanation.unsat(parser.parseStructure(programme, ""), programme.countFreeVariables()));
	}
	
	public void undoAction(SelectionPanelInterface pnlSelection, Action action) {
		if (action instanceof UserAction)
			undoAction(pnlSelection, (UserAction)action);
		else
			undoAction(pnlSelection, (InferenceAction)action);
	}
	
	private void undoAction(SelectionPanelInterface pnlSelection, UserAction action) {
		for (Course course : action.getBefore())
			cProgramme.update(course);
		updateChoices(action.getChoices());
		bUserChoices = new HashMap<String, Course>(cUserChoices);
		update();
		bProgramme = cProgramme.clone();
		if (!action.getNewPropagations().isEmpty()) {
			ArrayList<Course> before = new ArrayList<Course>();
			ArrayList<Course> after = new ArrayList<Course>();
			for (String code : action.getNewPropagations()) {
				after.add(action.getOldCourse(code));
				before.add(action.getNewCourse(code));
			}
			pnlSelection.showPropagationPopup(before, after);
		}
	}
	
	private void undoAction(SelectionPanelInterface pnlSelection, InferenceAction action) {
		for (Course course : action.getBefore())
			cProgramme.update(course);
		update();
		bProgramme = cProgramme.clone();
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
		else {
			if (automaton != null)
				pnlSelection.showSolutionPopup(calculateSolutions(), findBrokenRules());
			else
				pnlSelection.showUnsatPopup(calculateUnsatStructure(), findBrokenRules());
		}
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
			for (Course course : programme.getAllCourses().values())
				cProgramme.update(course);
			pnlSelection.addAction(new UserAction(bUserChoices, cUserChoices, oldPropagations, newPropagations, oldUnknowns, newUnknowns));
			update();
			bProgramme = cProgramme.clone();
			bUserChoices = new HashMap<String, Course>(cUserChoices);
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
			if (choice.getNotInterested()) {
				cUserChoices.put(choice.getCode(), choice.clone());
				if (automaton != null) {
					String s = choice.getSelected() + "";
					automaton.addSelection(choice.getCode(), s.charAt(0));
				}
			}
			else if (choice.getSelected() != Course.notSelected) {
				cUserChoices.put(choice.getCode(), choice.clone());
				if (automaton != null) {
					String s = choice.getSelected() + "";
					automaton.addSelection(choice.getCode(), s.charAt(0));
				}
			}
			else {
				cUserChoices.remove(choice.getCode());
				if (automaton != null)
					automaton.removeSelection(choice.getCode());
			}
		}
	}
	
	private void update() {
		HashMap<Course, Integer> add = new HashMap<Course, Integer>();
		ArrayList<String> remove = new ArrayList<String>();
		HashMap<String, Course> before = bProgramme.getAllCourses();
		HashMap<String, Course> after = cProgramme.getAllCourses();
		for (Course oCourse : before.values()) {
			Course nCourse = after.get(oCourse.getCode());
			if (!oCourse.equals(nCourse)) {
				remove.add(oCourse.getCode());
				if (nCourse.getSelected() != Course.notSelected)
					add.put(nCourse, nCourse.getSelected());
			}
		}
		setChanged();
		notifyObservers(new Message(add, remove));
	}
}
