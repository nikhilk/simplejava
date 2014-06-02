// JsonExpander.java
// jist/core
//

package jist.core.java.expanders;

import java.net.*;
import jist.core.*;
import jist.core.java.*;
import jist.util.*;

public final class JsonExpander implements JistExpander {

    private final static String PARSE_CODE_TEMPLATE = "%s %s = (%s)JSONValue.parse(\"%s\");";
    private final static String SIMPLE_JSON_MODULE = "maven:///com.googlecode.json-simple/json-simple/1.1.1/";

    private final JavaRuntime _runtime;

    public JsonExpander(JavaRuntime runtime) {
        _runtime = runtime;
    }

    private URI createJsonModule() {
        URI moduleURI = null;
        try {
            moduleURI = new URI(SIMPLE_JSON_MODULE);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return moduleURI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String expand(JistSource source, String macro, String declaration, String data) throws JistErrorException {
        URI moduleURI = createJsonModule();
        _runtime.getDependencies().addModule(moduleURI);

        JavaSource.addImport(source, "org.json.simple.*");

        String type = data.trim().startsWith("{") ? "JSONObject" : "JSONArray";
        String str = Strings.escape(data);
        return String.format(PARSE_CODE_TEMPLATE, type, declaration, type, str);
    }
}
