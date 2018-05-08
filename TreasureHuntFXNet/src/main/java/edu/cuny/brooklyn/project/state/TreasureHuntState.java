package edu.cuny.brooklyn.project.state;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.controller.FrameContainer;
import edu.cuny.brooklyn.project.net.StatusBroadcaster;
import edu.cuny.brooklyn.project.puzzler.Puzzler;
import edu.cuny.brooklyn.project.puzzler.PuzzlerMaker;
import edu.cuny.brooklyn.project.score.Scorer;
import edu.cuny.brooklyn.project.treasure.TreasureField;

public class TreasureHuntState implements Serializable{
    private static Logger LOGGER = LoggerFactory.getLogger(TreasureHuntState.class);

    
    private boolean gameStateChanged=true;
    
    private transient Path theGameFilePath=Paths.get(".","file.ser");
    //add seriazable id
    private static final long seriazableID =1L;
    public boolean isGameStateChanged() {
        return gameStateChanged;
    }

    public void setGameStateChanged(boolean gameStateChanged) {
        this.gameStateChanged = gameStateChanged;
    }

    public void saveTheGame(FrameContainer frameContainer) throws FileNotFoundException, IOException {
    	try{
    		FileOutputStream fs = new FileOutputStream(theGameFilePath.toString());
    		//FileOutputStream fs = new FileOutputStream("game.ser");
    		ObjectOutputStream os = new ObjectOutputStream(fs);
    		os.writeObject(frameContainer.getTreasureFrameViewControllerGetter().getScorer());
    		os.writeObject(frameContainer.getTreasureFrameViewControllerGetter().getTreasureField());
    		os.writeObject(frameContainer.getTreasureFrameViewControllerGetter().getAttempts());
    		os.writeObject(frameContainer.getPuzzlerFrameViewController().getPuzzler());
    		os.writeObject(frameContainer.getPuzzlerFrameViewController().getPuzzlerMaker());
    		os.writeObject(frameContainer.getPuzzlerFrameViewController().getAnsweringAttempts());
    		os.writeObject(frameContainer.title);
    		os.writeObject(frameContainer.clue);
    		os.close();
    		fs.close();
    	}catch(FileNotFoundException e){
    		e.printStackTrace();
    		LOGGER.debug("file not found");
    	}catch(IOException e){
    		e.printStackTrace();
    		LOGGER.debug("stream error");
    	}
        // TODO Auto-generated method stub
        LOGGER.debug("not yet implemented");
    }
    
    public FrameContainer openTheGame(FrameContainer frameContainer){
    	try{
    		FileInputStream fi = new FileInputStream(theGameFilePath.toString());
    		//FileInputStream fi = new FileInputStream("game.ser");
        	ObjectInputStream oi = new ObjectInputStream(fi);
        	frameContainer.getTreasureFrameViewControllerGetter().setScorer((Scorer)oi.readObject());
        	frameContainer.getTreasureFrameViewControllerGetter().setTreasureField((TreasureField)oi.readObject());
        	frameContainer.getTreasureFrameViewControllerGetter().setAttempts((Integer)oi.readObject());
        	frameContainer.getPuzzlerFrameViewController().setPuzzler((Puzzler)oi.readObject());
        	frameContainer.getPuzzlerFrameViewController().setPuzzlerMaker((PuzzlerMaker)oi.readObject());
        	frameContainer.getPuzzlerFrameViewController().setAnsweringAttempts((Integer)oi.readObject());
        	frameContainer.title = (String)oi.readObject();
        	frameContainer.clue = (String)oi.readObject();
        	oi.close();
        	fi.close();
    	}catch(FileNotFoundException e){
    		e.printStackTrace();
    		LOGGER.debug("file not found");
    	}catch(IOException e){
    		e.printStackTrace();
    		LOGGER.debug("stream error");
    	}catch(ClassNotFoundException e){
    		e.printStackTrace();
    		LOGGER.debug("class change error");
    	}
    	return frameContainer;
    }

    public Path getTheGameFilePath() {
        return theGameFilePath;
    }
}