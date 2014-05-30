// JavaPreprocessor.java
// jist/core
//

package jist.core.java;

import java.net.*;
import java.util.regex.*;
import jist.core.*;

final class JavaPreprocessor extends JistPreprocessor {

    private final static Pattern pragmaPattern =
        Pattern.compile("^(?<pragma>(import)|(class)|(package)|(require))\\s+(?<name>[a-zA-Z0-9_\\.\\*:/]+)\\s*;$");

    private final JavaRuntime _runtime;

    public JavaPreprocessor(JavaRuntime runtime) {
        super(runtime);
        _runtime = runtime;
    }

    @Override
    protected String processLine(String line) throws JistErrorException {
        Matcher matcher = pragmaPattern.matcher(line.trim());
        if (matcher.matches()) {
            processPragma(matcher.group("pragma"), matcher.group("name"));
            return null;
        }

        return super.processLine(line);
    }

    private void processPragma(String pragma, String name) throws JistErrorException {
        // TODO: Some useful validation...

        if (pragma.equals("import")) {
            _runtime.addImport(name);
        }
        else if (pragma.equals("class")) {
            _runtime.specifyClassName(name);
        }
        else if (pragma.equals("package")) {
            _runtime.specifyPackageName(name);
        }
        else if (pragma.equals("require")) {
            try {
                URI moduleURI = new URI(name);
                _runtime.addModule(moduleURI);
            }
            catch (URISyntaxException e) {
                throw new JistErrorException("Invalid module URL syntax.", e);
            }
        }
    }
}
