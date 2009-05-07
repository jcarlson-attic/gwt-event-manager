package com.playon.model;

public interface EventState extends HasIdentity {

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

	Code getCode();

}
