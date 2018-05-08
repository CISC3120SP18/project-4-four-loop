package edu.cuny.brooklyn.project.treasure;

import java.io.Serializable;

import edu.cuny.brooklyn.project.GameSettings;

public class TreasureGenerator implements Serializable{
	public Treasure generate() {
		return new SquareTreasure(GameSettings.DEFAULT_TREASURE_SIZE);
	}
}
