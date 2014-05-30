// JistRuntime.java
// jist/core
//

package jist.core;

import java.net.*;

public interface JistRuntime {

    public JistRuntime addImport(String name);

    public JistRuntime addModule(URI moduleURI) throws JistErrorException;

    public JistRuntime addStaticImport(String name);

    public void execute(Jist jist) throws Exception;

    public JistExpander getExpander(String name);

    public void initialize(JistOptions options);

    public JistRuntime specifyClassName(String name) throws JistErrorException;

    public JistRuntime specifyPackageName(String name) throws JistErrorException;
}
