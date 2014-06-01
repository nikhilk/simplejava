// JistErrorException.java
// jist/core
//

package jist.core;

/**
 * Errors raised during processing and executing Jists.
 */
@SuppressWarnings("serial")
public final class JistErrorException extends Exception {

    /**
     * {@inheritDoc}
     */
    public JistErrorException(String error) {
        super(error);
    }

    /**
     * {@inheritDoc}
     */
    public JistErrorException(String error, Throwable cause) {
        super(error, cause);
    }
}
