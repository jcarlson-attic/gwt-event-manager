package dojo;

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
					err = new DeferredCancelledException(err);
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
			this.resback(new DeferredExecutionException(result));
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

	public Deferred addDeferred(final Deferred dfd) {
		DeferredCommand dc = new DeferredCommand() {
			@Override
			public <T> Object execute(T result) {
				return dfd;
			}
		};
		this.addBoth(dc);
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
		Object result = this.results[fired];
		DeferredCommand cb = null;

		while ((chain.size() > 0) && this.paused == 0) {
			DeferredCommand dc = chain.poll()[fired];
			if (dc == null) {
				continue;
			}
			try {
				Object r = dc.execute(result);
				if (r != result) {
					result = r;
				}
				fired = ((result instanceof Exception) ? 1 : 0);
				if (result instanceof Deferred) {
					cb = new DeferredCommand() {

						@Override
						public <T> T execute(T result) {
							Deferred.this.resback(result);
							Deferred.this.paused--;
							if (Deferred.this.paused == 0
									&& Deferred.this.fired >= 0) {
								Deferred.this.fire();
							}
							return result;
						}

					};
					this.paused++;
				}

			} catch (Exception e) {
				fired = 1;
				result = e;
			}
		}
		this.fired = fired;
		this.results[fired] = result;
		if ((cb != null) && this.paused != 0) {
			((Deferred) result).addBoth(cb);
		}

	}

	public static interface Canceller {
		Object execute(Deferred dfd);
	}

	public static class DeferredExecutionException extends RuntimeException {
		private static final long serialVersionUID = 0;
		public Object result;

		public <T> DeferredExecutionException(T result) {
			super("Deferred Cancelled" + result != null ? (": " + result) : "");
			this.result = result;
		}
	}

	public static class DeferredCancelledException extends
			DeferredExecutionException {
		private static final long serialVersionUID = 0;

		public <T> DeferredCancelledException(T result) {
			super(result);
		}
	}
}
