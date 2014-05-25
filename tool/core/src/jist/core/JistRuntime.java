// JistRuntime.java
// jist/core
//

package jist.core;

public interface JistRuntime {

    public JistSession createSession(JistOptions options);

    public void execute(Jist jist) throws Exception;
}
