// MavenModuleManager.java
// jist/core
//

package jist.core.maven;

import java.io.File;
import java.net.*;
import java.util.*;
import jist.core.*;
import jist.util.Strings;

public final class MavenModuleManager implements ModuleManager {

    private static final String FILE_SCHEME = "file";
    private static final String MAVEN_SCHEME = "maven";

    private final String _mavenPath;
    private final String _mavenRepositoryPath;

    private final HashSet<URI> _modules;
    private final List<Artifact> _artifacts;

    private List<String> _resolvedModules;

    public MavenModuleManager(JistOptions options) {
        _mavenPath = options.getMavenPath();
        _mavenRepositoryPath = options.getMavenRepository();

        _modules = new HashSet<URI>();
        _artifacts = new ArrayList<Artifact>();
    }

    private List<String> resolveModules() {
        if (_resolvedModules == null) {
            ArrayList<String> jars = new ArrayList<String>();

            for (Artifact artifact : _artifacts) {
                String path = artifact.resolve(_mavenRepositoryPath);
                jars.add(path);
            }

            _resolvedModules = jars;
        }

        return _resolvedModules;
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

        if (_modules.add(moduleURI)) {
            if (mavenURI) {
                _artifacts.add(Artifact.fromMavenURI(moduleURI));
            }
            else {
                _artifacts.add(Artifact.fromFileURI(moduleURI));
            }
        }
    }

    @Override
    public ClassLoader getClassLoader() {
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
    public String getClassPath() {
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
}
