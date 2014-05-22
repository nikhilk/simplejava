// Artifact.java
// jist/core
//

package jist.core.maven;

import java.io.*;
import java.net.*;
import jist.util.*;

final class Artifact {

    private final String _groupId;
    private final String _artifactId;
    private final String _version;
    private final String _file;

    public Artifact(String file) {
        _groupId = null;
        _artifactId = null;
        _version = null;
        _file = file;
    }

    public Artifact(String groupId, String artifactId, String version) {
        _groupId = groupId;
        _artifactId = artifactId;
        _version = version;
        _file = null;
    }

    public static Artifact fromFileURI(URI uri) {
        return new Artifact(uri.getPath());
    }

    public static Artifact fromMavenURI(URI uri) {
        String[] pathParts = uri.getPath().split("/");
        return new Artifact(pathParts[1], pathParts[2], pathParts[3]);
    }

    public String resolve(String mavenRepository) {
        if (Strings.hasValue(_file)) {
            File file = new File(_file);
            return file.getAbsolutePath();
        }
        else {
            String path = _groupId.replace('.', File.separatorChar) + File.separator +
                          _artifactId + File.separator + _version + File.separator +
                          _artifactId + "-" + _version + ".jar";
            File file = new File(mavenRepository, path);

            // TODO: Invoke maven to resolve the associated artifact, if it doesn't exist

            return file.getPath();
        }
    }
}
