// JavaRuntime.java
// jist/core
//

package jist.core.services.java;

import jist.core.*;
import jist.core.services.*;
import org.apache.commons.lang3.*;

public final class JavaRuntime implements JistRuntime {

    private static final String JAVA_SOURCE_TEMPLATE =
"public final class %s implements Runnable {\n" +
"    public void run() {\n" +
"        %s\n" +
"    }\n" +
"}";

    private JistClassFactory _classFactory;

    public JavaRuntime(JistClassFactory classFactory) {
        _classFactory = classFactory;
    }

    private String createJavaSource(String name, String code) {
        return String.format(JAVA_SOURCE_TEMPLATE, name, code);
    }

    @Override
    public JistSession createSession() {
        return new JistSession(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Jist jist) throws Exception {
        String className = RandomStringUtils.randomAlphabetic(8);
        String source = createJavaSource(className, jist.getCode());

        Class<Runnable> jistClass = (Class<Runnable>)_classFactory.compile(className, source);
        Runnable jistInstance = jistClass.newInstance();
        jistInstance.run();
    }
}
