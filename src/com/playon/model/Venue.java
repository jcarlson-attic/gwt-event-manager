package com.playon.model;

import com.google.gwt.core.client.JavaScriptObject;

public class Venue extends JavaScriptObject {

	protected Venue() {
	}

	public final native String getName() /*-{
		return this.city;
	}-*/;

	public final native String getCity() /*-{
		return this.city;
	}-*/;

	public final native String getState() /*-{
		return this.state;
	}-*/;

	public final native String getCountry() /*-{
		return this.country;
	}-*/;

}
