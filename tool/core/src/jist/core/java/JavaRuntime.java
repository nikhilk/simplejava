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
    private JavaClassFactory _classFactory;
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
    protected abstract Map<String, String> createSource(Jist jist) throws IOException, JistErrorException;

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
        jist.addPreprocessor(_preprocessor);

        List<JavaFile> compilationUnits = new ArrayList<JavaFile>();
        Map<String, String> source = createSource(jist);
        for (Map.Entry<String, String> sourceEntry : source.entrySet()) {
            compilationUnits.add(new JavaFile(sourceEntry.getKey(), sourceEntry.getValue()));
        }

        boolean compiled = _classFactory.compile(compilationUnits);
        if (compiled) {
            runJist(jist, _classFactory.getClassLoader());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(JistRuntimeOptions options) {
        _dependencies = new JarDependencies(options);

        _classFactory = new JavaClassFactory(_dependencies);

        _preprocessor = new JavaPreprocessor(_dependencies);
        _preprocessor.registerExpander("text", new TextExpander(this));
    }
}
