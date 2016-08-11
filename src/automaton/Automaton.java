package automaton;

import global.Singleton;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Automaton {

	private State initialState;
	private State finalState;
	private Map<String,List<State>> states;
	private Map<String, Character> selection;
	private List<String> interpretations;
	private Set<String> restorations;
	private HashMap<String, Character> errors;
	private Singleton s;

	public Automaton(State initialState, Singleton s) {
		this.initialState = initialState;
		this.s = s;
		states = new TreeMap<String, List<State>>();
		selection = new TreeMap<String, Character>();
		interpretations = new ArrayList<String>();
		restorations = new TreeSet<String>();
		errors = new HashMap<String, Character>();
		init(this.initialState);
	}

	private void init(State state) {
		if (!state.getVariable().equals(""))
			insertState(state);
		else 
			finalState = state;
		for (Transition t : state.getoTransitions()) {
			State s = t.getTo();
			init(s);
		}
	}

	private void insertState(State state) {
		String key = state.getVariable();
		if (!states.containsKey(key))
			states.put(key, new ArrayList<State>());
		if (!states.get(key).contains(state))
			states.get(key).add(state);
	}

	public void addSelection(String key, char val) {
		if (getDomain(key).contains(val)) {
			selection.put(key, val);
			for (State s : states.get(key))
				s.update(val);
		}
		else
			errors.put(key,val);
	}

	public void removeSelection(String key) {
		if (errors.containsKey(key))
			errors.remove(key);
		else {
			selection.remove(key);
			for (State s : states.get(key))
				s.relax();
		}
	}

	private ArrayList<Character> getDomain(String code) {
		TreeSet<Character> domain = new TreeSet<Character>();
		for (State state : states.get(code))
			domain.addAll(state.getDomain());
		return new ArrayList<Character>(domain);
	}

	public ArrayList<HashMap<String, Character>> calculateSolutions() {
		interpretations = new ArrayList<String>();
		restorations = new TreeSet<String>();
		ArrayList<HashMap<String, Character>> solutions = new ArrayList<HashMap<String,Character>>();
		if (isConsistent()) {
			solutions.add(errors);
			return solutions;
		}
		else {
			long startTime = System.nanoTime();
			develop(initialState, "", "");
			long endTime = System.nanoTime();
			writeTime("solutions" + " ()", startTime, endTime, states.size() - selection.size());
			for (String solution : restorations) {
				HashMap<String, Character> sol = new HashMap<String, Character>();
				for (String code : solution.split(" "))
					if (!code.isEmpty())
						sol.put(code, selection.get(code));
				for (String code : errors.keySet())
					sol.put(code, errors.get(code));
				solutions.add(sol);
			}
			return solutions;
		}
	}

	private void develop(State state, String e, String relax) {
		if (state == finalState) {
			interpretations.add(e.trim());
			restorations.add(relax.trim());
		}
		String h = state.getVariable();
		for (Transition transition : state.getoTransitions()) {
			if (transition.isOptimal()) {
				if (transition.getWeight() > 0)
					develop(transition.getTo(), e + " " + h + " - " + "/", relax + " " + h);
				else
					develop(transition.getTo(), e + " " + h + " - " + transition.getVal(), relax);
			}
		}
	}

	public boolean isConsistent() {
		return finalState.getlCost()==0;
	}
	
	private void writeTime(String line, long startTime, long endTime, int freeVariables) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(s.getResultPath(), true));
			long duration = endTime - startTime;
			bw.write((line + " - " + ((double)duration)/1000000) + " - " + freeVariables + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
