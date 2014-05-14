// JistSession.java
// jist/core
//

package jist.core;

import java.util.*;
import jist.core.services.*;

public final class JistSession {

    private JistRuntime _runtime;
    private HashSet<String> _imports;

    public JistSession(JistRuntime runtime) {
        _runtime = runtime;
        _imports = new HashSet<String>();
    }

    public void addImport(String name) {
        _imports.add(name);
    }

    public JistRuntime getRuntime() {
        return _runtime;
    }

    public String[] getImports() {
        String[] names = new String[_imports.size()];

        _imports.toArray(names);
        Arrays.sort(names);

        return names;
    }
}
