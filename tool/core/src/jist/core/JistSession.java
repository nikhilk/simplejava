// JistSession.java
// jist/core
//

package jist.core;

import java.util.*;
import jist.util.*;

public final class JistSession {

    private final JistRuntime _runtime;
    private final HashSet<String> _imports;

    private final HashMap<String, JistExpander> _expanders;

    private String _className;
    private String _packageName;

    public JistSession(JistRuntime runtime) {
        _runtime = runtime;
        _imports = new HashSet<String>();

        _expanders = new HashMap<String, JistExpander>();
    }

    public void addImport(String name) {
        _imports.add(name);
    }

    public String getClassName() {
        if (_className == null) {
            // Class name prefixed with "_" to make sure we always have a valid
            // identifier name, even if the random string begins with a digit.
            _className = "_" + Strings.randomString(8);
        }
        return _className;
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
}
