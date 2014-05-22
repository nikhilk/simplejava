// JistRuntime.java
// jist/core
//

package jist.core;

public interface JistRuntime {

    public JistSession createSession();

    public void execute(Jist jist) throws Exception;
}
