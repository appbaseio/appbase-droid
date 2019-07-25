package io.appbase.trial;

import java.io.IOException;

import io.appbase.client.AppbaseClient;
import okhttp3.Response;

public class Main {

    public static void main(String[] args) {

        AppbaseClient ac = new AppbaseClient("https://scalr.api.appbase.io", "shopify-flipkart-test", "xJC6pHyMz", "54fabdda-4f7d-43c9-9960-66ff45d8d4cf");

        String query =
                "{ \"query\":{ " +
                    "\"bool\":{ " +
                        "\"must\":{ " +
                            "\"bool\":{ " +
                                "\"should\":[ " +
                                    "{ \"multi_match\":{" +
                                        " \"query\": \"oth\" ," +
                                        " \"fields\":[ \"title\", \"title.search\" ]," +
                                        " \"operator\":\"and\" } }," +
                                    "{ \"multi_match\":{" +
                                        " \"query\": \"oth\" ," +
                                        " \"fields\":[ \"title\", \"title.search\" ]," +
                                        " \"type\":\"phrase_prefix\"," +
                                        " \"operator\":\"and\" } } ], " +
                                "\"minimum_should_match\":\"1\" } } } } }";

        String analyticsQuery = "{ " +
                "\"X-Search-Id\": \"2204483485796\"," +
                " \"X-Search-Click\": \"true\"," +
                " \"X-Search-ClickPosition\": \"2\"," +
                " \"X-Search-Conversion\": \"true\" " +
                "}";

        // Making a search query
        try {
            Response response = ac.prepareSearch("products", query).execute();
            String body = response.body().string();
            String XSearchId = response.header("X-Search-Id");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Recording Analytics with default X-Search-Id
        try {
            String analytics = ac.prepareAnalytics(null).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Recording Analytics with custom X-Search-Id
        try {
            String analytics = ac.prepareAnalytics(analyticsQuery).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}