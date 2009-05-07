package com.playon.model.impl;

import com.playon.model.EventAvailability;

public class EventAvailabilityImpl extends HasIdentityImpl implements
		EventAvailability {

	protected EventAvailabilityImpl() {
	}

	@Override
	public Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private final native String getCodeString() /*-{
		return this.code;
	}-*/;

	@Override
	public final native String getMessage() /*-{
		return this.message;
	}-*/;

}
