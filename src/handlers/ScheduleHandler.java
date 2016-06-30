package handlers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

import data.CompleteSchedule;
import data.CourseSchedule;
import data.Lesson;
import data.Message;
import global.Singleton;
import gui.CalendarPanelInterface;

public class ScheduleHandler implements Observer {

	private Singleton s;
	private HashMap<String, CourseSchedule> courseSchedules;
	private CompleteSchedule schedule;
	private CalendarPanelInterface pnlCalendar;
	
	public ScheduleHandler(Singleton s, int stages, CalendarPanelInterface pnlCalendar) {
		this.s = s;
		this.pnlCalendar = pnlCalendar;
		courseSchedules = new HashMap<String, CourseSchedule>();
		schedule = new CompleteSchedule(stages);
	}
	
	public void addCourse(String code, int stage) {
		if (!courseSchedules.keySet().contains(code))
			loadSchedule(code);
		schedule.addSchedule(courseSchedules.get(code), stage);
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
	
	private void loadSchedule(String code) {
		CourseSchedule schedule = new CourseSchedule(code);
		ArrayList<String> courses = findShadowCourses(code);
		for (String path : s.getLessonsPaths()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String line = br.readLine();
				while (line != null) {
					Lesson l = parseLine(line, courses);
					if (l != null)
						schedule.addLesson(l);
					line = br.readLine();
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		courseSchedules.put(code, schedule);
	}
	
	private Lesson parseLine(String line, ArrayList<String> courses) {
		String items[] = line.split("\\|");
		if (!courses.contains(items[10]))
			return null;
		return new Lesson(items[1], parseDay(items[5]), Integer.parseInt(items[3].substring(4)), items[10], items[8], parseDate(items[4],items[6]), parseDate(items[4],items[7]), 
				items[18] + " " + items[19] + " (" + items[22] + ")", items[32], items[35], items[38]);
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

	private Date parseDate(String date, String time) {
		String d[] = date.split("\\.");
		String t[] = time.split("\\:");
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, Integer.parseInt(d[2]));
		cal.set(Calendar.MONTH, Integer.parseInt(d[1]));
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d[0]));
		int hour = Integer.parseInt(t[0]);
		if (hour < 12) {
			cal.set(Calendar.HOUR, hour);
			cal.set(Calendar.AM_PM, Calendar.AM);
		}
		else {
			cal.set(Calendar.HOUR, hour-12);
			cal.set(Calendar.AM_PM, Calendar.PM);
		}
		cal.set(Calendar.MINUTE, Integer.parseInt(t[1]));
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	private ArrayList<String> findShadowCourses(String code) {
		ArrayList<String> courses = new ArrayList<String>();
		courses.add(code);
		try {
			BufferedReader br = new BufferedReader(new FileReader(s.getShadowPath()));
			String line = br.readLine();
			while (line != null) {
				String items[] = line.split(",");
				if (items[0].equals(code))
					courses.add(items[1]);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return courses;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Message) {
			Message m = (Message) arg;
			ArrayList<String> remove = m.getRemove();
			for (String code : remove)
				removeCourse(code);
			HashMap<String, Integer> add = m.getAdd();
			for (String code : add.keySet())
				addCourse(code, add.get(code));
		}
		pnlCalendar.refresh();
	}
}
