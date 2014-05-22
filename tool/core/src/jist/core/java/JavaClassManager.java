// JistClassManager.java
// jist/core
//

package jist.core.java;

import java.io.*;
import java.security.*;
import javax.tools.*;

final class JavaClassManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private ClassLoader _moduleClassLoader;
    private JavaClass _class;

    public JavaClassManager(StandardJavaFileManager fileManager, ClassLoader moduleClassLoader) {
        super(fileManager);
        _moduleClassLoader = moduleClassLoader;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return new JistClassLoader(_moduleClassLoader);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) throws IOException {
        _class = new JavaClass(className);
        return _class;
    }

    private final class JistClassLoader extends SecureClassLoader {

        public JistClassLoader(ClassLoader moduleClassLoader) {
            super(moduleClassLoader);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = _class.getByteCode();
            return super.defineClass(name, bytes, 0, bytes.length);
        }
    }
}
