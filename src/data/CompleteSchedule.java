package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class CompleteSchedule {

	private HashMap<Integer, StageSchedule> stageSchedules;
	
	public CompleteSchedule(int stages) {
		stageSchedules = new HashMap<Integer, StageSchedule>();
		for (int i = 1; i <= stages; i++)
			stageSchedules.put(i, new StageSchedule());
	}
	
	public void addSchedule(CourseSchedule schedule, int stage) {
		for (int s : stageSchedules.keySet()) {
			if (s == stage)
				stageSchedules.get(s).addSchedule(schedule.getCode(), schedule);
			else
				stageSchedules.get(s).removedSchedule(schedule.getCode());
		}
	}
	
	public void removeSchedule(String code) {
		for (StageSchedule schedule : stageSchedules.values())
			schedule.removedSchedule(code);
	}
	
	public ArrayList<Lesson> getLessons(int stage, int week, int day) {
		return stageSchedules.get(stage).getLessons(week, day);
	}
	
	public TreeSet<Integer> getWeeks(int stage) {
		return stageSchedules.get(stage).getWeeks();
	}
	
	public int getStages() {
		return stageSchedules.size();
	}
}
