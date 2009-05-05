package com.playon.security;

import dojox.encoding.digests.SHA1;

public enum OAuthSignatureMethod {
	PLAINTEXT("PLAINTEXT"), HMAC_SHA1("HMAC-SHA1");

	private String value;

	private OAuthSignatureMethod(String s) {
		this.value = s;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public String sign(String baseString, String key) {
		switch (this) {
		case PLAINTEXT:
			return key;
		default:
			return SHA1.hmac(baseString, key);
		}
	}

	public static OAuthSignatureMethod getValue(String method) {
		if (method == null) {
			return null;
		}

		for (OAuthSignatureMethod mthd : OAuthSignatureMethod.values()) {
			if (mthd.value.equals(method)) {
				return mthd;
			}
		}

		throw new IllegalArgumentException("Unsupported Signature Method: "
				+ method);
	}

}
