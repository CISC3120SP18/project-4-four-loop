package edu.cuny.brooklyn.project.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusBroadcaster;
import edu.cuny.brooklyn.project.net.StatusMessage;
import edu.cuny.brooklyn.project.net.StatusReciever;
import edu.cuny.brooklyn.project.puzzler.PuzzlerSettings;
import edu.cuny.brooklyn.project.state.TreasureHuntState;
import edu.cuny.brooklyn.project.treasure.TreasureGenerator;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.ListChangeListener.Change;

import javafx.scene.layout.*;
import javafx.scene.control.*;


public class FrameContainer {
	private final static Logger LOGGER = LoggerFactory.getLogger(FrameContainer.class);
	private static boolean FIRST_RUN = true;
	private Thread recievingThread;
	private TextInputDialog nameDialog;

	private Stage stage, multiSetup, statisticsStage;
	private Scene scene, multiPopup, statisticsScene;
	
	private Parent mainView;
	private FrameViewController mainViewController;
	
	private Parent treasureFrame;
	private TreasureFrameViewController treasureFrameController;
	
	private Parent puzzlerFrame;
	private PuzzlerFrameViewController puzzlerFrameController;
	
	private Parent flashFrame;
	private FlashFrameViewController flashFrameController;

	private Parent multiplayerFrame;
	private MultiplayerViewController multiplayerViewController;
	
	private Parent statisticsFrame;
	private StatisticsViewController statisticsViewController;
	
	private TreasureGenerator treasureGenerator;
	
	private TreasureHuntState treasureHuntState;
	
	private StatusBroadcaster statusBroadcaster;

	private StatusReciever statusReciever;
	
	private GameStatistics statistics;

	//add title
	public String title;
	public String clue;

	
	public FrameContainer(Stage stage, ResourceBundle bundle, StatusReciever statusReciever) throws IOException {
		this.statusReciever = statusReciever;
		initializeContainer(stage, bundle);
	}
	
	//private static void print(String s) { System.out.println(s); }
	public void reload(ResourceBundle bundle) throws IOException {
		initializeContainer(stage, bundle);
		showFlashScreen(true);
	}

	public void setStatusBroadcaster(StatusBroadcaster statusBroadcaster) {
		if (statusBroadcaster == null) {
			throw new IllegalArgumentException("StatusBroadcaster object must not be null.");
		}
		this.statusBroadcaster = statusBroadcaster;
		
		mainViewController.setStatusBroadcaster(this.statusBroadcaster);
	}
	

	public void setGameStatistics (GameStatistics statistics) {
		if (statistics == null) {
			throw new IllegalArgumentException("StatusBroadcaster object must not be null.");
		}
		this.statistics = statistics;
		
	}
	
	
	public void showFlashScreen() {
		showFlashScreen(false);
	}

	public void showFlashScreen(boolean reload) {
		LOGGER.debug("showing flash screen.");
		showScreenWithFrame(reload, this.flashFrame, GameSettings.MSG_APP_TITLE_FLASH_KEY);
	}
	
	
	private void answerPuzzler() {
		LOGGER.debug("solving puzzler.");
		if (puzzlerFrameController.answerPuzzler()) {
			clue = TreasureClue.getClue(treasureFrameController.getTreasureField().getTreasureXLeft(),
					treasureFrameController.getTreasureField().getTreasureYTop(),
					treasureFrameController.getTreasureField().getTreasureBoundingBoxWidth(),
					treasureFrameController.getTreasureField().getTreasureBoundingBoxLength(),
					puzzlerFrameController.getAnsweringAttempts());
			treasureFrameController.setAttempts(puzzlerFrameController.getAnsweringAttempts());
			treasureFrameController.startLocatingTreasure(clue);
			showTreasureScreen();
		}
	}
	
