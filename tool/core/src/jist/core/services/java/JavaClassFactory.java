// JavaExecutor.java
// jist/core
//

package jist.core.services.java;

import java.util.*;
import javax.tools.*;
import jist.core.services.*;

public final class JavaClassFactory implements JistClassFactory {

    @SuppressWarnings("rawtypes")
    @Override
    public Class compile(String className, String source) throws Exception {
        List<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();
        compilationUnits.add(new JavaFile(className, source));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaClassManager classManager = new JavaClassManager(compiler);

        compiler.getTask(null, classManager, null, null, null, compilationUnits)
                .call();

        return classManager.getClassLoader(null).loadClass(className);
    }
}
