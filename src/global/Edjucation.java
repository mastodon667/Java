package global;

public enum Edjucation {

	TI ("TI", "Master in de Toegepaste Informatica", "DomainTI.json", "AutomatonTI.txt", "VariablesTI.txt"),
	CW ("CW", "Master Computerwetenschappen", "DomainCW.json", "AutomatonCW.txt", "VariablesCW.txt");
	
	private String code;
	private String name;
	private String json;
	private String automaton;
	private String variables;
	
	Edjucation(String code, String name, String json, String automaton, String variables) {
		this.code = code;
		this.name = name;
		this.json = json;
		this.automaton = automaton;
		this.variables = variables;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getJson() {
		return json;
	}
	
	public String getAutomaton() {
		return automaton;
	}
	
	public String getVariables() {
		return variables;
	}
}
