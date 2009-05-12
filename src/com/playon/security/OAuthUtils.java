package com.playon.security;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.gwt.http.client.URL;
import com.playon.security.OAuthSignature.OAuthParameter;
import com.playon.xhr.Request;

final class OAuthUtils {

	protected static final String FORM_ENCODED = "application/x-www-form-urlencoded";

	private OAuthUtils() {

	}

	protected static String generateBaseString(Request request,
			OAuthSignature sigBase) {
		String method = request.method.toString();
		String url = request.url;
		String content = request.content;
		String contentType = request.getHeader("Content-Type");

		Set<String[]> paramSet = OAuthUtils.getParamSet(url, content,
				contentType);

		for (OAuthParameter param : OAuthSignature.OAuthParameter.values()) {
			String value = sigBase.getParameter(param);
			if (value != null) {
				paramSet.add(new String[] { param.toString(),
						sigBase.getParameter(param) });
			}

		}

		StringBuffer params = new StringBuffer();
		for (Iterator<String[]> itr = paramSet.iterator(); itr.hasNext();) {
			String[] param = itr.next();
			params.append(OAuthUtils.encode(param[0]) + "="
					+ OAuthUtils.encode(param[1]));
			if (itr.hasNext()) {
				params.append("&");
			}
		}

		return method.toUpperCase() + "&"
				+ OAuthUtils.encode(OAuthUtils.normalizeUrl(url)) + "&"
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
				String k = URL.decodeComponent(kv[0]);
				String v = URL.decodeComponent(kv[1]);
				params.add(new String[] { k, v });
			}
		}

		// If contentType is application/x-www-form-urlencoded and content is
		// not empty, extract those params
		if (contentType != null
				&& contentType.toLowerCase().equals(OAuthUtils.FORM_ENCODED)
				&& content != null && !content.equals("")) {
			String[] kvPairs = content.split("&");
			for (String kvPair : kvPairs) {
				String[] kv = kvPair.split("=");
				String k = URL.decodeComponent(kv[0]);
				String v = URL.decodeComponent(kv[1]);
				params.add(new String[] { k, v });
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
