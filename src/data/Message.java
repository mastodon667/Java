package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Message {

	private HashMap<Course, Integer> add;
	private ArrayList<String> remove;
	
	public Message(HashMap<Course, Integer> add, ArrayList<String> remove) {
		this.add = add;
		this.remove = remove;
	}

	public HashMap<Course, Integer> getAdd() {
		return add;
	}

	public ArrayList<String> getRemove() {
		return remove;
	}
}
