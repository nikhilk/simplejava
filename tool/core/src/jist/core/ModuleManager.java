// ModuleManager.java
// jist/core
//

package jist.core;

import java.net.*;

public interface ModuleManager {

    public void addModule(URI moduleURI) throws JistErrorException;

    public ClassLoader getClassLoader() throws JistErrorException;

    public String getClassPath() throws JistErrorException;
}
