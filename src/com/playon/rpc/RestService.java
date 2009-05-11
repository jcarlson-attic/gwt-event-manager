package com.playon.rpc;

import java.util.Map;

import com.playon.model.HasIdentity;

import dojo.Deferred;

public interface RestService {

	Deferred get(String uri, Map<String, String> params);

	Deferred post(HasIdentity item);

	Deferred post(String uri, HasIdentity item);

	Deferred put(HasIdentity item);

	Deferred put(String uri, HasIdentity item);

	Deferred delete(String uri, Map<String, String> params);

}
