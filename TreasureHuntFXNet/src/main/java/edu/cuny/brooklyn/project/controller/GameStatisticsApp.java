package edu.cuny.brooklyn.project.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class GameStatisticsApp extends Application {
	
	//public static void main(String[] args) {
	//	launch(args);
	//}

	private final static Logger LOGGER = LoggerFactory.getLogger(GameStatisticsApp.class);

	
	
	private int total_attempts;
	private int total_rounds;
	private int average_attempt_puzzler;
	private int min_attempt_puzzler;
	private int max_attempt_puzzler;
	private int average_attempt_location;
	private int min_attempt_location;
	private int max_attempt_location;
	private int round_score;
	private int total_score;
	
	public void updateScore(int roundScore, int totalScore) {
		round_score=roundScore;
		total_score=totalScore;
		
	}
	public void setAttempts(int answeringAttempts) {
		max_attempt_location=answeringAttempts;
		total_attempts=max_attempt_location;
	}	
	
	public void start(Stage primaryStage) { 
		
		Label label = new Label ("You earned in this game:  "); 
		
		HBox hbox = new HBox(label); 
		hbox.setAlignment(Pos.TOP_CENTER); 
		Scene scene = new Scene(hbox, 550, 450); 
		primaryStage.setTitle("User's game information: "); 
		primaryStage.setScene(scene); 
		primaryStage.show(); 
		} 
		}


