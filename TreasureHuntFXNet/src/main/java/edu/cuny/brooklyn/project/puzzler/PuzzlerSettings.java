package edu.cuny.brooklyn.project.puzzler;

import java.util.Random;

public class PuzzlerSettings {
	public final static int UNSUPPORTED_PUZZLER = -1;
	public final static int MATH_PUZZLER = 100;
	public final static int MATH_PUZZLER_SQRT = 101;
	public static final int WORD_PUZZLER = 102;
	public final static int MATH_PUZZLER_HOWMANYZERO=103;
	public final static int PUZZLER_MONUMENT = 104;
	public final static int MATH_PUZZLER_SIMPLE = 105;
	
	private static int[] puzzlerTypes = {
			MATH_PUZZLER_SQRT,
			WORD_PUZZLER,
			MATH_PUZZLER_HOWMANYZERO,
			PUZZLER_MONUMENT,
			MATH_PUZZLER_SIMPLE
	};
	
	private static Random rng = new Random();
	
	public static int getRandomPuzzlerType() {
		int typeIndex = rng.nextInt(puzzlerTypes.length);
		return puzzlerTypes[typeIndex]; 
	}
	
	public static int getEasyPuzzlerType() {
		int[] EasyPuzzlers = {
			WORD_PUZZLER,
			PUZZLER_MONUMENT
		};
		return EasyPuzzlers[rng.nextInt(EasyPuzzlers.length)];

	}

	public static int getNormalPuzzlerType() {
		int[] NormalPuzzlers = {
			MATH_PUZZLER_SQRT,
			MATH_PUZZLER_SIMPLE
		};
		return NormalPuzzlers[rng.nextInt(NormalPuzzlers.length)];
	}

	public static int getHardPuzzlerType() {
		int[] HardPuzzlers = {
			MATH_PUZZLER_SQRT,
			MATH_PUZZLER_HOWMANYZERO
		};
		return HardPuzzlers[rng.nextInt(HardPuzzlers.length)];
	}
}
