// JistDependencies.java
// core/jist
//

package jist.core;

import java.net.*;

public interface JistDependencies {

    public String addModule(URI moduleURI) throws JistErrorException;
}
