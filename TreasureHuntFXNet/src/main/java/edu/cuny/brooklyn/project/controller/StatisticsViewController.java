package edu.cuny.brooklyn.project.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StatisticsViewController {
	@FXML
	private Label maxAttemptAnswerPuzzler;
	
	@FXML
	private Label minAttemptAnswerPuzzler;
	
	@FXML
	private Label averageAttemptAnswerPuzzler;
	
	@FXML
	private Label totalNumberAttempt;
	
	@FXML
	private Label totalRound;
	
	@FXML
	private Label minAttemtpLocateTreasure;
	
	@FXML
	private Label averageAttemptLocateTreasure;
	
	@FXML 
	private Label maxAttemptLocateTreasure;
	
	@FXML
	private Label roundScore;
	
	@FXML
	private Label totalScore;
	
	@FXML
	private Button quitButton;
	
	private GameStatistics statistics;
	
	public void setOnQuitButtonAction(EventHandler<ActionEvent> handler) {
		quitButton.setOnAction(handler);
	}
	
	public void showAll(){
		maxAttemptAnswerPuzzler.setText("max puzzler attempts: "+Integer.toString(statistics.getMaxAttemptPuzzler()));
		minAttemptAnswerPuzzler.setText("min puzzler attempts: "+Integer.toString(statistics.getMinAttemptsPuzzler()));
		averageAttemptAnswerPuzzler.setText("average puzzler attempts: "+Double.toString(statistics.getAverageAttemptsPuzzler()));
		totalNumberAttempt.setText("total attempts: "+Integer.toString(statistics.getTotalAttempts()));
		totalRound.setText("total round played: "+Integer.toString(statistics.getTotalRounds()));
		minAttemtpLocateTreasure.setText("min locate attempts: "+Integer.toString(statistics.getMinAttemptsLocate()));
		maxAttemptLocateTreasure.setText("max locate attemtps: "+Integer.toString(statistics.getMaxAttemptsLocate()));
		averageAttemptLocateTreasure.setText("average locate attempts: "+Double.toString(statistics.getAverageAttemptsLocate()));
		roundScore.setText("round score: "+Integer.toString(statistics.getTotalRounds()));
		totalScore.setText("total score: "+Integer.toString(statistics.getTotalScore()));
	}
	
	
	public void setStatistics(GameStatistics statistics){
		this.statistics=statistics;
	}
}
