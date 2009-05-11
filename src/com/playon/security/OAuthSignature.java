package com.playon.security;

import java.util.HashMap;
import java.util.Map;

public class OAuthSignature {

	public static enum OAuthSignatureLocation {
		HEADER, QUERYSTRING, BODY
	}

	public static enum OAuthParameter {
		OAUTH_BODY_HASH, OAUTH_CONSUMER_KEY, OAUTH_NONCE, OAUTH_SIGNATURE, OAUTH_SIGNATURE_METHOD, OAUTH_TIMESTAMP, OAUTH_TOKEN, OAUTH_VERSION;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

	}

	private Map<OAuthParameter, String> params;

	public OAuthSignature(OAuthParams params) {
		this.params = new HashMap<OAuthParameter, String>();
		this.setParameter(OAuthParameter.OAUTH_NONCE, params.nonce);
		this.setParameter(OAuthParameter.OAUTH_TIMESTAMP, params.timestamp);
		this.setParameter(OAuthParameter.OAUTH_VERSION, params.version);
		this.setParameter(OAuthParameter.OAUTH_SIGNATURE_METHOD,
				params.signatureMethod.toString());

		this
				.setParameter(OAuthParameter.OAUTH_CONSUMER_KEY,
						params.consumerKey);
		this.setParameter(OAuthParameter.OAUTH_TOKEN, params.token);
	}

	public void setParameter(OAuthParameter param, String value) {
		this.params.put(param, value);
	}

	public String getParameter(OAuthParameter param) {
		return this.params.get(param);
	}

}
