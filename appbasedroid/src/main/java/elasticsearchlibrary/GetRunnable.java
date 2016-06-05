package elasticsearchlibrary;

import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;

public class GetRunnable implements Runnable{
	String type; 
	String id;
	AsyncHandler<String> asyncHandler;
	AsyncHttpClient httpClient;
	private Appbase appbase;
	public GetRunnable(String type, String id, AsyncHandler<String> asyncHandler,AsyncHttpClient httpClient,Appbase appbase) {
		super();
		this.type = type;
		this.id = id;
		this.asyncHandler = asyncHandler;
		this.httpClient=httpClient;
		this.appbase=appbase ;
	}
	public void run() {
		System.out.println("running");
		ListenableFuture<String> f = httpClient.prepareGet(appbase.getURL(type, id))
				.addHeader("Authorization", "Basic " + appbase.getAuth())
				.setRequestTimeout(60000000).addQueryParams(appbase.getParameters())
				.execute(asyncHandler);
		try {
			System.out.println(f.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		System.out.println("returning from run");
		
	
	}
	
	
}
