package com.playon.model;

public interface Organization extends HasIdentity, HasId, HasName {

	String getShortName();

	String getNickname();

}
