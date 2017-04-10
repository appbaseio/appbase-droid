package client;

import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class AppbaseWebsocketClient extends WebSocketClient {
	Stream on;

	public AppbaseWebsocketClient(URI serverUri, Stream on, Draft draft) {
		super(serverUri, draft);
		this.on = on;
		trustAllHosts();
	}

	public AppbaseWebsocketClient(URI serverURI, Stream on) {
		super(serverURI);
		this.on = on;
		trustAllHosts();
	}

	/**
	 * Implements the method stated in {@link Stream} object passed in the
	 * Constructor.
	 */
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		if (on != null)
			on.onOpen(handshakedata);
		else
			System.out.println("null " + handshakedata.toString());
	}

	/**
	 * Implements the method stated in {@link Stream} object passed in the
	 * Constructor.
	 */
	@Override
	public void onClose(int code, String reason, boolean remote) {
		if (on != null)
			on.onClose(code, reason, remote);
		else
			System.out.println("null close");
	}

	/**
	 * Implements the method stated in {@link Stream} object passed in the
	 * Constructor.
	 */
	@Override
	public void onError(Exception ex) {
		if (on != null)
			on.onError(ex);
		else
			System.out.println("null " + ex.getMessage());

	}

	/**
	 * Implements the method stated in {@link Stream} object passed in the
	 * Constructor.
	 */
	@Override
	public void onMessage(String message) {
		if (on != null)
			on.onMessage(message);
		else
			System.out.println("null " + message);
	}

	private void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			this.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
