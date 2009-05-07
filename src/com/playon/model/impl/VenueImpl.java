package com.playon.model.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.playon.model.HasName;
import com.playon.model.Venue;

public class VenueImpl extends JavaScriptObject implements Venue, HasName {

	protected VenueImpl() {
	}

	@Override
	public final native String getCity() /*-{
		return this.city;
	}-*/;

	@Override
	public final native String getCountry() /*-{
		return this.country;
	}-*/;

	@Override
	public final native String getName() /*-{
		return this.name;
	}-*/;

	@Override
	public final native String getState() /*-{
		return this.state;
	}-*/;

}
