package elasticsearchlibrary;

import java.util.Random;

public class Tutor {

	static String randomId=null;
	Appbase appbase;
	static final String 
//						  user = "vspynv5Dg", pass =
//						  "f54091f5-ff77-4c71-a14c-1c29ab93fd15",
						 
	user = "7eJWHfD4P",
			pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
			URL = "http://scalr.api.appbase.io",
			appName = "jsfiddle-demo",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";
	Random r=new Random();
	/* Trial1796 */
	String type = "product", id = "1";
	
	
	
	public void setup(){
		appbase = new Appbase(URL, appName, user, pass);
		randomId=Trial.generateId();
	}
	

}
