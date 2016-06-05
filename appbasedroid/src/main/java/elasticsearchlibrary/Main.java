package elasticsearchlibrary;

import com.google.gson.JsonParser;

/**
 * Created by Tirth Shah on 10-05-2016 for Appbase.io
 */

public class Main {
	static JsonParser parser;
	static Appbase appbase;
	static final String user = "7eJWHfD4P",
			pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
			URL = "http://scalr.api.appbase.io",
			appName = "jsfiddle-demo",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";

	static String type = "product", id = "1";
	public static void main(String[] args) {
		String URL="http://scalr.api.appbase.io", app="Trial1796",
				  user="vspynv5Dg", pass="f54091f5-ff77-4c71-a14c-1c29ab93fd15";
		appbase = new Appbase(URL, app, user, pass);
		System.out.println("starting execution");
		appbase.getStream(type, id, new AppbaseHandler(false) {
			
			@Override
			public void onData(String data) {
				System.out.println(data);
			}
		});
		System.out.println("ending execution");
		
		
		

	}
}
