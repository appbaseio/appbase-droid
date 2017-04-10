package client;

import org.java_websocket.handshake.ServerHandshake;

public abstract class Stream {
	/**
	 * Current implementation doesn't do anything. For specific implementation
	 * method needs to be overridden.
	 * 
	 */
	public void onOpen(ServerHandshake handshakedata) {
	};

	/**
	 * Current implementation doesn't do anything. For specific implementation
	 * method needs to be overridden.
	 * 
	 */
	public void onClose(int code, String reason, boolean remote) {
	};

	/**
	 * Current implementation doesn't do anything. For specific implementation
	 * method needs to be overridden.
	 * 
	 */
	public void onError(Exception ex) {
	}

	/**
	 * Needs to be overridden and be stated what to with the message received.
	 * 
	 * @param message
	 *            The received message.
	 */
	public abstract void onMessage(String message);
}
