package observer;

import java.util.ArrayList;
import java.util.Observable;

import data.Course;

public class DialogObservable extends Observable {

	public void confirm(ArrayList<Course> result) {
		setChanged();
		notifyObservers(result);
	}
}
