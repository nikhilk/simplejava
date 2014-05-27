// StreamSource.java
// jist/core
//

package jist.core.sources;

import java.io.*;
import jist.core.*;
import jist.core.common.*;
import jist.util.*;

public final class StreamSource implements JistSource {

    private final JistSession _session;
    private final String _source;

    public StreamSource(JistSession session, String path) throws IOException {
        _session = session;

        InputStream input = System.in;
        if (path != null) {
            input = new FileInputStream(path);
        }

        _source = StreamReader.read(input);
    }

    @Override
    public String getSource(String path) throws IOException {
        if (path == null) {
            JistPreprocessor preprocessor = new JistPreprocessor(_session);
            return preprocessor.preprocessCode(_source);
        }

        return null;
    }
}
