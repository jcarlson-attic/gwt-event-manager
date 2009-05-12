package com.playon;

import com.google.gwt.i18n.client.Constants;

public interface Config extends Constants {

	String oauth_consumer_key();

	String oauth_consumer_secret();

	String webservices();

	String proxy();

}