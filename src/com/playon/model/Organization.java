package com.playon.model;

public class Organization extends HasNameAndId {

	protected Organization() {
	}

	public final native String getNickname() /*-{
		return this.nickname;
	}-*/;

	public final native String getShortName() /*-{
		return this.shortName;
	}-*/;

}
