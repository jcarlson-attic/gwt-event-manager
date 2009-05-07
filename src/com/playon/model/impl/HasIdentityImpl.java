package com.playon.model.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.playon.model.HasIdentity;

public abstract class HasIdentityImpl extends JavaScriptObject implements
		HasIdentity {

	@Override
	public final native String getURI() /*-{
		if (!this.uri && this.$ref) {
			return this.$ref;
		}
		return this.uri;
	}-*/;

}
