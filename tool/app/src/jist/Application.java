// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.java.*;
import jist.core.java.runtimes.*;
import jist.core.jists.*;

public final class Application {

    private static JistRuntime createRuntime(ApplicationOptions options) {
        JavaRuntime runtime;
        if (options.getRuntime().equals("eval")) {
            runtime = new JavaEvalRuntime();
        }
        else {
            runtime = new JavaSnippetRuntime();
        }

        runtime.initialize(options);
        return runtime;
    }

    private static Jist createJist(JistRuntime runtime, ApplicationOptions options) throws Exception {
        String location = options.getLocation();

        Jist jist;
        if (location == null) {
            jist = new ConsoleJist();
        }
        else if (options.isSingleFileLocation()) {
            jist = new FileJist(location);
        }
        else {
            jist = new DirectoryJist(location);
        }

        return jist;
    }

    public static void main(String[] args) throws Exception {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        try {
            JistRuntime runtime = createRuntime(options);
            Jist jist = createJist(runtime, options);

            runtime.execute(jist);
        }
        catch (JistErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}
