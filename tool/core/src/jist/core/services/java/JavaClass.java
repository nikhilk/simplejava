// JavaClass.java
// jist/core
//

package jist.core.services.java;

import java.io.*;
import java.net.*;
import javax.tools.*;

final class JavaClass extends SimpleJavaFileObject {

    private String _name;
    private ByteArrayOutputStream _outputStream;

    public JavaClass(String name) {
        super(URI.create("jist:///" + name + Kind.CLASS.extension), Kind.CLASS);

        _name = name;
        _outputStream = new ByteArrayOutputStream();
    }

    public byte[] getByteCode() {
        return _outputStream.toByteArray();
    }

    public String getName() {
        return _name;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return _outputStream;
    }
}
