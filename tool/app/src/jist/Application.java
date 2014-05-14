// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.services.java.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        String code = "System.out.println(\"Hello World!\");";

        JistClassFactory classFactory = new JavaClassFactory();
        JistRuntime runtime = new JavaRuntime(classFactory);

        JistSession session = runtime.createSession();
        Jist jist = new Jist(session, code);

        runtime.execute(jist);
    }
}
