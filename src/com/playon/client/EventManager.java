package com.playon.client;

import com.google.gwt.core.client.EntryPoint;
import com.playon.client.rpc.LogonService;
import com.playon.client.rpc.OAuthLogonService;

public class EventManager implements EntryPoint {

	@Override
	public void onModuleLoad() {
		LogonService svc = new OAuthLogonService();
		svc.logon("username", "password");
	}

}
