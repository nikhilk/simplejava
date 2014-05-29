// TextExpander.java
// jist/core
//

package jist.core.java.expanders;

import jist.core.*;
import jist.util.*;

public final class TextExpander implements JistExpander {

    private final static String TEXT_CODE_TEMPLATE = "String %s = \"%s\";";

    @Override
    public String expand(JistRuntime runtime, String macro, String declaration, String data) {
        String str = Strings.escape(data);
        return String.format(TEXT_CODE_TEMPLATE, declaration, str);
    }
}
