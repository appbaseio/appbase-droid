package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.appbase.client.AppbaseClient;
import okhttp3.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppbaseTests {
	static JsonParser parser;
	static String randomId = null;
	static String randomIds[] = null;
	static AppbaseClient appbase;
	static final String user = "vspynv5Dg", pass = "f54091f5-ff77-4c71-a14c-1c29ab93fd15", type = "product", id = "1",
			URL = "http://scalr.api.appbase.io", appName = "Trial1796";
	static String jsonString = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";
	
	static byte[] jsonBytes;
	static JsonObject jsonObject;
	static Random r;


	public static String generateId() {
		if (r == null) {
			r = new Random();
		}
		int n = r.nextInt(5) + 5;
		String id = "";
		for (int i = 0; i < n + 1; i++) {
			id += (char) (r.nextInt(25) + 97) + "";
		}
		return id;
	}

	public static String[] generateIds(int k) {
		if (r == null) {
			r = new Random();
		}
		String[] arr = new String[k];
		for (int i = 0; i < arr.length; i++) {

			int n = r.nextInt(5) + 5;
			arr[i] = "";
			for (int j = 0; j < n + 1; j++) {
				arr[i] += (char) (r.nextInt(25) + 97) + "";
			}
		}
		return arr;
	}

	@BeforeClass
	public static void setup() {
		/**
		 * SETUP new Appbase() Pass the parameters URL (base url eg.
		 * http://scalr.api.appbase.io) appName eg. jsfiddle-demo user name. The
		 * one provided for that particular app(eg. 7eJWHfD4P ) password
		 * corresponding to the userName(eg.
		 * 431d9cea-5219-4dfb-b798-f897f3a02665 )
		 */
		r = new Random();
		appbase = new AppbaseClient(URL, appName, user, pass);
		randomId = generateId();
		randomIds = generateIds(4);
		parser = new JsonParser();
		changeAll(jsonString);

	}

	public static void changeAll(String jsonString) {
		jsonObject = parser.parse(jsonString).getAsJsonObject();
		jsonBytes = jsonString.getBytes();

	}

	@Test
	public void AindexTest() {

		/**
		 * Index String result = appbase.index(type, id, jsonDoc); type and id
		 * of the object you need to insert. jsonDoc is the entire body we need
		 * to insert.
		 *
		 */

		String result = null;
		try {
			result = appbase.prepareIndex(type, randomIds[0], jsonString).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println(result);
		assertNotNull(result);
		JsonObject object = parser.parse(result).getAsJsonObject();
		String created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

		result = null;

		try {
			result = appbase.prepareIndex(type, randomIds[2], jsonObject).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

		result = null;

		try {
			result = appbase.prepareIndex(type, randomIds[3], jsonBytes).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

	}

	@Test
	public void BupdateTest() {
		int generatedPrice = 5;
		String jsonDoc = "{doc: {\"price\": " + generatedPrice + "}}";
		changeAll(jsonDoc);

		Response result = null;

		result = appbase.prepareUpdate(type, randomIds[0], null, jsonDoc).execute();
		
		System.out.println(result + "\n\n\n");

		JsonObject object = null;
		try {
			object = parser.parse(result.body().string()).getAsJsonObject();
		} catch (JsonSyntaxException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		assertNotEquals(object.get("_version").getAsInt(), 1);
		assertEquals(object.getAsJsonObject("_shards").get("successful"),
				object.getAsJsonObject("_shards").get("total"));
		assertNotEquals(object.getAsJsonObject("_shards").get("successful").getAsInt(), 0);

		result = appbase.prepareUpdate(type, randomIds[0], null, jsonDoc).execute();

		try {
			object = parser.parse(result.body().string()).getAsJsonObject();
		} catch (JsonSyntaxException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		assertEquals(object.getAsJsonObject("_shards").get("successful"),
				object.getAsJsonObject("_shards").get("total"));
		assertEquals(object.getAsJsonObject("_shards").get("successful").getAsInt(), 0);

	}

	@Test
	public void CdeleteTest() {
		String result = null;

		try {
			result = appbase.prepareDelete(type, randomIds[0]).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), true);
		assertEquals(object.getAsJsonObject("_shards").get("failed").getAsInt(), 0);

		try {
			result = appbase.prepareDelete(type, randomIds[0]).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), false);
		assertEquals(object.getAsJsonObject("_shards").get("failed").getAsInt(), 0);

	}

	@Test
	public void EgetTest() {

		String result = null;

		try {
			result = appbase.prepareGet(type, randomIds[1]).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		JsonObject object = parser.parse(result).getAsJsonObject();
		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), false);
	}

	@Test
	public void FgetTypesTest() {
		System.out.println(appbase.getTypes());
		String result = appbase.getMappings();
		JsonObject object = parser.parse(result).getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = object.getAsJsonObject(appName)
				.getAsJsonObject("mappings").entrySet();// will return members
														// of your object
		JsonArray ret = new JsonArray();
		for (Map.Entry<String, JsonElement> entry : entries) {
			ret.add(entry.getKey());
		}
		System.out.println(ret.toString());
		assertEquals(object.isJsonObject(), true);
	}

	@Test
	public void GsearchTest() {
		int generatedPrice = 5;
		String body = "{\"query\":{\"term\":{ \"price\" : " + generatedPrice + "}}}";
		String result = null;

		try {
			result = appbase.prepareSearch(type, body).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		JsonObject object = parser.parse(result).getAsJsonObject();

		assertEquals(object.isJsonObject(), true);
		assertEquals(object.getAsJsonObject("hits").get("total").getAsInt(), 0);
		generatedPrice = 5595;
		body = "{\"query\":{\"term\":{ \"price\" : " + generatedPrice + "}}}";

		try {
			result = appbase.prepareSearch(type, body).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.isJsonObject(), true);
		assertNotEquals(object.getAsJsonObject("hits").get("total").getAsInt(), 0);

	}

	// @Test
	public void AAAAAAAJsearchStreamToURLTest() {
		System.out.println("AAAAAAA");

		Response r = appbase
				.prepareSearchStreamToURL(type, "{\"query\":{\"term\":{ \"price\" : 5595}}}",
						"{ \"webhooks\": [  {   \"url\": \"http://requestb.in/1e5e7bn1\"  } ], \"query\": {  \"match_all\": {} }, \"type\": [  \"tweet\" ]}")
				.execute();
		try {
			System.out.println(r.body().string());
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println("AAAAAAA");

	}

	public void testIndex() {

		String user = "7eJWHfD4P", pass = "431d9cea-5219-4dfb-b798-f897f3a02665", URL = "http://scalr.api.appbase.io",
				appName = "jsfiddle-demo",
				jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";

		appbase = new AppbaseClient(URL, appName, user, pass);
		// Index 1
		Response response = appbase.prepareIndex(type, id, jsonDoc).execute();
		try {
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testSearch() {
		String user = "7eJWHfD4P", pass = "431d9cea-5219-4dfb-b798-f897f3a02665", URL = "http://scalr.api.appbase.io",
				appName = "jsfiddle-demo";

		appbase = new AppbaseClient(URL, appName, user, pass);

		// Search 1

		String body = "{\"query\":{\"term\":{ \"price\" : 5595}}}";
		Response response = appbase.prepareSearch(type, body).addHeader("any", "header")
				.addQueryParam("set", "parameter").execute();

		Response r = response;
		String a = null;
		try {
			a = r.body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println(a);

	}
}
