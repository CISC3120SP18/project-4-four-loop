package edu.cuny.brooklyn.project.puzzler;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuzzlerMaker {
	private final static Logger LOGGER = LoggerFactory.getLogger(PuzzlerMaker.class);
	
	public Puzzler makePuzzler(int type) {
		//int type = PuzzlerSettings.getRandomPuzzlerType();
		LOGGER.debug("Puzzler type = " + type);
		Puzzler puzzler;
		switch(type) {
		case PuzzlerSettings.MATH_PUZZLER_SQRT: 
			puzzler = new SqrtMathPuzzler(5, 15, 0.05);
			LOGGER.debug("Made a math puzzler: message = " + puzzler.getMessage() + " and answer = " + puzzler.getAnswer());
			break;
		case PuzzlerSettings.WORD_PUZZLER:
			Random rng = new Random();
			
			int randomNum = rng.nextInt(4);
			puzzler = new WordPuzzlerQuestions(randomNum);
			LOGGER.debug("Made a word puzzler: message = " + puzzler.getMessage() + " and answer = " + puzzler.getAnswer());
			break;
		default:
			LOGGER.error("Unsupported puzzler type = " + type);
			puzzler = null;
			throw new RuntimeException("Selected a non-existing puzzler type. This should never happen.");
		}
		return puzzler;
	}
	
}