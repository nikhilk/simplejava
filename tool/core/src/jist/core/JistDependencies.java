// JistDependencies.java
// core/jist
//

package jist.core;

import java.net.*;

/**
 * Implemented by dependency managers to track dependencies referenced
 * within Jist source.
 */
public interface JistDependencies {

    /**
     * Adds the module identified by the specified URI.
     * @param moduleURI the module identifier.
     * @return the import to reference to use the module if available, null otherwise.
     * @throws JistErrorException
     */
    public String addModule(URI moduleURI) throws JistErrorException;
}
