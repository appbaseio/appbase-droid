# appbase-droid
Appbase.io library for Java / Android

**appbase-droid** is a java library for appbase.io


##Installation
Currently you can clone the project on your local PC and run it on eclipse.
To install maven dependencies run raven install on eclipse.

##Usage

**Setup**
To start using this library you need to first initialize a Appbase() instance
```java
String user = "7eJWHfD4P",
       pass = "431d9cea-5219-4dfb-b798-f897f3a02665",
       URL = "http://scalr.api.appbase.io",
       appName = "jsfiddle-demo";
AppbaseClient appbase = new AppbaseClient(URL, appName, user, pass);

```

**Index**
To index you can simply do
```java
String result = appbase.index(type, randomId, jsonDoc);
```
You can also do 
```java
ListenableFuture<Response> f = prepareIndex(type, id, jsonDoc).execute();
```
**Update**
To update the document
```java
String jsonDoc = "{doc: {\"price\": 50}}";
String result = appbase.update(type, randomId, null, jsonDoc);
```
OR
```java
ListenableFuture<Response> f = prepareUpdate(type, id, parameters, jsonDoc).execute();
```
**Delete**
```java
String result = appbase.delete(type, randomId);
```
OR
```java
ListenableFuture<Response> f = prepareDelete(type, id).execute();
```
**Bulk**
```java
bulkExecute
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
	}
);
```

**Get as JSON**
To get any result as JsonObject use
```java
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

JsonParser parser = new JsonParser();
JsonObject object = parser.parse(result).getAsJsonObject();
```

