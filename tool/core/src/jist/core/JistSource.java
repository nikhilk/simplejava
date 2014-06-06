// JistSource.java
// jist/core
//

package jist.core;

import java.util.*;
import jist.util.*;

/**
 * Represents a unit of source code within a Jist.
 */
public final class JistSource {

    private final HashMap<String, Object> _metadata;
    private final String _name;
    private final String _text;
    private String _processedText;

    /**
     * Constructs a JistSource object.
     * @param name the name of the associated source unit.
     * @param text the associated source text.
     */
    public JistSource(String name, String text) {
        if (text == null) {
            text = "";
        }

        _name = name;
        _text = text;
        _metadata = new HashMap<String, Object>();

    }

    /**
     * Applies the specified preprocessor on the source code.
     * @param preprocessor the preprocessor containing preprocessing logic.
     * @param errorHandler the error handler to report errors to.
     * @return true if the preprocessor was applied successfully, false otherwise.
     */
    public boolean applyPreprocessor(JistPreprocessor preprocessor, JistErrorHandler errorHandler) {
        _processedText = preprocessor.process(this, errorHandler);
        return Strings.hasValue(_processedText);
    }

    /**
     * Gets a metadata value.
     * @param name the name of the metadata to lookup.
     * @return the value of the named metadata, null if it doesn't exist.
     */
    public Object getMetadata(String name) {
        return _metadata.get(name);
    }

    /**
     * Gets the name of the associated source unit.
     * @return the name of the associated source unit.
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the preprocessed text. Returns the original text if no preprocessor
     * has been applied.
     * @return the preprocessed text.
     */
    public String getProcessedText() {
        if (_processedText == null) {
            return _text;
        }
        return _processedText;
    }

    /**
     * The original, un-processed text associated with this JistSource.
     * @return
     */
    public String getText() {
        return _text;
    }

    /**
     * Sets a metadata value.
     * @param name the name of the metadata to set.
     * @param value the value of the named metadata.
     */
    public void setMetadata(String name, Object value) {
        _metadata.put(name, value);
    }
}
