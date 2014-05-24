// JavaRuntime.java
// jist/core
//

package jist.core.java;

import jist.core.*;
import jist.core.java.expanders.*;
import jist.core.java.runtimes.*;
import jist.util.*;

public abstract class JavaRuntime implements JistRuntime {

    private JavaClassFactory _classFactory;

    protected JavaRuntime() {
    }

    protected abstract String createImplementation(Jist jist);

    private String createJavaSource(Jist jist) {
        JistSession session = jist.getSession();
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

        String classCode = createImplementation(jist);
        sourceBuilder.append(classCode);

        return sourceBuilder.toString();
    }

    public static JistRuntime createRuntime(String name) {
        return (name.equals("eval")) ? new JavaEvalRuntime() : new JavaSnippetRuntime();
    }

    protected <T> Class<T> getClass(String fullName) {
        return _classFactory.getClass(fullName);
    }

    protected abstract void runJist(Jist jist);

    @Override
    public JistSession createSession(ModuleManager moduleManager) {
        JistSession session = new JistSession(this, moduleManager);
        return session.registerExpander("text", new TextExpander());
    }

    @Override
    public void execute(Jist jist) throws Exception {
        JistSession session = jist.getSession();
        String source = createJavaSource(jist);

        _classFactory = new JavaClassFactory(session);
        boolean compiled = _classFactory.compile(session.getClassName(), source);

        if (compiled) {
            runJist(jist);
        }
    }
}
