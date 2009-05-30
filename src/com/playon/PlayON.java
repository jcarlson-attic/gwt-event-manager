package com.playon;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class PlayON implements EntryPoint {

	@Override
	public void onModuleLoad() {
		String[] parts = new String[] { "foo", "bar" };
		String remnants = "";
		for (int p = 1; p < parts.length; p++) {
			remnants += parts[p] + (p < parts.length - 1 ? "." : "");
		}
		GWT.log(remnants, null);

	}

}
