package elasticsearchlibrary.examples;

import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.util.Base64;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import elasticsearchlibrary.AppbaseClient;

/**
 * Created by Tirth Shah on 10-05-2016 for Appbase.io
 */

public class Main {
	static JsonParser parser;
	static AppbaseClient appbase;
	static String user = "7eJWHfD4P", pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
			URL = "https://scalr.api.appbase.io", appName = "jsfiddle-demo",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";

	static String type = "product", id = "1";

	public static void main(String[] args) {
		System.out.println("https://"+user+":"+pass+"@scalr.api.appbase.io");
		appbase=new AppbaseClient("https://"+user+":"+pass+"@scalr.api.appbase.io", "jsfiddle-demo");
		try {
			System.out.println(appbase.prepareIndex(type, "1",jsonDoc).execute().get().getResponseBody());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		appbase.close();
//		AsyncHttpClient a=new DefaultAsyncHttpClient();
//		String r = null;
//		try {
//			r = a.preparePut(URL+"/"+appName+"/"+type+"/45").addHeader("Authorization", "Basic " + getAuth()).setBody(jsonDoc).execute().get().getResponseBody();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(r);
	}
	public static String getAuth() {
		String Auth = user + ":" + pass;
		return Base64.encode(Auth.getBytes());
	}


}
