package global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import gui.EdjucationSelection;

public final class Singleton {
    
	private static volatile Singleton instance;
	
	private String IDP_LOCATION;
	private Edjucation programme;
	
    private Singleton() { 
		EdjucationSelection select = new EdjucationSelection();
		programme = select.selectEdjucation();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.getConfigPath()));
			String line = br.readLine();
			if (line == null)
				line = "";
			IDP_LOCATION = line;
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static Singleton getInstance() {
        if (instance == null ) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
    
    public String getIspPath() {
		return "src/files/isp/";
	}
    
    public String getIdpPath() {
    	return IDP_LOCATION;
    }
    
    public String getExplanationsPath() {
		return "src/files/explanations/";
	}
    
    public String getConfigPath() {
    	return "src/files/config/config.txt";
    }
	
	public String getSchedulePath() {
		return "src/files/schedule/";
	}
	
	public String getJsonPath() {
		return "src/files/json/" + programme.getJson();
	}
	
	public ArrayList<String> getLessonsPaths() {
		File location = new File("src/files/lessons/");
		ArrayList<String> files = new ArrayList<String>();
		for (File file : location.listFiles()) {
			files.add(file.getAbsolutePath());
		}
		return files;
	}
	
	public String getShadowPath() {
		return "src/files/shadow/shadowcourses.txt";
	}
	
	public String getResultPath() {
		return "src/files/log/results.txt";
	}
	
	public String getAutomatonPath() {
		return "src/files/automaton/" + programme.getAutomaton();
	}
	
	public String getVariablesPath() {
		return "src/files/automaton/" + programme.getVariables();
	}
}
