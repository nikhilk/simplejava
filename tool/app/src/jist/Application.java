// Application.java
// jist/app
//

package jist;

import java.io.*;
import java.util.*;
import jist.core.*;
import jist.core.common.*;
import jist.core.java.*;
import joptsimple.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        String code =
"package aaa;\n" +
"class bbb;\n" +
"import java.util.*;\n" +
"System.out.println(\"Hello World!\");\n" +
"System.out.println(\"jist completed...\");\n" +
"\n" +
"%text -> foo\n" +
"some text;\n" +
"%text -> bar << ---\n" +
"some more bar text\n" +
"2nd line as well\n" +
"---\n\n" +
"%text -> baz <<- END\n" +
"    blah blah blah\n" +
"    blah blue bleeEND\n" +
"%text -> buz << END\n" +
"    blah blah blah\n" +
"    blah blue bleeEND\n" +
"System.out.println(buz);\n" +
"System.out.println(this.getClass().getName());";

        parseArguments(args);

        JistClassFactory classFactory = new JavaClassFactory();
        JistRuntime runtime = new JavaRuntime(classFactory);

        JistSession session = runtime.createSession();

        JistPreprocessor preprocessor = new JistPreprocessor(session);
        String compilableCode = preprocessor.preprocessCode(code);

        Jist jist = new Jist(session, code, compilableCode);
        runtime.execute(jist);
    }

    @SuppressWarnings("unchecked")
    private static void parseArguments(String[] args) throws IOException {
        OptionParser parser = new OptionParser();
        parser.nonOptions("the file or path containing the jist code").ofType(String.class);

        OptionSpec<String> runtimeOption =
            parser.accepts("runtime", "specifies the type of jist runtime to use to execute the jist")
                  .withRequiredArg()
                  .defaultsTo("snippet")
                  .ofType(String.class)
                  .describedAs("name");
        OptionSpec<Void> helpOption =
            parser.acceptsAll(Arrays.asList("help", "?"), "shows this help information")
                  .forHelp();

        String error = null;
        boolean showHelp = false;
        try {
            OptionSet options = parser.parse(args);
            showHelp = options.has(helpOption);

            List<String> arguments = (List<String>)options.nonOptionArguments();
            if (arguments.size() > 1) {
                error = "Only a single jist file or path can be specified.";
                showHelp = true;
            }
        }
        catch (OptionException e) {
            error = e.getMessage();
            showHelp = true;
        }

        if (showHelp) {
            StringWriter helpWriter = new StringWriter();
            parser.printHelpOn(helpWriter);

            showHelp(error, helpWriter.toString());
        }
    }

    private static void showHelp(String error, String usageHelp) {
        if (error != null) {
            System.out.println(error);
            System.out.println();
        }

        System.out.println("jist is a tool to let you focus on the gist of your code.");

        System.out.println("Usage:");
        System.out.println(usageHelp);

        System.out.println("More information is available at https://github.com/nikhilk/jist");
        System.out.println();
    }
}
