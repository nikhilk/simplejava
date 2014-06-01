// ClassFactory.java
// jist/core
//

package jist.core.java;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.tools.*;
import jist.core.*;
import jist.util.*;

final class ClassFactory extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private final JavaCompiler _compiler;
    private final List<String> _options;
    private final ClassLoader _classLoader;
    private final List<JavaFile> _compilationUnits;

    private final HashMap<String, ClassFile> _classFiles;
    private final HashMap<String, Class<?>> _classes;

    private ClassFactory(JavaCompiler compiler, List<String> options,
                         ClassLoader dependenciesClassLoader,
                         Map<String, String> sources) {
        super(compiler.getStandardFileManager(null, null, null));
        _compiler = compiler;
        _options = options;
        _classLoader = new CompilationClassLoader(dependenciesClassLoader);

        _compilationUnits = new ArrayList<JavaFile>();
        for (Map.Entry<String, String> sourceEntry : sources.entrySet()) {
            _compilationUnits.add(new JavaFile(sourceEntry.getKey(), sourceEntry.getValue()));
        }

        _classFiles = new HashMap<String, ClassFile>();
        _classes = new HashMap<String, Class<?>>();
    }

    public static ClassFactory create(JarDependencies dependencies, Map<String, String> sources)
        throws JistErrorException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = new ArrayList<String>();

        String classPath = dependencies.getClassPath();
        if (Strings.hasValue(classPath)) {
            options.add("-classpath");
            options.add(classPath);
        }

        return new ClassFactory(compiler, options, dependencies.getClassLoader(), sources);
    }

    public ClassLoader getClassLoader() {
        boolean compiled =
            _compiler.getTask(null, this, null, _options, null, _compilationUnits)
                     .call();

        if (compiled) {
            return _classLoader;
        }

        return null;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return _classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) throws IOException {
        ClassFile classFile = new ClassFile(className);
        _classFiles.put(className, classFile);

        return classFile;
    }


    private final class CompilationClassLoader extends SecureClassLoader {

        public CompilationClassLoader(ClassLoader dependenciesClassLoader) {
            super(dependenciesClassLoader);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Class<?> c = _classes.get(name);

            if (c == null) {
                ClassFile classFile = _classFiles.get(name);

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

    private final class JavaFile extends SimpleJavaFileObject {

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

    private final class ClassFile extends SimpleJavaFileObject {

        private String _name;
        private ByteArrayOutputStream _outputStream;

        public ClassFile(String name) {
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
}
