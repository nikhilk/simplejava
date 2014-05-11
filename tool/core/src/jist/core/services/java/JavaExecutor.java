// JavaExecutor.java
// jist/core
//

package jist.core.services.java;

import java.util.*;
import java.util.concurrent.*;
import javax.tools.*;
import jist.core.*;
import jist.core.services.*;
import org.apache.commons.lang3.*;

public final class JavaExecutor implements JistExecutor {

    private static final String JAVA_SOURCE_TEMPLATE =
"public final class %s extends jist.core.Jist {\n" +
"    @Override\n" +
"    public void execute() {\n" +
"        %s\n" +
"    }\n" +
"}";

    private String createJavaSource(String name, String code) {
        return String.format(JAVA_SOURCE_TEMPLATE, name, code);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void executeJist(String code) throws Exception {
        String className = RandomStringUtils.randomAlphabetic(8);
        String source = createJavaSource(className, code);

        List<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();
        compilationUnits.add(new JavaFile(className, source));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaClassManager classManager = new JavaClassManager(compiler);

        Callable<Boolean> compilationTask =
            compiler.getTask(null, classManager, null, null, null, compilationUnits);
        compilationTask.call();

        Class<Jist> jistClass = (Class<Jist>)classManager.getClassLoader(null)
                                                         .loadClass(className);
        Jist jist = jistClass.newInstance();
        jist.execute();
    }
}
