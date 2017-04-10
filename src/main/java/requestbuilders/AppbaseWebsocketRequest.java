//done
package requestbuilders;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonObject;

import client.Stream;
import client.AppbaseWebsocketClient;

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
		System.out.println(baseURL.charAt(4));
		if (baseURL.charAt(4) == 's') {
			baseURL = "wss" + baseURL.substring(5);
		} else {
			baseURL = "ws" + baseURL.substring(4);
		}
		System.out.println(baseURL);
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
		System.out.println(uri.toString());
		AppbaseWebsocketClient client = null;
		try {
			client = new AppbaseWebsocketClient(uri, appbaseOnMessage);
			client.connectBlocking();
			System.out.println(json.toString());
			client.send(json.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}

}
