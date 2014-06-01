// Jist.java
// jist/core
//

package jist.core;

import java.io.*;

/**
 * Represents the application to be compiled and executed.
 */
public abstract class Jist {

    protected final static String DEFAULT_NAME = "main.java";

    private JistPreprocessor _preprocessor;

    /**
     * Adds a preprocessor implementation to preprocess sources.
     * @param preprocessor the preprocessing logic to apply.
     */
    public void addPreprocessor(JistPreprocessor preprocessor) {
        _preprocessor = preprocessor;
    }

    /**
     * Creates the source unit corresponding to the specified name.
     * @param name the name of the source unit within the application.
     * @return the source unit if it exists, null otherwise.
     * @throws IOException
     */
    public JistSource getSource(String name) throws IOException {
        String text = getSourceText(name);
        JistSource source = new JistSource(name, text);

        if (_preprocessor != null) {
            source.applyPreprocessor(_preprocessor);
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
}
