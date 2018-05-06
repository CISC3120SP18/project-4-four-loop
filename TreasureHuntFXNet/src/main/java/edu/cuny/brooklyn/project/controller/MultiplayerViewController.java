package edu.cuny.brooklyn.project.controller;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusMessage;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.control.*;

public class MultiplayerViewController {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label tcpLabel;

    @FXML
    private Label udpLabel;

    @FXML
    private VBox playerContainer;

    private static final int USERNAME_I = 0,
    						 TCP_ADDRESS_I = 1,
    						 UDP_PORT_I = 2;
    
    public void initialize() {
		usernameLabel.setText(I18n.getBundle().getString(GameSettings.MSG_MULTIPLAYER_USERNAME));
		tcpLabel.setText(I18n.getBundle().getString(GameSettings.MSG_MULTIPLAYER_TCP_LABEL));
		udpLabel.setText(I18n.getBundle().getString(GameSettings.MSG_MULTIPLAYER_UDP_LABEL));
    }
    
    private HBox mapToHbox(StatusMessage msg) {
    	HBox player = new HBox();
    	Label nameLabel = new Label(msg.getName());
    	Label tcpAddressLabel = new Label(String.format("%s:%s", msg.getAddress().toString(), msg.getTcpServicePort()));
    	Label udpPortLabel = new Label(Integer.toString(msg.getUdpPort()));
    	player.getChildren().addAll(nameLabel, tcpAddressLabel, udpPortLabel);
    	return player;
    }
    
    private void fixRows(HBox player) { //Add
    	ObservableList<Node> players = playerContainer.getChildren();
    	String username = ((Label) player.getChildren().get(USERNAME_I)).getText(),
    		tcpAddress = ((Label) player.getChildren().get(TCP_ADDRESS_I)).getText(),
    		udpPort = ((Label) player.getChildren().get(UDP_PORT_I)).getText();
    	if (players.size() == 0) {
    		players.add(player);
    		return;
    	}
    	
    	FilteredList<Node> duplicateIPs = players.filtered(
    		(otherPlayer) -> tcpAddress.equals(((Label) ((HBox) otherPlayer).getChildren().get(TCP_ADDRESS_I)).getText())
    	);
    	if (duplicateIPs.size() > 0) return;
    	
    	FilteredList<Node> duplicateNames = players.filtered(
        		(otherPlayer) -> username.equals(((Label) ((HBox) otherPlayer).getChildren().get(USERNAME_I)).getText())
    	);
    	if (duplicateNames.size() > 0) duplicateNames.forEach((node) -> {
    		HBox otherPlayer = (HBox) node;
    		if (otherPlayer.getChildren().get(TCP_ADDRESS_I) instanceof VBox) {
    			VBox ipBank = (VBox) otherPlayer.getChildren().get(TCP_ADDRESS_I);
    			ipBank.getChildren().add(new Label(tcpAddress));
    		} else {
    			String address = ((Label) otherPlayer.getChildren().get(TCP_ADDRESS_I)).getText();
    			otherPlayer.getChildren().remove(TCP_ADDRESS_I);
    			VBox newIpBank = new VBox();
    			newIpBank.getChildren().addAll(new Label(address), new Label(tcpAddress));
    			otherPlayer.getChildren().add(TCP_ADDRESS_I, newIpBank);
    		}
    		return;
    	});
    	
    	playerContainer.getChildren().add(player);
    }
    
    //private void unfixRows(HBox player) { TODO: Remove Failed Connections from UI }
    
    protected void handleNewPlayer(Change<? extends StatusMessage> c) {
    	while(c.next()) {
    		// TODO: Bonus Multiplayer for(StatusMessage msg: c.getRemoved()) unfixRows(mapToHbox(msg));
    		for(StatusMessage msg: c.getAddedSubList()) fixRows(mapToHbox(msg));
    	}
    	return;
    }


}