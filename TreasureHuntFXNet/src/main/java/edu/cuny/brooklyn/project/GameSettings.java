package edu.cuny.brooklyn.project;

import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.validator.Validator;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

public class GameSettings {

	
	// default treasure field size
	public final static int FIELD_WIDTH = 70;
	public final static int FIELD_HEIGHT = 15;

	// field cell is empty
	public final static int FIELD_EMPTY = 0;
	
	// default treasure set up
	public final static int DEFAULT_TREASURE_SIZE = 10;
	public final static int DEFAULT_TREASURE_VALUE = 1;
	
	// number of types of puzzlers
	public final static int NUM_TYPES_OF_PUZZLERS = 2;
	
	
	// clue error
	public static final int DEFAULT_CLUE_ERROR_INCREMENT = 2;
	// clue error: to make it fun, error can be set proportional to the size of the treasure
	public static final double DEFAULT_CLUE_RELATIVE_ERROR_INCREMENT = 1.;
	
	
	// score computation
	public static int MAX_SCORE = 100;
	public static int SCORE_PENALTY = 10;

	// UI Settings
	public final static String APP_ICON_IMAGE = "icon/icon.png";
	public final static double SCENE_WIDTH = 900.;
	public final static double SCENE_HEIGHT = 600.;
	public final static double H_SPACING = 20.;
	public final static double V_SPACING = 20.;
	
	
	public final static double NEW_CANVAS_WIDTH = 795.0;
	public final static double NEW_CANVAS_HEIGHT = 472.0;
	
	public final static double CANVAS_WIDTH = 600;
	public final static double CANVAS_HEIGHT = 400;
	public final static Color CANVAS_COLOR = Color.WHITE;
	public final static double CANVAS_HOLDER_MIN_WIDTH = CANVAS_WIDTH;
	public final static double CANVAS_HOLDER_MIN_HEIGHT = CANVAS_HEIGHT;
	
	public final static Insets PADDING = new Insets(20., 20., 20., 20.);
	public final static Insets PADDING_X = new Insets(0., 20., 0., 20.); 
	
	public final static String SCORE_FORMAT = "%05d";

	public static final Color DEFAULT_TREASURE_CELL_COLOR = Color.GOLD;
	
	public static final String DEFAULT_CANVAS_HOLDER_STYLE = "-fx-background-color:white";
	
	// load using the class loader
	public static final String FRAME_VIEW_PATH = "edu/cuny/brooklyn/project/view/fxml_frameview.fxml";
	public static final String TREASURE_VIEW_PATH = "edu/cuny/brooklyn/project/view/fxml_treasureframeview.fxml";
	public static final String PUZZLER_VIEW_PATH = "edu/cuny/brooklyn/project/view/fxml_puzzlerframeview.fxml";
	public static final String FLASH_VIEW_PATH = "edu/cuny/brooklyn/project/view/fxml_flashframeview.fxml";
	public static final String MULTIPLAYER_VIEW_PATH = "edu/cuny/brooklyn/project/view/fxml_multiplayerframeview.fxml";
	public static final String STATISTICS_VIEW_PATH = "edu/cuny/brooklyn/project/view/fxml_statisticsPane.fxml";

	
	public final static String MSG_GAME_DESCRIPTION_KEY = "gameDescription";
	public final static String MSG_START_GAME_KEY = "startGame";
	public final static String MSG_START_MULTI_GAME_KEY = "startMultiGame";
	public final static String MSG_APP_TITLE_FLASH_KEY = "appTitleFlash";
	
	public final static String MSG_ANSWER_PUZZLER_KEY = "answerPuzzler";
	public final static String MSG_APP_TITLE_PUZZLER_KEY = "appTitlePuzzler";

	public final static String MSG_TOTAL_SCORE_KEY = "totalScore";
	public final static String MSG_ROUND_SCORE_KEY = "roundScore";
	public final static String MSG_LOCATE_TREASURE_KEY = "locateTreasure";
	public final static String MSG_APP_TITLE_TREASURE_HUNT_KEY = "appTitleTreasureHunt";
	public final static String MSG_NO_LABEL_AT_LOCATION_KEY = "noLabelAtLocation";	
	
	public final static String MSG_TREASURE_CLUE_PART_1_KEY = "treasureClue1stPart";
	public final static String MSG_TREASURE_CLUE_PART_2_KEY = "treasureClue2ndPart";

	public final static String MSG_APP_TITLE_MULTIPLAYER_LIST_KEY = "appTitleMultiplayerList";
	public final static String MSG_MULTIPLAYER_USERNAME = "username";
	public final static String MSG_MULTIPLAYER_TCP_LABEL = "tcpAddress";
	public final static String MSG_MULTIPLAYER_UDP_LABEL = "udpPort";
	public static final String USN_DIALOG_TITLE_TEXT = "usnTitle";
	public static final String USN_DIALOG_HEADER_TEXT = "usnHeader";
	public static final String USN_DIALOG_CONTENT_TEXT = "usnContext";
	public static int DEFAULT_DIFFULTY = 2;
	
	public final static String MSG_APP_TITLE_STATISTICS_LIST_KEY = "appTitleStatisticsList";
	
//	public static final String DIFF_EASY = I18n.getBundle().getString("diffEasy");
//	public static final String DIFF_NORMAL = I18n.getBundle().getString("diffNormal");
//	public static final String DIFF_HARD = I18n.getBundle().getString("diffHard");
	public static final String DIFF_EASY = "diffEasy";
	public static final String DIFF_NORMAL = "diffNormal";
	public static final String DIFF_HARD = "diffHard";
	
	// Settings
	public final static String DEFAULT_DIFFICULTY = DIFF_NORMAL;
	public final static String DEFAULT_TEXT_COLOR = "#000000";
	private static String CURRENT_DIFFICULTY;
	private static String CURRENT_TEXT_COLOR;

	public static void setDifficulty(String diff) {
		if (!Validator.isValidDifficulty(diff) || CURRENT_DIFFICULTY == diff) return;
		CURRENT_DIFFICULTY = diff;
	}

	public static String getDifficulty() {
		if (CURRENT_DIFFICULTY == null) return DEFAULT_DIFFICULTY;
		return CURRENT_DIFFICULTY;
	}

	public static void setTextColor(String hex) {
		CURRENT_TEXT_COLOR = hex;
	}

	public static String getTextColor() {
		if (CURRENT_TEXT_COLOR == null) return DEFAULT_TEXT_COLOR;
		return CURRENT_TEXT_COLOR;
	}

	public static String colorToHex(Color color) {
		return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
	}
}
