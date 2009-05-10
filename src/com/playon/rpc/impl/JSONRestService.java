package com.playon.rpc.impl;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.playon.model.HasIdentity;
import com.playon.rpc.RestService;
import com.playon.xhr.Method;
import com.playon.xhr.Request;

import dojo.Deferred;

public class JSONRestService implements RestService {

	@Override
	public Deferred get(String uri, Map<String, String> params) {
		Request request = this.buildRequest(Method.GET, uri);
		request.setParams(params);
		return this.sendRequest(request);
	}

	@Override
	public Deferred post(HasIdentity item) {
		if (item.getURI() == null || item.getURI().length() == 0) {
			throw new IllegalArgumentException("Item has to be identifiable");
		}
		return this.post(item.getURI(), item);
	}

	@Override
	public Deferred post(String uri, HasIdentity item) {
		Request request = this.buildRequest(Method.POST, uri);
		String content = this.serialize(item);
		request.setContent(content);
		return this.sendRequest(request);
	}

	@Override
	public Deferred put(HasIdentity item) {
		if (item.getURI() == null || item.getURI().length() == 0) {
			throw new IllegalArgumentException("Item has to be identifiable");
		}
		return this.put(item.getURI(), item);
	}

	@Override
	public Deferred put(String uri, HasIdentity item) {
		Request request = this.buildRequest(Method.PUT, uri);
		String content = this.serialize(item);
		request.setContent(content);
		return this.sendRequest(request);
	}

	@Override
	public Deferred delete(String uri, Map<String, String> params) {
		Request request = this.buildRequest(Method.DELETE, uri);
		request.setParams(params);
		return this.sendRequest(request);
	}

	protected Request buildRequest(Method method, String uri) {
		String url = this.endpoint;

		if (uri != null && uri.length() > 0) {
			url += uri;
		}

		Request request = new Request(method, url);
		return request;
	}

	protected String serialize(JavaScriptObject object) {
		JSONObject jsonObj = new JSONObject(object);
		return jsonObj.toString();
	}

	protected Deferred sendRequest(Request request) {
		// This method will invoke the XmlHttpRequest and eval() the response,
		// passing it to the Deferred
		Deferred dfd = new Deferred();
		dfd.callback(JavaScriptObject.createArray());
		return dfd;
	}

	protected String endpoint;

	public String getEndpoint() {
		return this.endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

}
