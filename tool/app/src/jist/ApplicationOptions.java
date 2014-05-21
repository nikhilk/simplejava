// JistOptions.java
// jist/app
//

package jist;

import java.io.*;
import java.util.*;
import jist.core.*;
import joptsimple.*;

final class ApplicationOptions extends JistOptions {

    private final static OptionParser _parser;
    private final static OptionSpec<String> _runtimeOption;
    private final static OptionSpec<String> _mavenPathOption;
    private final static OptionSpec<String> _mavenRepositoryOption;
    private final static OptionSpec<Void> _helpOption;

    private String _error;
    private boolean _showHelp;
    private String _location;
    private String _runtime;

    static {
        _parser = new OptionParser();
        _parser.nonOptions("the file or path containing the jist code").ofType(String.class);

        _runtimeOption =
            _parser.accepts("runtime", "specifies the type of jist runtime to use to execute the jist")
                   .withRequiredArg()
                   .ofType(String.class)
                   .describedAs("name")
                   .defaultsTo("snippet");

        _mavenPathOption =
            _parser.accepts("maven", "specifies the path to maven executable")
                   .withRequiredArg()
                   .ofType(String.class)
                   .describedAs("path")
                   .defaultsTo(getDefaultMavenPath());

        _mavenRepositoryOption =
            _parser.accepts("repository", "specifies the path to the local maven repository")
                   .withRequiredArg()
                   .ofType(String.class)
                   .describedAs("path")
                   .defaultsTo(getDefaultMavenRepository());

        _helpOption =
            _parser.acceptsAll(Arrays.asList("help", "?"), "shows this help information")
                   .forHelp();
    }

    private ApplicationOptions() {
    }

    @SuppressWarnings("unchecked")
    public static ApplicationOptions fromArguments(String[] args) {
        ApplicationOptions options = new ApplicationOptions();

        try {
            OptionSet parsedOptions = _parser.parse(args);
            options._showHelp = parsedOptions.has(_helpOption);

            if (!options._showHelp) {
                options = new ApplicationOptions();

                List<String> arguments = (List<String>)parsedOptions.nonOptionArguments();
                if (arguments.size() == 1) {
                    String value = arguments.get(0);
                    if (value.length() != 0) {
                        options._location = value;
                    }
                }
                else if (arguments.size() > 1) {
                    options._error = "Only a single jist file or path can be specified.";
                }

                options._runtime = parsedOptions.valueOf(_runtimeOption);

                options.setMavenPath(parsedOptions.valueOf(_mavenPathOption));
                options.setMavenRepository(parsedOptions.valueOf(_mavenRepositoryOption));
            }
        }
        catch (OptionException e) {
            options._error = e.getMessage();
        }

        return options;
    }

    private static String getDefaultMavenPath() {
        String[] pathList = System.getenv("PATH").split(File.pathSeparator);
        for (String pathPart : pathList) {
            File pathDirectory = new File(pathPart);
            File mavenFile = new File(pathDirectory, "mvn");

            if (mavenFile.exists() && mavenFile.isFile()) {
                return mavenFile.getPath();
            }
        }

        return null;
    }

    private static String getDefaultMavenRepository() {
        String path = System.getProperty("user.home");
        File homeDirectory = new File(path);
        File repoDirectory = new File(new File(homeDirectory, ".m2"), "repository");

        if (repoDirectory.exists() && repoDirectory.isDirectory()) {
            return repoDirectory.getPath();
        }

        return null;
    }

    public String getHelpContent() {
        StringWriter writer = new StringWriter();
        try {
            if (_error != null) {
                writer.write(_error);
                writer.write("\n\n");
            }

            writer.write("Usage:\n");
            _parser.printHelpOn(writer);
            writer.write("\n\n");

            writer.write("More information is available at https://github.com/nikhilk/jist");
            writer.write("\n\n");
        }
        catch (IOException e) {
        }

        return writer.toString();
    }

    public String getLocation() {
        return _location;
    }

    public String getRuntime() {
        return _runtime;
    }

    public boolean showHelp() {
        return _showHelp || (_error != null);
    }
}
