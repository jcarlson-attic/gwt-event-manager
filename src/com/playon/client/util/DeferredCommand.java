package com.playon.client.util;

public interface DeferredCommand {

	<T> T execute(T result);

}
