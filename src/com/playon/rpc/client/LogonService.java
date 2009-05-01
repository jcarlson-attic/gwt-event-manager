package com.playon.rpc.client;

import dojo.Deferred;

public interface LogonService {

	Deferred logon(String username, String password);

	Deferred logout();

}
