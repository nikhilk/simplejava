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

    private StreamSource(JistSession session, InputStream stream) throws IOException {
        _session = session;
        _source = StreamReader.read(stream);
    }

    public static StreamSource fromFile(JistSession session, String path) throws IOException {
        return new StreamSource(session, new FileInputStream(path));
    }

    public static StreamSource fromStandardInput(JistSession session) throws IOException {
        return new StreamSource(session, System.in);
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
