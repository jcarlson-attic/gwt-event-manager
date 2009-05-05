package com.playon.security;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.playon.security.OAuthSignature.Parameter;

import dojox.encoding.digests.SHA1;

public class OAuth {

	private OAuth() {

	}

	private static String consumerKey = "foo";
	private static String consumerSecret = "bar";

	private static String token = "baz";
	private static String tokenSecret = "qux";
	
	private static String nonce = "qwertyuiop";
	private static String timestamp = "404798700";
	
	private static String signatureMethod = "HMAC-SHA1";

	public static void signRequest(RequestBuilder builder) {
		OAuthSignature signature = new OAuthSignature(consumerKey, token, signatureMethod, nonce, timestamp);
		String baseString = OAuthUtils.generateBaseString(builder, signature);
		String key = OAuthUtils.generateKey(consumerSecret, tokenSecret);

		// Add oauth_body_hash ONLY if encryption is in use AND body is NOT
		// form-encoded
		// GET requests are not form encoded so they should include a body hash
		// http://oauth.googlecode.com/svn/spec/ext/body_hash/1.0/oauth-bodyhash.html#when_to_include
		if (signature.getParameter(Parameter.OAUTH_SIGNATURE_METHOD) != OAuthSignatureMethod.PLAINTEXT
				.toString()
				&& (builder.getHTTPMethod() == "GET" || builder
						.getHeader("Content-Type") != OAuthUtils.FORM_ENCODED)) {
			signature.setParameter(Parameter.OAUTH_BODY_HASH, SHA1.sha1(builder
					.getRequestData()));
		}

		OAuthSignatureMethod signer = OAuthSignatureMethod
				.getValue(signature.getParameter(Parameter.OAUTH_SIGNATURE_METHOD));

		signature.setParameter(Parameter.OAUTH_SIGNATURE, signer.sign(
				baseString, key));

		GWT.log(signature.getParameter(Parameter.OAUTH_SIGNATURE), null);
	}
}
