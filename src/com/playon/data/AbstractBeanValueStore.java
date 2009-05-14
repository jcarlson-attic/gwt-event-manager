package com.playon.data;

import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;

public abstract class AbstractBeanValueStore extends AbstractJSONReferenceStore {

	@Override
	public <T> T getValue(Item item, String attribute, T defaultValue) {
		String[] parts = attribute.split(".");
		if (parts.length == 1) {
			return super.getValue(item, attribute, defaultValue);
		}

		String part = parts[0];
		JSONValue value = (JSONValue) super.getValue(item, part, defaultValue);
		for (int i = 1; i < parts.length; i++) {
			if (this.isItem(value)) {

			}
			part = parts[i];
			value = value.isObject().get(part).isObject();
		}
		return (T) value;
	}

}
