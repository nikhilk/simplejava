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

    private final String _mavenPath;
    private final String _mavenRepositoryPath;

    private final HashSet<URI> _modules;

    public MavenModuleManager(JistOptions options) {
        _mavenPath = options.getMavenPath();
        _mavenRepositoryPath = options.getMavenRepository();

        _modules = new HashSet<URI>();
    }

    private boolean supportsMavenModules() {
        return Strings.hasValue(_mavenRepositoryPath) && Strings.hasValue(_mavenPath);
    }

    @Override
    public void addModule(URI moduleURI) throws JistErrorException {
        String scheme = moduleURI.getScheme();
        if (!scheme.equals(MAVEN_SCHEME) && !scheme.equals(FILE_SCHEME)) {
            throw new JistErrorException("The module url must either be a local file or a maven artifact.");
        }

        if (scheme.equals(MAVEN_SCHEME) && !supportsMavenModules()) {
            throw new JistErrorException("Maven could not be found.");
        }

        _modules.add(moduleURI);
    }

    @Override
    public ClassLoader getClassLoader() {
        // TODO: Implement this
        return null;
    }
}
