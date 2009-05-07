package com.playon.model.impl;

import com.playon.model.Organization;

public class OrganizationImpl extends HasNameAndIdImpl implements Organization {

	protected OrganizationImpl() {
	}

	@Override
	public final native String getNickname() /*-{
		return this.nickname;
	}-*/;

	@Override
	public final native String getShortName() /*-{
		return this.shortName;
	}-*/;

}
