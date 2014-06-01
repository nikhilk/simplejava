// JistRuntimeOptions.java
// jist/core
//

package jist.core;

/**
 * Options for initializing the JistRuntime.
 */
public abstract class JistRuntimeOptions {

    private String _basePath;
    private String _mavenPath;
    private String _mavenRepository;

    /**
     * Gets the base path to use to resolve local module paths.
     * @return the base path for path resolution.
     */
    public String getBasePath() {
        return _basePath;
    }

    /**
     * Sets the base path to use to resolve local module paths.
     * @param value the base path for path resolution.
     */
    public void setBasePath(String value) {
        _basePath = value;
    }

    /**
     * Gets the path of the maven executable.
     * @return the path of the maven executable
     */
    public String getMavenPath() {
        return _mavenPath;
    }

    /**
     * Sets the path of the maven executable.
     * @param value the path of the maven executable.
     */
    protected void setMavenPath(String value) {
        _mavenPath = value;
    }

    /**
     * Gets the path of the local maven repository.
     * @return the local maven repository path
     */
    public String getMavenRepository() {
        return _mavenRepository;
    }

    /**
     * Sets the path of the local maven repository.
     * @param value the local maven repository path
     */
    protected void setMavenRepository(String value) {
        _mavenRepository = value;
    }
}
