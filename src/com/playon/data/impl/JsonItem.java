package com.playon.data.impl;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;

import dojo.Deferred;
import dojo.data.Item;
import dojo.data.Store;

public class JsonItem implements Item {

	protected Store store;

	protected JSONObject datum;

	protected Command loader;

	protected Deferred loaded;

}
