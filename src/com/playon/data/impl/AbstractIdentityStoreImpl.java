package com.playon.data.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import dojo.data.Item;
import dojo.data.api.Identity;
import dojo.data.api.Request;

public abstract class AbstractIdentityStoreImpl extends AbstractReadStoreImpl
		implements Identity {

	public static final String ID_ATTRIBUTES = "idAttributes";

	private static final String ID_ATTR = "id";

	protected Map<String, IdentityItem> index = new HashMap<String, IdentityItem>();
	private String[] idAttributes = new String[] { ID_ATTR };

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
		return this._getIdentity(((ReadItem) item).datum);
	}

	protected String _getIdentity(JSONValue rawItem) {
		JSONObject obj = rawItem.isObject();
		if (obj == null) {
			return null;
		}

		String[] identifiers = this._getIdentityAttributes(rawItem);
		StringBuilder sb = new StringBuilder();

		for (String attr : identifiers) {
			if (!obj.containsKey(attr)) {
				throw new IllegalStateException("Item is not identifiable");
			}
			sb.append(obj.get(attr));
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
		JSONObject obj = rawItem.isObject();
		if (obj == null) {
			return null;
		}

		String[] attrs = new String[0];

		for (String attr : this.idAttributes) {
			if (obj.containsKey(attr)) {
				String[] newAttrs = new String[attrs.length + 1];
				System.arraycopy(attrs, 0, newAttrs, 0, attrs.length);
				newAttrs[newAttrs.length - 1] = attr;
				attrs = newAttrs;
			}
		}
		return attrs.length > 0 ? attrs : null;
	}

	@Override
	public void fetchItemByIdentity(final FetchItemCallback callback) {
		final Item item = this.index.get(callback.identity());

		if (item != null) {
			if (this.isItemLoaded(item)) {
				callback.onItem(item);
			} else {
				this.loadItem(new LoadItemCallback() {

					@Override
					public Item item() {
						return item;
					}

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
			this.fetch(new Request() {

				@Override
				public Integer count() {
					return null;
				}

				@Override
				public FetchCallback fetchCallback() {
					return new FetchCallback() {

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
				}

				@Override
				public Map<String, Object> query() {
					return null;
				}

				@Override
				public String queryString() {
					return callback.identity();
				}

				@Override
				public Map<String, Object> queryOptions() {
					return null;
				}

				@Override
				public Sort[] sort() {
					return null;
				}

				@Override
				public Integer start() {
					return null;
				}

			});
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
