// JistClassManager.java
// jist/core
//

package jist.core.services.java;

import java.io.*;
import java.security.*;
import javax.tools.*;

@SuppressWarnings("rawtypes")
final class JavaClassManager extends ForwardingJavaFileManager {

    private JavaClass _class;

    @SuppressWarnings("unchecked")
    public JavaClassManager(JavaCompiler compiler) {
        super(compiler.getStandardFileManager(null, null, null));
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return new JistClassLoader();
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) throws IOException {
        _class = new JavaClass(className);
        return _class;
    }

    private final class JistClassLoader extends SecureClassLoader {

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = _class.getByteCode();
            return super.defineClass(name, bytes, 0, bytes.length);
        }
    }
}
