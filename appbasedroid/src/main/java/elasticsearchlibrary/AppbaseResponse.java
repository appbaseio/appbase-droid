package elasticsearchlibrary;

import java.io.InputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Response;
import org.asynchttpclient.cookie.Cookie;
import org.asynchttpclient.uri.Uri;

import io.netty.handler.codec.http.HttpHeaders;

public class AppbaseResponse {
	
	public class ResponseBuilder{

	    /**
	     * Returns a hash code value for the object. This method is
	     * supported for the benefit of hash tables such as those provided by
	     * {@link java.util.HashMap}.
	     * <p>
	     * The general contract of {@code hashCode} is:
	     * <ul>
	     * <li>Whenever it is invoked on the same object more than once during
	     *     an execution of a Java application, the {@code hashCode} method
	     *     must consistently return the same integer, provided no information
	     *     used in {@code equals} comparisons on the object is modified.
	     *     This integer need not remain consistent from one execution of an
	     *     application to another execution of the same application.
	     * <li>If two objects are equal according to the {@code equals(Object)}
	     *     method, then calling the {@code hashCode} method on each of
	     *     the two objects must produce the same integer result.
	     * <li>It is <em>not</em> required that if two objects are unequal
	     *     according to the {@link java.lang.Object#equals(java.lang.Object)}
	     *     method, then calling the {@code hashCode} method on each of the
	     *     two objects must produce distinct integer results.  However, the
	     *     programmer should be aware that producing distinct integer results
	     *     for unequal objects may improve the performance of hash tables.
	     * </ul>
	     * <p>
	     * As much as is reasonably practical, the hashCode method defined by
	     * class {@code Object} does return distinct integers for distinct
	     * objects. (This is typically implemented by converting the internal
	     * address of the object into an integer, but this implementation
	     * technique is not required by the
	     * Java&trade; programming language.)
	     *
	     * @return  a hash code value for this object.
	     * @see     java.lang.Object#equals(java.lang.Object)
	     * @see     java.lang.System#identityHashCode
	     */
		public int hashCode() {
			return builder.hashCode();
		}
		   /**
	     * Indicates whether some other object is "equal to" this one.
	     * <p>
	     * The {@code equals} method implements an equivalence relation
	     * on non-null object references:
	     * <ul>
	     * <li>It is <i>reflexive</i>: for any non-null reference value
	     *     {@code x}, {@code x.equals(x)} should return
	     *     {@code true}.
	     * <li>It is <i>symmetric</i>: for any non-null reference values
	     *     {@code x} and {@code y}, {@code x.equals(y)}
	     *     should return {@code true} if and only if
	     *     {@code y.equals(x)} returns {@code true}.
	     * <li>It is <i>transitive</i>: for any non-null reference values
	     *     {@code x}, {@code y}, and {@code z}, if
	     *     {@code x.equals(y)} returns {@code true} and
	     *     {@code y.equals(z)} returns {@code true}, then
	     *     {@code x.equals(z)} should return {@code true}.
	     * <li>It is <i>consistent</i>: for any non-null reference values
	     *     {@code x} and {@code y}, multiple invocations of
	     *     {@code x.equals(y)} consistently return {@code true}
	     *     or consistently return {@code false}, provided no
	     *     information used in {@code equals} comparisons on the
	     *     objects is modified.
	     * <li>For any non-null reference value {@code x},
	     *     {@code x.equals(null)} should return {@code false}.
	     * </ul>
	     * <p>
	     * The {@code equals} method for class {@code Object} implements
	     * the most discriminating possible equivalence relation on objects;
	     * that is, for any non-null reference values {@code x} and
	     * {@code y}, this method returns {@code true} if and only
	     * if {@code x} and {@code y} refer to the same object
	     * ({@code x == y} has the value {@code true}).
	     * <p>
	     * Note that it is generally necessary to override the {@code hashCode}
	     * method whenever this method is overridden, so as to maintain the
	     * general contract for the {@code hashCode} method, which states
	     * that equal objects must have equal hash codes.
	     *
	     * @param   obj   the reference object with which to compare.
	     * @return  {@code true} if this object is the same as the obj
	     *          argument; {@code false} otherwise.
	     * @see     #hashCode()
	     * @see     java.util.HashMap
	     */
		public boolean equals(Object obj) {
			return builder.equals(obj);
		}

