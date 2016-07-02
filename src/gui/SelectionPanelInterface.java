package gui;

import java.util.ArrayList;
import java.util.HashMap;

import data.Action;
import data.Course;

public interface SelectionPanelInterface {

	public void showSolutionPopup(ArrayList<HashMap<Course, Course>> solutions, ArrayList<String> brokenRules);
	public void showPropagationPopup(ArrayList<Course> before, ArrayList<Course> after);
	public void addAction(Action action);
}
