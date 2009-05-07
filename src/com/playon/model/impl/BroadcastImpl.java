package com.playon.model.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.playon.model.Broadcast;
import com.playon.model.BroadcastAvailability;
import com.playon.model.BroadcastState;

public class BroadcastImpl extends JavaScriptObject implements Broadcast {

	protected BroadcastImpl() {
	}

	@Override
	public final native BroadcastAvailability getBroadcastAvailability() /*-{
		return this.availability;
	}-*/;

	@Override
	public final native BroadcastState getBroadcastState() /*-{
		return this.state;
	}-*/;

}
