package com.playon.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.RequestBuilder;
import com.playon.security.client.OAuth;

public class Test implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
				"http://localhost/oauth/authenticate");
		rb.setRequestData("username=jcarlson&password=playon");
		rb.setHeader("Content-Type", "application/x-www-form-urlencoded");

		OAuth.signRequest(rb);
	}

}
