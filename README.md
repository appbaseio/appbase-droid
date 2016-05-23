# appbase-droid
Appbase.io library for Java / Android

**appbase-droid** is a java library for appbase.io


##Installation
Currently you can clone the project on your local PC and run it on eclipse.

##Usage

**Setup**
To start using this library you need to first initialize a Appbase() instance
```java
  String user = "7eJWHfD4P",
			pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
			URL = "http://scalr.api.appbase.io",
			appName = "jsfiddle-demo";
	Appbase appbase = new Appbase(URL, appName, user, pass);

```

**Index**
To index you can simply do
```java
String result = appbase.index(type, randomId, jsonDoc);
```
**Update**
```java
String jsonDoc = "{doc: {\"price\": 50}}";
String result = appbase.update(type, randomId, null, jsonDoc);
```
**Delete**
```java
String result = appbase.delete(type, randomId);
```
**Bulk**
```java
BulkRequestObject[] bulk = new BulkRequestObject[4];
bulk[0] = new BulkRequestObject(type, randomId,
		BulkRequestObject.INDEX, jsonDoc);
bulk[1] = new BulkRequestObject(type, randomId,
		BulkRequestObject.UPDATE, "{doc: {\"price\":6}}");
bulk[2] = new BulkRequestObject(type, randomId,
		BulkRequestObject.DELETE, null);
bulk[3] = new BulkRequestObject(type, randomId, 100, jsonDoc);

appbase.bulk(bulk);
```
**Get**
```java
String result = appbase.get(type, randomId);
```
**Search**
```java
String body = "{\"query\":{\"term\":{ \"price\" : 5595}}}";
String result = appbase.search(type, body);
```
**GetTypes**
```java
String result = appbase.getTypes();
```
**GetStream**
```java
appbase.getStream(type, randomId, new AppbaseHandler(false) {
	@Override
	public void onData(String data) {
    System.out.println(data);
	}
});
```
**SearchStream**
```java
appbase.searchStream(type,
	"{\"query\":{\"term\":{ \"price\" : 5595}}}",
	new AppbaseHandler(false) {
		int i = 1;
		String generatedId;
		@Override
		public void onData(String data) {
      System.out.println(data);
		}
});
```

**Get as Json**
To get any result as Json use
To get the result as JsonObject use
```java
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
JsonParser	parser = new JsonParser();
JsonObject object = parser.parse(result).getAsJsonObject();
```

