package dojo.data.api;

import dojo.data.Item;

public interface Notification {

	void addNotificationListener(NotificationListener listener);

	void removeNotificationListener(NotificationListener listener);

	public interface NotificationListener {
		<T> void onSet(Item item, String attribute, T oldValue, T newValue);

		void onNew(Item newItem);

		<T> void onNew(Item newItem, Item parent, String attribute, T oldValue,
				T newValue);

		void onDelete(Item deletedItem);
	}

}
