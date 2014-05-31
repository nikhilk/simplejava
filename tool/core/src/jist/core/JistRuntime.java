// JistRuntime.java
// jist/core
//

package jist.core;

import java.net.*;

public interface JistRuntime {

    public String addModule(URI moduleURI) throws JistErrorException;

    public void execute(Jist jist) throws Exception;

    public void initialize(JistOptions options);
}
