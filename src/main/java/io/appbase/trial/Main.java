package io.appbase.trial;
import java.io.IOException;

import com.google.gson.JsonParser;

import io.appbase.client.AppbaseClient;
import io.appbase.client.Stream;

public class Main {

	public static void main(String[] args) {

		AppbaseClient ac = new AppbaseClient("https://scalr.api.appbase.io", "shopify-flipkart-test", "xJC6pHyMz", "54fabdda-4f7d-43c9-9960-66ff45d8d4cf");
		
		String query = "{ \"query\":{ \"bool\":{ \"must\":{ \"bool\":{ \"should\":[ { \"multi_match\":{ \"query\": \"oth\" ," +
				" \"fields\":[ \"title\", \"title.search\" ], \"operator\":\"and\" } }," +
				" { \"multi_match\":{ \"query\": \"oth\" ,  \"fields\":[ \"title\", \"title.search\" ], \"type\":\"phrase_prefix\"," +
				" \"operator\":\"and\" } } ], \"minimum_should_match\":\"1\" } } } } }";

		try {
			String response = ac.prepareSearch("products",query).execute().body().string();
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}