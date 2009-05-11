package com.playon;

import java.util.Map;

import com.google.gwt.core.client.GWT;

public class Application {

	private static AppConfigDefaults defaults = GWT
			.create(AppConfigDefaults.class);

	private static Map<String, String> config = defaults.config();

	public static String getParam(String param) {
		return config.get(param);
	}

	public static void setParam(String param, String value) {
		config.put(param, value);
	}

}
