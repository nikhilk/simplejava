// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.java.*;
import jist.core.sources.*;

public final class Application {

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
            JistRuntime runtime = JavaRuntime.createRuntime(options);
            Jist jist = new Jist(createSource(runtime, options));

            runtime.execute(jist);
        }
        catch (JistErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}
