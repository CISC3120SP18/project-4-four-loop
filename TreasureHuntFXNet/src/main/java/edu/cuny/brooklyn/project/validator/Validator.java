package edu.cuny.brooklyn.project.validator;


import static edu.cuny.brooklyn.project.GameSettings.DIFF_EASY;
import static edu.cuny.brooklyn.project.GameSettings.DIFF_HARD;
import static edu.cuny.brooklyn.project.GameSettings.DIFF_NORMAL;

import java.util.InputMismatchException;

public class Validator {
	public static boolean isValidCoordinates(String x, String y) {
        try{
        	int xPos = Integer.parseInt(x);
        	int yPos = Integer.parseInt(y);
        }catch(InputMismatchException e){
        	return false;
        }catch(NumberFormatException e){
        	return false;
        }
        return true;
    }

    public static boolean isValidFloatingAnswer(String answer) {
        try{
        	double a = Double.parseDouble(answer);
        }catch(NumberFormatException e){
        	return false;
        }catch(InputMismatchException e){
        	return false;
        }
        return true;
    }
    
    public static boolean isValidDifficulty(String diff) {
        return (diff == DIFF_EASY) || (diff == DIFF_NORMAL) || (diff == DIFF_HARD);
    }
}
