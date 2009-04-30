package com.playon.client.rpc;

import com.playon.client.util.Deferred;
import com.playon.client.util.DeferredCommand;

public class OAuthLogonService implements LogonService {

	private Deferred requestToken;

	public OAuthLogonService() {
		this.requestToken = this.getRequestToken();
		this.requestToken.addCallback(new DeferredCommand() {
			@Override
			public <T> T execute(T result) {
				OAuthLogonService.this.processToken(result);
				return result;
			}
		});
	}

	@Override
	public Deferred logon(final String username, final String password) {
		final Deferred logon = new Deferred();
		final OAuthLogonService self = this;

		this.requestToken.addCallback(new DeferredCommand() {
			@Override
			public <T> T execute(final T rToken) {
				Deferred authz = self.authorizeToken(username, password,
						(String) rToken);

				authz.addCallback(new DeferredCommand() {
					@Override
					public <V> V execute(V response) {
						Deferred aToken = self.getAccessToken((String) rToken);

						aToken.addCallback(new DeferredCommand() {
							@Override
							public <W> W execute(W aToken) {
								Object credentials = self.processToken(aToken);
								self.persistCredentials(credentials);
								logon.callback(true);
								return aToken;
							}
						});
						aToken.addErrback(new DeferredCommand() {
							@Override
							public <X> X execute(X error) {
								logon.errback(error);
								return error;
							}
						});

						return response;
					}
				});
				authz.addErrback(new DeferredCommand() {
					@Override
					public <Y> Y execute(Y err) {
						if (err instanceof IllegalArgumentException) {
							logon.errback("INVALID CREDENTIALS");
						} else {
							logon.errback(err);
						}
						return err;
					}
				});
				return rToken;
			}
		});
		this.requestToken.addErrback(new DeferredCommand() {
			@Override
			public <T> T execute(T error) {
				logon.errback(error);
				return error;
			}
		});

		return logon;
	}

	@Override
	public Deferred logout() {
		return new Deferred();
	}

	private Deferred getRequestToken() {
		return new Deferred();
	}

	private Deferred authorizeToken(String username, String password,
			String token) {
		return new Deferred();
	}

	private Deferred getAccessToken(String rToken) {
		return new Deferred();
	}

	private Object processToken(Object rawToken) {
		return rawToken;
	}

	private void persistCredentials(Object credentials) {

	}

}
