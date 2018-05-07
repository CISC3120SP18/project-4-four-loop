package edu.cuny.brooklyn.project.controller;

import java.io.IOException;
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

	private Stage stage;
	
	private Scene scene;
	
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
	
	private TreasureGenerator treasureGenerator;
	
	private TreasureHuntState treasureHuntState;
	
	private StatusBroadcaster statusBroadcaster;

	private StatusReciever statusReciever;
	
	private GameStatisticsApp statistics;
	private GameStatisticsApp score;


	
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
	

	public void setGameStatistics (GameStatisticsApp statistics) {
		if (statistics == null) {
			throw new IllegalArgumentException("StatusBroadcaster object must not be null.");
		}
		this.statistics = statistics;
		
		mainViewController.setStatusBroadcaster(this.statusBroadcaster);
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
			String clue = TreasureClue.getClue(treasureFrameController.getTreasureField().getTreasureXLeft(),
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

		
		this.stage = stage;
		
		nameDialog = new TextInputDialog("Johnny Appleseed");
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
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.PUZZLER_VIEW_PATH), bundle);
		puzzlerFrame = fxmlLoader.load();
		puzzlerFrameController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.FLASH_VIEW_PATH), bundle);
		flashFrame = fxmlLoader.load();
		flashFrameController = fxmlLoader.getController();
	
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.MULTIPLAYER_VIEW_PATH), bundle);
		multiplayerFrame = fxmlLoader.load();
		multiplayerViewController = fxmlLoader.getController();
		
		recievingThread = new Thread(statusReciever);
		statusReciever.getPartialResults().addListener((Change<? extends StatusMessage> c) -> multiplayerViewController.handleNewPlayer(c));
		recievingThread.setDaemon(true);
		recievingThread.start();
		
		//statistics = new GameStatisticsApp();
		//puzzlerFrameController.setGameStatistics(statistics);

		flashFrameController.setOnStartButtonAction(e -> startGame());
		flashFrameController.setOnStartMultiButtonAction(e -> startMultiplayerGame());
		puzzlerFrameController.setOnAnswerButtonAction(e -> answerPuzzler());
		treasureFrameController.setOnButtonTreasureAction(e -> treasureFrameController.doTreasureLocationAction());
		treasureFrameController.setOnContinueButtonAction(e -> startGame());
		treasureFrameController.setOnQuitButtonAction(e -> System.exit(0));
		
		if (treasureHuntState == null) {
			treasureGenerator = new TreasureGenerator();
		}
		treasureFrameController.getTreasureField().setTreasureGenerator(treasureGenerator);
		
		if (treasureHuntState == null) {
			treasureHuntState = new TreasureHuntState();
		}
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
	
	
	private void showPuzzlerScreen() {
		LOGGER.debug("showing puzzler screen.");
		treasureFrameController.getTreasureField().placeTreasure();
		LOGGER.debug("placed a treasure");
		this.puzzlerFrameController.showNewPuzzler();
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
		}
		
		if (title_key != null && !title_key.isEmpty()) {
			stage.setTitle(I18n.getBundle().getString(title_key));
		}
	}
	
	private boolean showUsernamePopup() {
		if (nameDialog.showAndWait().isPresent()) {
			statusBroadcaster.setUserName(nameDialog.getContentText());
			return true;
		}
		return false;
	}


	private void startGame() {
		showPuzzlerScreen();
		mainViewController.disableLocaleChange();
	}
	
	private void startMultiplayerGame() {
		final Stage multiSetup = new Stage();
		Scene multiPopup = new Scene(this.multiplayerFrame);
		
		multiSetup.initModality(Modality.APPLICATION_MODAL);
		multiSetup.initOwner(stage);
		multiSetup.setTitle(GameSettings.MSG_APP_TITLE_MULTIPLAYER_LIST_KEY);
		multiSetup.setScene(multiPopup);
		multiSetup.show();
	
	}
}
