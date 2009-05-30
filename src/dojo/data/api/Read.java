package dojo.data.api;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;

import dojo.Deferred;
import dojo.data.Item;

public interface Read {

	JSONValue getValue(Item item, String attribute, JSONValue defaultValue);

	JSONArray getValues(Item item, String attribute);

	String[] getAttributes(Item item);

	boolean hasAttribute(Item item, String attribute);

	boolean containsValue(Item item, String attribute, JSONValue value);

	boolean isItem(Object item);

	boolean isItemLoaded(Item item);

	void loadItem(Item item, LoadItemCallback callback);

	Deferred fetch(Request args);

	void close();

	String getLabel(Item item);

	String[] getLabelAttributes(Item item);

	public interface LoadItemCallback {

		void onItem(Item item);

		void onError(Exception error);

	}

}
