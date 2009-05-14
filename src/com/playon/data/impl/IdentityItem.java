package com.playon.data.impl;

public class IdentityItem extends ReadItem {

	protected String key;

	public IdentityItem(ReadItem item) {
		this.datum = item.datum;
		this.index = item.index;
		this.store = item.store;
	}

}
