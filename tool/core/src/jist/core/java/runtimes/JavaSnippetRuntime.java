// JavaSnippetRuntime.java
// jist/core
//

package jist.core.java.runtimes;

import jist.core.*;
import jist.core.java.*;

public final class JavaSnippetRuntime extends JavaRuntime {

    private static final String JAVA_SOURCE_TEMPLATE =
"public final class %s implements Runnable {\n" +
"    public void run() {\n" +
"        %s\n" +
"    }\n" +
"}\n";

    @Override
    protected String createImplementation(Jist jist) {
        return String.format(JAVA_SOURCE_TEMPLATE,
                             jist.getSession().getClassName(), jist.getCompilableCode());
    }

    @Override
    protected void runJist(Jist jist) {
        Class<Runnable> jistClass = getClass(jist.getSession(), jist.getSession().getFullName());

        Runnable jistInstance;
        try {
            jistInstance = jistClass.newInstance();
            jistInstance.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
