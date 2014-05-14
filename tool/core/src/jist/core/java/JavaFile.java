// JistFile.java
// jist/core
//

package jist.core.java;

import java.net.*;
import javax.tools.*;

final class JavaFile extends SimpleJavaFileObject {

    private String _name;
    private String _content;

    public JavaFile(String name, String content) {
        super(URI.create("jist:///" + name + Kind.SOURCE.extension), Kind.SOURCE);

        _name = name;
        _content = content;
    }

    public String getName() {
        return _name;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return _content;
    }
}
