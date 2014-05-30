// JistOptions.java
// jist/core
//

package jist.core;

public abstract class JistOptions {

    private String _basePath;
    private String _mavenPath;
    private String _mavenRepository;

    public String getBasePath() {
        return _basePath;
    }

    public void setBasePath(String value) {
        _basePath = value;
    }

    public String getMavenPath() {
        return _mavenPath;
    }

    protected void setMavenPath(String value) {
        _mavenPath = value;
    }

    public String getMavenRepository() {
        return _mavenRepository;
    }

    protected void setMavenRepository(String value) {
        _mavenRepository = value;
    }
}
