// StreamReader.java
// jist/core
//

package jist.util;

import java.io.*;

public final class StreamReader {

    private StreamReader() {
    }

    public static String read(InputStream input) throws IOException {
        BufferedReader reader = null;

        try {
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
}
