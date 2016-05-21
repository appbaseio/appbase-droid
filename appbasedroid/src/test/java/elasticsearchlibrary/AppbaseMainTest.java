package elasticsearchlibrary;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.asynchttpclient.HttpResponseBodyPart;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import elasticsearchlibrary.BulkRequestObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppbaseMainTest {
	static JsonParser parser;
	static String randomId = null;
	static Appbase appbase;
	static final String user = "7eJWHfD4P",
			pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
			URL = "http://scalr.api.appbase.io",
			appName = "jsfiddle-demo",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";
	Random r = new Random();
	String type = "product", id = "1";

	@BeforeClass
	public static void setup() {
		/**
		 * SETUP new Appbase() Pass the parameters URL (base url eg.
		 * http://scalr.api.appbase.io) appName eg. jsfiddle-demo user name. The
		 * one provided for that particular app(eg. 7eJWHfD4P ) password
		 * corresponding to the userName(eg.
		 * 431d9cea-5219-4dfb-b798-f897f3a02665 )
		 */

		appbase = new Appbase(URL, appName, user, pass);
		randomId = Trial.generateId();
		parser = new JsonParser();
	}

//	@Test
	public void AindexTest() {

		/**
		 * Index String result = appbase.index(type, id, jsonDoc); type and id
		 * of the object you need to insert. jsonDoc is the entire body we need
		 * to insert.
		 *
		 */

		// There will be tests for checking index for a new object and for one
		// which already exists
		String result = appbase.index(type, randomId, jsonDoc);
		assertNotNull(result);

		JsonObject object = parser.parse(result).getAsJsonObject();
		String created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("true", created);

		result = appbase.index(type, randomId, jsonDoc);
		assertNotNull(result);
		object = parser.parse(result).getAsJsonObject();
		created = object.get("created").getAsString();

		assertNotNull(created);
		assertEquals("false", created);

	}

//	@Test
	public void BupdateTest() {
		int generatedPrice = 5;
		String jsonDoc = "{doc: {\"price\": " + generatedPrice + "}}";
		String result = appbase.update(type, randomId, null, jsonDoc);
		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.getAsJsonObject("_shards").get("successful"),
				object.getAsJsonObject("_shards").get("total"));
		assertNotEquals(object.getAsJsonObject("_shards").get("successful")
				.getAsInt(), 0);

		result = appbase.update(type, randomId, null, jsonDoc);
		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.getAsJsonObject("_shards").get("successful"),
				object.getAsJsonObject("_shards").get("total"));
		assertEquals(object.getAsJsonObject("_shards").get("successful")
				.getAsInt(), 0);

	}

//	@Test
	public void CdeleteTest() {
		String result = appbase.delete(type, randomId);
		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), true);
		assertEquals(
				object.getAsJsonObject("_shards").get("failed").getAsInt(), 0);

		result = appbase.delete(type, randomId);
		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), false);
		assertEquals(
				object.getAsJsonObject("_shards").get("failed").getAsInt(), 0);
	}

//	@Test
	public void DbulkTest() {
		BulkRequestObject[] bulk = new BulkRequestObject[4];
		bulk[0] = new BulkRequestObject(type, randomId,
				BulkRequestObject.INDEX, jsonDoc);
		bulk[1] = new BulkRequestObject(type, randomId,
				BulkRequestObject.UPDATE, "{doc: {\"price\": " + 6 + "}}");
		bulk[2] = new BulkRequestObject(type, randomId,
				BulkRequestObject.DELETE, null);
		bulk[3] = new BulkRequestObject(type, randomId, 100, jsonDoc);

		appbase.bulk(bulk);
	}

//	@Test
	public void EgetTest() {

		appbase.index(type, randomId, jsonDoc);
		String result = appbase.get(type, randomId);
		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), true);
		appbase.delete(type, randomId);
		result = appbase.get(type, randomId);
		object = parser.parse(result).getAsJsonObject();
		assertEquals(object.get("found").getAsBoolean(), false);

	}

//	@Test
	public void FgetTypesTest() {
		String result = appbase.getTypes();
		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.isJsonObject(), true);
	}

//	@Test
	public void GsearchTest() {
		String body = "{\"query\":{\"term\":{ \"price\" : 5595}}}";
		String result = appbase.search(type, body);
		JsonObject object = parser.parse(result).getAsJsonObject();
		assertEquals(object.isJsonObject(), true);
		assertNotEquals(object.getAsJsonObject("hits").get("total").getAsInt(),
				0);
	}

//	@Test
	public void HgetStreamTest() {
		appbase.index(type, randomId, jsonDoc);
		appbase.getStream(type, randomId, new AppbaseHandler(false) {
			int i = 1;

			@Override
			public void onData(String data) {
				// TODO Auto-generated method stub

			}

			@Override
			public org.asynchttpclient.AsyncHandler.State onBodyPartReceived(
					HttpResponseBodyPart bodyPart) throws Exception {
				// TODO Auto-generated method stub
				if (i == 1) {
					appbase.update(type, randomId, null, "{doc: {\"price\": "
							+ 2 + "}}");
					i++;
				} else if (i > 1) {
					String result = new String(bodyPart.getBodyPartBytes());
					JsonObject object = parser.parse(result).getAsJsonObject();

					assertEquals(object.getAsJsonObject("_source").get("price")
							.getAsInt(), 2);
					return State.ABORT;
				}

				return State.CONTINUE;

			}
		});

	}

	@Test
	public void IsearchStreamTest() {
		appbase.searchStream(type,
				"{\"query\":{\"term\":{ \"price\" : 5595}}}",
				new AppbaseHandler(false) {
					int i = 1;
					JsonObject object = null;
					String randId=null;

					@Override
					public void onData(String data) {
						// TODO Auto-generated method stub

					}

					@Override
					public org.asynchttpclient.AsyncHandler.State onBodyPartReceived(
							HttpResponseBodyPart bodyPart) throws Exception {
						// TODO Auto-generated method stub
						if (i == 1) {
							System.out.println(bodyPart.getBodyByteBuffer().flip());
							String result =new String(bodyPart.getBodyPartBytes());
							randId=Trial.generateId();
							appbase.index(type,
									randId, jsonDoc);
							System.out.println(result);
//							JsonObject object = parser.parse(result)
//									.getAsJsonObject();
							this.object = object;
							i++;
						} else if (i > 1) {
							System.out.println(bodyPart.getBodyByteBuffer());
							System.out.println(bodyPart.getBodyPartBytes().length);
							String result =new String(bodyPart.getBodyPartBytes());
							System.out.println(result);
//							JsonObject object = parser.parse(result)
//									.getAsJsonObject();

							assertEquals(
									object.getAsJsonObject("_hits")
											.get("total").getAsInt() - 1,
									object.getAsJsonObject("_hits")
											.get("total").getAsInt());
							appbase.delete(type, randId);
							return State.ABORT;
						}

						return State.CONTINUE;

					}
				});

	}

	@Test
	public void JsearchStreamToURLTest() {
	}

}
