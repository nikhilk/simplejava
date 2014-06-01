// ConsoleJist.java
// jist/core
//

package jist.core.jists;

import java.io.*;
import jist.core.*;
import jist.util.*;

/**
 * Represents an application whose entire source is specified
 * via the standard input.
 */
public final class ConsoleJist extends Jist {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSourceText(String name) throws IOException {
        return StreamReader.read(System.in);
    }
}
