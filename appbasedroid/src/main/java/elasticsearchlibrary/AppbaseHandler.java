package elasticsearchlibrary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Tirth Shah on 14-05-2016.
 */

public abstract class AppbaseHandler implements AsyncHandler<String> {
	private boolean getResult;
	JsonArray jsonArray;

	public AppbaseHandler(boolean getResult) {
		this.getResult = getResult;
		jsonArray = new JsonArray();
	}

	public State onStatusReceived(HttpResponseStatus arg0) throws Exception {
		if (arg0.getStatusCode() > 500)
			return State.ABORT;

		return State.CONTINUE;
	}

	public State onHeadersReceived(HttpResponseHeaders arg0) throws Exception {
		return State.CONTINUE;
	}

	public String onCompleted() throws Exception {
		if (getResult)
			return jsonArray.toString();
		else
			return null;
	}

	public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
			throws Exception {
		ByteArrayOutputStream localBytes = new ByteArrayOutputStream();
		try {
			localBytes.write(bodyPart.getBodyPartBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (getResult) {
			JsonParser parser = new JsonParser();

			JsonObject object = parser.parse(localBytes.toString())
					.getAsJsonObject();
			jsonArray.add(object);
		}

		onData(localBytes.toString());

		return State.CONTINUE;
	}

	public abstract void onData(String data);

	public void onThrowable(Throwable arg0) {

	}

	public void encodeToJsonObject(String data) {

	}

	public void decodeFromJsonObject() {
	}

}