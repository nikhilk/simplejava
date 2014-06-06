// JavaEvalRuntime.java
// jist/core
//

package jist.core.java.runtimes;

import java.util.*;
import jist.core.*;
import jist.core.java.*;

public final class JavaEvalRuntime extends JavaRuntime {

    private static final String JAVA_SOURCE_TEMPLATE =
"public final class %s implements Runnable {\n" +
"    public void run() {\n" +
"        Object __value = %s;\n" +
"        if (__value == null) {\n" +
"            __value = \"<null>\";\n" +
"        }\n" +
"        System.out.println(\"Result: \" + __value);\n" +
"        System.out.println();\n" +
"    }\n" +
"}\n";

    private String _runnableClassName;

    @Override
    protected Map<String, String> createSources(Jist jist) {
        JistSource source = jist.getSource(JavaSource.DEFAULT_SOURCE_NAME);
        if (source == null) {
            getErrorHandler().handleError(null, 0, "Unable to read source code.");
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
