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

    private String _runnableClassName;

    @Override
    protected Map<String, String> createSource(Jist jist) throws IOException, JistErrorException {
        JistSource source = loadSource(jist, null);
        String className = JavaSource.getClassName(source);

        _runnableClassName = JavaSource.getFullClassName(source);

        String classImplementation = String.format(JAVA_SOURCE_TEMPLATE, className, source.getProcessedText());
        String compilableSource = JavaSource.createCompilableSource(source, classImplementation);

        Map<String, String> files = new HashMap<String, String>();
        files.put(className, compilableSource);

        return files;
    }

    @Override
    protected void runJist(Jist jist) {
        Class<Runnable> jistClass = getClass(_runnableClassName);

        Runnable jistInstance;
        try {
            jistInstance = jistClass.newInstance();
            jistInstance.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
