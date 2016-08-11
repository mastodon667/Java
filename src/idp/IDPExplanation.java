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
import java.util.HashMap;

public class IDPExplanation {
	private HashMap<String, String> inferences;
	private String vocabulary;
	private String unsatVocabulary;
	private String theory;
	private String unsatStructure;
	private Singleton s;

	public IDPExplanation(Singleton s) {
		this.s = s;
		String path = s.getExplanationsPath();
		inferences = new HashMap<String, String>();
		readMap(inferences, path + "inference.txt");
		vocabulary = read(path + "vocabulary.txt");
		unsatVocabulary = read(path + "unsatvoc.txt");
		theory = read(path + "theory.txt");
		unsatStructure = read(path + "unsatstruc.txt");
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
	
	private String build(String structure, String inference) {
		String input = "";
		input += "Vocabulary V { \n";
		input += vocabulary + "}\n\n";
		input += "Theory T : V { \n";
		input += theory + "}\n\n";
		input += "Structure S : V { \n";
		input += structure + "\n";
		input += unsatStructure + "}\n\n";
		input += "Vocabulary U { \n";
		input += unsatVocabulary + "}\n\n";
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
	
	public String unsat(String structure, int freeVariables) {
		String input = build(structure, "unsat");
		return open(input, "explanation", "", freeVariables);
	}
}
