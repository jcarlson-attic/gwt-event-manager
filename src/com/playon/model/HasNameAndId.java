package com.playon.model;

public class HasNameAndId extends HasIdentity {

	protected HasNameAndId() {
	}

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native Integer getId() /*-{
		return this.id;
	}-*/;

}
