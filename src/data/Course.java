package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {
	
	public static final int notSelected = 0;
	private String code;
	private String name;
	private int ects;
	private int semester;
	private ArrayList<Integer> stages;
	private ArrayList<String> connected;
	private int selected;
	private boolean notInterested;
	private CourseSchedule schedule;
	private HashMap<String, ArrayList<String>> simultaneous;
	private HashMap<String, String> conditions;
	
	public Course(String code, String name, int ects, int semester, ArrayList<Integer> stages, CourseSchedule schedule) {
		this.code = code;
		this.name = name;
		this.ects = ects;
		this.semester = semester;
		this.stages = new ArrayList<Integer>(stages);
		connected = new ArrayList<String>();
		selected = notSelected;
		notInterested = false;
		this.schedule = schedule;
		simultaneous = new HashMap<String, ArrayList<String>>();
		conditions = new HashMap<String, String>();
	}
	
	public void setSelected(int stage) {
		if (stage == notSelected) {
			selected = stage;
		}
		else if (stages.contains(stage)) {
			if (!notInterested)
				selected = stage;
		}
	}
	
	public void setNotInterested(boolean notInterested) {
		this.notInterested = notInterested;
		if (notInterested)
			setSelected(notSelected);
	}
	
	public void addConnection(String course) {
		connected.add(course);
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public int getEcts() {
		return ects;
	}
	
	public boolean getNotInterested() {
		return notInterested;
	}
	
	public int getSelected() {
		return selected;
	}
	
	public int getSemester() {
		return semester;
	}
	
	public ArrayList<Integer> getStages() {
		return stages;
	}
	
	public CourseSchedule getSchedule() {
		return schedule;
	}
	
	protected boolean isFree() {
		if (notInterested)
			return false;
		if (selected != notSelected)
			return false;
		return true;
	}
	
	public void addSimultaneousCourse(String sim, String course) {
		if (!simultaneous.containsKey(sim))
			simultaneous.put(sim, new ArrayList<String>());
		simultaneous.get(sim).add(course);
	}
	
	public void addCondition(int count, String condition) {
		String id = code + "_con" + count;
		conditions.put(id, condition);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Course))
			return false;
		Course o = (Course)obj;
		if (!code.equals(o.code))
			return false;
		if (!(selected == o.selected))
			return false;
		if (!(notInterested == o.notInterested))
			return false;
		return true;
	}
	
	public Course clone() {
		Course clone = init();
		clone.selected = selected;
		clone.notInterested = notInterested;
		return clone;
	}
	
	public Course init() {
		Course init = new Course(code, name, ects, semester, stages, schedule);
		for (String code : connected)
			init.addConnection(code);
		for (String code : simultaneous.keySet())
			init.simultaneous.put(code, simultaneous.get(code));
		for (String id : conditions.keySet())
			init.conditions.put(id, conditions.get(id));
		return init;
	}
	
	/**
	 * PRINT FUNCTIONS
	 */
	
	public String printInStage() {
		String s = "";
		for (int stage : stages)
			s += code + "," + stage + ";";
		return s;
	}
	
	public String printSelected() {
		String s = "";
		if (selected != notSelected)
			s += code + "," + selected + ";";
		return s;
	}
	
	public String printNotInterested() {
		String s = "";
		if (notInterested)
			s += code + ";";
		return s;
	}
	
	public String printInSemester() {
		String t = "";
		if (semester == 1)
			t += "Eerste";
		else if (semester == 2)
			t += "Tweede";
		else
			t += "Jaar";
		return code + "->" + t + ";";
	}
	
	public String printAmountOfEcts() {
		return code + "," + ects + ";";
	}
	
	@Override
	public String toString() {
		String s = "["+code+"] ";
		if (notInterested)
			s += "Geen interesse.";
		else
			if (selected == notSelected)
				s += "Niet geselecteerd.";
			else
				s += "Fase " + selected + ".";
		return s;
	}
	
	public String printLesson() {
		String s = "";
		if (semester == 1)
			s += schedule.printLesson(45);
		if (semester > 1)
			s += schedule.printLesson(12);
		return s;
	}
	
	public String printHasLesson() {
		String s = "";
		if (semester == 1)
			s += schedule.printHasLesson(45);
		if (semester > 1)
			s += schedule.printHasLesson(12);
		return s;
	}
	
	public String printStarts() {
		String s = "";
		if (semester == 1)
			s += schedule.printStarts(45);
		if (semester > 1)
			s += schedule.printStarts(12);
		return s;
	}
	
	public String printEnds() {
		String s = "";
		if (semester == 1)
			s += schedule.printEnds(45);
		if (semester > 1)
			s += schedule.printEnds(12);
		return s;
	}
	
	public String printConnected() {
		String s = "";
		for (String code : connected)
			s += this.code + "," + code + ";";
		return s;
	}
	
	public String printSimultaneous() {
		String s = "";
		for (String sim : simultaneous.keySet())
			s += sim + ";";
		return s;
	}
	
	public String printHasSimultaneous() {
		String s = "";
		for (String sim : simultaneous.keySet())
			s += code + "," + sim + ";";
		return s;
	}
	
	public String printHasCourse() {
		String s = "";
		for (String sim : simultaneous.keySet())
			for (String code : simultaneous.get(sim))
				s += sim + "," + code + ";";
		return s;
	}
	
	public String printCondition() {
		String s = "";
		for (String id : conditions.keySet())
			s += id + ";";
		return s;
	}
	
	public String printHasCondition() {
		String s = "";
		for (String id : conditions.keySet())
			s += code + "," + id + ";";
		return s;
	}
}
