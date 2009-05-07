package com.playon.model.impl;

import com.playon.model.EventState;

public class EventStateImpl extends HasIdentityImpl implements EventState {

	@Override
	public Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private final native String getCodeString() /*-{
		return this.code;
	}-*/;
}
