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
3. Only support methods related document and query DSL, ideally your Elasticsearch hosting environment provides with a read-only access.
4. **Bonus:** Support for streaming data queries when Elasticsearch is hosted on appbase.io app.

## Quick Start

TBD: How to get the library and write a basic code.

## Documentation Reference

A brief primer on the supported methods.

TBD: Link to online javadoc.

## Example

Link to an example app build with `appbase-droid`.
