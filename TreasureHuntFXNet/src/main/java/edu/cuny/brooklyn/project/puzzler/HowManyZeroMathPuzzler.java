package edu.cuny.brooklyn.project.puzzler;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HowManyZeroMathPuzzler extends MathPuzzler{
	private final static Logger LOGGER = LoggerFactory.getLogger(HowManyZeroMathPuzzler.class);
	
	private int answerValue;
	
	private Random rng;
	
	public HowManyZeroMathPuzzler(int numRange){
		rng=new Random();
		int num = rng.nextInt(numRange)+numRange-200;//numRange-200 means lower bound of the range.
		String message="How many number of trailing zeros in "+num+" factorial";
		int zero=0;
		
		for (int i=5; num/i>=1; i *= 5) {
            zero += num/i;
        }
        answerValue = zero;
		String answer=Integer.toString(zero);
		setMessage(message);
		setAnswer(answer);
	}
	
	public boolean isCorrect(String enteredAnswer){
		int entered=Integer.parseInt(enteredAnswer);
		if(entered==answerValue){
			LOGGER.debug("Correct answer");
			return true;
		}
		else{
			LOGGER.debug("Incorrect answer");
			return false;
		}
	}
}