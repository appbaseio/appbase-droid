package client;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import interceptor.BasicAuthInterceptor;
import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import requestbuilders.AppbaseRequestBuilder;
import requestbuilders.AppbaseWebsocketRequest;
import requestbuilders.Param;

public class AppbaseClient {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public Call newCall(Request request) {
		return ok.newCall(request);
	}

	private OkHttpClient ok;
	private String baseURL, app, URL, basicauth = null;
	private static final String SEPARATOR = "/";

	/**
	 * Constructor when the elasticsearch setup requires a user name and a
	 * password
	 * 
	 * @param baseURL
	 *            The base URL(example: "http://scalr.api.appbase.io").
	 * @param app
	 *            application name (example: "myFirstApp")
	 * @param username
	 *            the user name provided for the application
	 * @param password
	 *            the password corresponding to the userName
	 * 
	 */
	public AppbaseClient(String baseURL, String app, String username, String password) {

		initialize(username, password);
		this.baseURL = baseURL;
		this.app = app;
		constructURL();

	}

	private void setBasicAuth(String username, String password) {
		basicauth = Credentials.basic(username, password);
	}

	private void initialize(String username, String password) {
		setBasicAuth(username, password);
		ok = getUnsafeOkHttpClient().addInterceptor(new BasicAuthInterceptor(username, password)).build();

	}

