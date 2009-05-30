package com.playon.data.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.playon.rpc.RestService;
import com.playon.rpc.impl.JSONRestService;

import dojo.Deferred;
import dojo.DeferredCommand;
import dojo.data.api.Request;
import dojo.data.api.Response;

public class ReadOnlyRestStore extends AbstractClientCachingStore {

	public static final String SERVICE = "service";

	protected RestService service;

	public ReadOnlyRestStore() {
	}

	public ReadOnlyRestStore(Map<String, Object> options) {
		super(options);
		if (!options.containsKey(SERVICE)) {
			this.service = new JSONRestService();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Deferred fetch(Request request) {
		if (request == null) {
			request = new Request();
		}

		if (request.query == null) {
			request.query = new HashMap<String, Object>();
		}

		String uri = request.queryString != null ? request.queryString
				: (String) request.query.get("uri");
		Map<String, String> filter = (Map<String, String>) request.query
				.get("$filter");

		final Deferred fetch = super.fetch(request);
		final Deferred dfd = new Deferred();
		dfd.addCallback(new DeferredCommand() {
			@Override
			public <T> Response execute(T results) {
				Response response = new Response();
				response.rawText = (String) results;
				JSONValue rawValue = JSONParser.parse((String) results);
				if (rawValue.isArray() == null) {
					JSONArray array = new JSONArray();
					array.set(0, rawValue);
					rawValue = array;
				}
				response.rawItems = rawValue.isArray();
				com.google.gwt.user.client.DeferredCommand
						.addCommand(new Command() {
							@Override
							public void execute() {
								dfd.addCallbacks(new DeferredCommand() {
									@Override
									public <R> R execute(R results) {
										fetch.callback(results);
										return results;
									}
								}, new DeferredCommand() {
									@Override
									public <R> R execute(R results) {
										fetch.errback(results);
										return results;
									}
								});
							}
						});
				return response;
			}
		});

		Deferred xhr = this.service.get(uri, filter);
		xhr.addCallbacks(new DeferredCommand() {
			@Override
			public <R> R execute(R results) {
				dfd.callback(results);
				return results;
			}
		}, new DeferredCommand() {
			@Override
			public <R> R execute(R results) {
				dfd.errback(results);
				return results;
			}
		});

		return dfd;
	}

}
