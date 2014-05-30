// FileJist.java
// jist/core
//

package jist.core.jists;

import java.io.*;
import jist.core.*;
import jist.util.*;

public final class FileJist extends Jist {

    private final String _path;

    public FileJist(String path) {
        _path = path;
    }

    @Override
    protected String getSourceText(String name) throws IOException {
        return StreamReader.read(new FileInputStream(_path));
    }
}
