package data;

import java.util.ArrayList;
import java.util.TreeSet;

public class CourseSchedule {

	private String code;
	private ArrayList<Lesson> lessons;
	private TreeSet<Integer> weeks;
	
	public CourseSchedule(String code) {
		this.code = code;
		lessons = new ArrayList<Lesson>();
		weeks = new TreeSet<Integer>();
	}
	
	public void addLesson(Lesson lesson) {
		boolean duplicate = false;
		for (Lesson l : lessons) {
			if (l.getWeek() == lesson.getWeek())
				if (l.getId().equals(lesson.getId())) {
					duplicate = true;
					break;
				}
		}
		if (!duplicate) {
			lessons.add(lesson);
			weeks.add(lesson.getWeek());
		}
	}
	
	protected String getCode() {
		return code;
	}
	
	public String printLessons(int week) {
		String s = "";
		for (Lesson l : lessons) {
			if (l.getWeek() == week)
				s += l.getId() + ";";
		}
		return s;
	}
	
	public String printHasLesson(int week) {
		String s = "";
		for (Lesson l : lessons) {
			if (l.getWeek() == week)
				s += code + "," + l.getId() + ";";
		}
		return s;
	}
	
	public String printStart(int week) {
		String s = "";
		for (Lesson l : lessons) {
			if (l.getWeek() == week)
				s += l.getId() + "," + l.getStart() + ";";
		}
		return s;
	}
	
	public String printEnd(int week) {
		String s = "";
		for (Lesson l : lessons) {
			if (l.getWeek() == week)
				s += l.getId() + "," + l.getEnd() + ";";
		}
		return s;
	}
}
