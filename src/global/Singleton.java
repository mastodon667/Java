package global;

import java.io.File;
import java.util.ArrayList;

import gui.EdjucationSelection;

public final class Singleton {
    
	private static volatile Singleton instance;
	
	public final String IDP_LOCATION = "C:/Program Files/idp 3.5.0/bin/idp.bat";
	public final String PROJECT_LOCATION = "C:/Users/Herbert/workspace/ISP/";
	private Edjucation programme;
	
    private Singleton() { 
		EdjucationSelection select = new EdjucationSelection();
		programme = select.selectEdjucation();
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
		return PROJECT_LOCATION + "src/files/isp/";
	}
    
    public String getExplanationsPath() {
		return PROJECT_LOCATION + "src/files/explanations/";
	}
	
	public String getSchedulePath() {
		return PROJECT_LOCATION + "src/files/schedule/";
	}
	
	public String getJsonPath() {
		return PROJECT_LOCATION + "src/files/json/" + programme.getJson();
	}
	
	public ArrayList<String> getLessonsPaths() {
		File location = new File(PROJECT_LOCATION + "src/files/lessons/");
		ArrayList<String> files = new ArrayList<String>();
		for (File file : location.listFiles()) {
			files.add(file.getAbsolutePath());
		}
		return files;
	}
	
	public String getShadowPath() {
		return PROJECT_LOCATION + "src/files/shadow/shadowcourses.txt";
	}
	
	public String getResultPath() {
		return PROJECT_LOCATION + "src/files/log/results.txt";
	}
	
	public String getAutomatonPath() {
		return PROJECT_LOCATION + "src/files/automaton/" + programme.getAutomaton();
	}
	
	public String getVariablesPath() {
		return PROJECT_LOCATION + "src/files/automaton/" + programme.getVariables();
	}
}
