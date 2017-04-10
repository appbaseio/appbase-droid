package trial;

import java.io.IOException;
import java.util.Scanner;

import com.google.common.primitives.UnsignedInteger;
import com.google.gson.JsonParser;

import client.AppbaseClient;

public class Main {

	static JsonParser parser;
	static AppbaseClient appbase;
	int startId = 1, endId;
	static String user = "vspynv5Dg", pass = "f54091f5-ff77-4c71-a14c-1c29ab93fd15",
			URL = "https://vspynv5Dg:f54091f5-ff77-4c71-a14c-1c29ab93fd15@scalr.api.appbase.io", appName = "Trial1796",
			query = "{\"query\":{\"term\":{\"price\" : 5595}}}",
			jsonDoc = "{\"department_id\": 1,\"department_name\": \"Books\",\"name\": \"A Fake Book on Network Routing\",\"price\": 5595}";

	static String type = "product", id = "1";

	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		UnsignedInteger  a=UnsignedInteger.valueOf(s.nextLine());
//		sysoa.longValue(
//		String bin=Integer.toBinaryString(a);
//		String f="";
//		int max=Integer.toBinaryString(Integer.MAX_VALUE).length()-bin.length();
//		for (int i = 0; i < max; i++) {
//			f+="1";
//		}
//		System.out.println(bin);
//		String ans="";
//		for (int i = 0; i <bin.length() ; i++) {
//			
//			if(bin.charAt(i)=='1'){
//				System.out.println("char at "+i+" is "+1);
//				ans+="0";
//			}else{
//				ans+="1";
//			}
//		}
//		System.out.println(ans);
//		System.out.println(Integer.parseInt(f+ans,2));
//		System.out.println(Integer.MAX_VALUE);
		System.out.println(UnsignedInteger.MAX_VALUE.longValue()-a.longValue());
//		AppbaseClient ac = new AppbaseClient(URL, appName);
//		try {
//			System.out.println(ac.prepareIndex(type, jsonDoc).execute().body().string());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// AppbaseClient ac = new AppbaseClient(URL, appName, user, pass);
		//// AppbaseBullkBuilder bulk = new AppbaseBullkBuilder();
		// // bulk.add(ac.prepareIndex(type,id, jsonDoc));
		// // bulk.add(ac.prepareDelete(type, id));
		// // try {
		// // System.out.println(bulk.execute().body().string());
		// // } catch (IOException e) {
		// // e.printStackTrace();
		// // }
		// JsonObject j=new JsonObject();
		// JsonArray a=new JsonArray();
		// a.add("as");a.add("as1");a.add("as2");
		// j.add("arr", a);
		// XContentBuilder builder = null;
		// try {
		// builder = XContentFactory.jsonBuilder().startObject()
		// .field("user", "sid")
		// .field("postDate", new Date())
		// .field("message", "trying out Elasticsearch")
		// .endObject();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// okhttp3.Response r = null;
		// try {
		// r = ac.prepareIndex("hello", builder.string()).execute();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// try {
		// System.out.println("response: " + r.body().string());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}
}