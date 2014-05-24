// Application.java
// jist/app
//

package jist;

import java.io.*;
import jist.core.*;
import jist.core.common.*;
import jist.core.java.*;
import jist.core.maven.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        ApplicationOptions options = ApplicationOptions.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        ModuleManager moduleManager = new MavenModuleManager(options);
        JistRuntime runtime = JavaRuntime.createRuntime(options.getRuntime());

        JistSession session = runtime.createSession(moduleManager);
        JistPreprocessor preprocessor = new JistPreprocessor(session);

        String code = readCode(options.getLocation());
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
