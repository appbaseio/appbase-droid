package elasticsearchlibrary.requestbuilders;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Param;
import org.asynchttpclient.Realm;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.asynchttpclient.SignatureCalculator;
import org.asynchttpclient.channel.ChannelPoolPartitioning;
import org.asynchttpclient.cookie.Cookie;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.proxy.ProxyServer.Builder;
import org.asynchttpclient.request.body.generator.BodyGenerator;
import org.asynchttpclient.request.body.multipart.Part;
import org.asynchttpclient.uri.Uri;
import org.elasticsearch.index.query.QueryBuilder;
import org.reactivestreams.Publisher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import elasticsearchlibrary.handlers.AppbaseHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.resolver.NameResolver;

public class AppbaseRequestBuilder {

	BoundRequestBuilder request;

	public AppbaseRequestBuilder(BoundRequestBuilder request) {
		this.request = request;
	}

	public <T> ListenableFuture<T> execute(AsyncHandler<T> handler) {
		ListenableFuture<T> a=request.execute(handler);
		return  a;
	}

	public int hashCode() {
		return request.hashCode();
	}

	public ListenableFuture<Response> execute() {
		return request.execute();
	}

	public boolean equals(Object obj) {
		return request.equals(obj);
	}

	public AppbaseRequestBuilder setUrl(String url) {
		request.setUrl(url);
		return this;
	}

	public AppbaseRequestBuilder setUri(Uri uri) {
		request.setUri(uri);
		return this;
	}

	public AppbaseRequestBuilder setAddress(InetAddress address) {
		request.setAddress(address);
		return this;
	}

	public AppbaseRequestBuilder setLocalAddress(InetAddress address) {
		request.setLocalAddress(address);
		return this;
	}

	public AppbaseRequestBuilder setVirtualHost(String virtualHost) {
		request.setVirtualHost(virtualHost);
		return this;
	}

	public AppbaseRequestBuilder setHeader(CharSequence name, String value) {
		request.setHeader(name, value);
		return this;
	}

	public AppbaseRequestBuilder addHeader(CharSequence name, String value) {
		request.addHeader(name, value);
		return this;
	}

	public AppbaseRequestBuilder setHeaders(HttpHeaders headers) {
		request.setHeaders(headers);
		return this;
	}

	public AppbaseRequestBuilder setHeaders(Map<String, Collection<String>> headers) {
		request.setHeaders(headers);
		return this;
	}

	public AppbaseRequestBuilder setCookies(Collection<Cookie> cookies) {
		request.setCookies(cookies);
		return this;
	}

	public AppbaseRequestBuilder addCookie(Cookie cookie) {
		request.addCookie(cookie);
		return this;
	}

	public AppbaseRequestBuilder addOrReplaceCookie(Cookie cookie) {
		addOrReplaceCookie(cookie);
		return this;
	}

	public void resetCookies() {
		request.resetCookies();
	}

	public void resetQuery() {
		request.resetQuery();
	}

	public void resetFormParams() {
		request.resetFormParams();
	}

	public void resetNonMultipartData() {
		request.resetNonMultipartData();
	}

	public void resetMultipartData() {
		request.resetMultipartData();
	}

	public String toString() {
		return request.toString();
	}

	public AppbaseRequestBuilder setBody(File file) {
		request.setBody(file);
		return this;
	}

	public AppbaseRequestBuilder setBody(QueryBuilder qb) {
		request.setBody(qb.toString());
		return this;
	}

	public AppbaseRequestBuilder setBody(byte[] data) {
		request.setBody(data);
		return this;
	}

	public AppbaseRequestBuilder setBody(List<byte[]> data) {
		request.setBody(data);
		return this;
	}

	public AppbaseRequestBuilder setBody(String data) {
		request.setBody(data);
		return this;
	}
	
	public AppbaseRequestBuilder setBody(JsonObject data) {
		request.setBody(data.toString());
		return this;
	}
	public AppbaseRequestBuilder setBody(Map<String, Object> data) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(data);
		request.setBody(json);
		return this;
	}

	public AppbaseRequestBuilder setBody(ByteBuffer data) {
		request.setBody(data);
		return this;
	}

	public AppbaseRequestBuilder setBody(InputStream stream) {
		request.setBody(stream);
		return this;
	}

	public AppbaseRequestBuilder setBody(Publisher<ByteBuffer> publisher) {
		request.setBody(publisher);
		return this;
	}

	public AppbaseRequestBuilder setBody(Publisher<ByteBuffer> publisher, long contentLength) {
		request.setBody(publisher, contentLength);
		return this;
	}

	public AppbaseRequestBuilder setBody(BodyGenerator bodyGenerator) {
		request.setBody(bodyGenerator);
		return this;
	}

	public AppbaseRequestBuilder addQueryParam(String name, String value) {
		request.addQueryParam(name, value);
		return this;
	}

	public AppbaseRequestBuilder addQueryParams(List<Param> params) {
		request.addQueryParams(params);
		return this;
	}

	public AppbaseRequestBuilder setQueryParams(Map<String, List<String>> map) {
		request.setQueryParams(map);
		return this;
	}

	public AppbaseRequestBuilder setQueryParams(List<Param> params) {
		request.setQueryParams(params);
		return this;
	}

	public AppbaseRequestBuilder addFormParam(String name, String value) {
		request.addFormParam(name, value);
		return this;
	}

	public AppbaseRequestBuilder setFormParams(Map<String, List<String>> map) {
		request.setFormParams(map);
		return this;
	}

	public AppbaseRequestBuilder setFormParams(List<Param> params) {
		request.setFormParams(params);
		return this;
	}

	public AppbaseRequestBuilder addBodyPart(Part bodyPart) {
		request.addBodyPart(bodyPart);
		return this;
	}

	public AppbaseRequestBuilder setBodyParts(List<Part> bodyParts) {
		request.setBodyParts(bodyParts);
		return this;
	}

	public AppbaseRequestBuilder setProxyServer(ProxyServer proxyServer) {
		request.setProxyServer(proxyServer);
		return this;
	}

	public AppbaseRequestBuilder setProxyServer(Builder proxyServerBuilder) {
		request.setProxyServer(proxyServerBuilder);
		return this;
	}

	public AppbaseRequestBuilder setRealm(Realm realm) {
		request.setRealm(realm);
		return this;
	}

	public AppbaseRequestBuilder setFollowRedirect(boolean followRedirect) {
		request.setFollowRedirect(followRedirect);
		return this;
	}

	public AppbaseRequestBuilder setRequestTimeout(int requestTimeout) {
		request.setRequestTimeout(requestTimeout);
		return this;
	}

	public AppbaseRequestBuilder setRangeOffset(long rangeOffset) {
		request.setRangeOffset(rangeOffset);
		return this;
	}

	public AppbaseRequestBuilder setMethod(String method) {
		request.setMethod(method);
		return this;
	}

	public AppbaseRequestBuilder setCharset(Charset charset) {
		request.setCharset(charset);
		return this;
	}

	public AppbaseRequestBuilder setChannelPoolPartitioning(ChannelPoolPartitioning channelPoolPartitioning) {
		request.setChannelPoolPartitioning(channelPoolPartitioning);
		return this;
	}

	public AppbaseRequestBuilder setNameResolver(NameResolver<InetAddress> nameResolver) {
		request.setNameResolver(nameResolver);
		return this;
	}

	public AppbaseRequestBuilder setSignatureCalculator(SignatureCalculator signatureCalculator) {
		request.setSignatureCalculator(signatureCalculator);
		return this;
	}

	public Request build() {
		return request.build();
	}
}
