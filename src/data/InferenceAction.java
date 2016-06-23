package data;

import java.util.ArrayList;

public class InferenceAction extends Action {

	private String inference;
	private ArrayList<Course> before;
	private ArrayList<Course> after;
	
	public InferenceAction(String inference, ArrayList<Course> before, ArrayList<Course> after) {
		this.inference = inference;
		this.before = before;
		this.after = after;
	}
	
	@Override
	public String toString() {
		return inference;
	}
}
