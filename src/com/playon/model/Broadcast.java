package com.playon.model;

import com.google.gwt.core.client.JavaScriptObject;

public class Broadcast extends JavaScriptObject {

	protected Broadcast() {
	}

	public final native BroadcastAvailability getBroadcastAvailability() /*-{
		return this.availability;
	}-*/;

	public final native BroadcastState getBroadcastState() /*-{
		return this.state;
	}-*/;

}
