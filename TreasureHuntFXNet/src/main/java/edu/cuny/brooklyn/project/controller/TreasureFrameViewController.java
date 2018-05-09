package edu.cuny.brooklyn.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.score.Scorer;
import edu.cuny.brooklyn.project.treasure.TreasureField;
import edu.cuny.brooklyn.project.validator.Validator;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TreasureFrameViewController {
	private final static Logger LOGGER = LoggerFactory.getLogger(TreasureFrameViewController.class);
	
    @FXML
    private TextField xPosTreasure;

    @FXML
    private TextField yPosTreasure;

    @FXML
    private Button buttonTreasure;

    @FXML
    private Button buttonContinue;
    
    @FXML
    private Button buttonQuit;
    
    @FXML
    private Label totalScoreLabel;

    @FXML
    private Label roundScoreLabel;

    @FXML
    private Label clueLabel;

    @FXML
    private Label responseLabel;

    @FXML
    private Canvas canvas;
    
    @FXML
    private StackPane canvasHolder;

    //those three variable has to serialization
	private Scorer scorer;
	private int puzzlerAttempts;
	private TreasureField treasureField;
	
	private GameStatistics statistics;
	private int locateAttempts;
	
	private Circle circle;
	
	// for resizing
	private InvalidationListener resizeListener = o -> redrawTreasure();
	
	public void initialize() {
		scorer = new Scorer();
		puzzlerAttempts = 0;
		treasureField = new TreasureField();
		initializeScore();
		
		buttonTreasure.setOnAction(e -> doTreasureLocationAction());
		
		canvas.widthProperty().bind(canvasHolder.widthProperty().subtract(20));
		canvas.heightProperty().bind(canvasHolder.heightProperty().subtract(20));
		
		//------try let mouse click on the panel to choose the coordinate
		//------and the coordinate show on the two TextField-------
		canvasHolder.setOnMouseReleased(e ->{
			int xPos = (int)(e.getX()/(canvasHolder.getWidth())*(double)treasureField.getFieldWidth());
			int yPos = (int)(e.getY()/(canvasHolder.getHeight())*(double)treasureField.getFieldHeight());
			xPosTreasure.setText(Integer.toString(xPos));
			yPosTreasure.setText(Integer.toString(yPos));
		});
		
		circle = new Circle(3);
		circle.setFill(Color.RED);
		canvasHolder.getChildren().add(circle);
		//------------------------------------------------
		
	}
	
	
	
	public TreasureField getTreasureField() {
		return treasureField;
	}
	

	public void setAttempts(int answeringAttempts) {
		puzzlerAttempts = answeringAttempts;
	}

	public void startLocatingTreasure(String clue) {
		startGuessing(clue);

		canvas.widthProperty().removeListener(resizeListener);
		canvas.heightProperty().removeListener(resizeListener);
		//-----------display a circle that indicate the coordinate by clue given---
		displayClueByCircle(clue);
	}
	
	//----------get xpos, ypos, and clueError from TreasureClue----
	//----------the radius of circle adjust by ClueError.----------
	public void displayClueByCircle(String clue){
		double canvasH=GameSettings.NEW_CANVAS_HEIGHT;
		double canvasW=GameSettings.NEW_CANVAS_WIDTH;

		int Xpos = TreasureClue.getX();
		int Ypos = TreasureClue.getY();
		double y = -canvasH/2+((double)Ypos)/(double)treasureField.getFieldHeight()*canvasH;
		double x = -canvasW/2+((double)Xpos)/(double)treasureField.getFieldWidth()*canvasW;
		int radius = TreasureClue.getClueError();
		circle.setRadius(radius/3);
		circle.setTranslateX(x);
		circle.setTranslateY(y);
	}

	
	public void doTreasureLocationAction() {
		String xInputText = xPosTreasure.getText();
		String yInputText = yPosTreasure.getText();
		//clear textField after user entered the location.
		xPosTreasure.clear();
		yPosTreasure.clear();
		locateAttempts++;
		Validator validator = new Validator();
		
		int xInput = -1;
		int yInput = -1;
		if (xInputText.isEmpty()) {
			LOGGER.debug("User hasn't guessed X position of the treasure.");
		}

		if (yInputText.isEmpty()) {
			LOGGER.debug("User hasn't guessed Y position of the treasure.");
		}
		//---------validate the input-------------------------------
		if(!validator.isValidCoordinates(xInputText,yInputText)){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("Invalid coordinates input, try again");
			alert.showAndWait();
			xPosTreasure.clear();
			yPosTreasure.clear();
			return;
		}
		
		xInput = Integer.parseInt(xInputText);
		yInput = Integer.parseInt(yInputText);
		
		if (treasureField.foundTreasure(xInput, yInput)) {
			LOGGER.debug("Found treasure at location (" + xInput + "," + yInput + ")");
			doneGuessing();
			showTreasure();
			updateScore();
			statistics.addRound();
			//----update attempts of locate treasure, and score--------------
			statistics.updateAttemptTreasure(locateAttempts);
			statistics.updateScore(scorer.getRoundScore(), scorer.getTotalScore());
		} else {
			LOGGER.debug("No treasure at location (" + xInput + "," + yInput + ")");
			responseLabel.setVisible(true);
			responseLabel.setText(I18n.getBundle().getString(GameSettings.MSG_NO_LABEL_AT_LOCATION_KEY) + " (" + xInput + "," + yInput + ")");
		}
	}
	
	public void setOnButtonTreasureAction(EventHandler<ActionEvent> handler) {
		buttonTreasure.setOnAction(handler);
	}

	public void setOnContinueButtonAction(EventHandler<ActionEvent> handler) {
		buttonContinue.setOnAction(handler);
	}
	
	public void setOnQuitButtonAction(EventHandler<ActionEvent> handler) {
		buttonQuit.setOnAction(handler);
	}
	
	
	private void initializeScore() {
		totalScoreLabel.setText(String.format(GameSettings.SCORE_FORMAT, 0));
		roundScoreLabel.setText(String.format(GameSettings.SCORE_FORMAT, 0));
//		new Label(I18n.getBundle().getString(MSG_TOTAL_SCORE_KEY)),
//		new Label(I18n.getBundle().getString(MSG_ROUND_SCORE_KEY)),
	}

	//----set this method public---is invokes in frameContainer------
	public void clearCanvas() {
		canvas.getGraphicsContext2D().clearRect(0,  0,  canvas.getWidth(), canvas.getHeight());
	}

	
	private void doneGuessing() {
		clueLabel.setVisible(false);
		responseLabel.setVisible(false);
		xPosTreasure.setDisable(true);
		yPosTreasure.setDisable(true);
		buttonTreasure.setDisable(true);
	}
	
	private void startGuessing(String clue) {
		clueLabel.setText(clue);
		clueLabel.setVisible(true);
		responseLabel.setVisible(false);
		xPosTreasure.setDisable(false);
		yPosTreasure.setDisable(false);
		buttonTreasure.setDisable(false);
	}
	
	private void drawTreasure() {
		LOGGER.debug("redraw");
		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();
		double y = (double)treasureField.getTreasureYTop()/(double)treasureField.getFieldHeight()*canvasHeight;
		double x = (double)treasureField.getTreasureXLeft()/(double)treasureField.getFieldWidth()*canvasWidth;
		
		Image image = treasureField.getTreasureImage();
		double w = image.getWidth()/(double)treasureField.getFieldWidth()*canvasWidth;
		double h = image.getHeight()/(double)treasureField.getFieldHeight()*canvasHeight;

		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.drawImage(image, x, y, w, h);
	}
	
	private void redrawTreasure() {
		clearCanvas();
		drawTreasure();
	}
	
	private void showTreasure() {
		drawTreasure();
		
		canvas.widthProperty().addListener(resizeListener);
		canvas.heightProperty().addListener(resizeListener);
	}
	
	private void updateScore() {
		scorer.updateScore(puzzlerAttempts);
		totalScoreLabel.setText(String.format(GameSettings.SCORE_FORMAT, scorer.getTotalScore()));
		roundScoreLabel.setText(String.format(GameSettings.SCORE_FORMAT, scorer.getRoundScore()));
	}
	//---------------------------
	public Scorer getScorer(){
		return scorer;
	}
	
	public void setScorer(Scorer scorer){
		this.scorer = scorer;
	}
	
	public void setTreasureField(TreasureField treasureField){
		this.treasureField = treasureField;
	}
	
	public int getAttempts() {
		return puzzlerAttempts;
	}
	
	public void setStatistics(GameStatistics statistics){
		this.statistics=statistics;
	}
}