	private void initializeContainer(Stage stage, ResourceBundle bundle) throws IOException {
		if (treasureHuntState == null) {
			treasureGenerator = new TreasureGenerator();
		}
		
		if (treasureHuntState == null) {
			treasureHuntState = new TreasureHuntState();
		}
		if(treasureHuntState.readStatistics()!=null)
			statistics = treasureHuntState.readStatistics();
		else
			statistics = new GameStatistics();
		
		passStatisticsToTreasureHuntState();
		
		this.stage = stage;
		multiSetup = new Stage();
		multiSetup.initModality(Modality.APPLICATION_MODAL);
		multiSetup.setTitle(I18n.getBundle().getString(GameSettings.MSG_APP_TITLE_MULTIPLAYER_LIST_KEY));
		
		statisticsStage = new Stage();
		
		
		nameDialog = new TextInputDialog();
		nameDialog.setTitle(I18n.getBundle().getString(GameSettings.USN_DIALOG_TITLE_TEXT));
		nameDialog.setHeaderText(I18n.getBundle().getString(GameSettings.USN_DIALOG_HEADER_TEXT));
		nameDialog.setContentText(I18n.getBundle().getString(GameSettings.USN_DIALOG_CONTENT_TEXT));
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.FRAME_VIEW_PATH), bundle);
		mainView = fxmlLoader.load();
		mainViewController = fxmlLoader.getController();
		mainViewController.setContainer(this);
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.TREASURE_VIEW_PATH), bundle);
		treasureFrame = fxmlLoader.load();
		treasureFrameController = fxmlLoader.getController();
		treasureFrameController.setStatistics(statistics);
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.PUZZLER_VIEW_PATH), bundle);
		puzzlerFrame = fxmlLoader.load();
		puzzlerFrameController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.FLASH_VIEW_PATH), bundle);
		flashFrame = fxmlLoader.load();
		flashFrameController = fxmlLoader.getController();
	
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.MULTIPLAYER_VIEW_PATH), bundle);
		multiplayerFrame = fxmlLoader.load();
		multiplayerViewController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.STATISTICS_VIEW_PATH),bundle);
		statisticsFrame = fxmlLoader.load();
		statisticsViewController = fxmlLoader.getController();
		statisticsViewController.setStatistics(statistics);
		
		multiPopup = new Scene(this.multiplayerFrame);
		statisticsScene = new Scene(this.statisticsFrame);
		
		recievingThread = new Thread(statusReciever);
		statusReciever.getPartialResults().addListener((Change<? extends StatusMessage> c) -> multiplayerViewController.handleNewPlayer(c));
		recievingThread.setDaemon(true);
		recievingThread.start();
		
		//statistics = new GameStatisticsApp();
		//puzzlerFrameController.setGameStatistics(statistics);

		flashFrameController.setOnStartButtonAction(e -> startGame(PuzzlerSettings.MATH_PUZZLER_SQRT));
		flashFrameController.setOnStartMultiButtonAction(e -> startMultiplayerGame());
		flashFrameController.setOnPuzzlerModeAction(e -> startGame(PuzzlerSettings.WORD_PUZZLER));
		puzzlerFrameController.setOnAnswerButtonAction(e -> answerPuzzler());
		treasureFrameController.setOnButtonTreasureAction(e -> treasureFrameController.doTreasureLocationAction());
		treasureFrameController.setOnContinueButtonAction(e -> {startGame(PuzzlerSettings.MATH_PUZZLER_SQRT);treasureFrameController.clearCanvas();});
		treasureFrameController.setOnQuitButtonAction(e -> {QuitandSaveStatistics();System.exit(0);});
		statisticsViewController.setOnQuitButtonAction(e ->statisticsStage.hide());
		
		treasureFrameController.getTreasureField().setTreasureGenerator(treasureGenerator);
		
