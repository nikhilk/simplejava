// JistOptions.java
// jist/app
//

package jist;

import java.io.*;
import java.util.*;
import joptsimple.*;

final class Options {

    private final static OptionParser _parser;
    private final static OptionSpec<String> _runtimeOption;
    private final static OptionSpec<Void> _helpOption;

    private String _error;
    private boolean _showHelp;
    private String _location;
    private String _runtime;

    static {
        _parser = new OptionParser();
        _parser.nonOptions("the file or path containing the jist code").ofType(String.class);

        _runtimeOption = _parser.accepts("runtime", "specifies the type of jist runtime to use to execute the jist")
                                .withRequiredArg()
                                .defaultsTo("snippet")
                                .ofType(String.class)
                                .describedAs("name");
        _helpOption = _parser.acceptsAll(Arrays.asList("help", "?"), "shows this help information")
                             .forHelp();
    }

    private Options() {
    }

    @SuppressWarnings("unchecked")
    public static Options fromArguments(String[] args) {
        Options options = new Options();

        try {
            OptionSet parsedOptions = _parser.parse(args);
            options._showHelp = parsedOptions.has(_helpOption);

            if (!options._showHelp) {
                options = new Options();

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
            }
        }
        catch (OptionException e) {
            options._error = e.getMessage();
        }

        return options;
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
