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
import edu.cuny.brooklyn.project.controller.GameStatistics;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusBroadcaster;
import edu.cuny.brooklyn.project.net.StatusReciever;
import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TreasureHuntFXApp extends Application {
	private final static Logger LOGGER = LoggerFactory.getLogger(TreasureHuntFXApp.class);

	private StatusBroadcaster statusBroadcaster;
	private StatusReciever statusReciever;
	private GameStatistics statistics;
	public static void main(String[] args) {
		launch(args);
	}
	
	private static void print(String s) { System.out.println(s); }

	@Override
	public void start(Stage primaryStage) throws Exception {
		LOGGER.info("TreasureHuntFXApp started.");

		ResourceBundle bundle = ResourceBundle.getBundle(I18n.getBundleBaseName(), I18n.getDefaultLocale());
		primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(GameSettings.APP_ICON_IMAGE)));

		statusReciever = new StatusReciever();
		statusBroadcaster = new StatusBroadcaster();
		statistics = new GameStatistics();
		FrameContainer frameContainer = new FrameContainer(primaryStage, bundle, statusReciever);
		frameContainer.setGameStatistics(statistics);
		frameContainer.setStatusBroadcaster(statusBroadcaster);
		frameContainer.showFlashScreen(); // where the game begins

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
