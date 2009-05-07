package com.playon.model;

public class Video extends HasIdentity {

	protected Video() {
	}

	public final native String getURL() /*-{
		return this.url;
	}-*/;

}
