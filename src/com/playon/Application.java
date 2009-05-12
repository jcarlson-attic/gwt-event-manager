package com.playon;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;

public class Application {

	public static final Config config = GWT.create(Config.class);

	private static final Map<String, String> params = new HashMap<String, String>();

	private Application() {
	}

	public static String getParam(String param) {
		return params.get(param);
	}

	public static void setParam(String param, String value) {
		params.put(param, value);
	}

}
