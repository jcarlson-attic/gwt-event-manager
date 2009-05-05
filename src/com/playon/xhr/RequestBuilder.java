package com.playon.xhr;

public class RequestBuilder extends com.google.gwt.http.client.RequestBuilder {

	private String url;
	
	public RequestBuilder(com.playon.xhr.Method httpMethod, String url) {
		super(httpMethod.toString(), url);
	}

}
