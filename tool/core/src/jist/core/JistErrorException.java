// JistErrorException.java
// jist/core
//

package jist.core;

@SuppressWarnings("serial")
public final class JistErrorException extends Exception {

    public JistErrorException(String error) {
        super(error);
    }

    public JistErrorException(String error, Throwable cause) {
        super(error, cause);
    }
}
