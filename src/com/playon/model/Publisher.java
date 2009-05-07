package com.playon.model;

import com.google.gwt.core.client.JsArray;

public class Publisher extends HasNameAndId {

	protected Publisher() {
	}

	public final native JsArray<Activity> getActivities() /*-{
		return this.activities;
	}-*/;

	public final native JsArray<Organization> getOrganizations() /*-{
		return this.organizations;
	}-*/;
}
