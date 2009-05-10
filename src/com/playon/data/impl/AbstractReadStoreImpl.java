package com.playon.data.impl;

import java.util.NoSuchElementException;
import java.util.Set;

import com.google.gwt.json.client.JSONObject;

import dojo.Deferred;
import dojo.DeferredCommand;
import dojo.data.Item;
import dojo.data.Store;
import dojo.data.api.Read;
import dojo.data.api.Request;
import dojo.data.api.Request.FetchCallback;

public abstract class AbstractReadStoreImpl implements Store, Read {

	@Override
	public <T> T getValue(Item item, String attribute, T defaultValue) {
		JsonItem itm = this.assertItem(item);

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
		JsonItem itm = this.assertItem(item);
		Set<String> keys = itm.datum.keySet();
		String[] k = new String[keys.size()];
		return keys.toArray(k);
	}

	@Override
	public boolean hasAttribute(Item item, String attribute) {
		JsonItem itm = this.assertItem(item);
		return itm.datum.containsKey(attribute);
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
	public boolean isItem(Object item) {
		return item instanceof JsonItem && ((JsonItem) item).store == this;
	}

	@Override
	public boolean isItemLoaded(Item item) {
		return this.isItem(item) && ((JsonItem) item).loader == null;
	}

	@Override
	public void loadItem(Item item, final LoadItemCallback callback) {
		if (!this.isItemLoaded(item)) {
			Deferred dfd = ((JsonItem) item).loaded;
			dfd.addCallbacks(new DeferredCommand() {
				@Override
				public <T> T execute(T result) {
					callback.onItem((Item) result);
					return result;
				}
			}, new DeferredCommand() {
				@Override
				public <T> T execute(T result) {
					callback.onError((Exception) result);
					return result;
				}
			});
		}
	}

	@Override
	public Deferred fetch(final Request args) {
		Deferred dfd = new Deferred();

		dfd.addCallbacks(new DeferredCommand() {

			@Override
			public <T> Item[] execute(T results) {
				FetchCallback cb = args.fetchCallback();

				// TODO: How do we get the raw items?
				Object[] rawItems = new Object[0];

				cb.onBegin(rawItems.length, args);

				Item[] items = new Item[rawItems.length];
				for (int i = 0; i < rawItems.length; i++) {
					items[i] = AbstractReadStoreImpl.this
							.ingestItem(rawItems[i]);
				}

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
		// TODO: Empty the index
	}

	@Override
	public String getLabel(Item item) {
		StringBuilder sb = new StringBuilder();
		String[] attributes = this.getLabelAttributes(item);
		for (String attribute : attributes) {
			sb.append(this.getValue(item, attribute, ""));
		}
		String label = sb.toString();
		if (label.length() > 0) {
			return label;
		}
		return null;
	}

	@Override
	public String[] getLabelAttributes(Item item) {
		JsonItem itm = this.assertItem(item);
		if (itm.datum.containsKey("name")) {
			return new String[] { "name" };
		}
		return null;
	}

	protected Item ingestItem(Object rawItem) {
		// TODO
		Item wrapped = this.wrapItem(rawItem);
		Item item = this.placeInIndex(wrapped);
		return item;
	}

	protected Item wrapItem(Object rawItem) {
		JsonItem item = new JsonItem();
		item.datum = (JSONObject) rawItem;
		item.store = this;
		// TODO: Add the index information
		return item;
	}

	protected Item placeInIndex(Item item) {
		// TODO: Actually index the item
		return item;
	}

	protected JsonItem assertItem(Item item) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}
		return (JsonItem) item;
	}

}
