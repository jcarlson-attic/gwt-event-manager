package dojo;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class DeferredTest {

	boolean passed;

	@Test
	public void callback() {
		passed = false;

		Deferred dfd = new Deferred();
		DeferredCommand dc = new DeferredCommand() {
			@Override
			public <T> T execute(T result) {
				passed = true;
				return result;
			}
		};
		dfd.addCallback(dc);
		dfd.callback("hello, world");

		Assert.assertTrue(passed);
	}

	@Test
	public void errback() {
		passed = false;

		Deferred dfd = new Deferred();
		DeferredCommand dc = new DeferredCommand() {
			@Override
			public <T> T execute(T result) {
				passed = true;
				return result;
			}
		};
		dfd.addErrback(dc);
		dfd.errback("hello, world");

		Assert.assertTrue(passed);
	}

	@Test
	public void chain() {
		passed = false;

		Deferred dfd = new Deferred();
		DeferredCommand dc1 = new DeferredCommand() {
			@Override
			public <T> T execute(T result) {
				return result;
			}
		};
		dfd.addCallback(dc1);
		DeferredCommand dc2 = new DeferredCommand() {
			@Override
			public <T> T execute(T result) {
				passed = true;
				return result;
			}
		};
		dfd.addCallback(dc2);
		dfd.callback("hello, world");

		Assert.assertTrue(passed);
	}

	@Test
	public void resultType() {
		passed = false;

		Deferred dfd = new Deferred();
		DeferredCommand dc1 = new DeferredCommand() {
			@Override
			public <T> String execute(T result) {
				return "foo=bar&baz=qux";
			}
		};
		dfd.addCallback(dc1);
		DeferredCommand dc2 = new DeferredCommand() {
			@Override
			public <T> Map<String, String> execute(T result) {
				Map<String, String> token = new HashMap<String, String>();
				String[] pairs = result.toString().split("&");

				for (String p : pairs) {
					String[] kv = p.split("=");
					token.put(kv[0], kv[1]);
				}

				passed = true;
				return token;
			}
		};
		dfd.addCallback(dc2);

		DeferredCommand eb1 = new DeferredCommand() {
			@Override
			public <T> T execute(T error) {
				System.out.println(error);
				Assert.fail("Why am I in the errback handler?");
				return error;
			}
		};
		dfd.addErrback(eb1);
		dfd.callback(12345);

		Assert.assertTrue(passed);
	}

}
