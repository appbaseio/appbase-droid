# appbase-droid
[![](https://api.travis-ci.org/appbaseio/appbase-droid.svg?branch=master)](https://travis-ci.org/appbaseio/appbase-droid)

The missing Android client for Elasticsearch and appbase.io.


### Running Locally

```
mvn test
```

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

### Docs

Docs are under the `doc/` directory.

### Developing

The codebase resides under the `src/main/java/io/appbase` path.

* `src/main/java/io/appbase/client` contains the main interface methods.
* `src/main/java/io/appbase/requestbuilders` contains utility methods for supporting an Elasticsearch Java client like request builder functionality.
* `src/main/java/io/appbase/interceptor` contains a simple utility for supporting Basic Auth authentication.
* `src/main/java/io/appbase/trial` contains a sample code that uses the library.

Test files reside under `src/test/java/test` path.

* `src/test/java/test/AppbaseTest` is the main test file that currently tests 7 methods. It is also a good place to see the library usage.

---