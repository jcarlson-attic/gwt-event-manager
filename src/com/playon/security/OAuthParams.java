package com.playon.security;

import com.playon.Application;
import com.playon.security.OAuthSignature.OAuthSignatureLocation;

public class OAuthParams {

	public String consumerKey = Application.getParam("oauth_consumer_key");
	public String consumerSecret = Application
			.getParam("oauth_consumer_secret");

	public String token = Application.getParam("oauth_token");
	public String tokenSecret = Application.getParam("oauth_token_secret");

	public String nonce = OAuthUtils.nonce(16);
	public String timestamp = OAuthUtils.timestamp();

	public OAuthSignatureMethod signatureMethod = OAuthSignatureMethod.HMAC_SHA1;
	public OAuthSignatureLocation signatureLocation = OAuthSignature.OAuthSignatureLocation.HEADER;

	public String version = "1.0";

}
