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
    protected String processLine(JistSource source, String line) throws JistErrorException {
        Matcher matcher = pragmaPattern.matcher(line.trim());
        if (matcher.matches()) {
            processPragma(source, matcher.group("pragma"), matcher.group("name"));
            return null;
        }

        return super.processLine(source, line);
    }

    private void processPragma(JistSource source, String pragma, String name) throws JistErrorException {
        // TODO: Some useful validation...

        if (pragma.equals("import")) {
            JavaSource.addImport(source, name);
        }
        else if (pragma.equals("class")) {
            JavaSource.setClassName(source, name);
        }
        else if (pragma.equals("package")) {
            JavaSource.setPackageName(source, name);
        }
        else if (pragma.equals("require")) {
            try {
                URI moduleURI = new URI(name);
                String moduleImport = _runtime.addModule(moduleURI);

                if (moduleImport != null) {
                    JavaSource.addModule(source, moduleImport);
                }
            }
            catch (URISyntaxException e) {
                throw new JistErrorException("Invalid module URL syntax.", e);
            }
        }
    }
}
