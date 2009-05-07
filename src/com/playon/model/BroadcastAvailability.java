package com.playon.model;

import com.google.gwt.core.client.JavaScriptObject;

public class BroadcastAvailability extends JavaScriptObject {

	public enum Code {
		LIVE_VOD("live+vod"), LIVE("live"), VOD("vod");

		private String code;

		private Code(String code) {
			this.code = code;
		}

		public static Code getCode(String code) {
			for (Code cd : Code.values()) {
				if (cd.code.equalsIgnoreCase(code)) {
					return cd;
				}
			}
			return null;
		}
	}

	protected BroadcastAvailability() {
	}

	public final Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private native String getCodeString() /*-{
		return this.code;
	}-*/;

}
