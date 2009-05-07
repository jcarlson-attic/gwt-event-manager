package com.playon.model;

import com.google.gwt.core.client.JavaScriptObject;

public class HasIdentity extends JavaScriptObject {

	protected HasIdentity() {
	}

	public final native String getURI() /*-{
		if (!this.uri && this.$ref) {
			return this.$ref;
		}
		return this.uri;
	}-*/;

}
