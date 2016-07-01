package elasticsearchlibrary;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Param;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.Base64;
import org.elasticsearch.index.query.QueryBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import elasticsearchlibrary.handlers.AppbaseStreamHandler;
import elasticsearchlibrary.requestbuilders.AppbaseRequestBuilder;
import io.netty.handler.ssl.SslContext;
import elasticsearchlibrary.examples.Main;
import elasticsearchlibrary.handlers.AppbasePipedStreamHandler;


/**
 * 
 * @author Tirth Shah 
 *
 */
public class AppbaseClient extends DefaultAsyncHttpClient {

	private String baseURL, URL, password, userName, app;
	private ArrayList<Param> parameters = new ArrayList<Param>();

	private static final String SEPARATOR = "/";

	/**
	 * Constructor when the elasticsearch setup requires a user name and a
	 * password
	 * 
	 * @param URL
	 *            The base URL(example: "http://scalr.api.appbase.io").
	 * @param appName
	 *            application name (example: "myFirstApp")
	 * @param userName
	 *            the user name provided for the application
	 * @param password
	 *            the password corresponding to the userName
	 * 
	 */
	public AppbaseClient(String URL, String app, String userName, String password) {
		super(new DefaultAsyncHttpClientConfig.Builder().setAcceptAnyCertificate(true).build());
		this.baseURL = URL;
		this.password = password;
		this.userName = userName;
		this.app = app;
		constructURL();
		Param stream = new Param("streamonly", "true");
		getParameters().add(stream);
		

	}

	/**
	 * Constructor when the elasticsearch setup does not require user name and
	 * password
	 * 
	 * @param URL
	 *            The base URL(example: "http://scalr.api.appbase.io").
	 * @param appName
	 *            application name (example: "myFirstApp")
	 */
	public AppbaseClient(String URL, String app) {
		super(new DefaultAsyncHttpClientConfig.Builder().setAcceptAnyCertificate(true).build());
		this.baseURL = URL;
		this.password = null;
		this.userName = null;
		this.app = app;
		Param stream = new Param("stream", "true");
		getParameters().add(stream);
		setAuth(URL);

		constructURL();
	}
	private void setAuth(String URL){
		int subEnd=0;
		if(URL.contains("http://")){
			if(URL.contains("@")){
				int passwordStarted=0;
				for (int i = 7; i < URL.length(); i++) {
					
					if(URL.charAt(i)==':'){
						this.userName=URL.substring(7,i);
						passwordStarted=i;
					}
					if(passwordStarted!=0){
						if(URL.charAt(i)=='@'){
							this.password=URL.substring(passwordStarted+1,i);
							subEnd=i+1;
						}
					}
					
				}
				this.URL=URL.substring(0,7)+URL.substring(subEnd);
				System.out.println(this.URL);
				System.out.println(userName);
				System.out.println(password);
			}
		}else if(URL.contains("https://")){
			if(URL.contains("@")){
				int passwordStarted=0;
				for (int i = 8; i < URL.length(); i++) {
					
					if(URL.charAt(i)==':'){
						this.userName=URL.substring(8,i);
						passwordStarted=i;
					}
					if(passwordStarted!=0){
						if(URL.charAt(i)=='@'){
							this.password=URL.substring(passwordStarted+1,i);
							subEnd=i+1;
						}
					}
					
				}
				this.URL=URL.substring(0,8)+URL.substring(subEnd);
				System.out.println(this.URL);
				System.out.println(userName);
				System.out.println(password);
			}
		}


		if(userName!=null&&this.password==null){
			userName=null;
		}
		
	}

	// Getters

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

	// getURls

	/**
	 * Returns the constructed URL based on the type argument.
	 * 
	 * @param type
	 * @return constructed URL
	 */

	public String getURL(String type) {
		return URL + SEPARATOR + type;
	}

