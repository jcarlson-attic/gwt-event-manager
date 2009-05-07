package com.playon.rpc;

import com.playon.model.HasIdentity;

import dojo.Deferred;

public interface RestService {

	Deferred get(String uri);
	
	Deferred post(HasIdentity item);
	
	Deferred put(HasIdentity item);
	
	Deferred delete(HasIdentity item);
	
}
