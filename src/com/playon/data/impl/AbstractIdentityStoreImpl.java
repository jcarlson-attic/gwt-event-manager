package com.playon.data.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;
import dojo.data.api.Identity;
import dojo.data.api.Request;

public abstract class AbstractIdentityStoreImpl extends AbstractReadStoreImpl
		implements Identity {

	public static final String ID_ATTRIBUTES = "idAttributes";

	private static final String ID_ATTR = "uri";

	protected Map<String, IdentityItem> index = new HashMap<String, IdentityItem>();
	protected String[] idAttributes = new String[] { ID_ATTR };

	public AbstractIdentityStoreImpl() {
	}

	public AbstractIdentityStoreImpl(Map<String, Object> options) {
		super(options);
		if (options.containsKey(ID_ATTRIBUTES)) {
			this.idAttributes = (String[]) options.get(ID_ATTRIBUTES);
		}
	}

	@Override
	public void close() {
		super.close();
		this.index.clear();
	}

	@Override
	public String getIdentity(Item item) {
		if (!this.isItem(item)) {
			throw new NoSuchElementException(
					"Item is not a member of this store instance");
		}
		String idty = this._getIdentity(((ReadItem) item).datum);
		if (idty == null || idty == "") {
			throw new UnsupportedOperationException("Item is not identifiable");
		}
		return idty;
	}

	protected String _getIdentity(JSONValue rawItem) {
		if (rawItem == null || rawItem.isObject() == null) {
			return null;
		}

		String[] identifiers = this._getIdentityAttributes(rawItem);
		if (identifiers == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (String identifier : identifiers) {
			if (!rawItem.isObject().containsKey(identifier)) {
				return null;
			}
			sb.append(rawItem.isObject().get(identifier));
		}
		return sb.toString();
	}

	@Override
	public String[] getIdentityAttributes(Item item) {
		if (!this.isItem(item)) {
			return null;
		}
		JSONValue rawItem = ((ReadItem) item).datum;
		return this._getIdentityAttributes(rawItem);
	}

	protected String[] _getIdentityAttributes(JSONValue rawItem) {
		String[] attrs = this.idAttributes;
		return attrs.length > 0 ? attrs : null;
	}

	@Override
	public void fetchItemByIdentity(String identity,
			final FetchItemCallback callback) {
		final Item item = this.index.get(identity);

		if (item != null) {
			if (this.isItemLoaded(item)) {
				callback.onItem(item);
			} else {
				this.loadItem(item, new LoadItemCallback() {

					@Override
					public void onError(Exception error) {
						callback.onError(error);
					}

					@Override
					public void onItem(Item item) {
						callback.onItem(item);
					}

				});
			}
		} else {
			Request request = new Request();
			request.queryString = identity;
			request.fetchCallback = new Request.FetchCallback() {

				@Override
				public void onBegin(int size, Request request) {
				}

				@Override
				public void onComplete(List<Item> items, Request request) {
				}

				@Override
				public void onError(Exception error) {
					callback.onError(error);
				}

				@Override
				public void onItem(Item item, Request request) {
					callback.onItem(item);
				}

			};
			this.fetch(request);
		}
	}

	@Override
	protected ReadItem wrapItem(JSONValue rawItem) {
		Item ri = super.wrapItem(rawItem);
		IdentityItem itm = new IdentityItem((ReadItem) ri);
		itm.key = this._getIdentity(rawItem);
		return itm;
	}

	@Override
	protected ReadItem placeInIndex(ReadItem item) {
		String key = ((IdentityItem) item).key;
		if (this.index.containsKey(key)) {
			item = this.copyItem(this.index.get(key), (IdentityItem) item);
		} else {
			super.placeInIndex(item);
			this.index.put(key, (IdentityItem) item);
		}
		return item;
	}

	protected IdentityItem copyItem(IdentityItem oldItem, IdentityItem newItem) {
		oldItem.datum = newItem.datum;
		return oldItem;
	}

}
