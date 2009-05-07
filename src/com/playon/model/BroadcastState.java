package com.playon.model;

public interface BroadcastState extends HasIdentity {

	public enum Code {
		UNPROVISIONED, PROVISIONED, STREAMING, AIRING;

		public static Code getCode(String code) {
			if (code == null) {
				return null;
			}
			return Code.valueOf(code.toUpperCase());
		}
	}

	Code getCode();

}
