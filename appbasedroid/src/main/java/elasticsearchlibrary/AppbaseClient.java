package elasticsearchlibrary;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Param;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.Base64;
import org.elasticsearch.index.query.QueryBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import elasticsearchlibrary.handlers.AppbaseStreamHandler;
import elasticsearchlibrary.requestbuilders.AppbaseRequestBuilder;
import elasticsearchlibrary.handlers.AppbasePipedStreamHandler;

public class AppbaseClient extends DefaultAsyncHttpClient {

	private String baseURL, URL, password, userName, app;
	private ArrayList<Param> parameters = new ArrayList<Param>();

	private static final String SEPARATOR = "/";

	// Do not include a / anywhere.
	// URL is the base URL.
	// App is the app name.
	// userName is the userName for your app.
	// password which matches with the username.
	// use the setters to set the the URL, app, userName, password.
	/**
	 * new Appbase()
	 * 
	 * create a new Appbase() by passing in arguments:
	 * 
	 * @param URL
	 *            The base URL eg. "www.example.com" or
	 *            "http://scalr.api.appbase.io".
	 * @param appName
	 *            application name eg. "myFirstApp" or "jsfiddle-demo"
	 * @param userName
	 *            the user name provided for the app eg. "7eJWHfD4P"
	 * @param password
	 *            the password corresponding to the userName eg.
	 *            "431d9cea-5219-4dfb-b798-f897f3a02665"
	 * 
	 */
	public AppbaseClient(String URL, String app, String userName, String password) {

		this.baseURL = URL;
		this.password = password;
		this.userName = userName;
		this.app = app;
		constructURL();
		Param stream = new Param("stream", "true");
		getParameters().add(stream);

	}

	/**
	 * Returns the constructed URL based on the type argument.
	 * 
	 * @param type
	 * @return constructed URL with the given type
	 */

	public String getURL(String type) {
		return URL + SEPARATOR + type;
	}
	
	public String getURL(String[] type) {
		String returnURL=URL + SEPARATOR;
		for (int i = 0; i < type.length; i++) {
			returnURL+=type[i]+",";
		}
		return returnURL.substring(0, returnURL.length()-1);
	}

	/**
	 * Returns the constructed URL based on the type and id.
	 * 
	 * @param type
	 * @param id
	 * @return constructed URL with type and id
	 */
	public String getURL(String type, String id) {
		return URL + SEPARATOR + type + SEPARATOR + id;
	}

	/**
	 * Getter for userName
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Getter for password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * returns the constructed URL by adding a search term as its query
	 * parameter.
	 * 
	 * @param term
	 * @return Search URL with the term as a query parameter
	 */
	public String getSearchUrl(String term) {
		return URL + SEPARATOR + "_search?q=" + term;
	}

	/**
	 * returns the constructed URL for a type by adding the search term as its
	 * query parameter.
	 * 
	 * @param term
	 *            the term to be searched
	 * @return Search URL with the term as a query parameter
	 */
	public String getSearchUrl(String type, String term) {
		return URL + SEPARATOR + type + SEPARATOR + "_search?q=" + term;
	}

	/**
	 * If the Appbase object needs to be reused or if a wrong base URL is set,
	 * this can be used to reset it.
	 * 
	 * @param URL
	 */
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

	public ArrayList<Param> getParameters() {
		return parameters;
	}

	public void addDefaultParameters(ArrayList<Param> parameters) {
		this.parameters.addAll(parameters);
	}

	public void addDefaultParameter(Param parameter) {
		this.parameters.add(parameter);
	}

	// Main library methods

	// index()
	// update()
	// delete()
	// bulk()
	// get()
	// getTypes()
	// search()
	// getStream()
	// searchStream()
	// searchStreamToURL()

