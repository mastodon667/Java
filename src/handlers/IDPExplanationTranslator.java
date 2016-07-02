package handlers;

import global.Singleton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class IDPExplanationTranslator {

	private HashMap<Integer, String> rules;

	public IDPExplanationTranslator(Singleton s) {
		readRules(s.getExplanationsPath() + "rules.txt");
	}

	private void readRules(String path) {
		rules = new HashMap<Integer, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while (line != null) {
				String[] items = line.split(":");
				rules.put(Integer.parseInt(items[0]), items[1]);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> findBrokenRules(String input) {
		ArrayList<String> brokenRules = new ArrayList<String>();
		for (String line : input.split("\n")) {
			if (line.contains("satisfied<ct>")) {
				for (int rule : rules.keySet())
					if (line.contains(rule + ""))
						brokenRules.add(rules.get(rule));
				break;
			}
		}
		return brokenRules;
	}
}
