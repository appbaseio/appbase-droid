package elasticsearchlibrary;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import elasticsearchlibrary.handlers.AppbaseStreamHandler;
import elasticsearchlibrary.handlers.AppbaseHandler;
import elasticsearchlibrary.handlers.AppbaseHandlerSaveStream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppbaseTests {
	static JsonParser parser;
	static String randomId = null;
	static String randomIds[] = null;
	static AppbaseClient appbase;
	static final String 
	user = "vspynv5Dg", pass = "f54091f5-ff77-4c71-a14c-1c29ab93fd15", 
//			user = "7eJWHfD4P", pass = "431d9cea-5219-4dfb-b798-f897f3a02665", 
			type = "product", id = "1",
			URL = "http://scalr.api.appbase.io",
			appName = "Trial1796";
//	appName = "jsfiddle-demo";
	static String jsonString = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";
	static Map<String, String> jsonMap;
	static byte[] jsonBytes;
	static JsonObject jsonObject;
	static Random r;
	static Type typeToken;

	static Gson gson = new Gson();

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
		typeToken = new TypeToken<Map>() {
		}.getType();
		changeAll(jsonString);

	}

	public static void changeAll(String jsonString) {
		jsonObject = parser.parse(jsonString).getAsJsonObject();
		jsonBytes = jsonString.getBytes();
		jsonMap = gson.fromJson(jsonString, typeToken);

	}

	@Test
	public void AAAAAindexTest() {

		/**
		 * Index String result = appbase.index(type, id, jsonDoc); type and id
		 * of the object you need to insert. jsonDoc is the entire body we need
		 * to insert.
		 *
		 */

		// There will be tests for checking index for a new object and for one
		// which already exists
		String result = null;
		try {
			result = appbase.prepareIndex(type, randomIds[0], jsonString).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		assertNotNull(result);
		JsonObject object = parser.parse(result).getAsJsonObject();
		String created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

		result = appbase.index(type, randomIds[0], jsonString);
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("false", created);

		result = null;
		try {
			result = appbase.prepareIndex(type, randomIds[1], jsonMap).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

		result = appbase.index(type, randomIds[1], jsonMap);
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("false", created);

		result = null;
		try {
			result = appbase.prepareIndex(type, randomIds[2], jsonObject).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

		result = appbase.index(type, randomIds[2], jsonObject);
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("false", created);

		result = null;
		try {
			result = appbase.prepareIndex(type, randomIds[3], jsonBytes).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

		result = appbase.index(type, randomIds[3], jsonBytes);
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("false", created);
		for(;;){}

	}

	public void BupdateTest() {
		int generatedPrice = 5;
		String jsonDoc = "{doc: {\"price\": " + generatedPrice + "}}";
		changeAll(jsonDoc);

		Response result = null;
		try {
			result = appbase.prepareUpdate(type, randomIds[0], null, jsonDoc).execute().get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(result + "\n\n\n");

		JsonObject object = parser.parse(result.getResponseBody()).getAsJsonObject();
		assertNotEquals(object.getAsJsonObject("_version").getAsInt(), 1);
		assertEquals(object.getAsJsonObject("_shards").get("successful"),
				object.getAsJsonObject("_shards").get("total"));
		assertNotEquals(object.getAsJsonObject("_shards").get("successful").getAsInt(), 0);

		try {
			result = appbase.prepareUpdate(type, randomIds[0], null, jsonDoc).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		object = parser.parse(result.getResponseBody()).getAsJsonObject();
		assertEquals(object.getAsJsonObject("_shards").get("successful"),
				object.getAsJsonObject("_shards").get("total"));
		assertEquals(object.getAsJsonObject("_shards").get("successful").getAsInt(), 0);

	}

	@Test
	public void CdeleteTest() {
		String result = appbase.delete(type, randomIds[0]);
		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), true);
		assertEquals(object.getAsJsonObject("_shards").get("failed").getAsInt(), 0);

		result = appbase.delete(type, randomIds[0]);
		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), false);
		assertEquals(object.getAsJsonObject("_shards").get("failed").getAsInt(), 0);

	}

	public void EgetTest() {

		String result = null;
		try {
			result = appbase.prepareGet(type, randomIds[1]).execute(new AppbaseHandler<String>(String.class)).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObject object = parser.parse(result).getAsJsonObject();
		result = appbase.delete(type, randomIds[1]);
		result = appbase.get(type, randomIds[1]);
		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), false);
		appbase.index(result, randomIds[1], jsonString);
		result = null;
		try {
			result = appbase.prepareGet(type, randomIds[1]).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void AAAFgetTypesTest() {
		System.out.println(appbase.getTypes());
		String result = appbase.getMappings();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject object = parser.parse(result).getAsJsonObject();
		String json = gson.toJson(object);
		// System.out.println(json);
		Set<Map.Entry<String, JsonElement>> entries = object.getAsJsonObject("jsfiddle-demo")
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
			result = appbase.prepareSearch(type).setBody(body).execute().get().getResponseBody();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.isJsonObject(), true);
		assertNotEquals(object.getAsJsonObject("hits").get("total").getAsInt(), 0);
		generatedPrice = 5595;
		body = "{\"query\":{\"term\":{ \"price\" : " + generatedPrice + "}}}";
		result = appbase.search(type, body);
		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.isJsonObject(), true);
		assertNotEquals(object.getAsJsonObject("hits").get("total").getAsInt(), 0);

	}

	public void AAHgetStreamTest() {
		appbase.index(type, randomId, jsonString);
		appbase.getStream(type, randomId, new AppbaseStreamHandler<String>(String.class) {
			int i = 1;

			@Override
			public void onData(String data) {
				System.out.println(data);
			}

			@Override
			public org.asynchttpclient.AsyncHandler.State onBodyPartReceived(HttpResponseBodyPart bodyPart)
					throws Exception {
				if (i == 1) {
					appbase.update(type, randomId, null, "{doc: {\"price\": " + 2 + "}}");
					i++;
				} else if (i > 1) {
					String result = new String(bodyPart.getBodyPartBytes());
					JsonObject object = parser.parse(result).getAsJsonObject();
					assertEquals(object.getAsJsonObject("_source").get("price").getAsInt(), 2);
					return State.ABORT;
				}

				return State.CONTINUE;

			}
		});

	}

	@Test
	public void AAIsearchStreamTest() {
		appbase.searchStream(type, "{\"query\":{\"term\":{ \"price\" : 5595}}}",
				new AppbaseStreamHandler<String>(String.class) {

					@Override
					public void onData(String data) {
						System.out.println(data);
					}

				});

	}

	@Test
	public void AAAAAAAJsearchStreamToURLTest() {
		System.out.println("AAAAAAA");
		try {
			Response r=appbase.searchStreamToURL(type, "{\"query\":{\"term\":{ \"price\" : 5595}}}",
					"{ \"webhooks\": [  {   \"url\": \"http://requestb.in/1e5e7bn1\"  } ], \"query\": {  \"match_all\": {} }, \"type\": [  \"tweet\" ]}").get();
				System.out.println(r.getResponseBody());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
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
		ListenableFuture<Response> f = appbase.prepareIndex(type, id, jsonDoc).execute();
		try {
			Response r = f.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		// Index 2
		appbase.prepareIndex(URL, appName, jsonDoc).execute(new AppbaseHandlerSaveStream<String>(false, String.class) {

			@Override
			public void onData(String data) {
				// TODO Auto-generated method stub

			}

		});
		// Index 3

		ListenableFuture<Response> k = appbase.prepareIndex(type, id, jsonDoc).addQueryParam("same as add", "field")
				.addHeader("any", "header").execute();

		try {
			Response r = k.get();
			String a = r.getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void testSearch() {
		String user = "7eJWHfD4P", pass = "431d9cea-5219-4dfb-b798-f897f3a02665", URL = "http://scalr.api.appbase.io",
				appName = "jsfiddle-demo";

		appbase = new AppbaseClient(URL, appName, user, pass);

		// Search 1

		String body = "{\"query\":{\"term\":{ \"price\" : 5595}}}";
		ListenableFuture<Response> f = appbase.prepareSearch(type, body).addHeader("any", "header")
				.addQueryParam("set", "parameter").execute();

		try {
			Response r = f.get();
			String a = r.getResponseBody();
			System.out.println(a);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void ZtryStream() {
		PipedInputStream input = null;
		try {
			input = appbase.getPipedStream(type, "1");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			int data = input.read();
			while (data != -1) {
				System.out.print((char) data);
				data = input.read();
			}
			System.out.println();
		} catch (IOException e) {
			System.out.println();
			System.out.println("exception");
		}
	}
}
