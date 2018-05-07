package edu.cuny.brooklyn.project.puzzler;

public abstract class WordPuzzler extends Puzzler {
	public WordPuzzler() {
		this(null, null);
	}
	
	public WordPuzzler(String message, String answer) {
		super(message, answer, PuzzlerSettings.WORD_PUZZLER);
	}
}
