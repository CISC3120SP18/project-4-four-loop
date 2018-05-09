package edu.cuny.brooklyn.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.puzzler.Puzzler;
import edu.cuny.brooklyn.project.puzzler.PuzzlerMaker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PuzzlerFrameViewController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PuzzlerFrameViewController.class);
	
    @FXML
    private Label puzzlerLabel;

    @FXML
    private Button answerButton;

    @FXML
    private TextField puzzlerAnswer;
    
    //serialize them
	private PuzzlerMaker puzzlerMaker;
	private Puzzler puzzler;
	private int  answeringAttempts;

	private GameStatistics statistics;
	
	public void initialize() {
		puzzlerMaker = new PuzzlerMaker();
	}
	

	public int getAnsweringAttempts() {
		return answeringAttempts;
	}  
	
	public void showNewPuzzler(int type) {

		puzzler = puzzlerMaker.makePuzzler(type);
		puzzlerLabel.setText(puzzler.getMessage());
		answeringAttempts = 0;
	}
	
	public void setOnAnswerButtonAction(EventHandler<ActionEvent> handler) {
		answerButton.setOnAction(handler);
	}
	
	public boolean answerPuzzler() {
		String answer = puzzlerAnswer.getText();
		puzzlerAnswer.clear();//clear the textField after user enter the answer.
		if (answer.isEmpty()) {
			LOGGER.debug("User's answer to the puzzler is empty!");
			return false;
		}
		
		answeringAttempts ++;
		
		if (!puzzler.isCorrect(answer)) {
			LOGGER.debug("User's answer to the puzzler is wrong! This is attempt #" + answeringAttempts);
			return false;
		} else {
			statistics.updateAttemptPuzzler(answeringAttempts);
			LOGGER.debug("User's answer to the puzzler is correct, move on to locate the treasure." );
			return true;
		}
	}

	//--------------------
	public void reflashPuzzlerLabel(){
		puzzlerLabel.setText(puzzler.getMessage());
	}
	//----------------------
	
	//---------------------------------
	public void setAnsweringAttempts(int attempts){
		answeringAttempts = attempts;
	}

	public void setGameStatistics(GameStatistics statistics) {
		// TODO Auto-generated method stub
		this.statistics = statistics;
	}


	public void setPuzzler(Puzzler puzzler){
		this.puzzler = puzzler;
	}
	
	public Puzzler getPuzzler(){
		return puzzler;
	}
	
	public void setPuzzlerMaker(PuzzlerMaker puzzlerMaker){
		this.puzzlerMaker = puzzlerMaker;
	}
	
	public PuzzlerMaker getPuzzlerMaker(){
		return puzzlerMaker;
	}
	//----------------------------------------------
}
