package edu.cuny.brooklyn.project.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.collections.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatusReciever extends Task<ObservableList<StatusMessage>>{

	private ReadOnlyObjectWrapper<ObservableList<StatusMessage>> partialResults =
		new ReadOnlyObjectWrapper<>(this, "partialResults", FXCollections.observableArrayList(new ArrayList<>()));

	public final ObservableList<StatusMessage> getPartialResults() { return partialResults.get(); }
	public final ReadOnlyObjectProperty<ObservableList<StatusMessage>> partialResultsProperty() {
		return partialResults.getReadOnlyProperty();
	}

	private final static Logger LOGGER = LoggerFactory.getLogger(StatusReciever.class);
	private final static int PORT = 62017;
	private final static String ADDRESS = "0.0.0.0";

	private final static int BUFFER_SIZE = 1024;

	@Override
	protected ObservableList<StatusMessage> call() throws Exception {
		SocketAddress address = null;
		//try {
		address = new InetSocketAddress(InetAddress.getByName(ADDRESS), PORT);
		//} catch (UnknownHostException e) {
			//LOGGER.error(String.format("Address or name %s is not a valid address or name", ADDRESS), e);
			//System.exit(-1);
		//}

		try (DatagramSocket socket = new DatagramSocket(address)) {
			byte buf[] = new byte[BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			while (true) {
				socket.receive(packet);
				ByteArrayInputStream baos = new ByteArrayInputStream(packet.getData());
				ObjectInputStream oos = new ObjectInputStream(baos);
				StatusMessage msg = (StatusMessage) oos.readObject();
				msg.setUdpPort(PORT);
				Platform.runLater(() -> partialResults.get().add(msg));
				LOGGER.info(String.format("Local(%d@%s) <<<< Remote (%d@%s): %s", socket.getLocalPort(),
						socket.getLocalAddress().toString(), packet.getPort(), packet.getAddress().toString(),
						msg.toString()));
			}
		} catch (SocketException e) {
			LOGGER.error(String.format("Failed to create a DatagramSocket bound to host %s at port %d.", ADDRESS, PORT),
					e);
		} catch (IOException e) {
			LOGGER.error("Failed to read from the DatagramSocket", e);
		}
		return partialResults.get();
	}

}
