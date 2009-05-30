package dojo.data.api;

import java.util.List;
import java.util.Map;

import dojo.data.Item;

public class Request {

	public Map<String, Object> query;

	public String queryString;

	public Map<String, Object> queryOptions;

	public FetchCallback fetchCallback;

	public Integer start;

	public Integer count;

	public Sort[] sort;

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
