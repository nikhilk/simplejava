// JistErrorHandler.java
// jist/core
//

package jist.core;

/**
 * Implemented by the application to report errors.
 */
public interface JistErrorHandler {

    /**
     * Allows the application to handle an error while processing the jist, and report it.
     * @param location the location of the error if it is specific to a particular file.
     * @param lineNumber the line number of the error location, 0 if there is no association.
     * @param error the error message.
     */
    public void handleError(String location, int lineNumber, String error);

    /**
     * Allows the application to handle exceptions raised during executing the jist code.
     * @param e the exception raised when executing the jist.
     */
    public void handleException(Exception e);
}
