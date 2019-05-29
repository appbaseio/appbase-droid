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
public class AppbaseTest {
	static JsonParser parser;
	static String randomId = null;
	static String randomIds[] = null;
	static AppbaseClient appbase;
	static final String user = "HcMs8nqM5", pass = "53b342fd-4f27-4848-86e0-1c6233e91211", type = "_doc", id = "1",
			URL = "https://scalr.api.appbase.io", appName = "appbase-droid";
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

		assertNotNull(result);

		JsonObject object = parser.parse(result).getAsJsonObject();
		String created = object.get("result").getAsString();

		assertNotNull(created);
		assertEquals("created", created);

		result = null;

		try {
			result = appbase.prepareIndex(type, randomIds[2], jsonObject).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("result").getAsString();

		assertNotNull(created);
		assertEquals("created", created);

		result = null;

		try {
			result = appbase.prepareIndex(type, randomIds[3], jsonBytes).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("result").getAsString();

		assertNotNull(created);
		assertEquals("created", created);

	}

	@Test
	public void BupdateTest() {
		int generatedPrice = 5;
		String jsonDoc = "{ \"doc\": {\"price\": " + generatedPrice + "}}";
		changeAll(jsonDoc);

		Response result = appbase.prepareUpdate(type, randomIds[0], null, jsonDoc).execute();
		JsonObject object = null;
		try {
			object = parser.parse(result.body().string()).getAsJsonObject();
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
		assertNotNull(object);

		assertNotEquals(object.get("_version").getAsInt(), 1);
		assertEquals(object.getAsJsonObject("_shards").get("successful"),
				object.getAsJsonObject("_shards").get("total"));
		assertNotEquals(object.getAsJsonObject("_shards").get("successful").getAsInt(), 0);

		result = appbase.prepareUpdate(type, randomIds[0], null, jsonDoc).execute();

		try {
			object = parser.parse(result.body().string()).getAsJsonObject();
		} catch (JsonSyntaxException | IOException e) {
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
		assertNotNull(result);

		JsonObject object = parser.parse(result).getAsJsonObject();
		assertNotNull(object);
		assertEquals("deleted", object.get("result").getAsString());
		assertEquals(object.getAsJsonObject("_shards").get("failed").getAsInt(), 0);

		try {
			result = appbase.prepareDelete(type, randomIds[0]).execute().body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(result);

		object = parser.parse(result).getAsJsonObject();
		assertNotNull(object);
		assertEquals("not_found", object.get("result").getAsString());
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
		assertFalse(object.get("found").getAsBoolean());
	}

	@Test
	public void FgetTypesTest() {
		String result = appbase.getMappings();
		JsonObject object = parser.parse(result).getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = object.getAsJsonObject(appName).getAsJsonObject("mappings")
				.entrySet();// will return members
							// of your object
		JsonArray ret = new JsonArray();
		for (Map.Entry<String, JsonElement> entry : entries) {
			ret.add(entry.getKey());
		}
		assertTrue(object.isJsonObject());
	}

	@Test
	public void GsearchTest() {
		int generatedPrice = 5;
		String body = "{ \"query\": { \"term\": { \"price\": " + generatedPrice + " } } }";
		String result = null;

		try {
			result = appbase.prepareSearch(type, body).execute().body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(result);

		JsonObject object = parser.parse(result).getAsJsonObject();
		assertTrue(object.isJsonObject());
		assertEquals(object.getAsJsonObject("hits").get("total").getAsInt(), 0);
		generatedPrice = 5595;
		body = "{ \"query\": { \"term\": { \"price\": " + generatedPrice + " } } }";

		try {
			result = appbase.prepareSearch(type, body).execute().body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}

		object = parser.parse(result).getAsJsonObject();
		assertTrue(object.isJsonObject());
		assertNotEquals(object.getAsJsonObject("hits").get("total").getAsInt(), 0);
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
}
