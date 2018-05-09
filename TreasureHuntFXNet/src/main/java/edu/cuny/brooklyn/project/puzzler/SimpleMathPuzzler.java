package edu.cuny.brooklyn.project.puzzler;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMathPuzzler extends MathPuzzler {
	private final static Logger LOGGER = LoggerFactory.getLogger(SqrtMathPuzzler.class);
	
	private double toleratedAnswerRelativeError;
    private double answerValue;
    //private boolean allowError;
	
	private Random rng;
	
	public SimpleMathPuzzler(int minNumber, int maxNumber, double toleratedAnswerRelativeError) {
		this.toleratedAnswerRelativeError = toleratedAnswerRelativeError;
		//allowError = false;
		rng = new Random();
		if (minNumber <= 0) {
			LOGGER.warn("minNumber = " + minNumber + ", but expecting a number > 0. Use 2 instead ");
		}
		if (maxNumber <= 2) {
			LOGGER.warn("maxNumber = " + maxNumber + ", but expecting a number > 1. Use 2 instead ");			
		}
        int leftNum = minNumber + rng.nextInt(maxNumber - minNumber + 1),
            rightNum = minNumber + rng.nextInt(maxNumber - minNumber + 1),
            operand = rng.nextInt(2);
        String message;

        switch (operand) {
            case 0:
                message = String.format("Add(%s, %s)", leftNum, rightNum);
                answerValue = leftNum + rightNum;
                break;
            case 1:
                message = String.format("Subtract(%s, %s)", leftNum, rightNum);
                answerValue = leftNum - rightNum;
                break;
            /*case 2:
                message = String.format("Multiply(%s, %s)", leftNum, rightNum);
                answerValue = leftNum * rightNum;
                break;
            case 3:
                allowError = true;
                message = String.format("Divide(%s, %s)", leftNum, rightNum);
                answerValue = ((double) leftNum) / rightNum;
                break;
            */
                default:
                LOGGER.debug("Unsupported Simple Math Puzzler operand: This should never run! operand: " + operand);
                throw new RuntimeException("Unsupported Simple Math Puzzler operand: This should never run!");
        }
		//answerValue = Math.sqrt(num);
		String answer = Double.toString(answerValue);
		setMessage(message);
		setAnswer(answer);
	}
	

	public boolean isCorrect(String enteredAnswer) {
		double entered = Double.parseDouble(enteredAnswer);
		/*if (Math.abs((entered - answerValue) / answerValue) < toleratedAnswerRelativeError && allowError) {
			LOGGER.debug("Correct answer");
			return true;
        } else*/ 
        if (entered == answerValue) {
            LOGGER.debug("Correct answer");
            return true;
        } else {
			LOGGER.debug("Incorrect answer");
			return false;
		}
	}

}