// Jist.java
// jist/core
//

package jist.core;

import java.io.*;
import jist.util.*;

public abstract class Jist {

    private JistPreprocessor _preprocessor;

    public void initialize(JistRuntime runtime) {
        _preprocessor = runtime.getPreprocessor();
    }

    public String getSource(String name) throws IOException {
        String source = getSourceText(name);

        if (Strings.hasValue(source) && (_preprocessor != null)) {
            source = _preprocessor.preprocessSource(source);
        }

        return source;
    }

    protected abstract String getSourceText(String name) throws IOException;
}
