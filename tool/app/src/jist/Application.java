// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.java.*;
import jist.core.java.runtimes.*;
import jist.core.sources.*;

public final class Application {

    private static JistRuntime createRuntime(ApplicationOptions options) {
        JavaRuntime runtime;
        if (options.getRuntime().equals("eval")) {
            runtime = new JavaEvalRuntime();
        }
        else {
            runtime = new JavaSnippetRuntime();
        }

        return runtime;
    }

    private static JistSource createSource(JistRuntime runtime, ApplicationOptions options) throws Exception {
        String location = options.getLocation();
        if (location == null) {
            return StreamSource.fromStandardInput(runtime);
        }
        else if (options.isSingleFileLocation()) {
            return StreamSource.fromFile(runtime, location);
        }
        else {
            return new DirectorySource(runtime, location);
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        try {
            JistRuntime runtime = createRuntime(options);
            JistSource source = createSource(runtime, options);
            Jist jist = new Jist(source);

            runtime.execute(jist);
        }
        catch (JistErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}
