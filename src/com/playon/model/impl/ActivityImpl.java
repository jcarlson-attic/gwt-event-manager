package com.playon.model.impl;

import java.util.Set;

import com.playon.model.Activity;

public class ActivityImpl extends HasNameAndIdImpl implements Activity {

	protected ActivityImpl() {
	}

	@Override
	public final native Set<String> getTags() /*-{
		return this.tags;
	}-*/;

}
