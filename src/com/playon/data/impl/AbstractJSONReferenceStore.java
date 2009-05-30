package com.playon.data.impl;

import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;

public abstract class AbstractJSONReferenceStore extends
		AbstractIdentityStoreImpl {

	public static final String REF_ATTRIBUTE = "$ref";

	private String refAttribute = REF_ATTRIBUTE;

	public AbstractJSONReferenceStore() {
	}

	public AbstractJSONReferenceStore(Map<String, Object> options) {
		super(options);
		if (options.containsKey(REF_ATTRIBUTE)) {
			this.refAttribute = (String) options.get(REF_ATTRIBUTE);
		}
	}

	@Override
	protected Item ingestItem(JSONValue rawItem) {
		if (rawItem.isArray() != null) {
			JSONArray array = rawItem.isArray();
			for (int i = 0; i < array.size(); i++) {
				this.ingestItem(array.get(i));
			}
		}

		if (rawItem.isObject() != null && !this.isItem(rawItem)) {
			JSONObject obj = rawItem.isObject();
			String identity = this._getIdentity(rawItem);
			if (identity != null) {
				if (obj.containsKey(this.refAttribute)
						&& this.index.containsKey(obj.get(this.refAttribute))) {
					return this.index.get(obj.get(this.refAttribute));
				} else {
					for (String key : obj.keySet()) {
						JSONValue value = obj.get(key);
						Item childItem = this.ingestItem(value);
						if (childItem != null) {
							obj.put(key, (ReadItem) childItem);
						}
					}
					return super.ingestItem(rawItem);
				}
			}

		}
		return null;
	}

}
