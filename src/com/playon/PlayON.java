package com.playon;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.playon.xhr.RequestBuilder;

public class PlayON implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		RequestBuilder rb = GWT.create(RequestBuilder.class);
		
		rb.setRequestData("foo=bar&baz=qux");
	}

}
