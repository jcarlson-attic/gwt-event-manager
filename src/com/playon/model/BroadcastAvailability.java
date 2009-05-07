package com.playon.model;

public interface BroadcastAvailability {

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

	Code getCode();

}
