// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.common.*;
import jist.core.java.*;
import jist.core.sources.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        JistRuntime runtime = JavaRuntime.createRuntime(options.getRuntime());
        JistSession session = runtime.createSession(options);

        JistSource source = StreamSource.createSource(options.getLocation());
        String code = source.getMainSource();

        JistPreprocessor preprocessor = new JistPreprocessor(session);
        String compilableCode = preprocessor.preprocessCode(code);

        Jist jist = new Jist(session, code, compilableCode);

        try {
            runtime.execute(jist);
        }
        catch (JistErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}
