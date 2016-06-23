package data;

import java.util.HashMap;

public class StageSchedule {

	private int stage;
	private HashMap<String, CourseSchedule> courseSchedules;
	
	public StageSchedule(int stage) {
		this.stage = stage;
		courseSchedules = new HashMap<String, CourseSchedule>();
	}
	
	protected void addSchedule(String code, CourseSchedule schedule) {
		courseSchedules.put(code, schedule);
	}
	
	protected void removedSchedule(String code) {
		courseSchedules.remove(code);
	}
	
	
}
