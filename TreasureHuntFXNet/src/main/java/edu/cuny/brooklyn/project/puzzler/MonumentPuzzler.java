package edu.cuny.brooklyn.project.puzzler;

public class MonumentPuzzler extends Puzzler {
	
	public boolean isCorrect(String monument) {
		if(monument.equals(getAnswer())) {
			return true;
		}
		return false;
	}
	
	public MonumentPuzzler(String expectedAnswer, String message) {
		setMessage(message);
		setAnswer(expectedAnswer);
	}
	

}