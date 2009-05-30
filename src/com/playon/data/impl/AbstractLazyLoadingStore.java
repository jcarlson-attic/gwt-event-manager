package com.playon.data.impl;

import java.util.Map;

import dojo.data.Item;
import dojo.data.api.Identity;

public abstract class AbstractLazyLoadingStore extends AbstractBeanValueStore {

	public static final String LAZY_FLAG = "$ref";

	private String lazyFlag = LAZY_FLAG;

	public AbstractLazyLoadingStore() {
	}

	public AbstractLazyLoadingStore(Map<String, Object> options) {
		super(options);
		if (options.containsKey(LAZY_FLAG)) {
			this.lazyFlag = (String) options.get(LAZY_FLAG);
		}
	}

	@Override
	public boolean isItemLoaded(Item item) {
		return super.isItemLoaded(item)
				&& !this.hasAttribute(item, this.lazyFlag);
	}

	@Override
	public void loadItem(Item item, final LoadItemCallback callback) {
		if (!this.isItemLoaded(item)) {
			String identity = this.getValue(item, this.lazyFlag, null)
					.isString().stringValue();
			this.fetchItemByIdentity(identity,
					new Identity.FetchItemCallback() {

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
	}

}
