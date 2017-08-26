package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import io.appbase.client.AppbaseClient;

public class AppbaseBasicTest {

	AppbaseClient appbase;
	static final String user = "vspynv5Dg",
			pass = "f54091f5-ff77-4c71-a14c-1c29ab93fd15",
			URL = "http://scalr.api.appbase.io", appName = "Trial1796";
	String type = "product", id = "1";

	@Before
	public void setup() {
		appbase = new AppbaseClient(URL,appName,user,pass);
	}
}
