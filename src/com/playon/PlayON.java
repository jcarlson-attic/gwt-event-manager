package com.playon;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.playon.rpc.LogonService;
import com.playon.rpc.impl.OAuthLogonService;

import dojo.Deferred;
import dojo.DeferredCommand;

public class PlayON implements EntryPoint {

	@Override
	public void onModuleLoad() {

		LogonService svc = new OAuthLogonService();
		Deferred dfd = svc.logon("jcarlson", "playonsports");
		dfd.addBoth(new DeferredCommand() {
			@Override
			public <T> T execute(T results) {
				if (results instanceof Exception) {
					GWT.log("Doh!", (Exception) results);
				} else {
					GWT.log(results.toString(), null);
				}
				return results;
			}
		});
	}

}
