package com.playon.model.impl;

import com.playon.model.Video;

public class VideoImpl extends HasIdentityImpl implements Video {

	@Override
	public final native String getURL() /*-{
		return this.url;
	}-*/;

}
