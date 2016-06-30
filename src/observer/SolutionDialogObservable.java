package observer;

import java.util.ArrayList;
import java.util.Observable;

import data.Course;

public class SolutionDialogObservable extends Observable {

	public void confirm(ArrayList<Course> solution) {
		setChanged();
		notifyObservers(solution);
	}
}
