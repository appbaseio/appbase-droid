package elasticsearchlibrary;

import java.util.List;

import org.asynchttpclient.Param;

public class BulkRequestObject {
	String type;
	private String id;
	private String jsonDoc;
	private int method;
	public static final int INDEX = 0, DELETE = 1, UPDATE = 2;
	private List<Param> parameters=null;
	// Put jsonDoc as null if delete wanted.
	public BulkRequestObject(String type, String id, int method,
			String jsonDoc) {
		this.type = type;
		this.id = id;
		this.method = method;
		this.jsonDoc = jsonDoc;
	}
	public List<Param> getParameters() {
		return parameters;
	}
	public void setParameters(List<Param> parameters) {
		this.parameters = parameters;
	}
	public void addParameter(Param param){
		this.parameters.add(param);
	}
	public String getId() {
		return id;
	}
	public String getJsonDoc() {
		return jsonDoc;
	}
	public int getMethod() {
		return method;
	}
}