	/**
	 * Returns the constructed URL for multiple types
	 * 
	 * @param type
	 * @return constructed URL
	 */
	public String getURL(String[] type) {
		String returnURL = URL + SEPARATOR;
		for (int i = 0; i < type.length; i++) {
			returnURL += type[i] + ",";
		}
		return returnURL.substring(0, returnURL.length() - 1);
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
	 * returns the constructed URL by adding a search term as its query
	 * parameter.
	 * 
	 * @param term
	 *            the term to be searched
	 * @return Search URL with the term as a query parameter
	 */
	public String getSearchUrl(String term) {
		return URL + SEPARATOR + "_search?q=" + term;
	}

	/**
	 * 
	 * returns the constructed URL for a type by adding the search term as its
	 * query parameter.
	 * 
	 * @param type
	 * @param term
	 *            the term to be searched
	 * @return Search URL with the term as a query parameter
	 */
	public String getSearchUrl(String type, String term) {
		return URL + SEPARATOR + type + SEPARATOR + "_search?q=" + term;
	}

	/**
	 * Setter for URL
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

	/**
	 * Get the authentication headers using the userName and password
	 * 
	 * @return
	 */
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
	 * Prepare the request for indexing a document without providing the id. Id
	 * will be automatically created.
	 * 
	 * @param type
	 *            type of the object
	 * @param jsonDoc
	 *            the object to be indexed
	 * @return request builder with the provided configurations
	 */
	public AppbaseRequestBuilder prepareIndex(String type, String jsonDoc) {
		if (userName != null)
			return new AppbaseRequestBuilder(
					super.preparePut(getURL(type)).addHeader("Authorization", "Basic " + getAuth()).setBody(jsonDoc));
		else
			return new AppbaseRequestBuilder(super.preparePut(getURL(type)).setBody(jsonDoc));

	}

	/**
	 * Prepare the request for indexing a document without providing the id. Id
	 * will be automatically created.
	 * 
	 * @param type
	 *            type of the object
	 * @param jsonDoc
	 *            the object to be indexed
	 * @return request builder with the provided configurations
	 */
	public AppbaseRequestBuilder prepareIndex(String type, byte[] jsonDoc) {
		return prepareIndex(type, new String(jsonDoc));
	}

	/**
	 * Prepare the request for indexing a document without providing the id. Id
	 * will be automatically created.
	 * 
	 * @param type
	 *            type of the object
	 * @param jsonDoc
	 *            the object to be indexed
	 * @return request builder with the provided configurations
	 */
	public AppbaseRequestBuilder prepareIndex(String type, JsonObject jsonDoc) {
		return prepareIndex(type, jsonDoc.toString());
	}

	/**
	 * Prepare the request for indexing a document without providing the id. Id
	 * will be automatically created.
	 * 
	 * @param type
	 *            type of the object
	 * @param jsonDoc
	 *            the object to be indexed
	 * @return request builder with the provided configurations
	 */
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
	 * @return returns the AppbaseRequestBuilder object which can be executed
	 */

	public AppbaseRequestBuilder prepareIndex(String type, String id, String jsonDoc) {
		if (userName != null)
			return new AppbaseRequestBuilder(
					preparePut(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()).setBody(jsonDoc));
		else
			return new AppbaseRequestBuilder(preparePut(getURL(type, id)).setBody(jsonDoc));
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
	 * @return returns the AppbaseRequestBuilder object which can be executed
	 */

	public AppbaseRequestBuilder prepareIndex(String type, String id, byte[] jsonDoc) {
		return prepareIndex(type, id, new String(jsonDoc));
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
	 * @return returns the AppbaseRequestBuilder object which can be executed
	 */

	public AppbaseRequestBuilder prepareIndex(String type, String id, JsonObject jsonDoc) {
		return prepareIndex(type, id, jsonDoc.toString());
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
	 * @return returns the AppbaseRequestBuilder object which can be executed
	 */

	public AppbaseRequestBuilder prepareIndex(String type, String id, Map<String, String> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return prepareIndex(type, id, json);
	}

	/**
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document.
	 * We can pass just the portion of the object to be updated. parameters is a
	 * list of parameters which are the name value pairs which will be added
	 * during the execution
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

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters, String jsonDoc) {
		if (userName != null)
			return new AppbaseRequestBuilder(super.preparePost(getURL(type, id) + SEPARATOR + "_update")
					.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(parameters).setBody(jsonDoc));
		else
			return new AppbaseRequestBuilder(super.preparePost(getURL(type, id) + SEPARATOR + "_update")
					.addQueryParams(parameters).setBody(jsonDoc));

	}

	/**
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document.
	 * We can pass just the portion of the object to be updated. parameters is a
	 * list of parameters which are the name value pairs which will be added
	 * during the execution
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

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters, byte[] jsonDoc) {
		return prepareUpdate(type, id, parameters, new String(jsonDoc));
	}

	/**
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document.
	 * We can pass just the portion of the object to be updated. parameters is a
	 * list of parameters which are the name value pairs which will be added
	 * during the execution
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

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters, JsonObject jsonDoc) {
		return prepareUpdate(type, id, parameters, jsonDoc.toString());

	}

	/**
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document.
	 * We can pass just the portion of the object to be updated. parameters is a
	 * list of parameters which are the name value pairs which will be added
	 * during the execution
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

	public AppbaseRequestBuilder prepareUpdate(String type, String id, List<Param> parameters,
			Map<String, Object> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return prepareUpdate(type, id, parameters, json);

	}

	/**
	 * To prepare an {@link AppbaseRequestBuilder} object to delete a document.
	 * 
	 * @param type
	 *            the type of the object
	 * @param id
	 *            the id at which it need to be inserted
	 * @return the result after the operation. It contains the details of the
	 *         operations execution.
	 */
	public AppbaseRequestBuilder prepareDelete(String type, String id) {
		if (userName != null)
			return new AppbaseRequestBuilder(
					super.prepareDelete(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()));
		else
			return new AppbaseRequestBuilder(super.prepareDelete(getURL(type, id)));

	}

	/**
	 * When multiple requests need to be executed but in a sequence to reduce
	 * the bandwidth usage.
	 * 
	 * @param requestBuilders
	 *            an array of AppbaseRequestBuilders which need to be executed
	 * @return Array of the listenable futures containing the responses of
	 *         individual requests
	 */
	@SuppressWarnings("unchecked")
	public ListenableFuture<Response>[] bulkExecute(AppbaseRequestBuilder[] requestBuilders) {
		ListenableFuture<Response>[] response = new ListenableFuture[requestBuilders.length];
		for (int i = 0; i < requestBuilders.length; i++) {
			response[i] = (requestBuilders[i].execute());
		}
		return response;
	}

	/**
	 * When multiple requests need to be executed but in a sequence to reduce
	 * the bandwidth usage.
	 * 
	 * @param requestBuilders
	 *            an array of AppbaseRequestBuilders which need to be executed
	 * @return Array of the listenable futures containing the responses of
	 *         individual requests
	 */
	@SuppressWarnings("unchecked")
	public ListenableFuture<Response>[] bulkExecute(ArrayList<AppbaseRequestBuilder> requestBuilders) {
		ListenableFuture<Response>[] response = new ListenableFuture[requestBuilders.size()];
		for (int i = 0; i < requestBuilders.size(); i++) {
			response[i] = (requestBuilders.get(i).execute());
		}
		return response;
	}

	/**
	 * 
	 * Prepare an {@link AppbaseRequestBuilder} object to get the indexed
	 * objects by specifying type and id
	 * 
	 * @param type
	 *            type of the required object
	 * @param id
	 *            id of the required object
	 * @return the {@link AppbaseRequestBuilder} object having the required
	 *         configuration for get to execute
	 */
	public AppbaseRequestBuilder prepareGet(String type, String id) {
		if (userName != null)
			return new AppbaseRequestBuilder(
					super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()));
		else
			return new AppbaseRequestBuilder(super.prepareGet(getURL(type, id)));

	}

	/**
	 * Get the mappings of the Application
	 * 
	 * @return returns the json document as {@link String} of the mappings
	 */
	public String getMappings() {
		ListenableFuture<Response> f = prepareGetMappings().execute();
		try {
			return f.get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object to get the mappings of
	 * the Application
	 * 
	 * @return returns the json document as {@link String} of the mappings
	 */
	public AppbaseRequestBuilder prepareGetMappings() {
		if (userName != null)
			return new AppbaseRequestBuilder(
					prepareGet(this.URL + SEPARATOR + "_mapping").addHeader("Authorization", "Basic " + getAuth()));
		else
			return new AppbaseRequestBuilder(prepareGet(this.URL + SEPARATOR + "_mapping"));

	}

	/**
	 * Method to get an array of types
	 * 
	 * @return String containing JsonArray of the types
	 */
	public String getTypes() {

		String result = getMappings();
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(result).getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = object.getAsJsonObject("jsfiddle-demo")
				.getAsJsonObject("mappings").entrySet();// will return members
														// of your object
		JsonArray ret = new JsonArray();
		for (Map.Entry<String, JsonElement> entry : entries) {
			if (!entry.getKey().equals("_default_"))
				ret.add(entry.getKey());
		}
		return ret.toString();
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching by adding
	 * the search body
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}}
	 *            )
	 * @return returns the search result corresponding to the query
	 */
	public AppbaseRequestBuilder prepareSearch(String type, String body) {
		return prepareSearch(type).setBody(body);
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching without
	 * adding the body which will need to be manually added
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}}
	 *            )
	 * @return returns the search result corresponding to the query
	 */

	public AppbaseRequestBuilder prepareSearch(String type) {
		if (userName != null)
			return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search")
					.addHeader("Authorization", "Basic " + getAuth()));
		else
			return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search"));

	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching without
	 * adding the body which will need to be manually added
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}}
	 *            )
	 * @return returns the search result corresponding to the query
	 */

	public AppbaseRequestBuilder prepareSearch(String type, QueryBuilder qb) {
		return prepareSearch(type, qb.toString());
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching by adding
	 * the query body within multiple types
	 * 
	 * @param type
	 *            array of all the types in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}})
	 * @return returns the search result corresponding to the query
	 */

	public AppbaseRequestBuilder prepareSearch(String[] type, String body) {
		return prepareSearch(type).setBody(body);
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching without
	 * adding the body which will need to be manually added within multiple
	 * types
	 * 
	 * @param type
	 *            array of all the types in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}}
	 *            )
	 * @return returns the search result corresponding to the query
	 */
	public AppbaseRequestBuilder prepareSearch(String[] type) {
		if (userName != null)
			return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search")
					.addHeader("Authorization", "Basic " + getAuth()));
		else
			return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search"));

	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching without
	 * adding the body which will need to be manually added within multiple
	 * types
	 * 
	 * @param type
	 *            array of all the types in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}}
	 *            )
	 * @return returns the search result corresponding to the query
	 */
	public AppbaseRequestBuilder prepareSearch(String[] type, QueryBuilder qb) {
		return prepareSearch(type).setBody(qb.toString());
	}

	/**
	 * Prepares an {@link AppbaseRequestBuilder} object with the search config
	 * without the types and the query body, the body needs to be added
	 * seperately.
	 * 
	 * @return {@link AppbaseRequestBuilder} object with basic search config
	 */
	public AppbaseRequestBuilder prepareSearch() {
		if (userName != null)
			return new AppbaseRequestBuilder(
					super.preparePost(URL + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth()));
		else
			return new AppbaseRequestBuilder(super.preparePost(URL + SEPARATOR + "_search"));
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object to search by passing the
	 * query as a List of param objects. This will be added like query
	 * parameters not in the body.
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param body
	 *            List of Parameter objects which
	 * @return returns the search result corresponding to the query
	 */

	public AppbaseRequestBuilder prepareSearch(String type, java.util.List<Param> parameters) {
		if (userName != null)
			return new AppbaseRequestBuilder(super.preparePost(getURL(type) + SEPARATOR + "_search")
					.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(parameters));
		else
			return new AppbaseRequestBuilder(
					super.preparePost(getURL(type) + SEPARATOR + "_search").addQueryParams(parameters));

	}

	/**
	 * Get the stream for a indexed object. If any changes happen, the user can
	 * state what to happen by overriding the onData method or overriding the
	 * onBodyPartreceived.
	 * 
	 * @param type
	 *            type of the object
	 * @param id
	 *            id of the object
	 * @param appbaseHandler
	 *            An {@link AppbaseStreamHandler} object which specifies what to
	 *            do with the received data.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getStream(String type, String id, AppbaseStreamHandler appbaseHandler) {
		if (userName != null)
			super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth())
					.setRequestTimeout(60000000).addQueryParams(getParameters()).execute(appbaseHandler);
		else
			super.prepareGet(getURL(type, id)).setRequestTimeout(60000000).addQueryParams(getParameters())
					.execute(appbaseHandler);

	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object to get the stream for a
	 * indexed object. If any changes happen, the user can state what to happen
	 * by overriding the onData method or overriding the onBodyPartreceived.
	 * 
	 * @param type
	 *            type of the object
	 * @param id
	 *            id of the object
	 * @return {@link AppbaseRequestBuilder} object which needs to be executed
	 *         with an {@link AppbaseStreamHandler}
	 */
	public AppbaseRequestBuilder prepareGetStream(String type, String id) {
		return new AppbaseRequestBuilder(
				super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth())
						.setRequestTimeout(60000000).addQueryParams(getParameters()));
	}

	/**
	 * Still requires modification
	 * 
	 * @param type
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public PipedInputStream getPipedStream(String type, String id) throws IOException {
		PipedOutputStream output = new PipedOutputStream();

		final PipedInputStream input = new PipedInputStream(output);

		AppbasePipedStreamHandler handler = new AppbasePipedStreamHandler(output);
		super.prepareGet(getURL(type, id)).addHeader("Authorization", "Basic " + getAuth()).setRequestTimeout(60000000)
				.addQueryParams(getParameters()).execute(handler);
		return input;
	}

	/**
	 * 
	 * Get the search stream for a indexed object. If any changes happen and the
	 * changes contain some part of the query, the user can state what to happen
	 * by overriding the onData() method.
	 * 
	 * @param type
	 *            type of the object
	 * @param body
	 *            query body
	 * @param asyncHandler
	 *            An {@link AppbaseStreamHandler} object which specifies what to
	 *            do with the received data.
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void searchStream(String type, String body, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body).addQueryParams(getParameters()).execute(asyncHandler);

	}

	/**
	 * 
	 * Get the search stream for a indexed object. If any changes happen and the
	 * changes contain some part of the query, the user can state what to happen
	 * by overriding the onData() method.
	 * 
	 * @param type
	 *            type of the object
	 * @param body
	 *            query body
	 * @param asyncHandler
	 *            An {@link AppbaseStreamHandler} object which specifies what to
	 *            do with the received data.
	 */
	@SuppressWarnings("unchecked")
	public void searchStream(String type, QueryBuilder body, @SuppressWarnings("rawtypes") AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body.toString()).addQueryParams(getParameters())
				.execute(asyncHandler);

	}

	/**
	 * 
	 * Get the search stream for a indexed object. If any changes happen and the
	 * changes contain some part of the query, the user can state what to happen
	 * by overriding the onData() method.
	 * 
	 * @param type
	 *            array of the types in which the search should take place.
	 * @param body
	 *            query body
	 * @param asyncHandler
	 *            An {@link AppbaseStreamHandler} object which specifies what to
	 *            do with the received data.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void searchStream(String[] type, String body, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body).addQueryParams(getParameters()).execute(asyncHandler);
	}

	/**
	 * 
	 * Get the search stream for a indexed object. If any changes happen and the
	 * changes contain some part of the query, the user can state what to happen
	 * by overriding the onData() method.
	 * 
	 * @param type
	 *            type of the object
	 * @param body
	 *            query body
	 * @param asyncHandler
	 *            An {@link AppbaseStreamHandler} object which specifies what to
	 *            do with the received data.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void searchStream(String[] type, QueryBuilder body, AppbaseStreamHandler asyncHandler) {
		super.prepareGet(getURL(type) + SEPARATOR + "_search").addHeader("Authorization", "Basic " + getAuth())
				.setRequestTimeout(60000000).setBody(body.toString()).addQueryParams(getParameters())
				.execute(asyncHandler);
	}

	/**
	 * 
	 * Prepare an {@link AppbaseRequestBuilder} object to get the search stream
	 * for a indexed object. Needed to be executed with an
	 * {@link AppbaseStreamHandler}
	 * 
	 * @param type
	 *            type of the object
	 * @param body
	 *            query body
	 */

	public AppbaseRequestBuilder prepareSearchStream(String type, String body) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type) + SEPARATOR + "_search")
				.setRequestTimeout(60000000).addHeader("Authorization", "Basic " + getAuth()).setBody(body)
				.addQueryParams(getParameters()));
	}

	/**
	 * 
	 * Prepare an {@link AppbaseRequestBuilder} object to get the search stream
	 * for a indexed object. Needed to be executed with an
	 * {@link AppbaseStreamHandler}
	 * 
	 * @param type
	 *            array of types where the search should take place
	 * @param body
	 *            query body
	 */
	public AppbaseRequestBuilder prepareSearchStream(String[] type, String body) {
		return new AppbaseRequestBuilder(super.prepareGet(getURL(type) + SEPARATOR + "_search")
				.setRequestTimeout(60000000).addHeader("Authorization", "Basic " + getAuth()).setBody(body)
				.addQueryParams(getParameters()));
	}

	/**
	 * 
	 * Prepare an {@link AppbaseRequestBuilder} object to get the search stream
	 * for a indexed object. Needed to be executed with an
	 * {@link AppbaseStreamHandler} and the query body needs to be added
	 * 
	 * @param type
	 *            type of the object
	 */
	public AppbaseRequestBuilder prepareSearchStream(String type) {
		return new AppbaseRequestBuilder(
				super.prepareGet(getURL(type) + SEPARATOR + "_search").setRequestTimeout(60000000)
						.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(getParameters()));
	}

	/**
	 * 
	 * Prepare an {@link AppbaseRequestBuilder} object to get the search stream
	 * for a indexed object. Needed to be executed with an
	 * {@link AppbaseStreamHandler}
	 * 
	 * @param type
	 *            type of the object
	 * @param body
	 *            query body
	 */
	public AppbaseRequestBuilder prepareSearchStream(String[] type) {
		return new AppbaseRequestBuilder(
				super.prepareGet(getURL(type) + SEPARATOR + "_search").setRequestTimeout(60000000)
						.addHeader("Authorization", "Basic " + getAuth()).addQueryParams(getParameters()));
	}

	/**
	 * This method indexes the search request for it to be streamed to the
	 * provided URL
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param query
	 *            the search query
	 * @param webhook
	 *            the webhook containing the url for the search
	 * @return
	 */
	public AppbaseRequestBuilder prepareSearchStreamToURL(String type, String query, String webhook) {
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(query).getAsJsonObject();
		JsonObject o = parser.parse(webhook).getAsJsonObject();
		object.add("webhooks", o.get("webhooks"));
		JsonArray arr = new JsonArray();
		arr.add(type);
		object.add("type", arr);
		String path = ".percolator/webhooks-0-" + type + "-0-" + UUID.randomUUID();
		return prepareIndex(path, object);
	}

}