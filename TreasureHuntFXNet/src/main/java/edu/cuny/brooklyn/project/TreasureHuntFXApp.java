/**
 * The start-up code for the 1st class project of a sequence 5 projects in CISC 3120 
 * Sections MW2 and MW8 CUNY Brooklyn College. The project should result a simple 
 * text-based game application. 
 * 
 * Spring 2018 
 */

package edu.cuny.brooklyn.project;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.controller.FrameContainer;
import edu.cuny.brooklyn.project.controller.GameStatisticsApp;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusBroadcaster;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TreasureHuntFXApp extends Application {
	private final static Logger LOGGER = LoggerFactory.getLogger(TreasureHuntFXApp.class);

	private StatusBroadcaster statusBroadcaster;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LOGGER.info("TreasureHuntFXApp started.");

		ResourceBundle bundle = ResourceBundle.getBundle(I18n.getBundleBaseName(), I18n.getDefaultLocale());
		primaryStage.getIcons()
				.add(new Image(getClass().getClassLoader().getResourceAsStream(GameSettings.APP_ICON_IMAGE)));
		FrameContainer frameContainer = new FrameContainer(primaryStage, bundle);
		frameContainer.showFlashScreen(); // where the game begins

		statusBroadcaster = new StatusBroadcaster();
		statusBroadcaster.start();
		
		
		frameContainer.setStatusBroadcaster(statusBroadcaster);

		GameStatisticsApp statisticsObject = new GameStatisticsApp();
		GameStatisticsApp statistics;
	//	frameContainer.setGameStatistics(statistics);
		LOGGER.info("TreasureHuntFXApp exits.");
	}

	@Override
	public void stop() {
		LOGGER.info("Stopping StatusBoardcaster.");
		if (statusBroadcaster != null) {
			statusBroadcaster.close();
		}
	}
}
