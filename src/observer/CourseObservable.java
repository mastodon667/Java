package observer;

import java.util.ArrayList;
import java.util.Observable;

import data.Course;

public class CourseObservable extends Observable {

	public void change(Course course) {
		setChanged();
		ArrayList<Course> courses = new ArrayList<Course>();
		courses.add(course);
		notifyObservers(courses);
	}
}
