// Jist.java
// jist/core
//

package jist.core;

import java.io.*;
import jist.util.*;

public abstract class Jist {

    public String getSource(String name, JistPreprocessor preprocessor) throws IOException {
        String source = getSourceText(name);

        if (Strings.hasValue(source) && (preprocessor != null)) {
            source = preprocessor.preprocessSource(source);
        }

        return source;
    }

    protected abstract String getSourceText(String name) throws IOException;
}
