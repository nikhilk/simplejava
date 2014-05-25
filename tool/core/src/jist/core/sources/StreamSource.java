// StreamSource.java
// jist/core
//

package jist.core.sources;

import java.io.*;
import jist.core.*;
import jist.core.common.*;

public final class StreamSource implements JistSource {

    private final JistSession _session;
    private final String _baseLocation;
    private final String _source;

    private StreamSource(JistSession session, String baseLocation, String source) {
        _session = session;
        _baseLocation = baseLocation;
        _source = source;
    }

    public static StreamSource createSource(JistSession session, String path) throws IOException {
        String baseLocation;
        if (path == null) {
            baseLocation = System.getProperty("user.dir");
        }
        else {
            File file = new File(path);
            baseLocation = file.getParent();
        }

        String source = readSource(path);

        return new StreamSource(session, baseLocation, source);
    }

    private static String readSource(String path) throws IOException {
        BufferedReader reader = null;

        try {
            InputStream input = System.in;
            if (path != null) {
                input = new FileInputStream(path);
            }

            StringWriter writer = new StringWriter();
            reader = new BufferedReader(new InputStreamReader(input));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }

            return writer.toString();
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public String getBaseLocation() {
        return _baseLocation;
    }

    @Override
    public String getMainSource() throws IOException {
        JistPreprocessor preprocessor = new JistPreprocessor(_session);
        return preprocessor.preprocessCode(_source);
    }
}
