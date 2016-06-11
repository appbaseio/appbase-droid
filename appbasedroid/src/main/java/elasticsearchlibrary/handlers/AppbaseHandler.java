package elasticsearchlibrary.handlers;

import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.AsyncHandler.State;

import com.google.gson.JsonObject;

public interface AppbaseHandler<T> {

	State onStatusReceived(HttpResponseStatus arg0) throws Exception;

	State onHeadersReceived(HttpResponseHeaders arg0) throws Exception;

	T onCompleted() throws Exception;

	State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception;

	void onData(T data);

	void onThrowable(Throwable arg0);

	void encodeToJsonObject(String data);

	void decodeFromJsonObject();

	JsonObject formatResponse(JsonObject response);

}