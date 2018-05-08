package edu.cuny.brooklyn.project.controller;

import static java.util.Collections.enumeration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Set;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.collections.FXCollections.observableArrayList;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusMessage;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.StringProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.control.*;

public class MultiplayerViewController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MultiplayerViewController.class);

	@FXML
	private TableColumn<Player, String> usernames;
	
	@FXML
	private TableColumn<Player, String> udpPorts;
	
	@FXML
	private TableColumn<Player, StringProperty> tcpAddresses;
    
    private ArrayList<Player> dataArray;
    private ObservableList<Player> tableData;
    
public void initialize() {
    	dataArray = new ArrayList<Player>();
    	tableData = observableArrayList(dataArray);
    	usernames.getTableView().setEditable(false);
    	usernames.getTableView().setItems(tableData);
    	usernames.setCellValueFactory(new PropertyValueFactory<Player, String>("username"));
		usernames.setText(I18n.getBundle().getString(GameSettings.MSG_MULTIPLAYER_USERNAME));
		
		tcpAddresses.setCellValueFactory(col -> {
			final StringProperty ipProp = col.getValue().ipListProperty();
	        return Bindings.createObjectBinding(() -> ipProp);
	    });

	    tcpAddresses.setCellFactory(col -> {
	        TableCell<Player, StringProperty> c = new TableCell<>();
	        	ComboBox<String> comboBox = new ComboBox<String>();
	        	c.itemProperty().addListener((observable, oldValue, newValue) -> {
	        		if (newValue != null) {
	        			comboBox.setItems(observableArrayList(newValue.get().split(", ")));
	        			comboBox.getSelectionModel().select(0);
	        		}
	        	});
	        c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
	        return c;
	    });
		
		tcpAddresses.setText(I18n.getBundle().getString(GameSettings.MSG_MULTIPLAYER_TCP_LABEL));
		udpPorts.setCellValueFactory(new PropertyValueFactory<Player, String>("udpPort"));
		udpPorts.setText(I18n.getBundle().getString(GameSettings.MSG_MULTIPLAYER_UDP_LABEL));
		//btnContainer... TODO: implement multiplayer
    }
    
    private Player mapToPlayer(StatusMessage msg) throws NullPointerException {
    	String address = msg.getAddress().toString().trim();
    	if (address.isEmpty() || address.length() <= 3 || msg.getName().isEmpty()) throw new NullPointerException();
    	address = String.format("%s:%s", (address.startsWith("/") ? address.substring(1) : address), msg.getTcpServicePort());
    	Player player = new Player(msg.getName(), address, Integer.toString(msg.getUdpPort()));
    	//System.out.println("mapToPlayer: result = " + String.format("{ Username = %s, IPs = %s, UDP = %s }", player.getUsername(), player.getIpList(), player.getUdpPort()));
    	return player;
    }
    
    private void updateState(Player player) {
    	int numOfPlayers = tableData.size();
    	System.out.println(String.format("#ofPlayers in table: %s", numOfPlayers));
    	if (numOfPlayers < 1) {
    		tableData.add(player);
    		return;
    	}
    	for (int i = 0; i < numOfPlayers; i++) {
    		Player p = tableData.get(i);
    		if (p.getUsername().equals(player.getUsername())) {
    			int numOfIps = player.ipsAsStream().size();
    			for(int j = 0; j < numOfIps; j++) {
    				String ip = player.ipsAsStream().get(j);
    				if (p.ipsAsStream().contains(ip)) continue;
    				p.addIp(ip);
    			}
    			System.out.println("MergedIps res: " + p.getIpList());
    			return;
    		}
    	}
    	tableData.add(player);
    }
    
    protected void handleNewPlayer(Change<? extends StatusMessage> c) {
    	try {
    		while(c.next()) {
    			// TODO: Bonus Multiplayer for(StatusMessage msg: c.getRemoved()) ...
    			for(StatusMessage msg: c.getAddedSubList()) updateState(mapToPlayer(msg));
    		}
    	} catch (IllegalArgumentException|NullPointerException ex) {
    		LOGGER.error(ex.getMessage());
    		ex.printStackTrace();
    	} 
    }


}