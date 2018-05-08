package edu.cuny.brooklyn.project.puzzler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordPuzzlerQuestions extends WordPuzzler {
		private final static Logger LOGGER = LoggerFactory.getLogger(WordPuzzler.class);
		private String answerValue;
			
			public WordPuzzlerQuestions (int questionNumber) {
				
				if(questionNumber == 0) {
					String message = "In what year did Java release?" ;
					answerValue = "1995" ;
					setMessage(message);
					setAnswer(answerValue);
				}
				
				if(questionNumber == 0) {
					String message = "String is primitive data type in java(enter 'true' or 'false')" ;
					answerValue = "false" ;
					setMessage(message);
					setAnswer(answerValue);
				}
				
				if(questionNumber == 0) {
					String message = "Java always passes an argument by it value?(enter 'true' or 'false')" ;
					answerValue = "true" ;
					setMessage(message);
					setAnswer(answerValue);
				}
				
				if(questionNumber == 0) {
					String message = "In Java, down-casting is always allowed.(enter 'true' or 'false')" ;
					answerValue = "true" ;
					setMessage(message);
					setAnswer(answerValue);
				}
				
				if(questionNumber == 0) {
					String message = "In Java, construtor have a return type" ;
					answerValue = "false" ;
					setMessage(message);
					setAnswer(answerValue);
				}
				
				
			}
			
		public boolean isCorrect (String enteredAnswer) { 
			String entered = (enteredAnswer);
			if(entered.equalsIgnoreCase(answerValue)) { 
				LOGGER.debug("Correct answer!");
				return true;
			} else {
				LOGGER.debug("Incorrect answer!");
				return false;
			}
			
		}
}
