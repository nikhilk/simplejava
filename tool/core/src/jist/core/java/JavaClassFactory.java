// JavaExecutor.java
// jist/core
//

package jist.core.java;

import java.util.*;
import javax.tools.*;

final class JavaClassFactory {

    private JavaCompiler _compiler;
    private JavaClassManager _classManager;

    public JavaClassFactory() {
        _compiler = ToolProvider.getSystemJavaCompiler();
        _classManager = new JavaClassManager(_compiler);
    }

    public void compile(String name, String source) throws Exception {
        List<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();
        compilationUnits.add(new JavaFile(name, source));

        _compiler.getTask(null, _classManager, null, null, null, compilationUnits)
                 .call();
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getClass(String fullName) {
        try {
            return (Class<T>)_classManager.getClassLoader(null).loadClass(fullName);
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }
}
