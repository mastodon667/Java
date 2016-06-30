package idp;

import global.Singleton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class IDPISP {

	private HashMap<String, String> inferences;
	private HashMap<String, String> terms;
	private String vocabulary;
	private String unsatVocabulary;
	private String theory;
	private Singleton s;

	public IDPISP(Singleton s) {
		this.s = s;
		String path = s.getIspPath();
		inferences = new HashMap<String, String>();
		readMap(inferences, path + "inference.txt");
		terms = new HashMap<String, String>();
		readMap(terms, path + "terms.txt");
		vocabulary = read(path + "vocabulary.txt");
		unsatVocabulary = read(path + "unsatvoc.txt");
		theory = read(path + "theory.txt");
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
		input += structure + "}\n\n";
		input += "Procedure main() { \n";
		input += inferences.get(inference) + "\n";
		input += "}\n";
		return input;
	}
	
	private String open(String input, String inference, String term) {
		String output = "";
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
		return output;
	}
	
	public ArrayList<String> getTerms() {
		return new ArrayList<String>(terms.keySet());
	}
	
	public boolean sat(String structure) {
		String input = build(structure, "sat");
		return open(input, "sat", "").contains("true");
	}
	
	public String unsat(String structure) {
		String input = build(structure, "unsat");
		return open(input, "unsat", "");
	}
	
	public String expand(String structure) {
		String input = build(structure, "expansion");
		return open(input, "expansion", "");
	}
	
	public String propagate(String structure) {
		String input = build(structure, "propagation");
		return open(input, "propagation", "");
	}
	
	public String minimize(String structure, String term) {
		String input = build(structure, "minimization");
		input += "Term O : V { \n";
		input += terms.get(term) + "\n}\n\n";
		return open(input, "minimization", "(" + term + ")");
	}
}
