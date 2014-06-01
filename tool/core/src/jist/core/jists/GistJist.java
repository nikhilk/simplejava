// GistJist.java
// jist/core
//

package jist.core.jists;

import java.io.*;
import java.net.*;
import java.util.*;
import jist.core.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * Represents a Jist authored as a GitHub gist.
 */
public final class GistJist extends Jist {

    private final String _id;
    private Map<String, String> _gistFiles;

    /**
     * Creates and initializes an instance of GistJist.
     * @param id the unique id of the gist.
     */
    public GistJist(String id) {
        _id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSourceText(String name) throws IOException {
        if (_gistFiles == null) {
            _gistFiles = loadGistFiles();
        }

        if (name == null) {
            name = Jist.DEFAULT_NAME;
        }

        // Since GitHub doesn't allow '/' in gist file names, we'll use
        // a convention where ':' in the file name corresponds to the path
        // separator.
        name = name.replace(File.separatorChar, ':');

        return _gistFiles.get(name);
    }

    @SuppressWarnings("rawtypes")
    private Map<String, String> loadGistFiles() {
        HashMap<String, String> files = new HashMap<String, String>();

        try {
            URL url = new URL("https://api.github.com/gists/" + _id);
            Reader reader = new InputStreamReader(url.openStream());

            JSONParser parser = new JSONParser();
            Object parsedValue = parser.parse(reader);

            if (parsedValue instanceof JSONObject) {
                JSONObject gist = (JSONObject)parsedValue;
                JSONObject gistFiles = (JSONObject)gist.get("files");

                for (Object entry : gistFiles.entrySet()) {
                    Map.Entry mapEntry = (Map.Entry)entry;
                    JSONObject info = (JSONObject)mapEntry.getValue();

                    String name = (String)mapEntry.getKey();
                    String content = (String)info.get("content");

                    files.put(name, content);
                }
            }
        }
        catch (Exception e) {
            // TODO: JistErrorException
        }

        return files;
    }
}
