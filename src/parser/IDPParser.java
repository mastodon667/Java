package parser;

import data.CourseSchedule;
import data.Group;

public class IDPParser {

	public String parseISPStructure(Group programme) {
		// domeinen
        String strStage = "Fase = {1.." + programme.getStages() + "}";
        String strCourse = "Vak = {";
        String strGroup = "VakGroep = {";
        String strEcts = "Studiepunten = {0.." + programme.getMaxEcts() + "}";

        // predicaten
        String strIsType = "IsType = {";
        String strMandatory = "Verplicht = {";
        String strInFase = "InFase = {";
        String strInGroup = "InVakGroep = {";
        String strSelected = "Geselecteerd<ct> = {";
        String strNotInterested = "GeenInteresse = {";

        // functies
        String strInSemester = "InSemester<ct> = {";
        String strMinEcts = "MinAantalStudiepunten<ct> = {";
        String strMaxEcts = "MaxAantalStudiepunten<ct> = {";
        String strAmountOfEcts = "AantalStudiepunten<ct> = {";

        strCourse += programme.printCourse() + "}";
        strIsType += programme.printIsType() + "}";
        strMandatory += programme.printMandatoryCourses() + "}";
        strInFase += programme.printInStage() + "}";
        strInGroup += programme.printInGroup() + "}";
        strSelected += programme.printSelected() + "}";
        strNotInterested += programme.printNotInterested() + "}";
        strInSemester += programme.printInSemester() + "}";
        strMinEcts += programme.printMinEcts() + "}";
        strMaxEcts += programme.printMaxEcts() + "}";
        strAmountOfEcts += programme.printAmountOfEcts() + "}";
        strGroup += programme.printGroup() + "}";

        String s = "";
        s += strStage + "\n";
        s += strCourse + "\n";
        s += strGroup + "\n";
        s += strEcts + "\n";
        s += strIsType + "\n";
        s += strMandatory + "\n";
        s += strInFase + "\n";
        s += strInGroup + "\n";
        s += strInSemester + "\n";
        s += strMinEcts + "\n";
        s += strMaxEcts + "\n";
        s += strAmountOfEcts + "\n";
        s += strSelected + "\n";
        s += strNotInterested + "\n";

        return s;
	}
        

   public String parseExplanationStructure(Group programme) {
        // domeinen
    	String strStage = "Fase = {1.." + programme.getStages() + "}";
    	String strCourse = "Vak = {";
    	String strGroup = "VakGroep = {";
    	String strEcts = "Studiepunten = {0.." + programme.getMaxEcts() + "}";

        // predicaten
    	String strIsType = "IsType = {";
    	String strMandatory = "Verplicht = {";
    	String strInFase = "InFase = {";
    	String strInGroup = "InVakGroep = {";
    	String strSelected = "Geselecteerd<ct> = {";
    	String strNotInterested = "NietGeselecteerd = {";

        // functies
    	String strMinEcts = "MinAantalStudiepunten<ct> = {";
    	String strMaxEcts = "MaxAantalStudiepunten<ct> = {";
    	String strAmountOfEcts = "AantalStudiepunten<ct> = {";

        strCourse += programme.printCourse() + "}";
        strIsType += programme.printIsType() + "}";
        strMandatory += programme.printMandatoryCourses() + "}";
        strInFase += programme.printInStage() + "}";
        strInGroup += programme.printInGroup() + "}";
        strSelected += programme.printSelected() + "}";
        strNotInterested += programme.printNotInterested() + "}";
        strMinEcts += programme.printMinEcts() + "}";
        strMaxEcts += programme.printMaxEcts() + "}";
        strAmountOfEcts += programme.printAmountOfEcts() + "}";
        strGroup += programme.printGroup() + "}";

        String s = "";
        s += strStage + "\n";
        s += strCourse + "\n";
        s += strGroup + "\n";
        s += strEcts + "\n";
        s += strIsType + "\n";
        s += strMandatory + "\n";
        s += strInFase + "\n";
        s += strInGroup + "\n";
        s += strMinEcts + "\n";
        s += strMaxEcts + "\n";
        s += strAmountOfEcts + "\n";
        s += strSelected + "\n";
        s += strNotInterested + "\n";

        return s;
    }
   
   public String parseScheduleStructure(CourseSchedule schedule) {
	   String s ="";
	   //TODO: ADD FUNCTIONALITY
	   return s;
   }
}
