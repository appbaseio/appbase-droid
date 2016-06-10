package elasticsearchlibrary;


import java.io.IOException;
import java.io.PipedOutputStream;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Tirth Shah on 14-05-2016.
 */

public class AppbaseStreamHandler implements AsyncHandler<String> {
	String halfBody = "";
	JsonParser jsParser ;
	PipedOutputStream stream;
	public AppbaseStreamHandler(PipedOutputStream result) {
		jsParser = new JsonParser();
		stream=result;
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
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
			throws Exception {
		halfBody += new String(bodyPart.getBodyPartBytes());
		JsonObject object;
		try{
			JsonElement element = jsParser.parse(halfBody);
			object=element.getAsJsonObject();
			halfBody="";
			stream.write(object.toString().getBytes());
		}catch(Exception e){
			
		}
		return State.CONTINUE;
	}

	public void onThrowable(Throwable arg0) {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void encodeToJsonObject(String data) {

	}

	public void decodeFromJsonObject() {
	}

}