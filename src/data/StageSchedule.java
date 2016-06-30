package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class StageSchedule {

	private HashMap<String, CourseSchedule> courseSchedules;
	
	public StageSchedule() {
		courseSchedules = new HashMap<String, CourseSchedule>();
	}
	
	protected void addSchedule(String code, CourseSchedule schedule) {
		courseSchedules.put(code, schedule);
	}
	
	protected void removedSchedule(String code) {
		courseSchedules.remove(code);
	}
	
	protected ArrayList<Lesson> getLessons(int week, int day) {
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		for (CourseSchedule schedule : courseSchedules.values())
			lessons.addAll(schedule.getLessons(week, day));
		return lessons;
	}
	
	protected TreeSet<Integer> getWeeks() {
		TreeSet<Integer> weeks = new TreeSet<Integer>();
		for (CourseSchedule schedule : courseSchedules.values())
			weeks.addAll(schedule.getWeeks());
		return weeks;
	}
}
