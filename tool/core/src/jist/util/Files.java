// Files.java
// jist/core
//

package jist.util;

import java.io.*;

public final class Files {

    private Files() {
    }

    public static String getPath(File f) {
        try {
            return f.getCanonicalPath();
        }
        catch (IOException e) {
            return f.getAbsolutePath();
        }
    }
}