//		if (treasureHuntState == null) {
//			treasureGenerator = new TreasureGenerator();
//		}
//		treasureFrameController.getTreasureField().setTreasureGenerator(treasureGenerator);
//		
//		if (treasureHuntState == null) {
//			treasureHuntState = new TreasureHuntState();
//		}
//		if(treasureHuntState.readStatistics()!=null)
//			statistics = treasureHuntState.readStatistics();
//		else
//			statistics = new GameStatistics();
		
		mainViewController.setTreasureHuntState(treasureHuntState);
		
		if (this.statusBroadcaster != null) {
			mainViewController.setStatusBroadcaster(this.statusBroadcaster);
		} else {
			LOGGER.debug("this.statusBroadcaster is null");
		}
	}

	private void showTreasureScreen() {
		LOGGER.debug("showing treasure screen.");
		showScreenWithFrame(this.treasureFrame, GameSettings.MSG_APP_TITLE_TREASURE_HUNT_KEY);
	}
	
	
	private void showPuzzlerScreen(int difficultyLevel) {
		LOGGER.debug("showing puzzler screen.");
		treasureFrameController.getTreasureField().placeTreasure();
		LOGGER.debug("placed a treasure");
		puzzlerFrameController.setGameStatistics(statistics);
		this.puzzlerFrameController.showNewPuzzler(difficultyLevel);
		showScreenWithFrame(this.puzzlerFrame, GameSettings.MSG_APP_TITLE_PUZZLER_KEY);
	}
	
	private void showScreenWithFrame(Parent view, String title_key) {
		showScreenWithFrame(false, view, title_key);
	}
	
	private void showScreenWithFrame(boolean reload, Parent view, String title_key) {
		if (mainViewController == null) {
			throw new IllegalStateException("mainViewcontroller must not be null.");
		}
		
		if (reload || stage.getScene() == null) {
			scene = new Scene(mainView);
			stage.setScene(scene);
			stage.show();
		} 
		
		mainViewController.setFrameOnTop(view);
		
		if (view == this.flashFrame && FIRST_RUN) {
			FIRST_RUN = false;
			while(!showUsernamePopup());
			statusBroadcaster.start();
			statistics.addAttempts();
			//------------show the statistic frame when game start------------
			statisticsViewController.showAll();
			showStatisticsPane();
		}
		
		if (title_key != null && !title_key.isEmpty()) {
			title = title_key;
			stage.setTitle(I18n.getBundle().getString(title_key));
		}
	}
	
	private boolean showUsernamePopup() {
		Optional<String>  usn = nameDialog.showAndWait();
		if (usn.isPresent()) {
			String name = usn.get();
			if (!name.isEmpty() && name.length() > 2) {
				statusBroadcaster.setUserName(name);
				statusReciever.setUsername(name);
				return true;
			}
		}
		return false;
	}


	private void startGame(int difficultyLevel) {
		GameSettings.MAX_SCORE = 100;
		GameSettings.SCORE_PENALTY = 10;
		showPuzzlerScreen(difficultyLevel);
		mainViewController.disableLocaleChange();
	}
	
	private void NextRound() {
		GameSettings.MAX_SCORE = 100;
		GameSettings.SCORE_PENALTY = 10;
		showPuzzlerScreen(PuzzlerSettings.WORD_PUZZLER);
		mainViewController.disableLocaleChange();
	}
	
	private void startMultiplayerGame() {
		multiSetup.setScene(multiPopup);
		multiSetup.show();
	}
	
	//---------show the statistics frame----------------------
	private void showStatisticsPane(){
		statisticsStage.setScene(statisticsScene);
		statisticsStage.showAndWait();
		statisticsViewController.showAll();
	}
	
	public void QuitandSaveStatistics(){
		//statisticsViewController.showAll();
		showStatisticsPane();
		treasureHuntState.saveStatistics();
//		showScreenWithFrame(this.statisticsFrame, GameSettings.MSG_APP_TITLE_STATISTICS_LIST_KEY);
	}
	
	public void passStatisticsToTreasureHuntState(){
		treasureHuntState.setStatistics(statistics);
	}
	
	//----------method to show treasure screen----------------
	public void changeToTreasureScreen(){
		treasureFrameController.setAttempts(puzzlerFrameController.getAnsweringAttempts());
		treasureFrameController.startLocatingTreasure(clue);
		showTreasureScreen();
	}
	//--------------------------------------------------------
	//----------method to show to puzzler screen-----------------
	public void reShowPuzzlerScreen(){
		LOGGER.debug("showing puzzler screen.");
		this.puzzlerFrameController.reflashPuzzlerLabel();
		showScreenWithFrame(this.puzzlerFrame, GameSettings.MSG_APP_TITLE_PUZZLER_KEY);
	}
	//-----------------------------------------
	
	//make some getter
	//-------------------------------------------------------------------------
	public TreasureFrameViewController getTreasureFrameViewControllerGetter(){
		return treasureFrameController;
	}
	
	public PuzzlerFrameViewController getPuzzlerFrameViewController(){
		return puzzlerFrameController;
	}
	
	public void setTreasureFrameViewController(TreasureFrameViewController treasureFrameController){
		this.treasureFrameController = treasureFrameController;
	}
	
	public void setPuzzlerFrameViewController(PuzzlerFrameViewController puzzlerFrameController){
		this.puzzlerFrameController = puzzlerFrameController;
	}
	//-------------------------------------------------------------------------
}

