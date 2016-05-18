package elasticsearchlibrary;

/**
 * Created by Tirth Shah on 10-05-2016 for Appbase.io
 */

public class Main {
	public static void main(String[] args) {


		String user = "vspynv5Dg", pass = "f54091f5-ff77-4c71-a14c-1c29ab93fd15", app = "Trial1796", type = "product", id = "1";
		String URL = "http://scalr.api.appbase.io";
		String body="{\"query\":{\"term\":{ \"price\" : 559293482}}}";
		System.out.println(body);

		Appbase elastic = new Appbase(URL, app, user, pass);
		elastic.searchStream(type, body, new AppbaseHandler(false) {
			
			@Override
			public void onData(String data) {
				
				System.out.println(data);
			}
		} );
		

	}
}