	private static Builder getUnsafeOkHttpClient() {
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}
			} };

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
			builder.hostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			return builder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Constructor when the elasticsearch setup does not require user name and
	 * password
	 * 
	 * @param URL
	 *            The base URL(example: "http://scalr.api.appbase.io").
	 * @param app
	 *            application name (example: "myFirstApp")
	 */
	public AppbaseClient(String URL, String app) {
		this.baseURL = URL;
		initialize();
		this.app = app;
		constructURL();
	}

	private void initialize() {
		if (baseURL.contains("://") && baseURL.contains("@")) {
			int a = baseURL.indexOf("://");
			int b = baseURL.indexOf("@");
			if (baseURL.substring(a + 3, b).contains(":")) {
				String[] x = baseURL.substring(a + 3, b).split(":");
				setBasicAuth(x[0], x[1]);
				ok = getUnsafeOkHttpClient().addInterceptor(new BasicAuthInterceptor(x[0], x[1])).build();
			}
			this.baseURL = baseURL.substring(0, a + 3) + baseURL.substring(b + 1);
			return;
		}
		ok = getUnsafeOkHttpClient().build();
	}

	private String constructURL() {
		if (this.baseURL.endsWith("/")) {
			this.URL = this.baseURL + this.app;
			return this.URL;

		} else {
			this.URL = this.baseURL + "/" + this.app;
		}
		return this.URL;
	}

	// getURls

	// querify
	private String querify(String body) {
		return "{\"query\":" + body + "}";

	}

	/**
	 * Returns the constructed URL based on the type argument.
	 * 
	 * @param type
	 * @return constructed URL
	 */

	private String getURL(String type) {
		return URL + SEPARATOR + type;
	}

	/**
	 * Returns the constructed URL for multiple types
	 * 
	 * @param type
	 * @return constructed URL
	 */
	private String getURL(String[] type) {
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
	private String getURL(String type, String id) {
		return URL + SEPARATOR + type + SEPARATOR + id;
	}

	public void setURL(String URL) {
		this.baseURL = URL;
		constructURL();
	}

	public void setApp(String app) {
		this.app = app;
		constructURL();
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
	public AppbaseRequestBuilder prepareIndex(String type, String jsonDoc) {
		return new AppbaseRequestBuilder(this, type, null, AppbaseRequestBuilder.Index).url(getURL(type)).post(jsonDoc);
	}

	/**
	 * The {@link String} body is converted to {@link RequestBody}
	 * 
	 * @param jsonDoc
	 *            The {@link String} body which needs to be converted.
	 * @return The {@link RequestBody} object for the given String.
	 */
	public static RequestBody createBody(String jsonDoc) {
		return RequestBody.create(JSON, jsonDoc);
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
	 * @return returns the AppbaseRequestBuilderer object which can be executed
	 */

	public AppbaseRequestBuilder prepareIndex(String type, String id, String jsonDoc) {

		return new AppbaseRequestBuilder(this, type, id, AppbaseRequestBuilder.Index).url(getURL(type, id))
				.put(jsonDoc);
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
	 * @return returns the AppbaseRequestBuilderer object which can be executed
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
	 * @return returns the AppbaseRequestBuilderer object which can be executed
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
	 * @return returns the AppbaseRequestBuilderer object which can be executed
	 */

	public AppbaseRequestBuilder prepareIndex(String type, String id, Map<String, String> jsonDoc) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(jsonDoc);
		return prepareIndex(type, id, json);
	}

	/**
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document. We
	 * can pass just the portion of the object to be updated. parameters is a
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
		return new AppbaseRequestBuilder(this, type, id, AppbaseRequestBuilder.Update)
				.url(getURL(type, id) + SEPARATOR + "_update").post(jsonDoc).addQueryParams(parameters);
	}

	/**
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document. We
	 * can pass just the portion of the object to be updated. parameters is a
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
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document. We
	 * can pass just the portion of the object to be updated. parameters is a
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
	 * To prepare a {@link AppbaseRequestBuilder} object to update a document. We
	 * can pass just the portion of the object to be updated. parameters is a
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
		return new AppbaseRequestBuilder(this, type, id, AppbaseRequestBuilder.Delete).url(getURL(type, id)).delete();
	}

	/**
	 * When multiple requests need to be executed but in a sequence to reduce
	 * the bandwidth usage.
	 * 
	 * @param request
	 *            The request in bulk format.
	 * 
	 * @return The AppbaseRequestBuilder object for the query which needs to be
	 *         executed.
	 */

	public AppbaseRequestBuilder prepareBulkExecute(String request) {

		return new AppbaseRequestBuilder(this, null, null, AppbaseRequestBuilder.Rest).url(getURL("_bulk"))
				.post(request);
	}

	/**
	 * 
	 * Prepare an {@link AppbaseRequestBuilder} object to get the indexed objects
	 * by specifying type and id
	 * 
	 * @param type
	 *            type of the required object
	 * @param id
	 *            id of the required object
	 * @return the {@link AppbaseRequestBuilder} object having the required
	 *         configuration for get to execute
	 */
	public AppbaseRequestBuilder prepareGet(String type, String id) {
		return new AppbaseRequestBuilder(this, null, null, AppbaseRequestBuilder.Rest).url(getURL(type, id)).get();
	}

	/**
	 * Get the mappings of the Application
	 * 
	 * @return returns the json document as {@link String} of the mappings
	 */
	public String getMappings() {
		Response f = prepareGetMappings().execute();

		try {
			return new String(f.body().bytes());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object to get the mappings of the
	 * Application
	 * 
	 * @return returns the json document as {@link String} of the mappings
	 */
	public AppbaseRequestBuilder prepareGetMappings() {
		return new AppbaseRequestBuilder(this, null, null, AppbaseRequestBuilder.Rest)
				.url(this.URL + SEPARATOR + "_mapping").get();
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
		Set<Map.Entry<String, JsonElement>> entries = object.getAsJsonObject(app).getAsJsonObject("mappings")
				.entrySet();// will return members
							// of your object
		JsonArray ret = new JsonArray();
		for (Map.Entry<String, JsonElement> entry : entries) {
			if (!entry.getKey().equals("_default_"))
				ret.add(entry.getKey());
		}
		return ret.toString();
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching by adding the
	 * search body
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}}
	 *            )
	 * @return returns the search result corresponding to the query
	 */
	public AppbaseRequestBuilder prepareSearch(String type, String body) {
		return new AppbaseRequestBuilder(this, null, null, AppbaseRequestBuilder.Rest)
				.url(getURL(type) + SEPARATOR + "_search").post(querify(body));
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object for searching by adding the
	 * query body within multiple types
	 * 
	 * @param type
	 *            array of all the types in which the search must take place
	 * @param body
	 *            the query body (example: {"query":{"term":{ "price" : 5595}}})
	 * @return returns the search result corresponding to the query
	 */

	public AppbaseRequestBuilder prepareSearch(String[] type, String body) {
		return new AppbaseRequestBuilder(this, null, null, AppbaseRequestBuilder.Rest)
				.url(getURL(type) + SEPARATOR + "_search").post(querify(body));
	}

	/**
	 * Prepare an {@link AppbaseRequestBuilder} object to search by passing the
	 * query as a List of param objects. This will be added like query
	 * parameters not in the body.
	 * 
	 * @param type
	 *            type in which the search must take place
	 * @param parameters
	 *            List of Parameter objects which
	 * @return returns the search result corresponding to the query
	 */
	public AppbaseRequestBuilder prepareSearch(String type, List<Param> parameters) {
		return new AppbaseRequestBuilder(this, null, null, AppbaseRequestBuilder.Rest)
				.url(getURL(type) + SEPARATOR + "_search").addQueryParams(parameters).post(null);

	}

	/**
	 * The search results are streamed to the URL specified in webhoook.
	 * 
	 * @param type
	 *            The type in which the search must be done.
	 * @param query
	 *            The {@link String} query which needs to be executed.
	 * @param webhook
	 *            The URL at which the search results need to be streamed.
	 * @return The {@link AppbaseRequestBuilder} for the search stream to URL request.
	 */
	public AppbaseRequestBuilder prepareSearchStreamToURL(String type, String query, String webhook) {
		JsonParser parser = new JsonParser();
		query = querify(query);
		JsonObject object = parser.parse(query).getAsJsonObject();
		JsonObject o = parser.parse(webhook).getAsJsonObject();
		object.add("webhooks", o.get("webhooks"));
		JsonArray arr = new JsonArray();
		arr.add(type);
		object.add("type", arr);
		String path = ".percolator/webhooks-0-" + type + "-0-" + getSerializedJson(query);
		return prepareIndex(path, object);
	}

	private String getSerializedJson(String query) {
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(query).getAsJsonObject();
		Gson gson = new Gson();
		TreeMap<String, Object> map = gson.fromJson(object, TreeMap.class);
		String sortedJson = gson.toJson(map);
		return sortedJson;

	}

	private JsonObject getSearchStreamJson(String type, String request) {
		JsonObject json = new JsonObject();
		Random r = new Random();
		int n = r.nextInt(5) + 5;
		String id = "";
		for (int i = 0; i < n + 1; i++) {
			id += (char) (r.nextInt(25) + 97) + "";
		}
		json.addProperty("id", id);
		if (basicauth != null)
			json.addProperty("authorization", basicauth);
		json.addProperty("path", app + SEPARATOR + type + SEPARATOR + "_search?streamonly=true");

		JsonParser parser = new JsonParser();

		json.add("body", parser.parse(request).getAsJsonObject());
		json.addProperty("method", "POST");
		return json;
	}

	/**
	 * 
	 * @param type
	 *            type to be searched in.
	 * @param request
	 *            The search query.
	 * @return The {@link AppbaseWebsocketRequest} created which needs to be
	 *         executed to execute the query.
	 */
	public AppbaseWebsocketRequest prepareSearchStream(String type, String request) {
		return new AppbaseWebsocketRequest(getSearchStreamJson(type, request), baseURL);
	}

}
