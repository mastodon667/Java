package handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

import data.CompleteSchedule;
import data.Course;
import data.Lesson;
import data.Message;

public class ScheduleHandler implements Observer {

	private CompleteSchedule schedule;
	
	public ScheduleHandler(int stages) {
		schedule = new CompleteSchedule(stages);
	}
	
	public void addCourse(Course course, int stage) {
		schedule.addSchedule(course.getSchedule(), stage);
	}
	
	public void removeCourse(String code) {
		schedule.removeSchedule(code);
	}
	
	public ArrayList<Lesson> getLessons(int stage, String day, int week) {
		return schedule.getLessons(stage, week, parseDay(day));
	}
	
	public TreeSet<Integer> getWeeks(int stage) {
		return schedule.getWeeks(stage);
	}
	
	public int getStages() {
		return schedule.getStages();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Message) {
			Message m = (Message) arg;
			ArrayList<String> remove = m.getRemove();
			for (String code : remove)
				removeCourse(code);
			HashMap<Course, Integer> add = m.getAdd();
			for (Course course : add.keySet())
				addCourse(course, add.get(course));
		}
	}
	
	private int parseDay(String day) {
		if (day.equals("Maandag"))
			return 0;
		if (day.equals("Dinsdag"))
			return 1;
		if (day.equals("Woensdag"))
			return 2;
		if (day.equals("Donderdag"))
			return 3;
		return 4;
	}
}
