package com.playon.model.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.playon.model.BroadcastAvailability;

public class BroadcastAvailabilityImpl extends JavaScriptObject implements
		BroadcastAvailability {

	protected BroadcastAvailabilityImpl() {
	}

	@Override
	public Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private final native String getCodeString() /*-{
		return this.code;
	}-*/;

}
