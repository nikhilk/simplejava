// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.java.*;
import jist.core.sources.*;

public final class Application {

    private static JistSource createSource(JistSession session, ApplicationOptions options) throws Exception {
        String location = options.getLocation();
        if (location == null) {
            return StreamSource.fromStandardInput(session);
        }
        else if (options.isSingleFileLocation()) {
            return StreamSource.fromFile(session, location);
        }
        else {
            return new DirectorySource(session, location);
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        try {
            JistRuntime runtime = JavaRuntime.createRuntime(options.getRuntime());

            JistSession session = runtime.createSession(options);
            JistSource source = createSource(session, options);

            Jist jist = new Jist(session, source);
            runtime.execute(jist);
        }
        catch (JistErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}
