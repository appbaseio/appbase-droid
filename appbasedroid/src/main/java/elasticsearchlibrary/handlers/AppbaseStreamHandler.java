package elasticsearchlibrary.handlers;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;

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

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#onStatusReceived(org.asynchttpclient.HttpResponseStatus)
	 */
	public State onStatusReceived(HttpResponseStatus arg0) throws Exception {
		if (arg0.getStatusCode() > 500)
			return State.ABORT;

		return State.CONTINUE;
	}

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#onHeadersReceived(org.asynchttpclient.HttpResponseHeaders)
	 */
	public State onHeadersReceived(HttpResponseHeaders arg0) throws Exception {
		return State.CONTINUE;
	}

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#onCompleted()
	 */
	public T onCompleted() throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#onBodyPartReceived(org.asynchttpclient.HttpResponseBodyPart)
	 */
	@SuppressWarnings("unchecked")
	public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
		halfBody += new String(bodyPart.getBodyPartBytes());
		JsonObject object;
		System.out.println(halfBody);
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
				System.out.println("on data");
				T newReceived = gson.fromJson(object, type);
				System.out.println(newReceived + " hgvc");
				onData(newReceived);
			}

		} catch (Exception e) {

		}
		return State.CONTINUE;
	}

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#onData(T)
	 */
	public abstract void onData(T data);

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#onThrowable(java.lang.Throwable)
	 */
	public void onThrowable(Throwable arg0) {
		
	}

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#encodeToJsonObject(java.lang.String)
	 */
	public void encodeToJsonObject(String data) {

	}

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#decodeFromJsonObject()
	 */
	public void decodeFromJsonObject() {

	}

	/* (non-Javadoc)
	 * @see elasticsearchlibrary.handlers.AppbaseHandler#formatResponse(com.google.gson.JsonObject)
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
