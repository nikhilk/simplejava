// JistClassManager.java
// jist/core
//

package jist.core.java;

import java.io.*;
import java.security.*;
import java.util.*;
import javax.tools.*;

final class JavaClassManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private final ClassLoader _moduleClassLoader;
    private final HashMap<String, JavaClass> _classFiles;
    private final HashMap<String, Class<?>> _classes;

    public JavaClassManager(StandardJavaFileManager fileManager, ClassLoader moduleClassLoader) {
        super(fileManager);
        _moduleClassLoader = moduleClassLoader;
        _classFiles = new HashMap<String, JavaClass>();
        _classes = new HashMap<String, Class<?>>();
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return new JistClassLoader(_moduleClassLoader);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) throws IOException {
        JavaClass classFile = new JavaClass(className);
        _classFiles.put(className, classFile);

        return classFile;
    }


    private final class JistClassLoader extends SecureClassLoader {

        public JistClassLoader(ClassLoader moduleClassLoader) {
            super(moduleClassLoader);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Class<?> c = _classes.get(name);

            if (c == null) {
                JavaClass classFile = _classFiles.get(name);

                if (classFile != null) {
                    byte[] bytes = classFile.getByteCode();

                    c = defineClass(name, bytes, 0, bytes.length);
                    resolveClass(c);

                    _classes.put(name, c);
                }
            }

            if (c == null) {
                throw new ClassNotFoundException(name);
            }

            return c;
        }
    }
}
