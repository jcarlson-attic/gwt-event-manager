package com.playon.xhr;

import java.util.HashMap;
import java.util.Map;

public class Request {

	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String DEFAULT_CHARSET = "ISO-8859-1";

	public Method method;
	public String url;
	public Map<String, String> headers = new HashMap<String, String>();
	public String content = null;

	public Request(Method method, String url) {
		this.method = method;
		this.url = url;
	}

	// TODO: Support multiple headers of same name
	public String getHeader(String header) {
		return headers.get(header);
	}

	public void setContent(String content) {
		this.content = content;
	}

}