// JistExpander.java
// jist/core
//

package jist.core;

/**
 * Implemented by text expanders that process macro declarations in Jist source.
 */
public interface JistExpander {

    /**
     * Expands the specified macro declaration.
     * @param source the source containing the macro declaration.
     * @param macro the macro identifier within the declaration.
     * @param declaration the variable associated with the macro declaration.
     * @param data the data within the macro declaration.
     * @return the resulting text to use as a result of expanding the macro.
     */
    public String expand(JistSource source, String macro, String declaration, String data);
}
