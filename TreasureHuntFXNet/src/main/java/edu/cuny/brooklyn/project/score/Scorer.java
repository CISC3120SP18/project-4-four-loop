package edu.cuny.brooklyn.project.score;

import java.io.Serializable;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.controller.GameStatistics;


public class Scorer implements Serializable{

	private int totalScore;
	private int roundScore;
	
	public Scorer() {
		totalScore = 0;
		roundScore = 0;
	}
	
	
	public int getTotalScore() {
		//total_score=total_score+totalScore;
		return totalScore;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public void updateScore(int attempts) {
		roundScore =  GameSettings.MAX_SCORE - (attempts - 1) * GameSettings.SCORE_PENALTY*GameSettings.DEFAULT_DIFFULTY;
		totalScore += roundScore;
		
	}

}
