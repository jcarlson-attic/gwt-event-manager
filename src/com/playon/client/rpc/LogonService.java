package com.playon.client.rpc;

import com.playon.client.util.Deferred;

public interface LogonService {

	Deferred logon(String username, String password);

	Deferred logout();

}
