// DirectoryJist.java
// jist/core
//

package jist.core.jists;

import java.io.*;
import jist.core.*;
import jist.util.*;

/**
 * Represents an application whose source is contained in one or
 * more files within the specified directory.
 */
public final class DirectoryJist extends Jist {

    private final String _path;

    /**
     * Constructs a DirectoryJist.
     * @param path the path of the directory containing the sources.
     */
    public DirectoryJist(String path) {
        _path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSourceText(String name) throws IOException {
        if (name == null) {
            name = Jist.DEFAULT_NAME;
        }

        File file = new File(_path, name);
        if (file.exists() && file.isFile()) {
            return StreamReader.read(new FileInputStream(file));
        }

        return null;
    }
}
