// Jist.java
// jist/core
//

package jist.core;

import java.io.*;
import jist.core.common.*;
import jist.util.*;

public abstract class Jist {

    private JistPreprocessor _preprocessor;

    public void initialize(JistPreprocessor preprocessor) {
        _preprocessor = preprocessor;
    }

    public String getSource(String name) throws IOException {
        String text = getSourceText(name);

        if (Strings.hasValue(text) && (_preprocessor != null)) {
            text = _preprocessor.preprocessCode(text);
        }

        return text;
    }

    protected abstract String getSourceText(String name) throws IOException;
}
