// JistSource.java
// jist/core
//

package jist.core;

import java.io.*;
import java.util.*;

public final class JistSource {

    private final HashMap<String, Object> _metadata;
    private final String _text;
    private String _processedText;

    public JistSource(String text) {
        if (text == null) {
            text = "";
        }

        _text = text;
        _metadata = new HashMap<String, Object>();

    }

    public void applyPreprocessor(JistPreprocessor preprocessor) throws IOException {
        _processedText = preprocessor.preprocess(this);
    }

    public Object getMetadata(String name) {
        return _metadata.get(name);
    }

    public void setMetadata(String name, Object value) {
        _metadata.put(name, value);
    }

    public String getProcessedText() {
        return _processedText;
    }

    public String getText() {
        return _text;
    }
}
