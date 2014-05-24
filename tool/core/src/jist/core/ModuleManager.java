// ModuleManager.java
// jist/core
//

package jist.core;

import java.net.*;

public interface ModuleManager {

    public void addModule(URI moduleURI) throws JistErrorException;

    public void resolveModules(JistSession session) throws JistErrorException;
}
