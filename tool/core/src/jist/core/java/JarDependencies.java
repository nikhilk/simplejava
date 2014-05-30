// MavenModuleManager.java
// jist/core
//

package jist.core.java;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import jist.core.*;
import jist.util.*;

final class JarDependencies {

    private static final String FILE_SCHEME = "file";
    private static final String MAVEN_SCHEME = "maven";

    private static final String JIST_MODULE_IMPORT_ATTRIBUTE = "Jist-Module-Import";

    private final String _basePath;
    private final String _mavenPath;
    private final String _mavenRepositoryPath;

    private final HashSet<URI> _moduleURIs;
    private final List<Module> _modules;

    public String _classPath;
    public ClassLoader _classLoader;
    private HashSet<String> _imports;

    public JarDependencies(JistOptions options) {
        _basePath = options.getBasePath();
        _mavenPath = options.getMavenPath();
        _mavenRepositoryPath = options.getMavenRepository();

        _moduleURIs = new HashSet<URI>();
        _modules = new ArrayList<Module>();
    }

    public void addModule(URI moduleURI) throws JistErrorException {
        String scheme = moduleURI.getScheme();
        if (scheme == null) {
            // To allow for unqualified URLs, representing relative file paths.
            scheme = FILE_SCHEME;
        }

        if (!scheme.equals(MAVEN_SCHEME) && !scheme.equals(FILE_SCHEME)) {
            throw new JistErrorException("The module url must either be a local file or a maven artifact.");
        }

        boolean mavenURI = false;
        if (scheme.equals(MAVEN_SCHEME)) {
            if (!supportsMavenModules()) {
                throw new JistErrorException("Maven could not be found.");
            }

            mavenURI = true;
        }

        if (_moduleURIs.add(moduleURI)) {
            _modules.add(new Module(moduleURI, mavenURI));
        }
    }

    public ClassLoader getClassLoader() {
        if (_classLoader == null) {
            return Jist.class.getClassLoader();
        }

        return _classLoader;
    }

    public String getClassPath() {
        return _classPath;
    }

    public String[] getImports() {
        if (_imports == null) {
            return new String[0];
        }

        String[] names = new String[_imports.size()];

        _imports.toArray(names);
        Arrays.sort(names);

        return names;
    }

    private String getModuleImport(String moduleJar) {
        JarFile jar = null;

        try {
            jar = new JarFile(moduleJar);

            Manifest manifest = jar.getManifest();
            if (manifest != null) {
                Attributes attributes = manifest.getMainAttributes();
                if (attributes != null) {
                    return attributes.getValue(JIST_MODULE_IMPORT_ATTRIBUTE);
                }
            }
        }
        catch (IOException e) {
        }
        finally {
            if (jar != null) {
                try {
                    jar.close();
                }
                catch (IOException e) {
                }
            }
        }

        return null;
    }

    public void resolveModules() throws JistErrorException {
        ArrayList<String> jars = new ArrayList<String>();

        for (Module module : _modules) {
            String jar = module.resolve();
            jars.add(jar);
        }

        if (jars.size() != 0) {
            StringBuilder sb = new StringBuilder();
            URL[] urls = new URL[jars.size()];

            for (int i = 0; i < urls.length; i++) {
                String jar = jars.get(i);

                try {
                    if (i != 0) {
                        sb.append(File.pathSeparatorChar);
                    }
                    sb.append(jar);

                    URL url = new URL("jar", "", "file://" + jar + "!/");
                    urls[i] = url;

                    String moduleImport = getModuleImport(jar);
                    if (Strings.hasValue(moduleImport)) {
                        if (_imports == null) {
                            _imports = new HashSet<String>();
                        }

                        _imports.add(moduleImport);
                    }
                }
                catch (Exception e) {
                    throw new JistErrorException("Unable to load a required jar " + jar);
                }
            }

            _classPath = sb.toString();
            _classLoader = new URLClassLoader(urls);
        }
    }

    private boolean supportsMavenModules() {
        return Strings.hasValue(_mavenRepositoryPath) && Strings.hasValue(_mavenPath);
    }


    private final class Module {

        public final String _jar;
        public final String _artifact;
        public final boolean _requiresResolution;

        public Module(URI uri, boolean maven) {
            if (maven) {
                String[] pathParts = uri.getPath().split("/");
                String groupId = pathParts[1];
                String artifactId = pathParts[2];
                String version = pathParts[3];

                String path = groupId.replace('.', File.separatorChar) + File.separator +
                              artifactId + File.separator + version + File.separator +
                              artifactId + "-" + version + ".jar";

                File file = new File(_mavenRepositoryPath, path);
                _jar = file.getPath();

                _artifact = groupId + ":" + artifactId + ":" + version;
                _requiresResolution = !file.exists();
            }
            else {
                _jar = uri.getPath();
                _artifact = null;
                _requiresResolution = false;
            }
        }

        public String resolve() throws JistErrorException {
            if (_artifact != null) {
                if (_requiresResolution) {
                    String[] commandParts = new String[] {
                        _mavenPath,
                        "org.apache.maven.plugins:maven-dependency-plugin:2.8:get",
                        "-DremoteRepositories=central::default::http://repo1.maven.apache.org/maven2",
                        "-Dtransitive=false",
                        "-Dartifact=" + _artifact
                    };

                    Runtime runtime = Runtime.getRuntime();
                    try {
                        Process process = runtime.exec(commandParts);
                        process.waitFor();
                    }
                    catch (Exception e) {
                        throw new JistErrorException("Could not execute maven to resolve " + _artifact, e);
                    }

                    File file = new File(_jar);
                    if (!file.exists()) {
                        throw new JistErrorException("Could not resolve the artifact " + _artifact + " to a local jar.");
                    }
                }

                return _jar;
            }
            else {
                File file = new File(_jar);
                if (!file.isAbsolute()) {
                    file = new File(_basePath, _jar);
                }

                if (!file.exists()) {
                    throw new JistErrorException("The referenced jar " + _jar + " could not be found.");
                }

                try {
                    return file.getCanonicalPath();
                }
                catch (IOException e) {
                    throw new JistErrorException("The referenced jar " + _jar + " could not be resolved.");
                }
            }
        }
    }
}
