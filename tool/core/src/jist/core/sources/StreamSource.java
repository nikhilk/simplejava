// StreamSource.java
// jist/core
//

package jist.core.sources;

import java.io.*;
import jist.core.*;
import jist.core.common.*;
import jist.util.*;

public final class StreamSource implements JistSource {

    private final JistRuntime _runtime;
    private final String _source;

    private StreamSource(JistRuntime runtime, InputStream stream) throws IOException {
        _runtime = runtime;
        _source = StreamReader.read(stream);
    }

    public static StreamSource fromFile(JistRuntime runtime, String path) throws IOException {
        return new StreamSource(runtime, new FileInputStream(path));
    }

    public static StreamSource fromStandardInput(JistRuntime runtime) throws IOException {
        return new StreamSource(runtime, System.in);
    }

    @Override
    public String getSource(String path) throws IOException {
        if (path == null) {
            JistPreprocessor preprocessor = new JistPreprocessor(_runtime);
            return preprocessor.preprocessCode(_source);
        }

        return null;
    }
}
