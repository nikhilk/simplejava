// ConsoleJist.java
// jist/core
//

package jist.core.jists;

import java.io.*;
import jist.core.*;
import jist.util.*;

public final class ConsoleJist extends Jist {

    @Override
    protected String getSourceText(String name) throws IOException {
        return StreamReader.read(System.in);
    }
}
