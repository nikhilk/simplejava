// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.services.*;
import jist.core.services.java.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        String code = "System.out.println(\"Hello World!\");";

        JistClassFactory classFactory = new JavaClassFactory();
        JistRuntime runtime = new JavaRuntime(classFactory);

        Jist jist = new Jist(code);
        JistSession session = new JistSession();

        runtime.initializeSession(session);
        runtime.execute(jist, session);
    }
}
