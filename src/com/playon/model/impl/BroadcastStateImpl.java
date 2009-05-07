package com.playon.model.impl;

import com.playon.model.BroadcastState;

public class BroadcastStateImpl extends HasIdentityImpl implements
		BroadcastState {

	@Override
	public Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private final native String getCodeString() /*-{
		return this.code;
	}-*/;

}
