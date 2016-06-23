package data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Lesson {

	private String id;
	private int weekday;
	private int week;
	private String code;
	private String ectsD;
	private GregorianCalendar start, end;
	private String location;
	private String teacher;
	private String groupInfo;
	
	public Lesson(String id, int weekday, int week, String code, 
			String ectsD, Date start, Date end, String location, String teacher, String groupInfo) {
		this.id = id;
		this.weekday = weekday;
		this.week = week;
		this.code = code;
		this.ectsD = ectsD;
		this.start = new GregorianCalendar();
		this.start.setGregorianChange(start);
		this.end = new GregorianCalendar();
		this.end.setGregorianChange(end);
		this.location = location;
		this.teacher = teacher;
		this.groupInfo = groupInfo;
	}
	
	public int getDuration() {
		return (int) ((end.getTimeInMillis() - start.getTimeInMillis()) / (60*1000));
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
	
	protected String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
