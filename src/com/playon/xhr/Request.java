package com.playon.xhr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Request {

	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String DEFAULT_CHARSET = "ISO-8859-1";

	public Method method;
	public String url;
	public final List<Entry<String, String>> headers = new ArrayList<Entry<String, String>>();
	public String body = null;

	public Request() {
		this(null, null);
	}

	public Request(Method method, String url) {
		this(method, url, null);
	}

	public Request(Method method, String url, String body) {
		this.method = method;
		this.url = url;
		this.body = body;
	}

	public String getHeader(String header) {
		String value = null;
		Iterator<Entry<String, String>> itr = headers.iterator();
		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			if (header.equalsIgnoreCase(entry.getKey())) {
				value = entry.getValue();
			}
		}
		return value;
	}

}