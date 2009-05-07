package com.playon.model;

public interface EventAvailability extends HasIdentity {

	public enum Code {
		AVAILABLE, INTERRUPTED, DELAYED;

		public static Code getCode(String code) {
			if (code == null) {
				return null;
			}
			return Code.valueOf(code.toUpperCase());
		}
	}

	Code getCode();

	String getMessage();

}
