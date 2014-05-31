// JistExpander.java
// jist/core
//

package jist.core;

public interface JistExpander {

    public String expand(JistSource source, String macro, String declaration, String data);
}
