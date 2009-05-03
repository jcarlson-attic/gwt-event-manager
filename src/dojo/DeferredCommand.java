package dojo;

public interface DeferredCommand {

	<T> Object execute(T result);

}
