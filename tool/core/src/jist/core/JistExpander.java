// JistExpander.java
// jist/core
//

package jist.core;

public interface JistExpander {

    public String expand(JistSession session, String macro, String declaration, String data);
}
