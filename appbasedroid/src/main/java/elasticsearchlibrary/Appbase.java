package elasticsearchlibrary;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Param;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.Base64;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tirth Shah on 10-05-2016.
 */

public class Appbase {

	AsyncHttpClient httpClient;
	private String baseURL, URL, password, userName, app;
	private ArrayList<Param> parameters = new ArrayList<Param>();

	private static final String SEPARATOR = "/";

	// Do not include a / anywhere.
	// URL is the base URL.
	// App is the app name.
	// userName is the userName for your app.
	// password which matches with the username.
	// use the setters to set the the URL, app, userName, password.
	
	public class BulkRequestObject {
		private String type, id, jsonDoc;
		private int method;
		public static final int INDEX = 0, DELETE = 1, UPDATE = 2;

		// Put jsonDoc as null if delete wanted.
		public BulkRequestObject(String type, String id, int method,
				String jsonDoc) {
			this.type = type;
			this.id = id;
			this.method = method;
			this.jsonDoc = jsonDoc;
		}

	}

	public Appbase(String URL, String app, String userName, String password) {
		this.baseURL = URL;
		this.password = password;
		this.userName = userName;
		this.app = app;
		constructURL();
		httpClient = new DefaultAsyncHttpClient();
		Param stream = new Param("stream", "true");
		parameters.add(stream);

	}

	public String getURL(String type) {
		
		return URL +  SEPARATOR + type + SEPARATOR;
	}

	public String getURL(String type, String id) {
		System.out.println(URL + SEPARATOR + app + SEPARATOR + type + SEPARATOR + id);
		return URL  + SEPARATOR + type + SEPARATOR + id;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getSearchUrl(String term) {
		return URL + SEPARATOR + "_search?q=" + term;
	}

	public void setURL(String URL) {
		this.baseURL = URL;
		constructURL();
	}

	public void setApp(String app) {
		this.app = app;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void constructURL() {
		this.URL = this.baseURL + SEPARATOR + app;
	}

	public String getAuth() {
		String Auth = this.userName + ":" + this.password;
		return Base64.encode(Auth.getBytes());
	}

	// Main library methods

	/*
	 * index() 
	 * update() 
	 * delete() 
	 * bulk() 
	 * get() 
	 * getTypes() 
	 * search() 
	 * getStream() 
	 * searchStream() 
	 * searchStreamToURL()
	 */

	public String index(String type, String id, String jsonDoc) {

		RequestBuilder builder = new RequestBuilder("PUT");
		Request request = builder.setUrl(getURL(type, id))
				.addHeader("Authorization", "Basic " + getAuth())
				.setBody(jsonDoc).build();
		ListenableFuture<Response> f = httpClient.executeRequest(request);
		try {
			Response r = f.get();
			return r.toString();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String updateDocument(String type, String id, String jsonDoc) {
		return index(type, id, jsonDoc);
	}

	public String deleteDocument(String type, String id) {
		ListenableFuture<Response> f = httpClient
				.prepareDelete(getURL(type, id))
				.addHeader("Authorization", "Basic " + getAuth()).execute();
		try {
			Response r = f.get();
			return r.toString();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ArrayList<String> bulk(BulkRequestObject[] objects) {
		ArrayList<String> abc=new ArrayList<String>();
		for (int i = 0; i < objects.length; i++) {
			switch (objects[i].method) {
			case 0:
				abc.add(index(objects[i].type, objects[i].id,
						objects[i].jsonDoc));
				break;
			case 1:
				abc.add(deleteDocument(objects[i].type, objects[i].id));
				break;
			case 2:
				abc.add(
				updateDocument(objects[i].type, objects[i].id,
						objects[i].jsonDoc));
				break;
			default:
				abc.add(index(objects[i].type, objects[i].id,
						objects[i].jsonDoc));
				break;
			}

		}
		return abc;
	}

	public String get(String type, String id) {
		ListenableFuture<Response> f = httpClient.prepareGet(getURL(type, id))
				.addHeader("Authorization", "Basic " + getAuth()).execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String getTypes() {
		String URL=this.URL+SEPARATOR+"_mapping";
		ListenableFuture<Response> f=httpClient.prepareGet(URL)
		.addHeader("Authorization", "Basic " + getAuth()).execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String search( String type,String body) {
		System.out.println(getURL(type) + "_search");
		ListenableFuture<Response> f=httpClient.preparePost(getURL(type) + "_search")
				.addHeader("Authorization", "Basic " + getAuth())
				.setBody(body)
				.execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void searchUsingParameters(String type,java.util.List<Param> parameters){
		
	};

	public void getStream(String type, String id,
			AsyncHandler<String> asyncHandler) {
		ListenableFuture<String> f=httpClient.prepareGet(getURL(type, id))
				.addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000)
				.addQueryParams(parameters)
				.execute(asyncHandler);
		try {
			System.out.println(f.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void searchStream(String type,String body,AsyncHandler<String> asyncHandler) {
		System.out.println(getURL(type) + "_search");
		ListenableFuture<String> f=httpClient.preparePost(getURL(type) + "_search")
				.addHeader("Authorization", "Basic " + getAuth())
				.setBody(body)
				.addQueryParams(parameters)
				.execute(asyncHandler);
		try {
			System.out.println(f.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchStreamToURL(){
		
	}

	// Search Document
	public void searchDocument(String type, String id) {
		httpClient.prepareGet(getURL(type, id))
				.addHeader("Authorization", "Basic " + getAuth()).execute();

	}

	public void searchUri(String term) {

		httpClient.prepareGet(getSearchUrl(term))
				.addHeader("Authorization", "Basic " + getAuth()).execute();

	}


	// Extremely doubtful.
	public void autoId(String type, String jsonDoc) {
		RequestBuilder builder = new RequestBuilder("PUT");
		Request request = builder.setUrl(getURL(type))
				.addHeader("Authorization", "Basic " + getAuth())
				.setBody(jsonDoc).build();
		httpClient.executeRequest(request);
	}
}





// debug links

/*
 * private String URL="http://scalr.api.appbase.io", app="Trial1796",
 * userName="vspynv5Dg", password="f54091f5-ff77-4c71-a14c-1c29ab93fd15";
 */