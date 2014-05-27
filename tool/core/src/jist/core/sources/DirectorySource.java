// DirectorySource.java
// jist/core
//

package jist.core.sources;

import java.io.*;
import jist.core.*;

public final class DirectorySource implements JistSource {

    private final static String DEFAULT_NAME = "main.jist";

    private final JistSession _session;
    private final String _path;

    public DirectorySource(JistSession session, String path) {
        _session = session;
        _path = path;
    }

    @Override
    public String getSource(String name) throws IOException {
        if (name == null) {
            name = DEFAULT_NAME;
        }

        File file = new File(_path, name);
        if (file.exists() && file.isFile()) {
            StreamSource streamSource = StreamSource.fromFile(_session, file.getAbsolutePath());
            return streamSource.getSource(null);
        }

        return null;
    }
}
