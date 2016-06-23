package observer;

import java.util.HashMap;
import java.util.Observable;

import data.Course;

public class SolutionDialogObservable extends Observable {

	public void confirm(HashMap<Course, Course> solution) {
		setChanged();
		notifyObservers(solution);
	}
}
