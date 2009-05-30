package com.playon.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import dojo.Deferred;
import dojo.DeferredCommand;
import dojo.data.Item;
import dojo.data.Store;
import dojo.data.api.Read;
import dojo.data.api.Request;
import dojo.data.api.Response;

public abstract class AbstractReadStoreImpl implements Store, Read {

	public static final String LABEL_ATTRIBUTES = "labelAttributes";

	private static final String NAME_ATTR = "name";

	protected List<ReadItem> items = new ArrayList<ReadItem>();
	private String[] labelAttributes = new String[] { NAME_ATTR };

	public AbstractReadStoreImpl() {
	}

	public AbstractReadStoreImpl(Map<String, Object> options) {
		this();
		if (options.containsKey(LABEL_ATTRIBUTES)) {
			this.labelAttributes = (String[]) options.get(LABEL_ATTRIBUTES);
		}
	}

	@Override
	public JSONValue getValue(Item item, String attribute,
			JSONValue defaultValue) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}
		ReadItem itm = (ReadItem) item;

		if (this.hasAttribute(itm, attribute)) {
			JSONObject datum = itm.datum.isObject();
			return datum.get(attribute);
		}

		return null;
	}

	@Override
	public JSONArray getValues(Item item, String attribute) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}
		JSONValue values = this.getValue(item, attribute, null);
		if (values.isArray() != null) {
			return values.isArray();
		}
		JSONArray array = new JSONArray();
		array.set(0, values);
		return array;
	}

	@Override
	public String[] getAttributes(Item item) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}
		ReadItem itm = (ReadItem) item;
		JSONObject obj = itm.datum.isObject();
		if (obj != null) {
			Set<String> keys = obj.keySet();
			String[] k = new String[keys.size()];
			return keys.toArray(k);

		}
		return new String[0];
	}

	@Override
	public boolean hasAttribute(Item item, String attribute) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}
		ReadItem itm = (ReadItem) item;
		JSONObject obj = itm.datum.isObject();
		if (obj != null) {
			return obj.containsKey(attribute);
		}
		return false;
	}

	@Override
	public boolean containsValue(Item item, String attribute, JSONValue value) {
		JSONArray values = this.getValues(item, attribute);

		for (int i = 0; i < values.size(); i++) {
			JSONValue val = values.get(i);
			if (val == value) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isItem(Object object) {
		return object != null && object instanceof ReadItem
				&& ((ReadItem) object).store == this
				&& this.items.get(((ReadItem) object).index) == object;
	}

	@Override
	public boolean isItemLoaded(Item item) {
		return this.isItem(item);
	}

	@Override
	public void loadItem(Item item, final LoadItemCallback callback) {
		// no-op
	}

	@Override
	public Deferred fetch(final Request request) {
		Deferred dfd = new Deferred();

		dfd.addCallback(new DeferredCommand() {
			@Override
			public <T> Response execute(T response) {
				return AbstractReadStoreImpl.this.processRawItems(request,
						(Response) response);
			}
		});
		dfd.addCallback(new DeferredCommand() {
			@Override
			public <T> Response execute(T response) {
				return AbstractReadStoreImpl.this.sortItems(request,
						(Response) response);
			}
		});
		dfd.addCallback(new DeferredCommand() {
			@Override
			public <T> Response execute(T response) {
				return AbstractReadStoreImpl.this.doFetchCallbacks(request,
						(Response) response);
			}
		});
		dfd.addErrback(new DeferredCommand() {
			@Override
			public <T> T execute(T result) {
				Request.FetchCallback cb = request.fetchCallback;
				cb.onError((Exception) result);
				return result;
			}
		});

		return dfd;
	}

	@Override
	public void close() {
		this.items.clear();
	}

	@Override
	public String getLabel(Item item) {
		String[] labelAttrs = this.getLabelAttributes(item);
		if (labelAttrs != null) {
			StringBuilder sb = new StringBuilder();
			for (String attr : labelAttrs) {
				sb.append(this.getValue(item, attr, new JSONString("")));
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public String[] getLabelAttributes(Item item) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}

		String[] attrs = new String[0];

		for (String attr : this.labelAttributes) {
			if (this.hasAttribute(item, attr)) {
				String[] newAttrs = new String[attrs.length + 1];
				System.arraycopy(attrs, 0, newAttrs, 0, attrs.length);
				newAttrs[newAttrs.length - 1] = attr;
				attrs = newAttrs;
			}
		}
		return attrs.length > 0 ? attrs : null;
	}

	protected Response processRawItems(Request request, Response response) {

		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < response.rawItems.size(); i++) {
			JSONValue rawItem = response.rawItems.get(i);
			Item item = this.ingestItem(rawItem);
			items.add(item);
		}
		response.items = items;
		return response;
	}

	protected Response sortItems(Request request, Response response) {
		// TODO: SortItems
		return response;
	}

	protected Response doFetchCallbacks(Request request, Response response) {
		Request.FetchCallback cb = request.fetchCallback;

		if (cb != null) {
			cb.onBegin(response.items.size(), request);

			for (int i = 0; i < response.items.size(); i++) {
				cb.onItem(response.items.get(i), request);
			}

			cb.onComplete(response.items, request);

		}

		return response;
	}

	protected Item ingestItem(JSONValue rawItem) {
		ReadItem wrapped = this.wrapItem(rawItem);
		ReadItem item = this.placeInIndex(wrapped);
		return item;
	}

	protected ReadItem wrapItem(JSONValue rawItem) {
		ReadItem item = new ReadItem();
		item.datum = rawItem;
		item.store = this;
		item.index = this.items.size();
		return item;
	}

	protected ReadItem placeInIndex(ReadItem item) {
		this.items.add(item.index, item);
		return item;
	}

}
