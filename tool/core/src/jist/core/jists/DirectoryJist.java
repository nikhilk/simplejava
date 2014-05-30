// DirectoryJist.java
// jist/core
//

package jist.core.jists;

import java.io.*;
import jist.core.*;
import jist.util.*;

public final class DirectoryJist extends Jist {

    private final static String DEFAULT_NAME = "main.jist";

    private final String _path;

    public DirectoryJist(String path) {
        _path = path;
    }

    @Override
    protected String getSourceText(String name) throws IOException {
        if (name == null) {
            name = DEFAULT_NAME;
        }

        File file = new File(_path, name);
        if (file.exists() && file.isFile()) {
            return StreamReader.read(new FileInputStream(file));
        }

        return null;
    }
}
