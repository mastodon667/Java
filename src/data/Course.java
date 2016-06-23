package data;

import java.util.ArrayList;

public class Course {
	
	public static final int notSelected = 0;
	private String code;
	private String name;
	private int ects;
	private int semester;
	private ArrayList<Integer> stages;
	private int selected;
	private boolean notInterested;
	
	public Course(String code, String name, int ects, int semester, ArrayList<Integer> stages) {
		this.code = code;
		this.name = name;
		this.ects = ects;
		this.semester = semester;
		this.stages = new ArrayList<Integer>(stages);
		selected = notSelected;
		notInterested = false;
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
		Course clone = new Course(code, name, ects, semester, stages);
		clone.selected = selected;
		clone.notInterested = notInterested;
		return clone;
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
}
