package com.playon.security.client;

import java.util.HashMap;
import java.util.Map;

public class OAuthSignature {

	public static enum Parameter {
		OAUTH_BODY_HASH, OAUTH_CONSUMER_KEY, OAUTH_NONCE, OAUTH_SIGNATURE, OAUTH_SIGNATURE_METHOD, OAUTH_TIMESTAMP, OAUTH_TOKEN, OAUTH_VERSION;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

	}

	private Map<Parameter, String> params;

	public OAuthSignature(String consumerKey, String token,
			String signatureMethod) {
		this.params = new HashMap<Parameter, String>();
		this.setParameter(Parameter.OAUTH_NONCE, OAuthUtils.nonce(16));
		this.setParameter(Parameter.OAUTH_TIMESTAMP, OAuthUtils.timestamp());
		this.setParameter(Parameter.OAUTH_VERSION, "1.0");
		this.setParameter(Parameter.OAUTH_SIGNATURE_METHOD, signatureMethod);

		this.setParameter(Parameter.OAUTH_CONSUMER_KEY, consumerKey);
		this.setParameter(Parameter.OAUTH_TOKEN, token);
	}

	// TODO: Remove!
	public OAuthSignature(String consumerKey, String token,
			String signatureMethod, String nonce, String timestamp) {
		this(consumerKey, token, signatureMethod);
		this.setParameter(Parameter.OAUTH_NONCE, nonce);
		this.setParameter(Parameter.OAUTH_TIMESTAMP, timestamp);
	}

	public void setParameter(Parameter param, String value) {
		this.params.put(param, value);
	}

	public String getParameter(Parameter param) {
		return this.params.get(param);
	}

}
