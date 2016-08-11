package reader;

import global.Singleton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import data.CourseSchedule;
import data.Lesson;

public class ScheduleReader {
	
	private Singleton s;
	
	public ScheduleReader(Singleton s) {
		this.s = s;
	}

	protected CourseSchedule getSchedule(String code) {
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
		return schedule;
	}
	
	private Lesson parseLine(String line, ArrayList<String> courses) {
		String items[] = line.split("\\|");
		if (!courses.contains(items[10]))
			return null;
		return new Lesson(items[1], parseDay(items[5]), Integer.parseInt(items[3].substring(4)), items[10], items[8], parseDate(items[4],items[6]), parseDate(items[4],items[7]), 
				items[18] + " " + items[19] + " (" + items[22] + ")", items[32], items[35], items[38]);
	}
	
	private int parseDay(String day) {
		if (day.contains("Maandag"))
			return 0;
		if (day.contains("Dinsdag"))
			return 1;
		if (day.contains("Woensdag"))
			return 2;
		if (day.contains("Donderdag"))
			return 3;
		return 4;
	}

	private Date parseDate(String date, String time) {
		String d[] = date.split("\\.");
		String t[] = time.split("\\:");
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, Integer.parseInt(d[2]));
		cal.set(Calendar.MONTH, Integer.parseInt(d[1])-1);
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
}
