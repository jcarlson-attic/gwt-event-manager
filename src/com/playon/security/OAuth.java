package com.playon.security;

import com.google.gwt.core.client.GWT;
import com.playon.security.OAuthSignature.OAuthParameter;
import com.playon.xhr.Method;
import com.playon.xhr.Request;

import dojox.encoding.digests.SHA1;

public class OAuth {

	private OAuth() {

	}

	public static void signRequest(Request request, OAuthParams params) {
		OAuthSignature signature = new OAuthSignature(params);

		// Add oauth_body_hash ONLY if encryption is in use AND body is NOT
		// form-encoded
		// GET requests are not form encoded so they should include a body hash
		// http://oauth.googlecode.com/svn/spec/ext/body_hash/1.0/oauth-bodyhash.html#when_to_include
		if (signature.getParameter(OAuthParameter.OAUTH_SIGNATURE_METHOD) != OAuthSignatureMethod.PLAINTEXT
				.toString()
				&& (request.method == Method.GET || request
						.getHeader(Request.CONTENT_TYPE) != OAuthUtils.FORM_ENCODED)) {
			String data = request.content != null ? request.content : "";
			String hash = SHA1.sha1(data);
			signature.setParameter(OAuthParameter.OAUTH_BODY_HASH, hash);
		}

		String baseString = OAuthUtils.generateBaseString(request, signature);
		GWT.log(baseString, null);
		String key = OAuthUtils.generateKey(params.consumerSecret,
				params.tokenSecret);
		GWT.log(key, null);

		OAuthSignatureMethod signer = OAuthSignatureMethod.getValue(signature
				.getParameter(OAuthParameter.OAUTH_SIGNATURE_METHOD));

		String sig = signer.sign(baseString, key);
		GWT.log(sig, null);
		signature.setParameter(OAuthParameter.OAUTH_SIGNATURE, sig);

		OAuth.attachToHeader(request, signature);

	}

	protected static void attachToHeader(Request request,
			OAuthSignature signature) {
		StringBuilder sb = new StringBuilder();
		sb.append("OAuth ");
		int i = 1;
		for (OAuthParameter p : OAuthParameter.values()) {
			String value = signature.getParameter(p);
			if (value != null) {
				sb.append(p.toString()).append("=").append('"').append(
						OAuthUtils.encode(value)).append('"');
				if (i < OAuthParameter.values().length) {
					sb.append(", ");
					i++;
				}
			}
		}
		String authorization = sb.toString();
		GWT.log(authorization, null);
		request.headers.put("Authorization", authorization);
	}
}
