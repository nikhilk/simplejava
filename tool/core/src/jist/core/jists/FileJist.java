// FileJist.java
// jist/core
//

package jist.core.jists;

import java.io.*;
import jist.core.*;
import jist.util.*;

/**
 * Represents an application whose entire source is specified
 * in a single source file.
 */
public final class FileJist extends Jist {

    private final String _path;

    /**
     * Constructs a FileJist.
     * @param path the path of the file containing the source code.
     */
    public FileJist(String path) {
        _path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSourceText(String name) throws IOException {
        return StreamReader.read(new FileInputStream(_path));
    }
}
