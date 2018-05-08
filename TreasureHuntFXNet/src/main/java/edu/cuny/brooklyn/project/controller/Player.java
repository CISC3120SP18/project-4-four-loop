package edu.cuny.brooklyn.project.controller;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ListChangeListener.Change;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {
	private SimpleStringProperty username;
	private	ArrayList<String> ips;
	private ObservableList<String> ipBank;
	private SimpleStringProperty udpPort;
	private StringProperty ipProperty;
	private String ipToString;
	//private Button connector;
	
	public Player(String usn, String ip, String udp) {
		ipToString = "";
		ipProperty = new SimpleStringProperty(ipToString, "ipList");
		username = new SimpleStringProperty(usn);
		udpPort = new SimpleStringProperty(udp);
		ips = new ArrayList<String>();
		ipBank = observableArrayList(ips);
		ipBank.addListener((Change<? extends String> c) -> {
			if(c.next()) {
				if (c.wasAdded()) ipProperty.set(String.join(", ", c.getList()));
				//System.out.println("ipBank::Listener: value: " + ipProperty.get());
			}
		});
		ipBank.add(ip);
		
	}

	protected void addIp(String ip) { ipBank.add(ip); }
	
	public final SimpleStringProperty ussernameProperty() { return username; }
	public final String getUsername() { return username.get(); }
	public final void setUsername(String usn) { username.setValue(usn); }
	
	public final SimpleStringProperty udpPortProperty() { return udpPort; }
	public final String getUdpPort() { return udpPort.get(); }
	public final void setUdpPort(String udp) { udpPort.setValue(udp); }	
	
	public final StringProperty ipListProperty() { return ipProperty; }
	public final String getIpList() { return ipProperty.get(); }
	public final void setIpList(String ipl) { ipProperty.set(ipl); }
	
	//public ArrayList<String> getIps() { return ips; }
	protected ObservableList<String> ipsAsStream() {
		return ipBank;
	}
	
}
