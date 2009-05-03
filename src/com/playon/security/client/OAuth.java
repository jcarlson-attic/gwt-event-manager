package com.playon.security.client;

import java.util.Date;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;

public class OAuth {

	public static final String HMAC_SHA1 = "HMAC-SHA1";
	public static final String PLAINTEXT = "PLAINTEXT";

	private OAuth() {

	}

	public static void signRequest(RequestBuilder builder) {
		OAuthSignature sigBase = new OAuthSignature();
		String baseString = OAuth.generateBaseString(builder, sigBase);
		String key = OAuth.generateKey("", "");
		String signature = OAuth.generateSignature(baseString, key, "");
	}

	private static String generateBaseString(RequestBuilder builder,
			OAuthSignature sigBase) {

		return "";
	}

	private static String generateKey(String consumerSecret, String tokenSecret) {
		return OAuth.encode(consumerSecret) + "&"
				+ OAuth.encode(tokenSecret != null ? tokenSecret : "");
	}

	private static String generateSignature(String baseString, String key, String signatureMethod) {
		if (signatureMethod != OAuth.HMAC_SHA1 && signatureMethod != OAuth.PLAINTEXT) {
			throw new IllegalArgumentException("Only " + OAuth.HMAC_SHA1 + " and " + OAuth.PLAINTEXT + " signature methods are supported.");
		}
		if (signatureMethod == OAuth.PLAINTEXT) {
			return key;
		} else {
			return "";
		}
	}

	private static String encode(String s) {
		return URL.encodeComponent(s).replace("!", "%21").replace("*", "%2A")
				.replace("'", "%27").replace("(", "%28").replace(")", "%29");
	}

	private static class OAuthSignature {
		String oauth_consumer_key;
		String oauth_token;
		String oauth_signature;
		String oauth_body_hash;
		String oauth_signature_method;

		String oauth_nonce = nonce(16);
		String oauth_timestamp = timestamp();
		String oauth_version = "1.0";

		private static String tab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";

		private String nonce(int length) {
			String s = "";
			for (int i = 0; i < length; i++) {
				int n = (int) Math.floor(Math.random() * tab.length());
				s += tab.charAt(n);
			}
			return s;
		}

		private String timestamp() {
			Date d = new Date();
			return "" + ((d.getTime() / 1000) - 2);
		}
	}

}
