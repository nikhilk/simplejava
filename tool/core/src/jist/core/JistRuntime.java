// JistRuntime.java
// jist/core
//

package jist.core;

/**
 * Implemented by Jist executors.
 */
public interface JistRuntime {

    /**
     * Executes the specified jist.
     * @param jist the jist to execute.
     * @throws Exception
     */
    public void execute(Jist jist) throws Exception;

    /**
     * Initializes the runtime.
     * @param options the options to customize the runtime behavior.
     * @param errorHandler the error handler to report errors to.
     */
    public void initialize(JistRuntimeOptions options, JistErrorHandler errorHandler);

    /**
     * Gets the preprocessor to apply to sources within the Jist.
     * @return the preprocessor to apply.
     */
    public JistPreprocessor getPreprocessor();
}
