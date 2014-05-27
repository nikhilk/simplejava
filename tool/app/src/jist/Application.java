// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.java.*;
import jist.core.sources.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        try {
            JistRuntime runtime = JavaRuntime.createRuntime(options.getRuntime());

            JistSession session = runtime.createSession(options);
            JistSource source = new StreamSource(session, options.getLocation());

            Jist jist = new Jist(session, source);
            runtime.execute(jist);
        }
        catch (JistErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}
