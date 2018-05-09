package edu.cuny.brooklyn.project.controller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.state.TreasureHuntState;


public class GameStatistics implements Serializable{
	
	//public static void main(String[] args) {
	//	launch(args);
	//}

	private final static Logger LOGGER = LoggerFactory.getLogger(GameStatistics.class);

	private TreasureHuntState treasureHuntState;
	
	private int solvedPuzzler;
	private int foundTreasure;
	private int total_attempts;
	private int total_rounds;
	private double average_attempt_puzzler;
	private int min_attempt_puzzler;
	private int max_attempt_puzzler;
	private double average_attempt_location;
	private int min_attempt_location;
	private int max_attempt_location;
	private int round_score;
	private int total_score;
	
	public GameStatistics(){
		solvedPuzzler=0;
		foundTreasure=0;
		total_attempts=0;
		total_rounds=0;
		average_attempt_puzzler=0;
		min_attempt_puzzler=0;
		max_attempt_puzzler=0;
		average_attempt_location=0;
		min_attempt_location=0;
		max_attempt_location=0;
		round_score=0;
		total_score=0;
	}
	
	public void addRound(){
		total_rounds++;
	}
	
	public void addAttempts(){
		total_attempts++;
	}
	
	public void updateAttemptPuzzler(int attempts){
		if(attempts>max_attempt_puzzler)
			max_attempt_puzzler=attempts;
		else if(attempts<min_attempt_puzzler)
			min_attempt_puzzler=attempts;
		
		average_attempt_puzzler=(average_attempt_puzzler*solvedPuzzler+attempts)/++solvedPuzzler;
	}
	
	public void updateAttemptTreasure(int attempts){
		if(attempts>max_attempt_location)
			max_attempt_location=attempts;
		else if(attempts<min_attempt_location)
			min_attempt_location=attempts;
		
		average_attempt_location=(average_attempt_location*foundTreasure+attempts)/++foundTreasure;
	}
	
	public void updateScore(int roundScore, int totalScore) {
		round_score=roundScore;
		total_score=totalScore;
		
	}
	public void setAttempts(int answeringAttempts) {
		max_attempt_location=answeringAttempts;
		total_attempts=max_attempt_location;
	}	
	
	public int getRoundScore(){
		return round_score;
	}
	
	public int getTotalScore(){
		return total_score;
	}
	
	public int getTotalAttempts(){
		return total_attempts;
	}
	
	public int getTotalRounds(){
		return total_rounds;
	}
	
	public int getMaxAttemptPuzzler(){
		return max_attempt_puzzler;
	}
	
	public int getMinAttemptsPuzzler(){
		return min_attempt_puzzler;
	}
	
	public double getAverageAttemptsPuzzler(){
		return average_attempt_puzzler;
	}
	
	public int getMaxAttemptsLocate(){
		return max_attempt_location;
	}
	
	public int getMinAttemptsLocate(){
		return min_attempt_location;
	}
	
	public double getAverageAttemptsLocate(){
		return average_attempt_location;
	}
	//------setters-------------------------------------
	
	
	public void setRoundScore(int round_score){
		this.round_score=round_score;
	}
	
	public void setTotalScore(int total_score){
		this.total_score=total_score;
	}
	
	public void setTotalAttempts(int total_attempts){
		this.total_attempts=total_attempts;
	}
	
	public void setTotalRounds(int total_rounds){
		this.total_rounds=total_rounds;
	}
	
	public void setMaxAttemptPuzzler(int max_attempt_puzzler){
		this.max_attempt_puzzler=max_attempt_puzzler;
	}
	
	public void setMinAttemptsPuzzler(int min_attempt_puzzler){
		this.min_attempt_puzzler=min_attempt_puzzler;
	}
	
	public void setAverageAttemptsPuzzler(double average_attempt_puzzler){
		this.average_attempt_puzzler=average_attempt_puzzler;
	}
	
	public void setMaxAttemptsLocate(int max_attempt_location){
		this.max_attempt_location=max_attempt_location;
	}
	
	public void setMinAttemptsLocate(int min_attempt_location){
		this.min_attempt_location=min_attempt_location;
	}
	
	public void setAverageAttemptsLocate(double average_attempt_location){
		this.average_attempt_location=average_attempt_location;
	}
	
//	public void start(Stage primaryStage) { 
//		
//		Label label = new Label ("You earned in this game:  "); 
//		
//		HBox hbox = new HBox(label); 
//		hbox.setAlignment(Pos.TOP_CENTER); 
//		Scene scene = new Scene(hbox, 550, 450); 
//		primaryStage.setTitle("User's game information: "); 
//		primaryStage.setScene(scene); 
//		primaryStage.show(); 
//		} 
	
}


