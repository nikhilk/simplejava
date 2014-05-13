// JistRuntime.java
// jist/core
//

package jist.core.services;

import jist.core.*;

public interface JistRuntime {

    public void execute(Jist jist, JistSession session) throws Exception;

    public void initializeSession(JistSession session);
}
