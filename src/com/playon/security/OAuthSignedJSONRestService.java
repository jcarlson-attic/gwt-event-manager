package com.playon.security;

import com.playon.Application;
import com.playon.rpc.impl.JSONRestService;
import com.playon.xhr.Request;

import dojo.Deferred;

public class OAuthSignedJSONRestService extends JSONRestService {

	@Override
	protected Deferred sendRequest(Request request) {
		String webservices = Application.config.webservices();
		String proxy = Application.config.proxy();

		if (request.url.startsWith(proxy)) {
			request.url = webservices + request.url.substring(proxy.length());
		}

		OAuthParams params = new OAuthParams();
		OAuth.signRequest(request, params);

		if (request.url.startsWith(webservices)) {
			request.url = proxy + request.url.substring(webservices.length());
		}

		return super.sendRequest(request);
	}

}
