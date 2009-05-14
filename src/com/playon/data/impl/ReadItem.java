package com.playon.data.impl;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;
import dojo.data.Store;

public class ReadItem extends JSONObject implements Item {

	protected Store store;

	protected JSONValue datum;

	protected int index;

}
