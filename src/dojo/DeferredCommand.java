package dojo;

public interface DeferredCommand {

	<T> T execute(T result);

}
