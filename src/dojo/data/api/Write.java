package dojo.data.api;

import dojo.data.Item;

public interface Write {

	Item newItem(Object item);

	Item newItem(Object item, Item parent, String attribute);

	boolean deleteItem(Item item);

	<T> boolean setValue(Item item, String attribute, T value);

	<T> boolean setValues(Item item, String attribute, T[] values);

	boolean unsetAttribute(Item item, String attribute);

	void save(SaveCallback callback);

	boolean revert();

	boolean isDirty(Item item);

	public interface SaveCallback {
		void onComplete();

		void onError(Exception error);
	}

}
