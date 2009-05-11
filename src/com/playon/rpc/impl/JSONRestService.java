package com.playon.rpc.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.playon.model.HasIdentity;
import com.playon.rpc.RestService;
import com.playon.xhr.Method;
import com.playon.xhr.Request;
import com.playon.xhr.RestRequestBuilder;

import dojo.Deferred;

public class JSONRestService implements RestService {

	@Override
	public Deferred get(String uri, Map<String, String> params) {
		Request request = this.buildRequest(Method.GET, uri, params);
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
		Request request = this.buildRequest(Method.POST, uri, null);
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
		Request request = this.buildRequest(Method.PUT, uri, null);
		String content = this.serialize(item);
		request.setContent(content);
		return this.sendRequest(request);
	}

	@Override
	public Deferred delete(String uri, Map<String, String> params) {
		Request request = this.buildRequest(Method.DELETE, uri, null);
		return this.sendRequest(request);
	}

	protected Request buildRequest(Method method, String uri,
			Map<String, String> params) {
		String url = this.endpoint;

		if (uri != null && uri.length() > 0) {
			url += uri;
		}

		if (params != null) {
			StringBuilder qs = new StringBuilder().append("?");
			Iterator<Entry<String, String>> itr = params.entrySet().iterator();
			while (itr.hasNext()) {
				Entry<String, String> param = itr.next();
				qs.append(param.getKey()).append("=").append(param.getValue());
				if (itr.hasNext()) {
					qs.append("&");
				}
			}
			url += qs.toString();
		}

		Request request = new Request(method, url);
		return request;
	}

	protected String serialize(JavaScriptObject object) {
		JSONObject jsonObj = new JSONObject(object);
		return jsonObj.toString();
	}

	protected Deferred sendRequest(Request request) {
		final Deferred dfd = new Deferred();

		RequestBuilder rb = new RestRequestBuilder(request.method, request.url);
		rb.setRequestData(request.content);

		for (Entry<String, String> header : request.headers.entrySet()) {
			rb.setHeader(header.getKey(), header.getValue());
		}

		rb.setCallback(new RequestCallback() {

			@Override
			public void onError(com.google.gwt.http.client.Request request,
					Throwable exception) {
				dfd.errback(exception);
			}

			@Override
			public void onResponseReceived(
					com.google.gwt.http.client.Request request,
					Response response) {
				if (response.getStatusCode() == 200) {
					JSONValue results = JSONParser.parse(response.getText());

					JSONObject object = results.isObject();
					if (object != null) {
						dfd.callback(object.getJavaScriptObject());
					}
					JSONArray array = results.isArray();
					if (array != null) {
						JsArray<JavaScriptObject> jsa = array
								.getJavaScriptObject().cast();
						dfd.callback(jsa);
					}
					JSONBoolean bool = results.isBoolean();
					if (bool != null) {
						dfd.callback(bool);
					}
					JSONNumber number = results.isNumber();
					if (number != null) {
						dfd.callback(number);
					}
					JSONString string = results.isString();
					if (string != null) {
						dfd.callback(string);
					}
					JSONNull nil = results.isNull();
					if (nil != null) {
						dfd.callback(nil);
					}

				} else {
					dfd.errback(new RequestException(response.getStatusText()));
				}
			}

		});
		try {
			rb.send();
		} catch (RequestException e) {
			dfd.errback(e);
		}

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
