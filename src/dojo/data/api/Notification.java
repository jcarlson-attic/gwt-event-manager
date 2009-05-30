package dojo.data.api;

import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;

public interface Notification {

	void addNotificationListener(NotificationListener listener);

	void removeNotificationListener(NotificationListener listener);

	public interface NotificationListener {
		void onSet(Item item, String attribute, JSONValue oldValue,
				JSONValue newValue);

		void onNew(Item newItem);

		void onNew(Item newItem, Item parent, String attribute,
				JSONValue oldValue, JSONValue newValue);

		void onDelete(Item deletedItem);
	}

}
