package edu.cuny.brooklyn.project.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.controller.DecisionWrapper.UserDecision;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusBroadcaster;
import edu.cuny.brooklyn.project.state.TreasureHuntState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class FrameViewController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FrameViewController.class);

	@FXML
	private Canvas targetCanvas;

	@FXML
	private ComboBox<Locale> lcComboBox;

	@FXML
	private StackPane frameHolder;

	private TreasureHuntState treasureHuntState;

	private StatusBroadcaster statusBroadcaster;
	
	private FrameContainer frameContainer;
	
	public void initialize() {
		try {
			initializeI18n();
		} catch (IOException e) {
			LOGGER.error("Cannot initialize i18n", e);
		} catch (URISyntaxException e) {
			LOGGER.error("Cannot initialize i18n", e);
		}
	}
	
	public void disableLocaleChange() {
		lcComboBox.setDisable(true); 
	}

	@FXML
	private void exitGame(ActionEvent event) {
		LOGGER.debug("calling exitGame(ActionEvent event).");
		exitGame((Event) event);
	}

	private void exitGame(Event event) {
		LOGGER.debug("calling exitGame(Event event).");
		validateTreasureHunt();
		validateStatusBroadcaster();
		if (treasureHuntState.isGameStateChanged()) {
			UserDecision decision = NotificationHelper
					.askUserDecision(new DecisionWrapper(UserDecision.CancelPendingAction));
			switch (decision) {
			case CancelPendingAction:
				event.consume();
				break;
			case DiscardGame:
				statusBroadcaster.close();
				Platform.exit();
				break;
			case SaveGame:
				try {
					treasureHuntState.saveTheGame(frameContainer);
					LOGGER.debug(String.format("Saved the game at %s.", treasureHuntState.getTheGameFilePath()));
					statusBroadcaster.close();
					Platform.exit();
				} catch (FileNotFoundException e) {
					LOGGER.error(String.format("Cannot found the file %s while saving the game.",
							treasureHuntState.getTheGameFilePath()), e);
					NotificationHelper.showFileNotFound(treasureHuntState.getTheGameFilePath());
				} catch (IOException e) {
					LOGGER.error(String.format("Cannot write to the file %s while saving the game.",
							treasureHuntState.getTheGameFilePath()), e);
					NotificationHelper.showWritingError(treasureHuntState.getTheGameFilePath());
				}
				break;
			default:
				throw new IllegalArgumentException(String.format("User decision's value (%s) is unexpected", decision));
			}
		} else {
			statusBroadcaster.close();
			Platform.exit();
		}
	}

	@FXML
	void newGame(ActionEvent event) {

	}

	@FXML
	void openGame(ActionEvent event) {
		try{
			treasureHuntState.openTheGame(frameContainer);
			if(frameContainer.title.equals(GameSettings.MSG_APP_TITLE_FLASH_KEY))
				frameContainer.showFlashScreen(false);
			else if(frameContainer.title.equals(GameSettings.MSG_APP_TITLE_PUZZLER_KEY))
				frameContainer.reShowPuzzlerScreen();
			else if(frameContainer.title.equals(GameSettings.MSG_APP_TITLE_TREASURE_HUNT_KEY))
				frameContainer.changeToTreasureScreen();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			LOGGER.debug("open the game sucessful.");
		}
	}

	@FXML
	void saveTheGame(ActionEvent event) {
		try {
			treasureHuntState.saveTheGame(frameContainer);
			LOGGER.debug(String.format("Saved the game at %s.", treasureHuntState.getTheGameFilePath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.error(String.format("Cannot found the file %s while saving the game.",
					treasureHuntState.getTheGameFilePath()), e);
			NotificationHelper.showFileNotFound(treasureHuntState.getTheGameFilePath());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(String.format("Cannot write to the file %s while saving the game.",
					treasureHuntState.getTheGameFilePath()), e);
			NotificationHelper.showWritingError(treasureHuntState.getTheGameFilePath());
		}
	}

	private void initializeI18n() throws IOException, URISyntaxException {
		List<Locale> lcList = I18n.getSupportedLocale();
		LOGGER.debug("The number of locale bundles got: " + lcList.size());
		lcComboBox.getItems().addAll(lcList);
		Callback<ListView<Locale>, ListCell<Locale>> lcCellFactory = new Callback<ListView<Locale>, ListCell<Locale>>() {

			@Override
			public ListCell<Locale> call(ListView<Locale> lv) {
				return new ListCell<Locale>() {
					@Override
					protected void updateItem(Locale lc, boolean empty) {
						super.updateItem(lc, empty);
						if (lc == null || empty) {
							setText("");
						} else {
							setText(I18n.getDisplayLC(lc));
						}
					}
				};
			}
		};
		lcComboBox.setValue(I18n.getSelectedLocale());
		lcComboBox.setConverter(new StringConverter<Locale>() {

			@Override
			public Locale fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Locale lc) {
				return I18n.getDisplayLC(lc);
			}
		});
		lcComboBox.setCellFactory(lcCellFactory);
		lcComboBox.valueProperty().addListener((observedLocale, oldLocale, newLocale) -> {
			LOGGER.debug(String.format("Change locale from %s to %s.", oldLocale, newLocale));
			 try {
				 LOGGER.debug("TODO: change language results to a new game. Need to handle it better.");
				 reLoadScene(newLocale);
			 } catch (IOException e) {
				 LOGGER.error("failed to load locale specific scene.", e);
			 }
		});
	}

	 private void reLoadScene(Locale locale) throws IOException {
		 I18n.setSelectedLocale(locale);
		 I18n.setBundle(ResourceBundle.getBundle(I18n.getBundleBaseName(), locale));
		 frameContainer.reload(I18n.getBundle());
	 }

	private void validateStatusBroadcaster() {
		if (statusBroadcaster == null) {
			throw new IllegalStateException("StatusBroadcaster object must not be null");
		}
	}

	private void validateTreasureHunt() {
		if (treasureHuntState == null) {
			throw new IllegalStateException("TreasureHunt object must not be null");
		}
	}

	public void setFrameOnTop(Parent view) {
		frameHolder.getChildren().clear();
		frameHolder.getChildren().add(view);
	}

	public void setContainer(FrameContainer frameContainer) {
		if (frameContainer == null) {
			throw new IllegalArgumentException("FrameContainer object must not be null.");
		}
		this.frameContainer = frameContainer;
	}

	public void setTreasureHuntState(TreasureHuntState treasureHuntState) {
		if (treasureHuntState == null) {
			throw new IllegalArgumentException("TreasureHuntState object must not be null.");
		}
		this.treasureHuntState = treasureHuntState;
	}

	public void setStatusBroadcaster(StatusBroadcaster statusBroadcaster) {
		if (statusBroadcaster == null) {
			throw new IllegalArgumentException("StatusBroadcaster object must not be null.");
		}
		this.statusBroadcaster = statusBroadcaster;
	}
}
