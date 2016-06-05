package elasticsearchlibrary;


import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Tirth Shah on 14-05-2016.
 */

public abstract class AppbaseHandler implements AsyncHandler<String> {
	private boolean getResult;
	JsonArray jsonArray;
	String halfBody = "";
	JsonParser jsParser ;
	public boolean getResult(){
		return getResult;
	}
	public AppbaseHandler(boolean getResult) {
		this.getResult = getResult;
		jsonArray = new JsonArray();
		jsParser = new JsonParser();
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
			return "nothing was to be returned";
	}

	public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
			throws Exception {

		halfBody += new String(bodyPart.getBodyPartBytes());
		JsonObject object;
		try{
			JsonElement element = jsParser.parse(halfBody);
			object=element.getAsJsonObject();
			if(getResult){
				jsonArray.add(object);
			}
			halfBody="";
			onData(object.toString());
		}catch(Exception e){
			
		}
		return State.CONTINUE;
	}

	public abstract void onData(String data);

	public void onThrowable(Throwable arg0) {
		System.out.println(arg0);
	}

	public void encodeToJsonObject(String data) {

	}

	public void decodeFromJsonObject() {
	}

}