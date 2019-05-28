//done
package io.appbase.requestbuilders;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonObject;

import io.appbase.client.Stream;
import io.appbase.client.AppbaseWebsocketClient;

public class AppbaseWebsocketRequest {
	JsonObject json;
	String baseURL;

	public AppbaseWebsocketRequest(JsonObject json, String baseURL) {
		super();
		this.json = json;
		this.baseURL = baseURL;
		changeURL();
	}

	private void changeURL() {
		if (baseURL.startsWith("https://")) {
			baseURL = "wss" + baseURL.substring(5);
		} else if(baseURL.startsWith("http://")) {
			baseURL = "ws" + baseURL.substring(4);
		} else {
			baseURL = "ws://" + baseURL;
		}
	}

	/**
	 * 
	 * @param appbaseOnMessage
	 *            The implementation of abstract methods is used once the
	 *            connection has been established.
	 * @return Connects to the websocket and implements the methods onMessage
	 *         (and maybe other overridden ones of the Stream object).
	 */
	public AppbaseWebsocketClient execute(Stream appbaseOnMessage) {
		URI uri = null;
		try {
			uri = new URI(baseURL);
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		AppbaseWebsocketClient client = null;
		try {
			client = new AppbaseWebsocketClient(uri, appbaseOnMessage);
			client.connectBlocking();
			client.send(json.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}

}
