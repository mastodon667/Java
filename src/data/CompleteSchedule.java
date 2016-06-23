package data;

import java.util.HashMap;

public class CompleteSchedule {

	private HashMap<Integer, StageSchedule> stageSchedules;
	
	public CompleteSchedule(int stages) {
		stageSchedules = new HashMap<Integer, StageSchedule>();
		for (int i = 1; i <= stages; i++)
			stageSchedules.put(i, new StageSchedule(i));
	}
	
	public void addSchedule(CourseSchedule schedule, int stage) {
		for (int s : stageSchedules.keySet()) {
			if (s == stage)
				stageSchedules.get(stage).addSchedule(schedule.getCode(), schedule);
			else
				stageSchedules.get(stage).removedSchedule(schedule.getCode());
		}
	}
	
	public void removeSchedule(String code) {
		for (StageSchedule schedule : stageSchedules.values())
			schedule.removedSchedule(code);
	}
}
