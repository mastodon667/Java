package parser;

import data.Group;

public class IDPParser {

	public String parseStructure(Group programme, String structure) {
		// domeinen
        String strStage = "Fase = {1.." + programme.getStages() + "}";
        String strCourse = "Vak = {" + programme.printCourse() + "}";
        String strConnected = "Verbonden = {" + programme.printConnected() + "}";
        String strGroup = "Groep = {" + programme.printGroup() + "}";
        String strCourseGroup = "VakGroep = {" + programme.printCourseGroup() + "}";
        String strProgramme = "Opleiding = {" + programme.printType("Opleiding") + "}";
        String strCommon = "AlgemeenVormend = {" + programme.printType("AlgemeenVormend") + "}";
        String strAdditionalSpecialisation = "VerdereSpecialisatie = {" + programme.printType("VerdereSpecialisatie") + "}";
        String strChoiceGroup = "KeuzeGroep = {" + programme.printChoiceGroup() + "}";
        String strSpecialisation = "Specialisatie = {" + programme.printType("Specialisatie") + "}";
        String strBachelor = "BachelorVerbredend = {" + programme.printType("BachelorVerbredend") + "}";
        String strEcts = "Studiepunten = {-" + programme.getMaxEcts() + ".." + programme.getMaxEcts() + "}";
        
        // predicaten
        String strMandatory = "Verplicht = {" + programme.printMandatoryCourses() + "}";
        String strInFase = "InFase = {" + programme.printInStage() + "}";
        String strInGroup = "InGroep = {" + programme.printInGroup() + "}";
        String strSelected = "Geselecteerd";
        if (structure.equals("consistent"))
        	strSelected += " = {" + programme.printSelected() + "}";
        else
        	strSelected += "<ct> = {" + programme.printSelected() + "}";
        String strNotInterested = "GeenInteresse = {" + programme.printNotInterested() + "}";

        // functies
        String strInSemester = "InSemester = {" + programme.printInSemester() + "}";
        String strMinEcts = "MinAantalStudiepunten = {" + programme.printMinEcts() + "}";
        String strMaxEcts = "MaxAantalStudiepunten = {" + programme.printMaxEcts() + "}";
        String strAmountOfEcts = "AantalStudiepunten = {" + programme.printAmountOfEcts() + "}";

        String s = "";
        s += strStage + "\n";
        s += strCourse + "\n";
        s += strConnected + "\n";
        s += strGroup + "\n";
        s += strCourseGroup + "\n";
        s += strProgramme + "\n";
        s += strCommon + "\n";
        s += strAdditionalSpecialisation + "\n";
        s += strChoiceGroup + "\n";
        s += strSpecialisation + "\n";
        s += strBachelor + "\n";
        s += strEcts + "\n";
        s += strMandatory + "\n";
        s += strInFase + "\n";
        s += strInGroup + "\n";
        s += strInSemester + "\n";
        s += strMinEcts + "\n";
        s += strMaxEcts + "\n";
        s += strAmountOfEcts + "\n";
        s += strSelected + "\n";
        s += strNotInterested + "\n";
        
        if (structure.equals("studiepunten")) {
        }
        else if (structure.equals("semester")) {
        }
        else if (structure.equals("lessenrooster")) {
        	String strSlot = "Slot = { 0..130 }";
            String strOverlap = "Overlap = { 0..1000 }";
            String strLesson = "Les = {" + programme.printLesson() + "}";

            String strHasLesson = "HeeftLes = {" + programme.printHasLesson() + "}";
            String strStarts = "Start = {" + programme.printStarts() + "}";
            String strEnds = "Eindigt = {" + programme.printEnds() + "}";
     	   
            s += strSlot + "\n";
            s += strOverlap + "\n";
            s += strLesson + "\n";
            s += strHasLesson + "\n";
            s += strStarts + "\n";
            s += strEnds + "\n";
        }
        return s;
	}
}
