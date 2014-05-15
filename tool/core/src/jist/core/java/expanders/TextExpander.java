// TextExpander.java
// jist/core
//

package jist.core.java.expanders;

import jist.core.*;
import org.apache.commons.lang3.*;

public final class TextExpander implements JistExpander {

    private final static String TEXT_CODE_TEMPLATE = "String %s = \"%s\";";

    @Override
    public String expand(JistSession session, String macro, String declaration, String data) {
        String str = StringEscapeUtils.escapeJava(data);
        return String.format(TEXT_CODE_TEMPLATE, declaration, str);
    }
}
