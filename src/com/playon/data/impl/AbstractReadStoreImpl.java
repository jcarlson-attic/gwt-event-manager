package com.playon.data.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import dojo.Deferred;
import dojo.DeferredCommand;
import dojo.data.Item;
import dojo.data.Store;
import dojo.data.api.Read;
import dojo.data.api.Request;
import dojo.data.api.Request.FetchCallback;

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
	public <T> T getValue(Item item, String attribute, T defaultValue) {
		ReadItem itm = this.assertItem(item);

		if (!this.hasAttribute(itm, attribute)) {
			return defaultValue;
		}

		// TODO
		return null;
	}

	@Override
	public <T> T[] getValues(Item item, String attribute) {
		this.assertItem(item);
		T[] values = this.getValue(item, attribute, null);
		return values;
	}

	@Override
	public String[] getAttributes(Item item) {
		ReadItem itm = this.assertItem(item);
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
		ReadItem itm = this.assertItem(item);
		JSONObject obj = itm.datum.isObject();
		if (obj != null) {
			return obj.containsKey(attribute);
		}
		return false;
	}

	@Override
	public <T> boolean containsValue(Item item, String attribute, T value) {
		T[] values = this.getValues(item, attribute);

		for (T val : values) {
			if (val == value) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isItem(Object object) {
		if (!(object instanceof ReadItem)) {
			return false;
		}

		ReadItem item = (ReadItem) object;

		if (item.store != this) {
			return false;
		}

		ReadItem inItems = (ReadItem) this.items.get(item.index);
		if (inItems.datum != item.datum) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isItemLoaded(Item item) {
		return this.isItem(item);
	}

	@Override
	public void loadItem(final LoadItemCallback callback) {
		// no-op
	}

	@Override
	public Deferred fetch(final Request args) {
		Deferred dfd = new Deferred();

		dfd.addCallbacks(new DeferredCommand() {

			@Override
			public <T> List<Item> execute(T results) {
				FetchCallback cb = args.fetchCallback();

				List<Item> items = AbstractReadStoreImpl.this.processRawItems(
						args, (List<JSONObject>) results);

				cb.onBegin(items.size(), args);

				// TODO: Sort

				for (Item item : items) {
					cb.onItem(item, args);
				}

				// TODO: Violates Dojo API. Use null instead of items if onItem
				// was present
				cb.onComplete(items, args);

				return items;
			}

		}, new DeferredCommand() {
			@Override
			public <T> T execute(T result) {
				FetchCallback cb = args.fetchCallback();
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
				sb.append(this.getValue(item, attr, ""));
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public String[] getLabelAttributes(Item item) {
		this.assertItem(item);

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

	protected List<Item> processRawItems(Request args, List<JSONObject> rawItems) {

		List<Item> items = new ArrayList<Item>();
		Iterator<JSONObject> itr = rawItems.iterator();
		while (itr.hasNext()) {
			JSONObject rawItem = itr.next();
			Item item = this.ingestItem(rawItem);
			items.add(item);
		}
		return items;
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

	protected ReadItem assertItem(Item item) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}
		return (ReadItem) item;
	}

}
