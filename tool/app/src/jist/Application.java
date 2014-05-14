// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.common.*;
import jist.core.java.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        String code = "System.out.println(\"Hello World!\");";

        JistClassFactory classFactory = new JavaClassFactory();
        JistRuntime runtime = new JavaRuntime(classFactory);

        JistSession session = runtime.createSession();
        JistPreprocessor preprocessor = new JistPreprocessor(session);

        Jist jist = new Jist(session, preprocessor.preprocessCode(code));
        runtime.execute(jist);
    }
}
