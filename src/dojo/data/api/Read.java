package dojo.data.api;

import dojo.Deferred;
import dojo.data.Item;

public interface Read {

	<T> T getValue(Item item, String attribute, T defaultValue);

	<T> T[] getValues(Item item, String attribute);

	String[] getAttributes(Item item);

	boolean hasAttribute(Item item, String attribute);

	<T> boolean containsValue(Item item, String attribute, T value);

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
