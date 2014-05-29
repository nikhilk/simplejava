// JistExpander.java
// jist/core
//

package jist.core;

public interface JistExpander {

    public String expand(JistRuntime runtime, String macro, String declaration, String data);
}
