package elasticsearchlibrary;

import org.asynchttpclient.Response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class AppbaseResponse implements Response{
	public JsonObject getResponseBodyAsJsonObject(){
		JsonParser parser=new JsonParser(); 
		return parser.parse(getResponseBody()).getAsJsonObject();
		
	};

}
