package edu.cuny.brooklyn.project.net;

import java.io.Serializable;
import java.net.InetAddress;

public class StatusMessage implements Serializable {
    private static final long serialVersionUID = -666227046530917575L;
    private String username;
    private int udpPort;
    private InetAddress address;
    private int tcpServicePort;

    public int getUdpPort() {
    	return udpPort;
    }
    
    public void setUdpPort(int p) {
    	udpPort = p;
    }
    
    public String getName() {
        return username;
    }

    public void setName(String n) {
        username = n;
    }
    
    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getTcpServicePort() {
        return tcpServicePort;
    }

    public void setTcpServicePort(int tcpServicePort) {
        this.tcpServicePort = tcpServicePort;
    }

    public StatusMessage(InetAddress address, int tcpServicePort) {
        this.address = address;
        this.tcpServicePort = tcpServicePort;
    }
    
    public String toString() {
        return String.format("{ InetAddress= %s, Port = %d }", address.toString(), tcpServicePort);
    }
}
