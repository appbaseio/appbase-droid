package elasticsearchlibrary.requestbuilders;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.resolver.NameResolver;

public class AppbaseRequestBuilder {
	

	BoundRequestBuilder request;

	public AppbaseRequestBuilder(BoundRequestBuilder request) {
		super();
		this.request = request;
	}

	public <T> ListenableFuture<T> execute(AsyncHandler<T> handler) {
		return request.execute(handler);
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

	public BoundRequestBuilder setUrl(String url) {
		return request.setUrl(url);
	}

	public BoundRequestBuilder setUri(Uri uri) {
		return request.setUri(uri);
	}

	public BoundRequestBuilder setAddress(InetAddress address) {
		return request.setAddress(address);
	}

	public BoundRequestBuilder setLocalAddress(InetAddress address) {
		return request.setLocalAddress(address);
	}

	public BoundRequestBuilder setVirtualHost(String virtualHost) {
		return request.setVirtualHost(virtualHost);
	}

	public BoundRequestBuilder setHeader(CharSequence name, String value) {
		return request.setHeader(name, value);
	}

	public BoundRequestBuilder addHeader(CharSequence name, String value) {
		return request.addHeader(name, value);
	}

	public BoundRequestBuilder setHeaders(HttpHeaders headers) {
		return request.setHeaders(headers);
	}

	public BoundRequestBuilder setHeaders(Map<String, Collection<String>> headers) {
		return request.setHeaders(headers);
	}

	public BoundRequestBuilder setCookies(Collection<Cookie> cookies) {
		return request.setCookies(cookies);
	}

	public BoundRequestBuilder addCookie(Cookie cookie) {
		return request.addCookie(cookie);
	}

	public BoundRequestBuilder addOrReplaceCookie(Cookie cookie) {
		return request.addOrReplaceCookie(cookie);
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
	
	public BoundRequestBuilder setBody(File file) {
		return request.setBody(file);
	}
	
	public BoundRequestBuilder setBody(QueryBuilder qb) {
		return request.setBody(qb.toString());
	}

	public String toString() {
		return request.toString();
	}

	public BoundRequestBuilder setBody(byte[] data) {
		return request.setBody(data);
	}

	public BoundRequestBuilder setBody(List<byte[]> data) {
		return request.setBody(data);
	}

	public BoundRequestBuilder setBody(String data) {
		return request.setBody(data);
	}

	public BoundRequestBuilder setBody(ByteBuffer data) {
		return request.setBody(data);
	}

	public BoundRequestBuilder setBody(InputStream stream) {
		return request.setBody(stream);
	}

	public BoundRequestBuilder setBody(Publisher<ByteBuffer> publisher) {
		return request.setBody(publisher);
	}

	public BoundRequestBuilder setBody(Publisher<ByteBuffer> publisher, long contentLength) {
		return request.setBody(publisher, contentLength);
	}

	public BoundRequestBuilder setBody(BodyGenerator bodyGenerator) {
		return request.setBody(bodyGenerator);
	}

	public BoundRequestBuilder addQueryParam(String name, String value) {
		return request.addQueryParam(name, value);
	}

	public BoundRequestBuilder addQueryParams(List<Param> params) {
		return request.addQueryParams(params);
	}

	public BoundRequestBuilder setQueryParams(Map<String, List<String>> map) {
		return request.setQueryParams(map);
	}

	public BoundRequestBuilder setQueryParams(List<Param> params) {
		return request.setQueryParams(params);
	}

	public BoundRequestBuilder addFormParam(String name, String value) {
		return request.addFormParam(name, value);
	}

	public BoundRequestBuilder setFormParams(Map<String, List<String>> map) {
		return request.setFormParams(map);
	}

	public BoundRequestBuilder setFormParams(List<Param> params) {
		return request.setFormParams(params);
	}

	public BoundRequestBuilder addBodyPart(Part bodyPart) {
		return request.addBodyPart(bodyPart);
	}

	public BoundRequestBuilder setBodyParts(List<Part> bodyParts) {
		return request.setBodyParts(bodyParts);
	}

	public BoundRequestBuilder setProxyServer(ProxyServer proxyServer) {
		return request.setProxyServer(proxyServer);
	}

	public BoundRequestBuilder setProxyServer(Builder proxyServerBuilder) {
		return request.setProxyServer(proxyServerBuilder);
	}

	public BoundRequestBuilder setRealm(Realm realm) {
		return request.setRealm(realm);
	}

	public BoundRequestBuilder setFollowRedirect(boolean followRedirect) {
		return request.setFollowRedirect(followRedirect);
	}

	public BoundRequestBuilder setRequestTimeout(int requestTimeout) {
		return request.setRequestTimeout(requestTimeout);
	}

	public BoundRequestBuilder setRangeOffset(long rangeOffset) {
		return request.setRangeOffset(rangeOffset);
	}

	public BoundRequestBuilder setMethod(String method) {
		return request.setMethod(method);
	}

	public BoundRequestBuilder setCharset(Charset charset) {
		return request.setCharset(charset);
	}

	public BoundRequestBuilder setChannelPoolPartitioning(ChannelPoolPartitioning channelPoolPartitioning) {
		return request.setChannelPoolPartitioning(channelPoolPartitioning);
	}

	public BoundRequestBuilder setNameResolver(NameResolver<InetAddress> nameResolver) {
		return request.setNameResolver(nameResolver);
	}

	public BoundRequestBuilder setSignatureCalculator(SignatureCalculator signatureCalculator) {
		return request.setSignatureCalculator(signatureCalculator);
	}

	public Request build() {
		return request.build();
	}
}
