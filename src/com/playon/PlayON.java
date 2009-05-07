package com.playon;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.playon.model.Broadcast;
import com.playon.model.BroadcastState;

public class PlayON implements EntryPoint {

	@Override
	public void onModuleLoad() {

		Broadcast b = getJSO();

		BroadcastState bs = b.getBroadcastState();

		GWT.log(bs.getCode().toString(), null);

	}

	native Broadcast getJSO() /*-{
		return {
			state: {
				uri: "/foo/1/bar/baz",
				code: "unprovisioned"
			},
			availability: {
				code: "available"
			}
		};
	}-*/;

}
