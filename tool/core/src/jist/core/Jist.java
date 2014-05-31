// Jist.java
// jist/core
//

package jist.core;

import java.io.*;

public abstract class Jist {

    public JistSource getSource(String name) throws IOException {
        String text = getSourceText(name);
        return new JistSource(text);
    }

    protected abstract String getSourceText(String name) throws IOException;
}
