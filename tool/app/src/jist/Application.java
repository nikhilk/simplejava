// Application.java
// jist/app
//

package jist;

import java.io.*;
import jist.core.*;
import jist.core.common.*;
import jist.core.java.runtimes.*;
import jist.core.maven.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        Options options = Options.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        ModuleManager modules = new MavenModuleManager();

        JistRuntime runtime;
        if (options.getRuntime().equals("eval")) {
            runtime = new JavaEvalRuntime();
        }
        else {
            runtime = new JavaSnippetRuntime();
        }

        JistSession session = runtime.createSession(modules);
        JistPreprocessor preprocessor = new JistPreprocessor(session);

        String code = readCode(options.getLocation());
        String compilableCode = preprocessor.preprocessCode(code);

        Jist jist = new Jist(session, code, compilableCode);
        runtime.execute(jist);
    }

    private static String readCode(String location) throws IOException {
        BufferedReader reader = null;

        try {
            InputStream input = System.in;
            if (location != null) {
                input = new FileInputStream(location);
            }

            StringWriter writer = new StringWriter();
            reader = new BufferedReader(new InputStreamReader(input));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }

            return writer.toString();
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
