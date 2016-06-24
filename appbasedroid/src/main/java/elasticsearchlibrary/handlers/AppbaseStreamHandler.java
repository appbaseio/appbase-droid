package elasticsearchlibrary.handlers;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.AsyncHandler.State;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Tirth Shah on 14-05-2016.
 */

public abstract class AppbaseStreamHandler<T> implements AsyncHandler<T>{
	JsonArray jsonArray;
	String halfBody = "";
	JsonParser jsParser;
	private Class<T> type;

	public AppbaseStreamHandler(Class<T> type) {
		jsonArray = new JsonArray();
		jsParser = new JsonParser();
		this.type = type;
	}

    /**
     * Invoked as soon as the HTTP status line has been received
     *
     * @param responseStatus the status code and test of the response
     * @return a {@link State} telling to CONTINUE or ABORT the current processing.
     * @throws Exception if something wrong happens
     */
	public State onStatusReceived(HttpResponseStatus arg0) throws Exception {
		if (arg0.getStatusCode() > 500)
			return State.ABORT;

		return State.CONTINUE;
	}

    /**
     * Invoked as soon as the HTTP headers has been received. Can potentially be invoked more than once if a broken server
     * sent trailing headers.
     *
     * @param headers the HTTP headers.
     * @return a {@link State} telling to CONTINUE or ABORT the current processing.
     * @throws Exception if something wrong happens
     */
	public State onHeadersReceived(HttpResponseHeaders arg0) throws Exception {
		return State.CONTINUE;
	}

    /**
     * Invoked once the HTTP response processing is finished.
     * <br>
     * Gets always invoked as last callback method.
     *
     * @return T Value that will be returned by the associated {@link java.util.concurrent.Future}
     * @throws Exception if something wrong happens
     */
	public T onCompleted() throws Exception {
		return null;
	}

    /**
     * Invoked as soon as some response body part are received. Could be invoked many times.
     * Beware that, depending on the provider (Netty) this can be notified with empty body parts.
     *
     * @param bodyPart response's body part.
     * @return a {@link State} telling to CONTINUE or ABORT the current processing. Aborting will also close the connection.
     * @throws Exception if something wrong happens
     */
	@SuppressWarnings("unchecked")
	public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
		halfBody += new String(bodyPart.getBodyPartBytes());
		JsonObject object;
		try {
			JsonElement element = jsParser.parse(halfBody);
			object = element.getAsJsonObject();
			halfBody = "";
			Gson gson = new Gson();
			object=formatResponse(object);
			if (type == String.class) {
				onData((T) object.toString());
			} else if (type == JsonElement.class || JsonElement.class.isAssignableFrom(type)) {
				onData((T) object);
			} else {
				T newReceived = gson.fromJson(object, type);
				onData(newReceived);
			}

		} catch (Exception e) {

		}
		return State.CONTINUE;
	}


	/**
	 * Invoked as soon as a complete json body part is received. To be overridden by the user to make use of the data received. 
	 * @param data 
	 */
	public abstract void onData(T data);

    /**
     * Invoked when an unexpected exception occurs during the processing of the response. The exception may have been
     * produced by implementation of onXXXReceived method invocation.
     *
     * @param t a {@link Throwable}
     */
	public void onThrowable(Throwable arg0) {
		
	}


	/**
	 * To format the received json to remove the data not required 
	 * @param response The {@link JsonObject} to be formatted
	 * @return formatted {@link JsonObject}
	 */
	public JsonObject formatResponse(JsonObject response) {
		JsonObject formatted = new JsonObject();
		formatted = response.remove("_source").getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = response.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			if (entry.getKey().startsWith("_")) {
				formatted.add(entry.getKey(), entry.getValue());
			}
		}
		return formatted;
	}

}
