// JavaRuntime.java
// jist/core
//

package jist.core.java;

import java.io.*;
import java.util.*;
import jist.core.*;
import jist.core.java.expanders.*;

/**
 * Base class for Jists executed as Java code.
 */
public abstract class JavaRuntime implements JistRuntime {

    private JarDependencies _dependencies;
    private JavaPreprocessor _preprocessor;

    /**
     * Creates and initializes an instance of a JavaRuntime.
     */
    protected JavaRuntime() {
    }

    /**
     * Generates code to be compiled for the specified Jist.
     * @param jist the jist to convert to compilabe code.
     * @return compilable code stored in form of file name/content tuples.
     * @throws IOException
     * @throws JistErrorException
     */
    protected abstract Map<String, String> createSources(Jist jist) throws IOException, JistErrorException;

    /**
     * Gets the dependencies associated with the current runtime/jist.
     * @return the dependencies manager.
     */
    public JistDependencies getDependencies() {
        return _dependencies;
    }

    /**
     * Runs the specified jist once its associated code has been compiled.
     * @param jist the jist to run.
     * @param classLoader the class loader to load compiled classes.
     */
    protected abstract void runJist(Jist jist, ClassLoader classLoader);

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Jist jist) throws Exception {
        Map<String, String> sources = createSources(jist);
        ClassFactory classFactory = ClassFactory.create(_dependencies, sources);

        ClassLoader classLoader = classFactory.getClassLoader();
        if (classLoader != null) {
            runJist(jist, classLoader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(JistRuntimeOptions options) {
        _dependencies = new JarDependencies(options);

        _preprocessor = new JavaPreprocessor(this);
        _preprocessor.registerExpander("text", new TextExpander(this));
        _preprocessor.registerExpander("json", new JsonExpander(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JistPreprocessor getPreprocessor() {
        return _preprocessor;
    }
}
