package com.playon;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PlayON implements EntryPoint {

	@Override
	public void onModuleLoad() {

		// Service returns JavaScriptObjects to Store
		JavaScriptObject obj = getObject();

		// Client requests the "foo" attribute of item
		JSONObject json = new JSONObject(obj);
		JSONValue value = json.get("qux");

		JSONObject object = value.isObject();
		if (object != null) {
			log("Object", object);
		}
		JSONArray array = value.isArray();
		if (array != null) {
			JsArray<JavaScriptObject> jsa = array.getJavaScriptObject().cast();
			log("Array", jsa);
		}
		JSONBoolean bool = value.isBoolean();
		if (bool != null) {
			log("Boolean", bool);
		}
		JSONNumber number = value.isNumber();
		if (number != null) {
			log("Number", number);
		}
		JSONString string = value.isString();
		if (string != null) {
			log("String", string);
		}
		JSONNull nil = value.isNull();
		if (nil != null) {
			log("Null", nil);
		}

	}

	<T> void log(String type, T value) {
		GWT.log(type + ": " + value.toString(), null);
	}

	native JavaScriptObject getObject() /*-{
		return {
			foo: "bar",
			baz: ["a", "b", "c"],
			qux: 12345,
			etc: {
				blah: "whatev"
			}
		};
	}-*/;

}