	/**
	 * To index a object easily by just inputing the parameters.
	 * 
	 * @param type
	 *            the type of the object
	 * @param id
	 *            the id at which it need to be inserted
	 * @param jsonDoc
	 *            the String which is the JSON for the object to be inserted
	 * @return the result after the operation. It contains the details of the
	 *         operations execution.
	 */
	public String index(String type, String id, String jsonDoc) {
		ListenableFuture<Response> f = prepareIndex(type, id, jsonDoc).execute();
		try {
			Response r = f.get();
			return r.getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String index(String type, String id, byte[] jsonDoc) {
		return index(type, id, new String(jsonDoc));
	}

	public String index(String type, String id, JsonObject jsonDoc) {
		return index(type, id, jsonDoc.getAsString());
	}

	public String index(String type, String id, Map<String, Object> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return index(type, id, json);
	}

	/**
	 * Index a document without providing the id. Id will be automatically
	 * created.
	 * 
	 * @param type
	 *            type of the object
	 * @param jsonDoc
	 *            the object to be indexed
	 * @return
	 */
	public ListenableFuture<Response> index(String type, String jsonDoc) {
		return prepareIndex(type, jsonDoc).execute();
	}

	private AppbaseRequestBuilder prepareIndex(String type, String jsonDoc) {
		return new AppbaseRequestBuilder(super.preparePut(getURL(type)).addHeader("Authorization", "Basic " + getAuth()).setBody(jsonDoc));
	}

	public AppbaseRequestBuilder prepareIndex(String type, byte[] jsonDoc) {
		return prepareIndex(type, new String(jsonDoc));
	}

	public AppbaseRequestBuilder prepareIndex(String type, JsonObject jsonDoc) {
		return prepareIndex(type, jsonDoc.getAsString());
	}

	public AppbaseRequestBuilder prepareIndex(String type, Map<String, Object> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return prepareIndex(type, json);
	}

	/**
	 * To prepare the index. To have control on when it is executed or to add
	 * parameters or queries
	 * 
	 * @param type
	 *            the type of the object
	 * @param id
	 *            the id at which it need to be inserted
	 * @param jsonDoc
	 *            the String which is the JSON for the object to be inserted
	 * @return returns the BuundRequestBuilder object which can be executed
	 */

	public AppbaseRequestBuilder prepareIndex(String type, String id, String jsonDoc) {
		return new AppbaseRequestBuilder(preparePut(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()).setBody(jsonDoc));
	}

	public AppbaseRequestBuilder prepareIndex(String type, String id, byte[] jsonDoc) {
		return prepareIndex(type, id, new String(jsonDoc));
	}

	public AppbaseRequestBuilder prepareIndex(String type, String id, JsonObject jsonDoc) {
		return prepareIndex(type, id, jsonDoc.getAsString());
	}

	public AppbaseRequestBuilder prepareIndex(String type, String id, Map<String, Object> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return prepareIndex(type, id, json);
	}

	/**
	 * To update a document. We can pass just the portion of the object to be
	 * updated. parameters is a list of parameters which are the name value
	 * pairs which will be added during the execution
	 * 
	 * @param type
	 *            the type of the object
	 * @param id
	 *            the id at which it need to be inserted
	 * @param parameters
	 *            A list of all the parameters for a specific update
	 * @param jsonDoc
	 *            the String which is the JSON for the object to be inserted
	 * @return the result after the operation. It contains the details of the
	 *         operations execution.
	 */

	public String update(String type, String id, List<Param> parameters, String jsonDoc) {
		ListenableFuture<Response> f = prepareUpdate(type, id, parameters, jsonDoc).execute();
		try {
			Response r = f.get();
			return r.getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String update(String type, String id, List<Param> parameters, byte[] jsonDoc) {
		return update(type, id, parameters, new String(jsonDoc));
	}

	public String update(String type, String id, List<Param> parameters, JsonObject jsonDoc) {
		return update(type, id, parameters, jsonDoc.getAsString());
	}

	public String update(String type, String id, List<Param> parameters, Map<String, Object> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return update(type, id, parameters, json);
	}

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters, String jsonDoc) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type, id) + SEPARATOR + "_update")
				.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(parameters).setBody(jsonDoc));
	}

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters, byte[] jsonDoc) {
		return prepareUpdate(type, id, parameters, new String(jsonDoc));
	}

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters, JsonObject jsonDoc) {
		return prepareUpdate(type, id, parameters, jsonDoc.getAsString());

	}

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters,
			Map<String, Object> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return prepareUpdate(type, id, parameters, json);

	}

	/**
	 * To delete a document
	 * 
	 * @param type
	 *            the type of the object
	 * @param id
	 *            the id at which it need to be inserted
	 * @return the result after the operation. It contains the details of the
	 *         operations execution.
	 */
	public String delete(String type, String id) {
		ListenableFuture<Response> f = prepareDelete(type, id).execute();
		try {
			Response r = f.get();
			return r.getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	public AppbaseRequestBuilder prepareDelete(String type, String id) {
		return new AppbaseRequestBuilder(super.prepareDelete(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()));
	}

	public ArrayList<ListenableFuture<Response>> bulkExecute(AppbaseRequestBuilder[] requestBuilders) {
		ArrayList<ListenableFuture<Response>> response = new ArrayList<ListenableFuture<Response>>();
		for (int i = 0; i < requestBuilders.length; i++) {
			response.add(requestBuilders[i].execute());
		}
		return response;
	}

	public ArrayList<ListenableFuture<Response>> bulkExecute(ArrayList<AppbaseRequestBuilder> requestList) {
		ArrayList<ListenableFuture<Response>> response = new ArrayList<ListenableFuture<Response>>();
		for (int i = 0; i < requestList.size(); i++) {
			response.add(requestList.get(i).execute());
		}
		return response;
	}

	/**
	 * 
	 * Method to get the indexed objects by specifying type and id
	 * 
	 * @param type
	 *            type of the required object
	 * @param id
	 *            id of the required object
	 * @return the json String of the required object
	 */

	public String get(String type, String id) {
		ListenableFuture<Response> f = super.prepareGet(getURL(type, id))
				.addHeader("Authorization", "Basic " + getAuth()).execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AppbaseRequestBuilder prepareGet(String type, String id) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()));
	}

	/**
	 * Returns the mappings
	 * 
	 * @return returns the json document as String of the mappings
	 */
	public String getTypes() {
		ListenableFuture<Response> f = super.prepareGet(this.URL + SEPARATOR + "_mapping")
				.addHeader("Authorization", "Basic " + getAuth()).execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AppbaseRequestBuilder prepareGetTypes() {
		return new AppbaseRequestBuilder(super.prepareGet(this.URL + SEPARATOR + "_mapping").addHeader("Authorization", "Basic " + getAuth()));
	}

	/**
	 * Search by passing the query body
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param body
	 *            the query body eg. ( {"query":{"term":{ "price" : 5595}}} )
	 * @return returns the search result corresponding to the query
	 */
	public String search(String type, String body) {
		ListenableFuture<Response> f = super.preparePost(getURL(type) + SEPARATOR + "_search")
				.addHeader("Authorization", "Basic " + getAuth()).setBody(body).execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AppbaseRequestBuilder prepareSearch(String type, String body) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setBody(body));
	}

	public AppbaseRequestBuilder prepareSearch(String type) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth()));
	}

	public AppbaseRequestBuilder prepareSearch(String type, QueryBuilder qb) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setBody(qb.toString()));
	}


	public AppbaseRequestBuilder prepareSearch(String[] type, String body) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setBody(body));
	}

	public AppbaseRequestBuilder prepareSearch(String[] type) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth()));
	}

	public AppbaseRequestBuilder prepareSearch(String[] type, QueryBuilder qb) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setBody(qb.toString()));
	}

	public AppbaseRequestBuilder prepareSearch() {
		return new AppbaseRequestBuilder(super.preparePost(URL + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth()));
	}

	/**
	 * 
	 * @param type
	 *            type in which search must be done
	 * @param parameters
	 *            parameters for searching
	 * @return
	 */
	public String searchUsingParameters(String type, java.util.List<Param> parameters) {
		ListenableFuture<Response> f = super.preparePost(getURL(type) + SEPARATOR + "_search")
				.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(parameters).execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AppbaseRequestBuilder prepareSearchUsingParameters(String type, java.util.List<Param> parameters) {
		return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				).addQueryParams(parameters);
	}

	/**
	 * Get the stream for a indexed object. If any changes happen, the user can
	 * state what to happen by overriding the onData method or overriding the
	 * onBodyPartRecieved.
	 * 
	 * @param type
	 *            type of the object
	 * @param id
	 *            id of the object
	 * @param asyncHandler
	 *            a async handler object. It is preferable to pass a Appbase
	 *            Handler object as single body may come as multiple body parts
	 *            which need to be managed which is implemented by Appbase
	 *            Handler.
	 */
	@SuppressWarnings("unchecked")
	public void getStream(String type, String id, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()).setRequestTimeout(60000000)
				.addQueryParams(getParameters()).execute(asyncHandler);
	}

	public AppbaseRequestBuilder prepareGetStream(String type, String id) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).addQueryParams(getParameters()));
	}

	public PipedInputStream getPipedStream(String type, String id) throws IOException {
		PipedOutputStream output = new PipedOutputStream();

		final PipedInputStream input = new PipedInputStream(output);

		AppbasePipedStreamHandler handler = new AppbasePipedStreamHandler(output);
		super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()).setRequestTimeout(60000000)
				.addQueryParams(getParameters()).execute(handler);
		return input;
	}

	/**
	 * Get the search stream for a indexed object. If any changes happen and the
	 * changes contain some part of the query, the user can state what to happen
	 * by overriding the onData() method or the onBodyPartRecieved() method.
	 * 
	 * @param type
	 *            type of the object
	 * @param id
	 *            id of the object
	 * @param appbaseHandlerSaveStream
	 *            a async handler object. It is preferable to pass a Appbase
	 *            Handler object as single body may come as multiple body parts
	 *            which need to be managed which is implemented by Appbase
	 *            Handler.
	 */
	@SuppressWarnings("unchecked")
	public void searchStream(String type, String body, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body).addQueryParams(getParameters()).execute(asyncHandler);

	}

	@SuppressWarnings("unchecked")
	public void searchStream(String type, QueryBuilder body, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body.toString()).addQueryParams(getParameters())
				.execute(asyncHandler);

	}
	@SuppressWarnings("unchecked")
	public void searchStream(String[] type, String body, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body).addQueryParams(getParameters()).execute(asyncHandler);

	}

	@SuppressWarnings("unchecked")
	public void searchStream(String[] type, QueryBuilder body, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body.toString()).addQueryParams(getParameters())
				.execute(asyncHandler);

	}
	
	public AppbaseRequestBuilder prepareSearchStream(String type, String body) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type) + SEPARATOR + "_search").setRequestTimeout(60000000)
				.addHeader("Authorization", "Basic " + getAuth()).setBody(body).addQueryParams(getParameters()));
	}

	
	public AppbaseRequestBuilder prepareSearchStream(String[] type, String body) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type) + SEPARATOR + "_search").setRequestTimeout(60000000)
				.addHeader("Authorization", "Basic " + getAuth()).setBody(body).addQueryParams(getParameters()));
	}
	
	public AppbaseRequestBuilder prepareSearchStream(String type) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type) + SEPARATOR + "_search").setRequestTimeout(60000000)
				.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(getParameters()));
	}
	
	public AppbaseRequestBuilder prepareSearchStream(String[] type) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type) + SEPARATOR + "_search").setRequestTimeout(60000000)
				.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(getParameters()));
	}

	public void searchStreamToURL() {

	}

	// Search Document
	public void searchDocument(String type, String id) {
		super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()).execute();

	}

	/**
	 * Search by term
	 * 
	 * @param term
	 *            term to be searched
	 */
	public void searchUri(String term) {

		super.prepareGet(getSearchUrl(term)).addHeader("Authorization", "Basic " + getAuth()).execute();	
	}
}

	// Extremely doubtful.

	// public void getStreamThread(String type, String id, AsyncHandler<String>
	// asyncHandler) {
	// GetRunnable getRunnable = new GetRunnable(type, id, asyncHandler,
	// httpClient, this);
	// Thread thread = new Thread(getRunnable);
	// System.out.println("started the thread");
	// thread.star