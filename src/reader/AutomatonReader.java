package reader;

import global.Singleton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import automaton.Automaton;
import automaton.State;
import automaton.Transition;

public class AutomatonReader {
	
	private Automaton isp;
	private dk.brics.automaton.Automaton ispAlt;

	public AutomatonReader(String vPath, String aPath, Singleton s) {
		try {
			ArrayList<String> v = readVariables(vPath);
			String[] variables = v.toArray(new String[v.size()]);
			FileInputStream in = new FileInputStream(aPath);
			ispAlt = dk.brics.automaton.Automaton.load(in);
			isp = new Automaton(convert(0, variables, ispAlt.getInitialState(),new HashMap<dk.brics.automaton.State, State>()), s);
		} catch (ClassCastException | ClassNotFoundException | IOException e) {
			isp = null;
		}
	}
	
	private ArrayList<String> readVariables(String vPath) throws IOException {
		ArrayList<String> variables = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(vPath));
		String line = br.readLine();
		while (line != null) {
			variables.add(line.replace("\n", ""));
			line = br.readLine();
		}
		br.close();
		return variables;
	}
	
	public State convert(int pos, String[] variables, dk.brics.automaton.State stateAlt, HashMap<dk.brics.automaton.State, State> seen) {
		State state = null;
		if (seen.containsKey(stateAlt)) {
			state = seen.get(stateAlt);
		}
		else {
			if (pos < variables.length)
				state = new State(variables[pos], 1);
			else {
				state = new State("", 0);
			}
			seen.put(stateAlt, state);
			for (dk.brics.automaton.Transition t : stateAlt.getTransitions()) {
				Transition o = new Transition(t.getMax(),convert(pos+1,variables,t.getDest(),seen));
				state.addoTransition(o);
			}
		}
		return state;
	}
	
	public Automaton getAutomaton() {
		return isp;
	}
}
