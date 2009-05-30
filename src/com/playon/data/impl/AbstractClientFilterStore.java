package com.playon.data.impl;

import java.util.Map;

import dojo.data.api.Request;
import dojo.data.api.Response;

public abstract class AbstractClientFilterStore extends
		AbstractLazyLoadingStore {

	public AbstractClientFilterStore() {
	}

	public AbstractClientFilterStore(Map<String, Object> options) {
		super(options);
	}

	@Override
	protected Response processRawItems(Request request, Response response) {
		return super.processRawItems(request, response);
	}

}
