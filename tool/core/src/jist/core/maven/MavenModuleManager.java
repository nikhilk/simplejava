// MavenModuleManager.java
// jist/core
//

package jist.core.maven;

import java.io.*;
import java.net.*;
import java.util.*;
import jist.core.*;
import jist.util.*;

public final class MavenModuleManager implements ModuleManager {

    private static final String FILE_SCHEME = "file";
    private static final String MAVEN_SCHEME = "maven";

    private final String _mavenPath;
    private final String _mavenRepositoryPath;

    private final HashSet<URI> _moduleURIs;
    private final List<Module> _modules;

    public MavenModuleManager(JistOptions options) {
        _mavenPath = options.getMavenPath();
        _mavenRepositoryPath = options.getMavenRepository();

        _moduleURIs = new HashSet<URI>();
        _modules = new ArrayList<Module>();
    }

    private List<String> resolveModules() throws JistErrorException {
        ArrayList<String> jars = new ArrayList<String>();

        for (Module module : _modules) {
            String jar = module.resolve();
            jars.add(jar);
        }

        return jars;
    }

    private boolean supportsMavenModules() {
        return Strings.hasValue(_mavenRepositoryPath) && Strings.hasValue(_mavenPath);
    }

    @Override
    public void addModule(URI moduleURI) throws JistErrorException {
        String scheme = moduleURI.getScheme();
        boolean mavenURI = false;

        if (!scheme.equals(MAVEN_SCHEME) && !scheme.equals(FILE_SCHEME)) {
            throw new JistErrorException("The module url must either be a local file or a maven artifact.");
        }

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

    @Override
    public ClassLoader getClassLoader() throws JistErrorException {
        List<String> jars = resolveModules();

        URL[] urls = new URL[jars.size()];
        for (int i = 0; i < urls.length; i++) {
            try {
                urls[i] = new URL("jar", "", "file://" + jars.get(i) + "!/");
            }
            catch (MalformedURLException e) {
            }
        }

        return new URLClassLoader(urls);
    }

    @Override
    public String getClassPath() throws JistErrorException {
        List<String> jars = resolveModules();

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String jar : jars) {
            if (!first) {
                sb.append(File.pathSeparatorChar);
            }

            sb.append(jar);
            first = false;
        }

        return sb.toString();
    }


    private final class Module {

        public String _jar;
        public String _artifact;
        public boolean _requiresResolution;

        public Module(URI uri, boolean maven) {
            _jar = uri.getPath();

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
        }

        public String resolve() throws JistErrorException {
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
            else {
                File file = new File(_jar);
                if (!file.exists()) {
                    throw new JistErrorException("The referenced jar " + _jar + " could not be found.");
                }
            }

            return _jar;
        }
    }
}
