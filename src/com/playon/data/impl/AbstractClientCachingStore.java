package com.playon.data.impl;

import java.util.Map;

public abstract class AbstractClientCachingStore extends
		AbstractLazyLoadingStore {

	public AbstractClientCachingStore() {
	}

	public AbstractClientCachingStore(Map<String, Object> options) {
		super(options);
	}

}
