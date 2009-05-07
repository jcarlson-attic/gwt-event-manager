package com.playon.model.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.playon.model.Organization;
import com.playon.model.Participant;

public class ParticipantImpl extends JavaScriptObject implements Participant {

	protected ParticipantImpl() {
	}

	@Override
	public final native Organization getOrganization() /*-{
		return this.organization;
	}-*/;

	@Override
	public final native Boolean isHost() /*-{
		return this.host;
	}-*/;

}
