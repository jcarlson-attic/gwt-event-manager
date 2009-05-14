package dojo.data.api;

import java.util.List;
import java.util.Map;

import dojo.data.Item;

public interface Request {

	Map<String, Object> query();

	String queryString();

	Map<String, Object> queryOptions();

	FetchCallback fetchCallback();

	Integer start();

	Integer count();

	Sort[] sort();

	public interface FetchCallback {
		void onBegin(int size, Request request);

		void onItem(Item item, Request request);

		void onComplete(List<Item> items, Request request);

		void onError(Exception error);
	}

	public class Sort {
		public String attribute;
		public boolean descending;
	}

}
