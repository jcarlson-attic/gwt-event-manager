package com.playon.data;

import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.playon.data.impl.AbstractIdentityStoreImpl;
import com.playon.data.impl.ReadItem;

import dojo.data.Item;

public class AbstractJSONReferenceStore extends AbstractIdentityStoreImpl {

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
		Item item = null;

		if (rawItem.isArray() != null) {
			JSONArray array = rawItem.isArray();
			for (int i = 0; i < array.size(); i++) {
				JSONValue value = array.get(i);
				JSONObject obj = value.isObject();
				if (obj != null) {
					this.ingestItem(obj);
					continue;
				}
				JSONArray arr = value.isArray();
				if (arr != null) {
					this.ingestItem(arr);
					continue;
				}
			}
		}

		if (rawItem.isObject() != null && !this.isItem(rawItem)) {
			String[] identifiers = this._getIdentityAttributes(rawItem);
			JSONObject obj = rawItem.isObject();
			if (identifiers != null) {
				if (obj.containsKey(this.refAttribute)
						&& this.index.containsKey(obj.get(this.refAttribute))) {
					item = this.index.get(obj.get(this.refAttribute));
				} else {
					item = super.ingestItem(rawItem);
				}
			}

			for (String key : obj.keySet()) {
				JSONValue value = obj.get(key);
				ReadItem datum = (ReadItem) this.ingestItem(value);
				if (datum != null) {
					obj.put(key, datum);
				}
			}
		}
		return item;
	}

}
