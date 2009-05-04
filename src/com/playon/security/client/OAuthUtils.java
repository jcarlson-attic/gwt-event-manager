package com.playon.security.client;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;

import dojox.encoding.digests.SHA1;

final class OAuthUtils {

	protected static final String FORM_ENCODED = "application/x-www-form-urlencoded";

	private OAuthUtils() {

	}

	protected static String generateBaseString(RequestBuilder builder,
			OAuth.Signature sigBase) {
		String method = builder.getHTTPMethod();
		String url = builder.getUrl();
		String content = builder.getRequestData();
		String contentType = builder.getHeader("Content-Type");

		Set<String[]> paramSet = OAuthUtils.getParamSet(url, content,
				contentType);

		paramSet.add(new String[] { "oauth_consumer_key",
				sigBase.oauth_consumer_key });
		paramSet.add(new String[] { "oauth_token", sigBase.oauth_token });
		paramSet.add(new String[] { "oauth_nonce", sigBase.oauth_nonce });
		paramSet
				.add(new String[] { "oauth_timestamp", sigBase.oauth_timestamp });
		paramSet.add(new String[] { "oauth_signature_method",
				sigBase.oauth_signature_method });
		paramSet.add(new String[] { "oauth_version", sigBase.oauth_version });

		StringBuffer params = new StringBuffer();
		for (Iterator<String[]> itr = paramSet.iterator(); itr.hasNext();) {
			String[] param = itr.next();
			params.append(OAuthUtils.encode(param[0]) + "="
					+ OAuthUtils.encode(param[1]));
			if (itr.hasNext()) {
				params.append("&");
			}
		}

		return method.toUpperCase() + "&" + OAuthUtils.encode(OAuthUtils.normalizeUrl(url)) + "&"
				+ OAuthUtils.encode(params.toString());
	}

	protected static SortedSet<String[]> getParamSet(String url,
			String content, String contentType) {
		SortedSet<String[]> params = new TreeSet<String[]>(
				new ParamComparator());

		// If URL has querystring, extract those params
		if (url.indexOf('?') > -1) {
			String qs = url.substring(url.indexOf('?') + 1);
			if (qs.indexOf('#') > -1) {
				qs = qs.substring(0, qs.indexOf('#'));
			}
			String[] kvPairs = qs.split("&");
			for (String kvPair : kvPairs) {
				String[] kv = kvPair.split("=");
				params.add(kv);
			}
		}

		// If contentType is application/x-www-form-urlencoded and content is
		// not empty, extract those params
		if (contentType.toLowerCase().equals(OAuthUtils.FORM_ENCODED)
				&& content != null && !content.equals("")) {
			String[] kvPairs = content.split("&");
			for (String kvPair : kvPairs) {
				String[] kv = kvPair.split("=");
				params.add(kv);
			}
		}

		return params;
	}

	protected static native String normalizeUrl(String url) /*-{
		var parser = /^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@]*):?([^:@]*))?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/, match = parser.exec(url);

		var p = match[1].toLowerCase(), a = match[2].toLowerCase(), b = (p == "http" && match[7] == 80) || (p == "https" && match[7] == 443);
		if (b) {
		    if (a.lastIndexOf(":") > -1) {
		        a = a.substring(0, a.lastIndexOf(":"));
		    }
		}
		var path = match[9] || "/";
		return p + "://" + a + path;
	}-*/;

	protected static String generateKey(String consumerSecret,
			String tokenSecret) {
		return OAuthUtils.encode(consumerSecret) + "&"
				+ OAuthUtils.encode(tokenSecret != null ? tokenSecret : "");
	}

	protected static String generateSignature(String baseString, String key,
			String signatureMethod) {
		if (signatureMethod != OAuth.HMAC_SHA1
				&& signatureMethod != OAuth.PLAINTEXT) {
			throw new IllegalArgumentException("Only " + OAuth.HMAC_SHA1
					+ " and " + OAuth.PLAINTEXT
					+ " signature methods are supported.");
		}
		if (signatureMethod == OAuth.PLAINTEXT) {
			return key;
		} else {
			return SHA1.hmac(baseString, key);
		}
	}

	protected static String encode(String s) {
		return URL.encodeComponent(s).replace("!", "%21").replace("*", "%2A")
				.replace("'", "%27").replace("(", "%28").replace(")", "%29");
	}

	private static String tab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";

	protected static String nonce(int length) {
		String s = "";
		for (int i = 0; i < length; i++) {
			int n = (int) Math.floor(Math.random() * tab.length());
			s += tab.charAt(n);
		}
		return s;
	}

	protected static String timestamp() {
		Date d = new Date();
		return "" + ((d.getTime() / 1000) - 2);
	}

	private static class ParamComparator implements Comparator<String[]> {

		@Override
		public int compare(String[] a, String[] b) {
			int key = a[0].compareTo(b[0]);
			if (key != 0) {
				return key;
			}
			int value = a[1].compareTo(b[1]);
			return value;
		}

	}

}
