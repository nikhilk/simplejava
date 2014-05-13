// JistSession.java
// jist/core
//

package jist.core;

import java.util.*;

public final class JistSession {

    private HashSet<String> _imports;

    public JistSession() {
        _imports = new HashSet<String>();
    }

    public void addImport(String name) {
        _imports.add(name);
    }

    public String[] getImports() {
        String[] names = new String[_imports.size()];

        _imports.toArray(names);
        Arrays.sort(names);

        return names;
    }
}
