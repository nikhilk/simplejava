// JistSession.java
// jist/core
//

package jist.core;

import java.util.*;

public final class JistSession {

    private JistRuntime _runtime;
    private HashSet<String> _imports;

    private HashMap<String, JistExpander> _expanders;

    public JistSession(JistRuntime runtime) {
        _runtime = runtime;
        _imports = new HashSet<String>();

        _expanders = new HashMap<String, JistExpander>();
    }

    public void addImport(String name) {
        _imports.add(name);
    }

    public JistExpander getExpander(String name) {
        return _expanders.get(name);
    }

    public String[] getImports() {
        String[] names = new String[_imports.size()];

        _imports.toArray(names);
        Arrays.sort(names);

        return names;
    }

    public JistRuntime getRuntime() {
        return _runtime;
    }

    public void registerExpander(String name, JistExpander expander) {
        _expanders.put(name, expander);
    }
}
