package dojo.data.api;

import dojo.data.Item;

public interface Identity {

	String getIdentity(Item item);

	String[] getIdentityAttributes(Item item);

	void fetchItemByIdentity(String identity, FetchItemCallback callback);

	public interface FetchItemCallback {

		void onItem(Item item);

		void onError(Exception error);

	}

}
