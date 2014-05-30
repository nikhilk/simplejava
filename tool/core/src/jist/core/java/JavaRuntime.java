// JavaRuntime.java
// jist/core
//

package jist.core.java;

import java.io.*;
import java.net.*;
import java.util.*;
import jist.core.*;
import jist.core.java.expanders.*;
import jist.util.*;

public abstract class JavaRuntime implements JistRuntime {

    private JarDependencies _dependencies;
    private JavaClassFactory _classFactory;

    private final HashMap<String, JistExpander> _expanders;

    private final HashSet<String> _imports;
    private final HashSet<String> _staticImports;

    private String _className;
    private String _packageName;

    protected JavaRuntime() {
        _expanders = new HashMap<String, JistExpander>();
        _expanders.put("text", new TextExpander());

        _imports = new HashSet<String>();
        _staticImports = new HashSet<String>();
    }

    protected abstract String createImplementation(Jist jist) throws IOException;

    private String createJavaSource(String implementation) throws IOException {
        StringBuilder sourceBuilder = new StringBuilder();

        if (Strings.hasValue(_packageName)) {
            sourceBuilder.append("package ");
            sourceBuilder.append(_packageName);
            sourceBuilder.append(";\n\n");
        }

        for (String importedReference : getImports()) {
            sourceBuilder.append("import ");
            sourceBuilder.append(importedReference);
            sourceBuilder.append(";\n");
        }

        for (String importedReference : getStaticImports()) {
            sourceBuilder.append("import static ");
            sourceBuilder.append(importedReference);
            sourceBuilder.append(";\n");
        }

        sourceBuilder.append("\n");

        sourceBuilder.append(implementation);
        return sourceBuilder.toString();
    }

    protected <T> Class<T> getClass(String fullName) {
        return _classFactory.getClass(fullName);
    }

    protected String getClassName() {
        if (_className == null) {
            // Class name prefixed with "_" to make sure we always have a valid
            // identifier name, even if the random string begins with a digit.
            _className = "_" + Strings.randomString(8);
        }
        return _className;
    }

    protected String getFullClassName() {
        if (Strings.isNullOrEmpty(_packageName)) {
            return getClassName();
        }
        else {
            return _packageName + "." + getClassName();
        }
    }

    private String[] getImports() {
        String[] names = new String[_imports.size()];

        _imports.toArray(names);
        Arrays.sort(names);

        return names;
    }

    private String[] getStaticImports() {
        String[] names = new String[_staticImports.size()];

        _staticImports.toArray(names);
        Arrays.sort(names);

        return names;
    }

    protected abstract void runJist(Jist jist);

    @Override
    public JistRuntime addImport(String name) {
        _imports.add(name);
        return this;
    }

    @Override
    public JistRuntime addModule(URI moduleURI) throws JistErrorException {
        _dependencies.addModule(moduleURI);
        return this;
    }

    @Override
    public JistRuntime addStaticImport(String name) {
        _staticImports.add(name);
        return this;
    }

    @Override
    public void execute(Jist jist) throws Exception {
        // First create the class implementation. This will cause pragmas
        // to get processed, and collect session info.
        String implementation = createImplementation(jist);

        // Now resolve dependencies, during which course, additional information
        // may be added to the session
        _dependencies.resolveModules(this);

        // Finally generate the compilation source, with all the collected information
        String source = createJavaSource(implementation);

        boolean compiled = _classFactory.compile(_className, source);
        if (compiled) {
            runJist(jist);
        }
    }

    @Override
    public JistExpander getExpander(String name) {
        return _expanders.get(name);
    }

    @Override
    public JistPreprocessor getPreprocessor() {
        return new JavaPreprocessor(this);
    }

    @Override
    public void initialize(JistOptions options) {
        _dependencies = new JarDependencies(options);
        _classFactory = new JavaClassFactory(_dependencies);
    }

    @Override
    public JistRuntime specifyClassName(String name) throws JistErrorException {
        if (_className != null) {
            throw new JistErrorException("Class name cannot be set multiple times.");
        }

        _className = name;
        return this;
    }

    @Override
    public JistRuntime specifyPackageName(String name) throws JistErrorException {
        if (_packageName != null) {
            throw new JistErrorException("Package name cannot be set multiple times.");
        }

        _packageName = name;
        return this;
    }
}
