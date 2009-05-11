package com.playon;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.playon.security.OAuthSignedJSONRestService;

import dojo.Deferred;
import dojo.DeferredCommand;

public class PlayON implements EntryPoint {

	@Override
	public void onModuleLoad() {

		OAuthSignedJSONRestService svc = new OAuthSignedJSONRestService();
		svc.setEndpoint(Application.getParam("proxy"));

		Deferred dfd = svc.get("/oauth/request_token", null);

		dfd.addCallbacks(new DeferredCommand() {
			@Override
			public <T> T execute(T results) {
				GWT.log("Success! " + (String) results, null);
				return results;
			}
		}, new DeferredCommand() {
			@Override
			public <T> T execute(T results) {
				GWT.log("Crap!", (Exception) results);
				return results;
			}
		});

	}

}
