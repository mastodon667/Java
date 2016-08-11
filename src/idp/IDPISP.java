package idp;

import global.Singleton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class IDPISP {

	private HashMap<String, String> inferences;
	private HashMap<String, String> terms;
	private String vocabulary;
	private HashMap<String, String> vocabularies;
	private String unsatVocabulary;
	private String theory;
	private HashMap<String, String> theories;
	private Singleton s;

	public IDPISP(Singleton s) {
		this.s = s;
		String path = s.getIspPath();
		inferences = new HashMap<String, String>();
		readMap(inferences, path + "inference.txt");
		terms = new HashMap<String, String>();
		readMap(terms, path + "terms.txt");
		vocabulary = read(path + "vocabulary.txt");
		theories = new HashMap<String, String>();
		vocabularies = new HashMap<String, String>();
		for (String term : terms.keySet()) {
			theories.put(term, read(path + "theory_" + term.toLowerCase() + ".txt"));
			vocabularies.put(term, read(path + "vocabulary_" + term.toLowerCase() + ".txt"));
		}
		unsatVocabulary = read(path + "unsatvoc.txt");
		theory = read(path + "theory.txt");
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
	
	private String read(String path) {
		String var = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while (line != null) {
				var += line + "\n";
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return var;
	}

	private void readMap(HashMap<String, String> map, String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while (line != null) {
				String[] item = line.split(";");
				map.put(item[0], item[1]);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String build(String structure, String inference, String term) {
		String input = "";
		input += "Vocabulary V { \n";
		input += vocabulary + "\n";
		if (!term.isEmpty())
			input += vocabularies.get(term) + "\n";
		input += "}\n";
		input += "Theory T : V { \n";
		input += theory + "\n";
		if (!term.isEmpty())
			input += theories.get(term) + "\n";
		input += "}\n";
		input += "Structure S : V { \n";
		input += structure + "\n}\n";
		input += "Procedure main() { \n";
		input += inferences.get(inference) + "\n}\n";
		return input;
	}
	
	private String open(String input, String inference, String term, int freeVariables) {
		String output = "";
		long startTime = System.nanoTime();
		try {
			Process p = Runtime.getRuntime().exec(s.IDP_LOCATION);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			out.write(input);
			out.flush();
			out.close();
			String line = in.readLine();
			while (line != null) {
				output += line + "\n";
				line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.nanoTime();
		writeTime(inference + " (" + term + ")", startTime, endTime, freeVariables);
		return output;
	}
	
	public ArrayList<String> getTerms() {
		return new ArrayList<String>(terms.keySet());
	}
	
	public boolean sat(String structure, int freeVariables) {
		String input = build(structure, "sat", "");
		return open(input, "sat", "", freeVariables).contains("true");
	}
	
	public boolean consistent(String structure, int freeVariables) {
		String input = build(structure, "sat", "");
		return open(input, "consistent", "", freeVariables).contains("true");
	}
	
	public String unsat(String structure, int freeVariables) {
		String input = build(structure, "unsat", "");
		input += "Vocabulary U { \n";
		input += unsatVocabulary + "\n}\n";
		return open(input, "unsat", "", freeVariables);
	}
	
	public String expand(String structure, int freeVariables) {
		String input = build(structure, "expansion", "");
		return open(input, "expansion", "", freeVariables);
	}
	
	public String propagate(String structure, int freeVariables) {
		String input = build(structure, "propagation", "");
		return open(input, "propagation", "", freeVariables);
	}
	
	public String minimize(String structure, String term, int freeVariables) {
		String input = build(structure, "minimization", term);
		input += "Term O : V { \n";
		input += terms.get(term) + "\n}\n";
		return open(input, "minimization", term, freeVariables);
	}
}
