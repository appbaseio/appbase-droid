package elasticsearchlibrary.examples;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import elasticsearchlibrary.AppbaseClient;
import elasticsearchlibrary.handlers.AppbaseStreamHandler;

/**
 * Created by Tirth Shah on 10-05-2016 for Appbase.io
 */

public class Main {
	static JsonParser parser;
	static AppbaseClient appbase;
	static final String user = "7eJWHfD4P", pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
			URL = "http://scalr.api.appbase.io", appName = "jsfiddle-demo",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";

	static String type = "product", id = "1";

	public static void main(String[] args) {

		String URL = "http://scalr.api.appbase.io", app = "Trial1796", user = "vspynv5Dg",
				pass = "f54091f5-ff77-4c71-a14c-1c29ab93fd15";
		appbase = new AppbaseClient(URL, app, user, pass);
		r();
	}

	public static void r() {
		
	}

}