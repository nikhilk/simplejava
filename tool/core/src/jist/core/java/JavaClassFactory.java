// JavaExecutor.java
// jist/core
//

package jist.core.java;

import java.util.*;
import javax.tools.*;
import jist.core.*;
import jist.util.*;

final class JavaClassFactory {

    private final ModuleManager _moduleManager;
    private JavaClassManager _classManager;

    public JavaClassFactory(ModuleManager moduleManager) {
        _moduleManager = moduleManager;
    }

    public void compile(String name, String source) throws Exception {
        List<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();
        compilationUnits.add(new JavaFile(name, source));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = new ArrayList<String>();

        String classPath = _moduleManager.getClassPath();
        if (Strings.hasValue(classPath)) {
            options.add("-classpath");
            options.add(classPath);
        }

        _classManager = new JavaClassManager(compiler.getStandardFileManager(null, null, null),
                                             _moduleManager.getClassLoader());
        compiler.getTask(null, _classManager, null, options, null, compilationUnits)
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
