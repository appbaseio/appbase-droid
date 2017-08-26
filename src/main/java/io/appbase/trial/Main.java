package io.appbase.trial;
import java.io.IOException;

import com.google.gson.JsonParser;

import io.appbase.client.AppbaseClient;
import io.appbase.client.Stream;

public class Main {

	static JsonParser parser;
	static AppbaseClient appbase;
	int startId = 1, endId;
	static String user = "vspynv5Dg", pass = "f54091f5-ff77-4c71-a14c-1c29ab93fd15",
			URL = "https://vspynv5Dg:f54091f5-ff77-4c71-a14c-1c29ab93fd15@scalr.api.appbase.io", appName = "Trial1796",
			query = "{\"query\":{\"term\":{\"price\" : 5595}}}",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}",
			webhook = "{ \"webhooks\": [  {   \"url\": \"http://requestb.in/1e5e7bn1\"  } ], \"query\": {  \"match_all\": {} }, \"type\": [  \"tweet\" ]}";

	static String type = "product", id = "1";

	public static void main(String[] args) {
		AppbaseClient ac = new AppbaseClient(URL, appName);
		try {
			String response = ac.prepareSearchStreamToURL(type, query, webhook).execute().body().string();
			System.out.println("response " + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}