// JavaRuntime.java
// jist/core
//

package jist.core.java;

import java.io.*;
import java.net.*;
import java.util.*;
import jist.core.*;
import jist.core.java.expanders.*;

public abstract class JavaRuntime implements JistRuntime {

    private JarDependencies _dependencies;
    private JavaClassFactory _classFactory;
    private JavaPreprocessor _preprocessor;

    protected JavaRuntime() {
    }

    protected abstract Map<String, String> createSource(Jist jist) throws IOException, JistErrorException;

    protected <T> Class<T> getClass(String fullName) {
        return _classFactory.getClass(fullName);
    }

    protected JistSource loadSource(Jist jist, String name) throws IOException, JistErrorException {
        JistSource source = jist.getSource(name);
        source.applyPreprocessor(_preprocessor);

        return source;
    }

    protected abstract void runJist(Jist jist);

    @Override
    public String addModule(URI moduleURI) throws JistErrorException {
        return _dependencies.addModule(moduleURI);
    }

    @Override
    public void execute(Jist jist) throws Exception {
        Map<String, String> source = createSource(jist);

        List<JavaFile> compilationUnits = new ArrayList<JavaFile>(source.size());
        for (Map.Entry<String, String> sourceEntry : source.entrySet()) {
            compilationUnits.add(new JavaFile(sourceEntry.getKey(), sourceEntry.getValue()));
        }

        boolean compiled = _classFactory.compile(compilationUnits);
        if (compiled) {
            runJist(jist);
        }
    }

    @Override
    public void initialize(JistOptions options) {
        _dependencies = new JarDependencies(options);

        _classFactory = new JavaClassFactory(_dependencies);

        _preprocessor = new JavaPreprocessor(this);
        _preprocessor.addExpander("text", new TextExpander(this));
    }
}
