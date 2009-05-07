package com.playon.model;

public class EventAvailability extends HasIdentity {

	public enum Code {
		AVAILABLE, INTERRUPTED, DELAYED;

		public static Code getCode(String code) {
			if (code == null) {
				return null;
			}
			return Code.valueOf(code.toUpperCase());
		}
	}

	protected EventAvailability() {
	}

	public final Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private native String getCodeString() /*-{
		return this.code;
	}-*/;

	public final native String getMessage() /*-{
		return this.message;
	}-*/;

}
