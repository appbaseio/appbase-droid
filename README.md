# appbase-droid
[![](https://api.travis-ci.org/appbaseio/appbase-droid.svg?branch=master)](https://travis-ci.org/appbaseio/appbase-droid)

The missing Android client for Elasticsearch and appbase.io.

## Download

Download [the latest JAR](https://search.maven.org/remote_content?g=io.appbase&a=appbase-droid&v=LATEST) or configure this dependency:

Maven:

    <dependency>
      <groupId>io.appbase</groupId>
      <artifactId>appbase-droid</artifactId>
      <version>1.0.0</version>
    </dependency>


Gradle:

    implementation("io.appbase:appbase-droid:1.0.0")

Snapshots of the development version are available in [Sonatype's `snapshots` repository](https://oss.sonatype.org/content/repositories/snapshots/).

## Overview

`appbase-droid` is an Elasticsearch client library usable from an Android device.

We built this as there are currently no good or maintained Elasticsearch libraries out there for Android.

Our design goals in building `appbase-droid` are:
1. Provide a lightweight library for querying right from Android (**1MB** in size),
2. Maintain compatibility with Java so it can also be used as a lightweight Java alternative (**20x** lighter than the official Elasticsearch client library),
3. Only support methods related document and query DSL, ideally your Elasticsearch hosting environment comes with a read-only access.
4. **Bonus:** Support for streaming data queries when Elasticsearch is hosted on appbase.io app; build live charts, newsfeeds, streaming search.

## Quick Start

TBD: How to get the library and write a basic code.

1. Creating the client.

    ```java
    AppbaseClient client = new AppbaseClient(url, appname, username, password);
    ```

2. Index a document.

    ```java
    String result = client.prepareIndex(type, id, body)
      .execute()
      .body()
      .string();
    System.out.println(result);
    ```
    
    Sample Output:
    
    ```json
    {
      "_index": "droid-test",
      "_type": "_doc",
      "_id": "xvvooe",
      "_version": 1,
      "result": "created",
      "_shards": {
        "total": 2,
        "successful": 2,
        "failed": 0
      },
      "_seq_no": 18,
      "_primary_term": 3
    }
    ```

3.  Update a document.

    ```java
    String result = client.prepareUpdate(type, id, parameters, doc)
      .execute()
      .body()
      .string();
    System.out.println(result);
    ```
    
    Sample Output:
    
    ```json
    {
      "_index": "droid-test",
      "_type": "_doc",
      "_id": "eqsrxtmggk",
      "_version": 2,
      "result": "updated",
      "_shards": {
        "total": 2,
        "successful": 2,
        "failed": 0
      },
      "_seq_no": 26,
      "_primary_term": 3
    }
    ```

4. Delete a document.

    ```java
    String result = client.prepareDelete(type, id)
      .execute()
      .body()
      .string();
    ```
    Sample Output:
    
    ```json
    {
      "_index": "droid-test",
      "_type": "_doc",
      "_id": "smemya",
      "_version": 3,
      "result": "deleted",
      "_shards": {
        "total": 2,
        "successful": 2,
        "failed": 0
      },
      "_seq_no": 26,
      "_primary_term": 3
    }
    ```

5. Get a document.

    ```java
    String result = client.prepareGet(type, id)
      .execute()
      .body()
      .string();
    ```
    Sample Output:
    
    ```json
    {
      "_index": "droid-test",
      "_type": "_doc",
      "_id": "nvyxcec",
      "found": false
    }
    ```

6. Make a search request.
    ```java
    String query =  "{ \"term\": { \"price\": \"100\" } }";
    String result = client.prepareSearch(type, query)
      .execute()
      .body()
      .string();
    ```
    Sample Output:
    
    ```json
    {
      "took": 0,
      "timed_out": false,
      "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
      },
      "hits": {
        "total": 1,
        "max_score": 1.0,
        "hits": [
          {
            "_index": "droid-test",
            "_type": "_doc",
            "_id": "bkxfsvddmo",
            "_score": 1.0,
            "_source": {
              "department_id": 1,
              "price": 5,
              "department_name": "Books",
              "name": "A Fake Book on Network Routing"
            }
          }
        ]
      }
    }
    ```

## Documentation Reference

A brief primer on the supported methods.

See the [online documentation reference](https://opensource.appbase.io/appbase-droid/doc/).

## Example

Link to an example app built with `appbase-droid`.

## How To Run Locally

### Clone

`git clone https://github.com/appbaseio/appbase-droid`

### Testing

```
mvn test
```

will test all the supported methods.

### Developing

The codebase resides under the `src/main/java/io/appbase` path.

* `src/main/java/io/appbase/client` contains the main interface methods.
* `src/main/java/io/appbase/requestbuilders` contains utility methods for supporting an Elasticsearch Java client like request builder functionality.
* `src/main/java/io/appbase/interceptor` contains a simple utility for supporting Basic Auth authentication.
* `src/main/java/io/appbase/trial` contains a sample code that uses the library.

Test files reside under `src/test/java/test` path.

* `src/test/java/test/AppbaseTest` is the main test file that currently tests 7 methods. It is also a good place to see the library usage.

---