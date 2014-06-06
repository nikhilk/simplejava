// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.java.*;
import jist.core.java.runtimes.*;
import jist.core.jists.*;

public final class Application implements JistErrorHandler {

    private final JistRuntime _runtime;
    private final Jist _jist;

    private boolean _errors;

    private Application(ApplicationOptions options) {
        _runtime = createRuntime(options);
        _jist = createJist(_runtime, options);
    }

    private JistRuntime createRuntime(ApplicationOptions options) {
        JavaRuntime runtime;
        if (options.getRuntime().equals("eval")) {
            runtime = new JavaEvalRuntime();
        }
        else {
            runtime = new JavaSnippetRuntime();
        }

        runtime.initialize(options, this);
        return runtime;
    }

    private Jist createJist(JistRuntime runtime, ApplicationOptions options) {
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

        jist.initialize(runtime.getPreprocessor(), this);
        return jist;
    }

    public static void main(String[] args) {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        Application app = new Application(options);
        app.run();
    }

    private void run() {
        try {
            _runtime.execute(_jist);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void handleError(String location, int lineNumber, String error) {
        _errors = true;

        // TODO: Implement this
    }

    @Override
    public void handleException(Exception e) {
        _errors = true;

        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    @Override
    public boolean hasErrors() {
        return _errors;
    }
}
