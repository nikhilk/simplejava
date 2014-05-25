// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.common.*;
import jist.core.java.*;
import jist.core.maven.*;
import jist.core.sources.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        JistSource source = StreamSource.createSource(options.getLocation());
        JistRuntime runtime = JavaRuntime.createRuntime(options.getRuntime());
        ModuleManager moduleManager = new MavenModuleManager(options);

        JistSession session = runtime.createSession(moduleManager);
        JistPreprocessor preprocessor = new JistPreprocessor(session);

        String code = source.getMainSource();
        String compilableCode = preprocessor.preprocessCode(code);

        Jist jist = new Jist(session, code, compilableCode);

        try {
            moduleManager.resolveModules(session);
            runtime.execute(jist);
        }
        catch (JistErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}
