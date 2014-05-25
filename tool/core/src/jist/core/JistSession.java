// JistSession.java
// jist/core
//

package jist.core;

import java.net.*;
import java.util.*;
import jist.util.*;

public abstract class JistSession {

    private final JistRuntime _runtime;
    private final JistDependencies _dependencies;
    private final HashMap<String, JistExpander> _expanders;

    private String _className;
    private String _packageName;
    private String _classPath;
    private ClassLoader _classLoader;

    private final HashSet<String> _imports;
    private final HashSet<String> _staticImports;

    protected JistSession(JistRuntime runtime, JistDependencies dependencies) {
        _runtime = runtime;
        _dependencies = dependencies;
        _expanders = new HashMap<String, JistExpander>();

        _imports = new HashSet<String>();
        _staticImports = new HashSet<String>();
    }

    public JistSession addImport(String name) {
        _imports.add(name);
        return this;
    }

    public void addModule(URI moduleURI) throws JistErrorException {
        _dependencies.addModule(moduleURI);
    }

    public JistSession addStaticImport(String name) {
        _staticImports.add(name);
        return this;
    }

    public ClassLoader getClassLoader() {
        if (_classLoader == null) {
            return Jist.class.getClassLoader();
        }

        return _classLoader;
    }

    public String getClassName() {
        if (_className == null) {
            // Class name prefixed with "_" to make sure we always have a valid
            // identifier name, even if the random string begins with a digit.
            _className = "_" + Strings.randomString(8);
        }
        return _className;
    }

    public String getClassPath() {
        return _classPath;
    }

    public JistDependencies getDependencies() {
        return _dependencies;
    }

    public JistExpander getExpander(String name) {
        return _expanders.get(name);
    }

    public String getFullName() {
        String packageName = getPackageName();

        if (packageName == null) {
            return getClassName();
        }
        else {
            return packageName + "." + getClassName();
        }
    }

    public String[] getImports() {
        String[] names = new String[_imports.size()];

        _imports.toArray(names);
        Arrays.sort(names);

        return names;
    }

    public String getPackageName() {
        return _packageName;
    }

    public JistRuntime getRuntime() {
        return _runtime;
    }

    public String[] getStaticImports() {
        String[] names = new String[_staticImports.size()];

        _staticImports.toArray(names);
        Arrays.sort(names);

        return names;
    }

    public JistSession registerExpander(String name, JistExpander expander) {
        _expanders.put(name, expander);
        return this;
    }

    public JistSession specifyClassName(String name) throws JistErrorException {
        if (_className != null) {
            throw new JistErrorException("Class name cannot be set multiple times.");
        }

        _className = name;
        return this;
    }

    public JistSession specifyPackageName(String name) throws JistErrorException {
        if (_packageName != null) {
            throw new JistErrorException("Package name cannot be set multiple times.");
        }

        _packageName = name;
        return this;
    }

    public JistSession useDependencies(String classPath, ClassLoader classLoader) {
        _classPath = classPath;
        _classLoader = classLoader;

        return this;
    }
}
