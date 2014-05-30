// JistPreprocessor.java
// jist/core
//

package jist.core;

import java.io.*;

public interface JistPreprocessor {

    public String preprocessSource(String source) throws IOException;
}
