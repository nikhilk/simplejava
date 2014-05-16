// JavaRuntime.java
// jist/core
//

package jist.core.java;

import jist.core.*;
import jist.core.java.expanders.*;
import org.apache.commons.lang3.*;

public final class JavaRuntime implements JistRuntime {

    private static final String JAVA_SOURCE_TEMPLATE =
"public final class %s implements Runnable {\n" +
"    public void run() {\n" +
"        %s\n" +
"    }\n" +
"}\n";

    private JistClassFactory _classFactory;

    public JavaRuntime(JistClassFactory classFactory) {
        _classFactory = classFactory;
    }

    private String createJavaSource(Jist jist) {
        JistSession session = jist.getSession();
        StringBuilder sourceBuilder = new StringBuilder();

        String packageName = session.getPackageName();
        if (!StringUtils.isEmpty(packageName)) {
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

        String classCode = String.format(JAVA_SOURCE_TEMPLATE,
                                         session.getClassName(), jist.getCompilableCode());
        sourceBuilder.append(classCode);

        return sourceBuilder.toString();
    }

    @Override
    public JistSession createSession() {
        JistSession session = new JistSession(this);
        return session.registerExpander("text", new TextExpander());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Jist jist) throws Exception {
        String source = createJavaSource(jist);

        Class<Runnable> jistClass = (Class<Runnable>)_classFactory.compile(jist, source);
        Runnable jistInstance = jistClass.newInstance();
        jistInstance.run();
    }
}
