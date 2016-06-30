package data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Lesson {

	private int id;
	private int weekday;
	private int week;
	private String code;
	private String ectsD;
	private GregorianCalendar start, end;
	private String location;
	private String teacher;
	private String notes;
	private String groupInfo;
	
	public Lesson(String id, int weekday, int week, String code, 
			String ectsD, Date start, Date end, String location, String teacher, String notes, String groupInfo) {
		this.id = Integer.parseInt(id);
		this.weekday = weekday;
		this.week = week;
		this.code = code;
		this.ectsD = ectsD;
		this.start = new GregorianCalendar();
		this.start.setTimeInMillis(start.getTime());;
		this.end = new GregorianCalendar();
		this.end.setTimeInMillis(end.getTime());
		this.location = location;
		this.teacher = teacher;
		this.notes = notes;
		this.groupInfo = groupInfo;
	}
	
	public int getDuration() {
		return (int) ((end.getTimeInMillis() - start.getTimeInMillis()) / (60*1000));
	}
	
	public GregorianCalendar getStartOfClass() {
		return start;
	}
	
	protected int getWeekday() {
		return weekday;
	}
	
	protected int getStart() {
		int slot = weekday*26;
		slot += (start.get(Calendar.HOUR_OF_DAY)-8)*2;
		if (start.get(Calendar.MINUTE) >= 30)
			slot += 1;
		return slot;
	}
	
	protected int getEnd() {
		int slot = weekday*26;
		slot += (end.get(Calendar.HOUR_OF_DAY)-8)*2;
		if (end.get(Calendar.MINUTE) >= 30)
			slot += 1;
		return slot;
	}
	
	protected int getWeek() {
		return week;
	}
	
	protected int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		String s = "Course: " + code + " - " + notes + " " + groupInfo + "\n";
		s += "Class #" + id + " in week " + week + "\n";
		s += "Teacher: " + teacher + "\n";
		s += "Location: " + location + "\n";
		s += "From: " + start.getTime() + "Until: " + end.getTime() + "\n\n";
		return s;
	}
}
