// MavenModuleManager.java
// jist/core
//

package jist.core.maven;

import java.io.*;
import java.net.*;
import java.util.*;
import jist.core.*;
import jist.util.Strings;

public final class MavenModuleManager implements ModuleManager {

    private static final String FILE_SCHEME = "file";
    private static final String MAVEN_SCHEME = "maven";

    private HashSet<URI> _modules;

    private String _mavenPath;
    private String _mavenRepositoryPath;

    public MavenModuleManager() {
        _modules = new HashSet<URI>();
    }

    private boolean ensureMaven() {
        if (Strings.hasValue(_mavenRepositoryPath) && Strings.hasValue(_mavenPath)) {
            return true;
        }

        String path = System.getProperty("user.home");
        File homeDirectory = new File(path);
        File repoDirectory = new File(new File(homeDirectory, ".m2"), "repository");

        if (repoDirectory.exists() && repoDirectory.isDirectory()) {
            _mavenRepositoryPath = repoDirectory.getPath();
        }

        String[] pathList = System.getenv("PATH").split(File.pathSeparator);
        for (String pathPart : pathList) {
            File pathDirectory = new File(pathPart);
            File mavenFile = new File(pathDirectory, "mvn");

            if (mavenFile.exists() && mavenFile.isFile()) {
                _mavenPath = mavenFile.getPath();
            }
        }

        if (Strings.isNullOrEmpty(_mavenRepositoryPath) ||
            Strings.isNullOrEmpty(_mavenPath)) {
            return false;
        }

        return true;
    }

    @Override
    public void addModule(URI moduleURI) throws JistErrorException {
        String scheme = moduleURI.getScheme();
        if (!scheme.equals(MAVEN_SCHEME) && !scheme.equals(FILE_SCHEME)) {
            throw new JistErrorException("The module url must either be a local file or a maven artifact.");
        }

        if (scheme.equals(MAVEN_SCHEME) && !ensureMaven()) {
            throw new JistErrorException("Unable to locate maven or the local maven repository.");
        }

        _modules.add(moduleURI);
    }

    @Override
    public ClassLoader getClassLoader() {
        // TODO: Implement this
        return null;
    }
}
