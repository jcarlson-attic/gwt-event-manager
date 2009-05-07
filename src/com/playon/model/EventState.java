package com.playon.model;

public class EventState extends HasIdentity {

	public enum Code {
		SCHEDULED("scheduled"), IN_PRODUCTION("inprod"), COMPLETE("complete"), CANCELLED(
				"cancelled");

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

	protected EventState() {
	}

	public final Code getCode() {
		return Code.getCode(this.getCodeString());
	}

	private final native String getCodeString() /*-{
		return this.code;
	}-*/;

}
