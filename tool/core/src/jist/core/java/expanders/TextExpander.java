// TextExpander.java
// jist/core
//

package jist.core.java.expanders;

import jist.core.*;
import jist.core.java.*;
import jist.util.*;

/**
 * JistExpander implementation to handle literal text, and produce a String
 * java variable with literal text.
 */
public final class TextExpander implements JistExpander {

    private final static String TEXT_CODE_TEMPLATE = "String %s = \"%s\";";

    /**
     * Creates an instance of TextExpander.
     * @param runtime the associated runtime.
     */
    public TextExpander(JavaRuntime runtime) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String expand(JistSource source, String macro, String declaration, String data) throws JistErrorException {
        String str = Strings.escape(data);
        return String.format(TEXT_CODE_TEMPLATE, declaration, str);
    }
}
