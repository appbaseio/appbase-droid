package requestbuilders;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import client.AppbaseClient;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class AppbaseRequestBuilder {
	public static final int Index = 1, Update = 2, Delete = 3, Rest = 0;
	Request.Builder builder;
	String type = null, id = null;
	AppbaseClient ac;
	URI uri;
	int method;
	String body;

	public AppbaseRequestBuilder(AppbaseClient a, String type, String id, int method) {
		this.ac = a;
		this.id = id;
		this.type = type;
		this.method = method;
		builder = new Builder();

	}

	/**
	 * 
	 * @param name
	 *            Key {@link String}
	 * @param value
	 *            Value {@link String}
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder addHeader(String name, String value) {
		builder.addHeader(name, value);
		return this;
	}

	/**
	 * 
	 * @return Builds the request according to the current state of the request
	 *         builder.
	 */
	public Request build() {
		return builder.build();
	}

	/**
	 * See {@link CacheControl}
	 */
	public AppbaseRequestBuilder cacheControl(CacheControl cacheControl) {
		builder.cacheControl(cacheControl);
		return this;
	}

	/**
	 * Set method type to delete
	 * 
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder delete() {
		builder.delete();
		return this;
	}

	/**
	 * Sets method type to delete and add the body.
	 * 
	 * @param body
	 *            Set the body of the request.
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder delete(String body) {
		this.body = body;
		builder.delete(AppbaseClient.createBody(body));
		return this;
	}

	/**
	 * Checks if builder equals the obj.
	 */
	public boolean equals(Object obj) {
		return builder.equals(obj);
	}

	/**
	 * Sets method type to get.
	 * 
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder get() {
		builder.get();
		return this;
	}

	public int hashCode() {
		return builder.hashCode();
	}

	/**
	 * 
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder head() {
		builder.head();
		return this;
	}

	/**
	 * Adds header with the key and value.
	 * 
	 * @param name
	 *            Key {@link String}
	 * @param value
	 *            Value {@link String}
	 * @return
	 */
	public AppbaseRequestBuilder header(String name, String value) {
		builder.header(name, value);
		return this;
	}

	/**
	 * Adds {@link Headers}
	 * 
	 * @param headers
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder headers(Headers headers) {
		builder.headers(headers);
		return this;
	}

	/**
	 * Sets method and body.
	 * 
	 * @param method
	 *            The method of request.
	 * @param body
	 *            The body of the request.
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder method(String method, String body) {
		this.body = body;
		builder.method(method, AppbaseClient.createBody(body));
		return this;
	}

	public AppbaseRequestBuilder patch(String body) {
		this.body = body;
		builder.patch(AppbaseClient.createBody(body));
		return this;
	}

	/**
	 * Sets method to post and adds the body.
	 * 
	 * @param body
	 *            The request body.
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder post(String body) {
		this.body = body;
		builder.post(AppbaseClient.createBody(body));
		return this;
	}

	/**
	 * Sets method to put and adds the body.
	 * 
	 * @param body
	 *            The request body
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder put(String body) {
		this.body = body;
		builder.put(AppbaseClient.createBody(body));
		return this;
	}

	/**
	 * 
	 * @param name
	 *            removes the respective header.
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder removeHeader(String name) {
		builder.removeHeader(name);
		return this;
	}

	public AppbaseRequestBuilder tag(Object tag) {
		builder.tag(tag);
		return this;
	}

	public String toString() {
		return builder.toString();
	}

	/**
	 * 
	 * @param url
	 *            The URL which need to be set
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder url(HttpUrl url) {
		uri = url.uri();
		return this;
	}

	/**
	 * 
	 * @param url
	 *            The URL which need to be set
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder url(String url) {
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			uri = null;
		}
		return this;
	}

	/**
	 * 
	 * @param url
	 *            The URL which need to be set
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder url(URL url) {
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			uri = null;
		}

		return this;
	}

	/**
	 * 
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder addQueryParams(List<Param> parameters) {
		if (parameters == null) {
			return this;
		}
		for (int i = 0; i < parameters.size(); i++) {
			try {
				appendUri(uri, parameters.get(i).key + "=" + parameters.get(i).value);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return this;
	}

	/**
	 * 
	 * @param key
	 *            The key of the key value pair of query parameter to be added.
	 * @param value
	 *            The value of the key value pair of query parameter to be
	 *            added.
	 * @return The modified {@link AppbaseRequestBuilder}.
	 */
	public AppbaseRequestBuilder addQueryParam(String key, String value) {

		try {
			appendUri(uri, key + "=" + value);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return this;
	}

	private static URI appendUri(URI oldUri, String appendQuery) throws URISyntaxException {

		String newQuery = oldUri.getQuery();
		if (newQuery == null) {
			newQuery = appendQuery;
		} else {
			newQuery += "&" + appendQuery;
		}

		URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), newQuery,
				oldUri.getFragment());

		return newUri;
	}

	/**
	 * 
	 * @return The {@link Response} of the Request is returned.
	 */
	public Response execute() {
		builder.url(uri.toString());
		try {
			return ac.newCall(builder.build()).execute();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
