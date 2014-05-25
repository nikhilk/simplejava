// JistSource.java
// jist/core
//

package jist.core;

import java.io.*;

public interface JistSource {

    public String getBaseLocation();

    public String getMainSource() throws IOException;
}
