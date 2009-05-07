package com.playon.model.impl;

import java.util.List;

import com.playon.model.Activity;
import com.playon.model.Organization;
import com.playon.model.Publisher;

public class PublisherImpl extends HasNameAndIdImpl implements Publisher {

	protected PublisherImpl() {
	}

	@Override
	public final native List<Activity> getActivities() /*-{
		return this.activities;
	}*-*/;

	@Override
	public final native List<Organization> getOrganizations() /*-{
		return this.organizations;
	}-*/;

}
