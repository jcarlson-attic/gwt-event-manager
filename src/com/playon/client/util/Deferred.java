package com.playon.client.util;

import java.util.LinkedList;

public class Deferred {

	private static int _nextId = 1;

	private LinkedList<DeferredCommand[]> chain;
	protected int id;
	private int fired;
	private int paused;
	private Object[] results;
	private Canceller canceller;
	private boolean silentlyCancelled;

	public Deferred() {
		this.chain = new LinkedList<DeferredCommand[]>();
		this.id = Deferred._nextId++;
		this.fired = -1;
		this.paused = 0;
		this.results = new Object[2];
		this.canceller = null;
		this.silentlyCancelled = false;
	}

	public Deferred(Canceller canceller) {
		this();
		this.canceller = canceller;
	}

	public void cancel() {
		Object err = null;
		if (this.fired == -1) {
			if (this.canceller != null) {
				err = this.canceller.execute(this);
			} else {
				this.silentlyCancelled = true;
			}
			if (this.fired == -1) {
				if (!(err instanceof Exception)) {
					err = new DeferredException(err, "cancel");
				}
				this.errback(err);
			}
		} else if ((this.fired == 0) && (this.results[0] instanceof Deferred)) {
			((Deferred) this.results[0]).cancel();
		}
	}

	public <T> void callback(T result) {
		this.check();
		this.resback(result);
	}

	public <T> void errback(T result) {
		this.check();
		if (!(result instanceof Exception)) {
			this.resback(new DeferredException(result, "error"));
		} else {
			this.resback(result);
		}
	}

	public Deferred addBoth(DeferredCommand cb) {
		return this.addCallbacks(cb, cb);
	}

	public Deferred addCallback(DeferredCommand cb) {
		return this.addCallbacks(cb, null);
	}

	public Deferred addErrback(DeferredCommand eb) {
		return this.addCallbacks(null, eb);
	}

	public Deferred addCallbacks(DeferredCommand cb, DeferredCommand eb) {
		DeferredCommand[] cbPair = new DeferredCommand[2];
		cbPair[0] = cb;
		cbPair[1] = eb;
		chain.add(cbPair);
		if (this.fired >= 0) {
			this.fire();
		}
		return this;
	}

	private <T> void resback(T result) {
		this.fired = ((result instanceof Exception) ? 1 : 0);
		this.results[this.fired] = result;
		this.fire();
	}

	private void check() {
		if (this.fired != -1) {
			if (!this.silentlyCancelled) {
				throw new Error("already called");
			}
			this.silentlyCancelled = false;
		}
	}

	private void fire() {
		LinkedList<DeferredCommand[]> chain = this.chain;
		int fired = this.fired;
		Object res = this.results[fired];
		final Deferred self = this;
		DeferredCommand cb = null;

		while ((chain.size() > 0) && this.paused == 0) {
			DeferredCommand f = chain.poll()[fired];
			if (f == null) {
				continue;
			}
			try {
				Object ret = f.execute(res);
				if (ret != null) {
					res = ret;
				}
				fired = ((res instanceof Exception) ? 1 : 0);
				if (res instanceof Deferred) {
					cb = new DeferredCommand() {

						@Override
						public <T> T execute(T result) {
							self.resback(result);
							self.paused--;
							if (self.paused == 0 && self.fired >= 0) {
								self.fire();
							}
							return result;
						}

					};
					this.paused++;
				}

			} catch (Exception e) {
				fired = 1;
				res = e;
			}
		}
		this.fired = fired;
		this.results[fired] = res;
		if ((cb != null) && this.paused != 0) {
			((Deferred) res).addBoth(cb);
		}

	}

	public static interface Canceller {
		Object execute(Deferred dfd);
	}

	public static class DeferredException extends Exception {
		private static final long serialVersionUID = 0;
		public Object cancelResult;
		public String type;

		public DeferredException(Object result, String type) {
			super("Deferred Cancelled" + result != null ? (": " + result) : "");
			this.cancelResult = result;
			this.type = type;
		}
	}

}
