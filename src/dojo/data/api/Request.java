package dojo.data.api;

import java.util.Map;

import dojo.data.Item;

public interface Request {

	Map<String, Object> query();

	Map<String, Object> queryOptions();

	FetchCallback fetchCallback();

	int start();

	int count();

	Sort[] sort();

	public interface FetchCallback {
		void onBegin(int size, Request request);

		void onItem(Item item, Request request);

		void onComplete(Item[] items, Request request);

		void onError(Exception error);
	}

	public class Sort {
		public String attribute;
		public boolean descending;
	}

}
