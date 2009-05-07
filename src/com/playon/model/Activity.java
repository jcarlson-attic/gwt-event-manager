package com.playon.model;

import com.google.gwt.core.client.JsArrayString;

public class Activity extends HasNameAndId {

	protected Activity() {
	}

	public final native JsArrayString getTags() /*-{
		return this.tags;
	}-*/;

}
