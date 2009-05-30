package com.playon.data.impl;

import java.util.Map;

import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;

public abstract class AbstractBeanValueStore extends AbstractJSONReferenceStore {

	public AbstractBeanValueStore() {
	}

	public AbstractBeanValueStore(Map<String, Object> options) {
		super(options);
	}

	@Override
	public JSONValue getValue(Item item, String attribute,
			JSONValue defaultValue) {
		String[] parts = attribute.split(".");
		if (parts.length == 1) {
			return super.getValue(item, attribute, defaultValue);
		}

		String part = parts[0];
		JSONValue value = super.getValue(item, part, defaultValue);
		for (int i = 1; i < parts.length; i++) {
			if (this.isItem(value)) {
				String remnants = "";
				for (int p = 1; p < parts.length; p++) {
					remnants += parts[p] + (p < parts.length - 1 ? "." : "");
				}
				return this.getValue(item, remnants, defaultValue);
			}
			part = parts[i];
			value = value.isObject().get(part);
		}
		return value;
	}

}
