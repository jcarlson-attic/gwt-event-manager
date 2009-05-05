package com.playon.rpc;

import dojo.Deferred;

public interface LogonService {

	Deferred logon(String username, String password);

	Deferred logout();

}
