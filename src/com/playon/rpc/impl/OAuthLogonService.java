package com.playon.rpc.impl;

import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.playon.Application;
import com.playon.rpc.LogonService;
import com.playon.security.OAuth;
import com.playon.security.OAuthParams;
import com.playon.security.OAuthSignature;
import com.playon.security.OAuthSignatureMethod;
import com.playon.security.OAuthToken;
import com.playon.xhr.Method;
import com.playon.xhr.Request;

import dojo.Deferred;
import dojo.DeferredCommand;

public class OAuthLogonService implements LogonService {

	private Deferred requestToken;

	public OAuthLogonService() {
		// Get a requestToken for this service instance
		this.requestToken = this.getRequestToken();
	}

	@Override
	public Deferred logon(String username, String password) {
		final Deferred logon = new Deferred();

		// Wait for the requestToken to be available
		logon.addDeferred(this.requestToken);

		// Authorize requestToken with username and password
		logon.addCallback(this.authorizeToken(username, password));

		// Retrieve the accessToken
		logon.addCallback(this.getAccessToken());

		// Kickoff the deferred chain
		logon.callback(null);
		return logon;
	}

	@Override
	public Deferred logout() {
		return new Deferred();
	}

	private Deferred getRequestToken() {
		final Deferred dfd = new Deferred();

		dfd.addCallbacks(new DeferredCommand() {
			@Override
			public <T> OAuthToken execute(T result) {
				OAuthToken token = OAuthLogonService.this
						.processToken((String) result);
				return token;
			}
		}, new DeferredCommand() {
			@Override
			public <T> Object execute(T result) {
				return new RuntimeException(
						"Unable to retrieve OAuth Request Token");
			}
		});

		String url = Application.config.proxy() + "/oauth/request_token";
		Request request = new Request(Method.GET, url);
		OAuthParams params = new OAuthParams();
		String webservices = Application.config.webservices();
		String proxy = Application.config.proxy();

		if (request.url.startsWith(proxy)) {
			request.url = webservices + request.url.substring(proxy.length());
		}

		OAuth.signRequest(request, params);

		if (request.url.startsWith(webservices)) {
			request.url = proxy + request.url.substring(webservices.length());
		}

		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, request.url);
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
					dfd.callback(response.getText());
				} else {
					dfd
							.errback(new RuntimeException(
									"Request Token retrieval failed. Some kind of server problem."));
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

	private DeferredCommand authorizeToken(final String username,
			final String password) {
		// this deferred needs to return the requestToken it was called with
		return new DeferredCommand() {
			@Override
			public <T> Deferred execute(final T reqToken) {
				final Deferred http = new Deferred();

				String url = Application.config.proxy() + "/oauth/authenticate";
				Request request = new Request(Method.POST, url);
				request.content = "oauth_callback="
						+ URL.encodeComponent(GWT.getModuleBaseURL()
								+ "auth_ok.html");
				request.headers.put(Request.CONTENT_TYPE, Request.FORM_ENCODED);
				OAuthParams params = new OAuthParams();
				params.consumerKey = username;
				params.consumerSecret = password;
				params.token = ((OAuthToken) reqToken).token;
				params.tokenSecret = "";
				params.signatureMethod = OAuthSignatureMethod.PLAINTEXT;
				params.signatureLocation = OAuthSignature.OAuthSignatureLocation.QUERYSTRING;

				String webservices = Application.config.webservices();
				String proxy = Application.config.proxy();
				if (request.url.startsWith(proxy)) {
					request.url = webservices
							+ request.url.substring(proxy.length());
				}

				OAuth.signRequest(request, params);

				if (request.url.startsWith(webservices)) {
					request.url = proxy
							+ request.url.substring(webservices.length());
				}

				RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
						request.url);
				for (Entry<String, String> header : request.headers.entrySet()) {
					rb.setHeader(header.getKey(), header.getValue());
				}
				rb.setRequestData(request.content);
				rb.setCallback(new RequestCallback() {

					@Override
					public void onError(
							com.google.gwt.http.client.Request request,
							Throwable exception) {
						http.errback(exception);
					}

					@Override
					public void onResponseReceived(
							com.google.gwt.http.client.Request request,
							Response response) {
						if (response.getStatusCode() == 200) {
							http.callback(reqToken);
						} else if (response.getStatusCode() == 401) {
							http.errback(new IllegalArgumentException(
									"Invalid username or password"));
						} else {
							http.errback(new RuntimeException(
									"Authentication failed. Some kind of server problem:"
											+ response.getText()));
						}
					}

				});
				try {
					rb.send();
				} catch (RequestException e) {
					http.errback(e);
				}

				return http;
			}
		};
	}

	private DeferredCommand getAccessToken() {
		// this deferred should return true when user is logged on
		return new DeferredCommand() {
			@Override
			public <T> Deferred execute(T reqToken) {
				final Deferred http = new Deferred();

				String url = Application.config.proxy() + "/oauth/access_token";
				Request request = new Request(Method.GET, url);
				OAuthParams params = new OAuthParams();
				params.token = ((OAuthToken) reqToken).token;
				params.tokenSecret = ((OAuthToken) reqToken).secret;

				String webservices = Application.config.webservices();
				String proxy = Application.config.proxy();
				if (request.url.startsWith(proxy)) {
					request.url = webservices
							+ request.url.substring(proxy.length());
				}

				OAuth.signRequest(request, params);

				if (request.url.startsWith(webservices)) {
					request.url = proxy
							+ request.url.substring(webservices.length());
				}

				RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
						request.url);
				for (Entry<String, String> header : request.headers.entrySet()) {
					rb.setHeader(header.getKey(), header.getValue());
				}
				rb.setCallback(new RequestCallback() {

					@Override
					public void onError(
							com.google.gwt.http.client.Request request,
							Throwable exception) {
						http.errback(exception);
					}

					@Override
					public void onResponseReceived(
							com.google.gwt.http.client.Request request,
							Response response) {
						if (response.getStatusCode() == 200) {
							OAuthToken aToken = OAuthLogonService.this
									.processToken(response.getText());
							OAuthLogonService.this.persistCredentials(aToken);
							http.callback(true);
						} else {
							http.errback(new RuntimeException(
									"Unable to retrieve OAuth Access Token."));
						}
					}

				});
				try {
					rb.send();
				} catch (RequestException e) {
					http.errback(e);
				}

				return http;
			}
		};
	}

	private OAuthToken processToken(String rawToken) {
		String[] params = rawToken.split("&");
		String token = params[0].split("=")[1];
		String secret = params[1].split("=")[1];

		token = URL.decodeComponent(token);
		secret = URL.decodeComponent(secret);

		OAuthToken processedToken = new OAuthToken(token, secret);
		return processedToken;
	}

	private void persistCredentials(OAuthToken credentials) {
		Application.setParam("oauth_token", credentials.token);
		Application.setParam("oauth_token_secret", credentials.secret);
	}

}
