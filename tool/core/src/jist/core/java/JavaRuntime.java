// JavaRuntime.java
// jist/core
//

package jist.core.java;

import java.io.*;
import jist.core.*;
import jist.core.java.expanders.*;
import jist.core.java.runtimes.*;
import jist.util.*;

public abstract class JavaRuntime implements JistRuntime {

    protected JavaRuntime() {
    }

    protected abstract String createImplementation(Jist jist) throws IOException;

    private String createJavaSource(JistSession session, String implementation) throws IOException {
        StringBuilder sourceBuilder = new StringBuilder();

        String packageName = session.getPackageName();
        if (Strings.hasValue(packageName)) {
            sourceBuilder.append("package ");
            sourceBuilder.append(packageName);
            sourceBuilder.append(";\n\n");
        }

        for (String importedReference : session.getImports()) {
            sourceBuilder.append("import ");
            sourceBuilder.append(importedReference);
            sourceBuilder.append(";\n");
        }

        for (String importedReference : session.getStaticImports()) {
            sourceBuilder.append("import static ");
            sourceBuilder.append(importedReference);
            sourceBuilder.append(";\n");
        }

        sourceBuilder.append("\n");

        sourceBuilder.append(implementation);
        return sourceBuilder.toString();
    }

    public static JistRuntime createRuntime(String name) {
        return (name.equals("eval")) ? new JavaEvalRuntime() : new JavaSnippetRuntime();
    }

    protected <T> Class<T> getClass(JistSession session, String fullName) {
        JavaClassFactory classFactory = ((JavaSession)session).getClassFactory();
        return classFactory.getClass(fullName);
    }

    protected abstract void runJist(Jist jist);

    @Override
    public JistSession createSession(JistOptions options) {
        JistDependencies dependencies = new JarDependencies(options);

        JistSession session = new JavaSession(this, dependencies);
        session.registerExpander("text", new TextExpander());

        return session;
    }

    @Override
    public void execute(Jist jist) throws Exception {
        // First create the class implementation. This will cause pragmas
        // to get processed, and collect session info.
        String implementation = createImplementation(jist);

        // Now resolve dependencies, during which course, additional information
        // may be added to the session
        JavaSession session = (JavaSession)jist.getSession();
        session.getDependencies().resolveModules(session);

        // Finally generate the compilation source, with all the collectioned information
        String source = createJavaSource(session, implementation);

        boolean compiled = session.getClassFactory().compile(session.getClassName(), source);
        if (compiled) {
            runJist(jist);
        }
    }
}
