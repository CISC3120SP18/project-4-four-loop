package edu.cuny.brooklyn.project.controller;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;

public class TreasureClue {
	private final static Logger LOGGER = LoggerFactory.getLogger(TreasureClue.class);
	
	private static Random rng = new Random();
	
	/*
	 * In fact, we can have different types of clues. That can make the game a little more interesting. 
	 */
	private static int x;
	private static int y;
	private static int clueError;

	public static String getClue(int xLeft, int yTop, int width, int length, int attempts) {
		clueError = (int) (attempts * GameSettings.DEFAULT_CLUE_RELATIVE_ERROR_INCREMENT * Math.max(width, length));
		int xOffset = rng.nextInt(clueError) - clueError / 2;
		int yOffset = rng.nextInt(clueError) - clueError / 2;
		
		// x and y may be out of bound, so are user's inputs
		x = xLeft + width/2 + xOffset;
		y = yTop + length/2 + yOffset;
		
		LOGGER.debug(String.format("Treasure is at (xLeft, yTop) -- (xRight, yBottom) = (%d, %d) -- (%d, %d)",
				xLeft, yTop, xLeft + width, yTop + length));
		LOGGER.debug(String.format("Clue is given at (x, y) = (%d, %d)", x, y));
		
		return I18n.getBundle().getString(GameSettings.MSG_TREASURE_CLUE_PART_1_KEY) 
				+ " " +  Math.max(width, length) + " " + 
				I18n.getBundle().getString(GameSettings.MSG_TREASURE_CLUE_PART_2_KEY) + " (" + x + "," + y + ").";
	}

	public static int getX(){
		return x;
	}
	
	public static int getY(){
		return y;
	}

	public static int getClueError(){
		return clueError;
	}
}
