package dojo.data.api;

import dojo.data.Item;

public interface Identity {

	String getIdentity(Item item);

	String[] getIdentityAttributes(Item item);

	void fetchItemByIdentity(FetchItemCallback callback);

	public interface FetchItemCallback {

		String identity();

		void onItem(Item item);

		void onError(Exception error);

	}

}
