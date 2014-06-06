// Jist.java
// jist/core
//

package jist.core;

import java.io.*;
import jist.util.*;

/**
 * Represents the application to be compiled and executed.
 */
public abstract class Jist {

    private JistErrorHandler _errorHandler;
    private JistPreprocessor _preprocessor;

    /**
     * Creates the source unit corresponding to the specified name.
     * @param name the name of the source unit within the application.
     * @return the source unit if it exists, null otherwise.
     */
    public JistSource getSource(String name) {
        String text = null;
        try {
            text = getSourceText(name);
        }
        catch (IOException e) {
            _errorHandler.handleError(name, 0, "Error reading source. " + e.getMessage());
        }

        if (Strings.isNullOrEmpty(text)) {
            return null;
        }

        JistSource source = new JistSource(name, text);
        if (_preprocessor != null) {
            if (!source.applyPreprocessor(_preprocessor, _errorHandler)) {
                return null;
            }
        }

        return source;
    }

    /**
     * Reads the source text corresponding to the specified name.
     * @param name the name of the source unit to read (or null for the default unit).
     * @return the text of the specified source unit.
     * @throws IOException
     */
    protected abstract String getSourceText(String name) throws IOException;

    /**
     * Initializes the jist.
     * @param preprocessor the preprocessing logic to apply.
     * @param errorHandler the error handler to report errors to.
     */
    public void initialize(JistPreprocessor preprocessor, JistErrorHandler errorHandler) {
        _preprocessor = preprocessor;
        _errorHandler = errorHandler;
    }
}
