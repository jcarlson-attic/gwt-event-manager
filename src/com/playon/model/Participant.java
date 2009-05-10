package com.playon.model;

import com.google.gwt.core.client.JavaScriptObject;

public class Participant extends JavaScriptObject {

	protected Participant() {
	}

	public final native Organization getOrganization() /*-{
		return this.organization;
	}-*/;

	public final native boolean isHost() /*-{
		return this.host === true;
	}-*/;

}
