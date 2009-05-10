package dojo.data.api;

import dojo.data.Item;

public interface Identity {

	<T> T getIdentity(Item item);

	<T> T[] getIdentityAttributes(Item item);

	<T> void fetchItemByIdentity(T identity, FetchItemCallback callback);

	public interface FetchItemCallback {

		void onItem(Item item);

		void onError(Exception error);

	}

}
