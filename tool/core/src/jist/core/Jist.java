// Jist.java
// jist/core
//

package jist.core;

import java.io.*;

public final class Jist {

    private final JistSource _source;

    public Jist(JistSource source) {
        _source = source;
    }

    public String getSource(String name) throws IOException {
        return _source.getSource(name);
    }
}
