package com.playon.xhr;

import com.google.gwt.http.client.RequestBuilder;

public class RestRequestBuilder extends RequestBuilder {

	public RestRequestBuilder(com.playon.xhr.Method method, String url) {
		super(method.toString(), url);
	}

}
