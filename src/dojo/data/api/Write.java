package dojo.data.api;

import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;

public interface Write {

	Item newItem(Object item);

	Item newItem(Object item, Item parent, String attribute);

	boolean deleteItem(Item item);

	boolean setValue(Item item, String attribute, JSONValue value);

	boolean setValues(Item item, String attribute, JSONValue[] values);

	boolean unsetAttribute(Item item, String attribute);

	void save(SaveCallback callback);

	boolean revert();

	boolean isDirty(Item item);

	public interface SaveCallback {
		void onComplete();

		void onError(Exception error);
	}

}
