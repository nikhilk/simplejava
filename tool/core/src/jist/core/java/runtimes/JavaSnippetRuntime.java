// JavaSnippetRuntime.java
// jist/core
//

package jist.core.java.runtimes;

import java.io.*;
import java.util.*;
import jist.core.*;
import jist.core.java.*;

public final class JavaSnippetRuntime extends JavaRuntime {

    private static final String JAVA_SOURCE_TEMPLATE =
"public final class %s implements Runnable {\n" +
"    public void run() {\n" +
"        %s\n" +
"    }\n" +
"}\n";

    private String[] _externalImports;
    private String[] _externalModules;
    private String _runnableClassName;

    @Override
    protected Map<String, String> createSources(Jist jist) throws IOException, JistErrorException {
        JistSource metadata = jist.getSource(JavaSource.METADATA_SOURCE_NAME);
        if (metadata != null) {
            _externalImports = JavaSource.getImports(metadata);
            _externalModules = JavaSource.getModules(metadata);
        }

        JistSource source = jist.getSource(JavaSource.DEFAULT_SOURCE_NAME);
        if (_externalImports != null) {
            for (String s : _externalImports) {
                JavaSource.addImport(source, s);
            }
        }
        if (_externalModules != null) {
            for (String s : _externalModules) {
                JavaSource.addModule(source, s);
            }
        }

        String className = JavaSource.getClassName(source);
        _runnableClassName = JavaSource.getFullClassName(source);

        String classImplementation = String.format(JAVA_SOURCE_TEMPLATE, className, source.getProcessedText());
        String compilableSource = JavaSource.createCompilableSource(source, classImplementation);

        Map<String, String> files = new HashMap<String, String>();
        files.put(className, compilableSource);

        return files;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void runJist(Jist jist, ClassLoader classLoader) {
        try {
            Class<Runnable> jistClass = (Class<Runnable>)classLoader.loadClass(_runnableClassName);
            Runnable jistInstance = jistClass.newInstance();

            jistInstance.run();
        }
        catch (Exception e) {
            getErrorHandler().handleException(e);
        }
    }
}
