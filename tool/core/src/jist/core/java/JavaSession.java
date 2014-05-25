// JavaSession.java
// jist/core
//

package jist.core.java;

import jist.core.*;

final class JavaSession extends JistSession {

    private final JavaClassFactory _classFactory;

    public JavaSession(JistRuntime runtime, JistDependencies dependencies) {
        super(runtime, dependencies);
        _classFactory = new JavaClassFactory(this);
    }

    public JavaClassFactory getClassFactory() {
        return _classFactory;
    }
}
