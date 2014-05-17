// Application.java
// jist/app
//

package jist;

import java.io.*;
import jist.core.*;
import jist.core.common.*;
import jist.core.java.runtimes.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        Options options = Options.fromArguments(args);
        if (options.showHelp()) {
            System.out.println(options.getHelpContent());
            return;
        }

        String code = readCode(options.getLocation());

        JistRuntime runtime = new JavaSnippetRuntime();
        JistSession session = runtime.createSession();

        JistPreprocessor preprocessor = new JistPreprocessor(session);
        String compilableCode = preprocessor.preprocessCode(code);

        Jist jist = new Jist(session, code, compilableCode);
        runtime.execute(jist);
    }

    private static String readCode(String location) throws IOException {
        InputStream input = System.in;
        if (location != null) {
            input = new FileInputStream(location);
        }

        StringWriter writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.write('\n');
        }

        reader.close();

        return writer.toString();
    }
}
