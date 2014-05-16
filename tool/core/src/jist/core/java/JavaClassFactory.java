// JavaExecutor.java
// jist/core
//

package jist.core.java;

import java.util.*;
import javax.tools.*;
import jist.core.*;

public final class JavaClassFactory implements JistClassFactory {

    @SuppressWarnings("rawtypes")
    @Override
    public Class compile(Jist jist, String source) throws Exception {
        JistSession session = jist.getSession();

        List<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();
        compilationUnits.add(new JavaFile(session.getClassName(), source));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaClassManager classManager = new JavaClassManager(compiler);

        compiler.getTask(null, classManager, null, null, null, compilationUnits)
                .call();

        return classManager.getClassLoader(null).loadClass(session.getFullName());
    }
}
