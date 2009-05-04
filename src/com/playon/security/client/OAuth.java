package com.playon.security.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;

public class OAuth {

	public static final String HMAC_SHA1 = "HMAC-SHA1";
	public static final String PLAINTEXT = "PLAINTEXT";

	private OAuth() {

	}

	public static void signRequest(RequestBuilder builder) {
		Signature sigBase = new Signature();
		String baseString = OAuthUtils.generateBaseString(builder, sigBase);
		String key = OAuthUtils.generateKey("123", "987");
		sigBase.oauth_signature = OAuthUtils.generateSignature(baseString, key, "HMAC-SHA1");
		GWT.log(sigBase.oauth_signature, null);
	}

	protected static class Signature {
		String oauth_consumer_key = "abc";
		String oauth_token = "xyz";
		String oauth_signature;
		String oauth_body_hash;
		String oauth_signature_method = OAuth.HMAC_SHA1;

		String oauth_nonce = "qwertyuiop"; //OAuthUtils.nonce(16);
		String oauth_timestamp = "404798700"; //OAuthUtils.timestamp();
		String oauth_version = "1.0";

	}

}
