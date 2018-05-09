package edu.cuny.brooklyn.project.puzzler;

import java.io.Serializable;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;

public class PuzzlerMaker implements Serializable{
	private final static Logger LOGGER = LoggerFactory.getLogger(PuzzlerMaker.class);
	private int type;
	private double toleratedAnswerRelativeError=0.05;
	private int numberRange=200;
	
	public Puzzler makePuzzler(int type) {
		String diff = GameSettings.getDifficulty(); 
		
		if (diff == GameSettings.DIFF_EASY) {
			type = PuzzlerSettings.getEasyPuzzlerType();
			setDifficulty(1);
		} else if (diff == GameSettings.DIFF_NORMAL) {
			type = PuzzlerSettings.getNormalPuzzlerType();
			setDifficulty(2);
		} else { 
			type = PuzzlerSettings.getHardPuzzlerType();
			setDifficulty(3);
		}
		//type = PuzzlerSettings.getRandomPuzzlerType();
		//int type = PuzzlerSettings.getRandomPuzzlerType();
		LOGGER.debug("Puzzler type = " + type);
		Puzzler puzzler;
		switch(type) {
		case PuzzlerSettings.MATH_PUZZLER_SQRT: 
			puzzler = new SqrtMathPuzzler(5, 15, toleratedAnswerRelativeError);
			LOGGER.debug("Made a math puzzler: message = " + puzzler.getMessage() + " and answer = " + puzzler.getAnswer());
			break;
		case PuzzlerSettings.WORD_PUZZLER:
			Random rng = new Random();
			
			int randomNum = rng.nextInt(4);
			puzzler = new WordPuzzlerQuestions(randomNum);
			LOGGER.debug("Made a word puzzler: message = " + puzzler.getMessage() + " and answer = " + puzzler.getAnswer());
			break;
		case PuzzlerSettings.MATH_PUZZLER_HOWMANYZERO:
			puzzler = new HowManyZeroMathPuzzler(numberRange);
			LOGGER.debug("Made a math puzzler: message = " + puzzler.getMessage() + " and answer = " + puzzler.getAnswer());
			break;
		case PuzzlerSettings.PUZZLER_MONUMENT:
			puzzler = new MonumentPuzzler("Statue of Liberty","What was given to NYC by the French");
			LOGGER.debug("Made a monument puzzler: message = " + puzzler.getMessage() + " and answer = " + puzzler.getAnswer());
			break;
		case PuzzlerSettings.MATH_PUZZLER_SIMPLE:
			puzzler = new SimpleMathPuzzler(0, numberRange, toleratedAnswerRelativeError);
			LOGGER.debug("Made a simple math puzzler: message = " + puzzler.getMessage() + " and answer = " + puzzler.getAnswer());
			break;
		default:
			LOGGER.error("Unsupported puzzler type = " + type);
			puzzler = null;
			throw new RuntimeException("Selected a non-existing puzzler type. This should never happen.");
		}
		return puzzler;
	}
	
	public int getPuzzlerType(){
		return type;
	}
	//change the difficulty
	public void setDifficulty(int level){
		switch(level){
		case 1:
			toleratedAnswerRelativeError=0.05;
			numberRange=200;
			break;
		case 2:
			toleratedAnswerRelativeError=0.005;
			numberRange=400;
			break;
		case 3:
			toleratedAnswerRelativeError=0.0005;
			numberRange=1000;
			break;
		}
	}
}