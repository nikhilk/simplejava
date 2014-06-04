// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.java.*;
import jist.core.java.runtimes.*;
import jist.core.jists.*;

public final class Application {

    private Application() {
    }

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

    private static Jist createJist(JistRuntime runtime, ApplicationOptions options) {
        String location = options.getLocation();

        Jist jist;
        if (location == null) {
            jist = new ConsoleJist();
        }
        else if (options.isGistLocation()) {
            jist = new GistJist(location);
        }
        else if (options.isSingleFileLocation()) {
            jist = new FileJist(location);
        }
        else {
            jist = new DirectoryJist(location);
        }

        JistPreprocessor preprocessor = runtime.getPreprocessor();
        if (preprocessor != null) {
            jist.addPreprocessor(preprocessor);
        }

        return jist;
    }

    public static void main(String[] args) {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        JistRuntime runtime = createRuntime(options);
        Jist jist = createJist(runtime, options);

        try {
            runtime.execute(jist);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
