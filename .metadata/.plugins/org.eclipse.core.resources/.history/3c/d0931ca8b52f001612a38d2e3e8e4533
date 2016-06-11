package elasticsearchlibrary;

import java.util.Random;

public class Tutor {

	static String randomId=null;
	AppbaseClient appbase;
	static final String 
//						  user = "vspynv5Dg", pass =
//						  "f54091f5-ff77-4c71-a14c-1c29ab93fd15",
						 
	user = "7eJWHfD4P",
			pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
			URL = "http://scalr.api.appbase.io",
			appName = "jsfiddle-demo",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";
	Random r;
	/* Trial1796 */
	String type = "product", id = "1";
	
	
	
	public void setup(){
		
		/**
		 * new Appbase()
		 * 
		 * create a new Appbase() by passing in arguments:
		 * URL- The base URL eg. "www.example.com" or "http://scalr.api.appbase.io".
		 * appName- eg. "myFirstApp" or "jsfiddle-demo"
		 * user- the userName provided for the app eg. "7eJWHfD4P"
		 * password corresponding to the userName eg. "431d9cea-5219-4dfb-b798-f897f3a02665"
		 */
		
		
		appbase = new AppbaseClient(URL, appName, user, pass);
		
		
		
		r=new Random();
		randomId=Trial.generateId();
	}
	
	public void tryIndex(){
		/**
		 * type-the type of document eg. "product"
		 */
		appbase.index(type, randomId, jsonDoc);
    }
    public void tryDelete(){

    }
    public void tryBulk(){

    }
    public void tryUpdate(){

    }
    public void trySearch(){

    }
    public void tryGetStream(){

    }
    
    public String tryGetTypes(){
    	return appbase.getTypes();
    	
    }
    
    public void trySearchStream(){

    }
    public void trySearchStreamToUrl(){

    }
}
