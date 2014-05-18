// JavaRuntime.java
// jist/core
//

package jist.core.java;

import jist.core.*;
import jist.core.java.expanders.*;
import jist.util.*;

public abstract class JavaRuntime implements JistRuntime {

    private JavaClassFactory _classFactory;

    protected JavaRuntime() {
        _classFactory = new JavaClassFactory();
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

        sourceBuilder.append("\n");

        String classCode = createImplementation(jist);
        sourceBuilder.append(classCode);

        return sourceBuilder.toString();
    }

    protected <T> Class<T> getClass(String fullName) {
        return _classFactory.getClass(fullName);
    }

    protected abstract void runJist(Jist jist);
    @Override
    public JistSession createSession() {
        JistSession session = new JistSession(this);
        return session.registerExpander("text", new TextExpander());
    }

    @Override
    public void execute(Jist jist) throws Exception {
        String source = createJavaSource(jist);

        _classFactory.compile(jist.getSession().getClassName(), source);
        runJist(jist);
    }
}
