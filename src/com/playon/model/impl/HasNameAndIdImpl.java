package com.playon.model.impl;

import com.playon.model.HasId;
import com.playon.model.HasName;

public abstract class HasNameAndIdImpl extends HasIdentityImpl implements
		HasName, HasId {

	protected HasNameAndIdImpl() {
	}

	@Override
	public final native String getName() /*-{
		return this.name;
	}-*/;

	@Override
	public final native Integer getId() /*-{
		return this.id;
	}-*/;

}