		public org.asynchttpclient.Response.ResponseBuilder accumulate(HttpResponseStatus status) {
			return builder.accumulate(status);
		}

		public org.asynchttpclient.Response.ResponseBuilder accumulate(HttpResponseHeaders headers) {
			return builder.accumulate(headers);
		}

		public org.asynchttpclient.Response.ResponseBuilder accumulate(HttpResponseBodyPart bodyPart) {
			return builder.accumulate(bodyPart);
		}

        /**
         * Build a {@link Response} instance
         * 
         * @return a {@link Response} instance
         */
		public Response build() {
			return builder.build();
		}

        /**
         * Reset the internal state of this builder.
         */
		public void reset() {
			builder.reset();
		}

	    /**
	     * Returns a string representation of the object. In general, the
	     * {@code toString} method returns a string that
	     * "textually represents" this object. The result should
	     * be a concise but informative representation that is easy for a
	     * person to read.
	     * It is recommended that all subclasses override this method.
	     * <p>
	     * The {@code toString} method for class {@code Object}
	     * returns a string consisting of the name of the class of which the
	     * object is an instance, the at-sign character `{@code @}', and
	     * the unsigned hexadecimal representation of the hash code of the
	     * object. In other words, this method returns a string equal to the
	     * value of:
	     * <blockquote>
	     * <pre>
	     * getClass().getName() + '@' + Integer.toHexString(hashCode())
	     * </pre></blockquote>
	     *
	     * @return  a string representation of the object.
	     */
		public String toString() {
			return builder.toString();
		}

		org.asynchttpclient.Response.ResponseBuilder builder=new org.asynchttpclient.Response.ResponseBuilder();
	}
	
	
	Response response;
	

    /**
     * Returns the status code for the request.
     * 
     * @return The status code
     */
	public int getStatusCode() {
		return response.getStatusCode();
	}

    /**
     * Returns the status text for the request.
     * 
     * @return The status text
     */
	public String getStatusText() {
		return response.getStatusText();
	}

    /**
     * Return the entire response body as a byte[].
     * 
     * @return the entire response body as a byte[].
     */
	public byte[] getResponseBodyAsBytes() {
		return response.getResponseBodyAsBytes();
	}

	public ByteBuffer getResponseBodyAsByteBuffer() {
		return response.getResponseBodyAsByteBuffer();
	}

	public InputStream getResponseBodyAsStream() {
		return response.getResponseBodyAsStream();
	}

	public String getResponseBody(Charset charset) {
		return response.getResponseBody(charset);
	}

	public String getResponseBody() {
		return response.getResponseBody();
	}

	public Uri getUri() {
		return response.getUri();
	}

	public String getContentType() {
		return response.getContentType();
	}

	public String getHeader(String name) {
		return response.getHeader(name);
	}

	public List<String> getHeaders(String name) {
		return response.getHeaders(name);
	}

	public HttpHeaders getHeaders() {
		return response.getHeaders();
	}

	public boolean isRedirected() {
		return response.isRedirected();
	}

	public String toString() {
		return response.toString();
	}

	public List<Cookie> getCookies() {
		return response.getCookies();
	}

	public boolean hasResponseStatus() {
		return response.hasResponseStatus();
	}

	public boolean hasResponseHeaders() {
		return response.hasResponseHeaders();
	}

	public boolean hasResponseBody() {
		return response.hasResponseBody();
	}

	public SocketAddress getRemoteAddress() {
		return response.getRemoteAddress();
	}

	public SocketAddress getLocalAddress() {
		return response.getLocalAddress();
	}
}
