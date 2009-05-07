package com.playon.model;

public class BroadcastState extends HasIdentity {

	public enum Code {
		UNPROVISIONED, PROVISIONED, STREAMING, AIRING;

		public static Code getCode(String code) {
			if (code == null) {
				return null;
			}
			return Code.valueOf(code.toUpperCase());
		}
	}

	protected BroadcastState() {
	}

	public final Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private native String getCodeString() /*-{
		return this.code;
	}-*/;

}
