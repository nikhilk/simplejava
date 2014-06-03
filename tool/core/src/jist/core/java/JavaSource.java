// JavaSource.java
// jist/core
//

package jist.core.java;

import java.util.*;
import jist.core.*;
import jist.util.*;

public final class JavaSource {

    public final static String DEFAULT_SOURCE_NAME = "main.java";
    public final static String METADATA_SOURCE_NAME = "jist.java";

    private final static String CLASS_KEY = "class";
    private final static String PACKAGE_KEY = "package";
    private final static String IMPORTS_KEY = "imports";
    private final static String MODULES_KEY = "modules";

    private JavaSource() {
    }

    public static void addImport(JistSource source, String name) {
        getImports(source, true).add(name);
    }

    public static void addModule(JistSource source, String name) {
        getModules(source, true).add(name);
    }

    public static String createCompilableSource(JistSource source, String classImplementation) {
        StringBuilder sourceBuilder = new StringBuilder();

        String packageName = getPackageName(source);
        if (Strings.hasValue(packageName)) {
            sourceBuilder.append("package ");
            sourceBuilder.append(packageName);
            sourceBuilder.append(";\n\n");
        }

        String[] imports = getImports(source);
        for (String importedReference : imports) {
            sourceBuilder.append("import ");
            sourceBuilder.append(importedReference);
            sourceBuilder.append(";\n");
        }

        String[] modules = getModules(source);
        for (String importedReference : modules) {
            sourceBuilder.append("import static ");
            sourceBuilder.append(importedReference);
            sourceBuilder.append(";\n");
        }

        sourceBuilder.append("\n");

        sourceBuilder.append(classImplementation);
        return sourceBuilder.toString();
    }

    public static String getClassName(JistSource source) {
        String name = (String)source.getMetadata(CLASS_KEY);
        if (name == null) {
            // Class name prefixed with "_" to make sure we always have a valid
            // identifier name, even if the random string begins with a digit.
            name = "_" + Strings.randomString(8);

            source.setMetadata(CLASS_KEY, name);
        }

        return name;
    }

    public static String getFullClassName(JistSource source) {
        String packageName = getPackageName(source);
        String className = getClassName(source);

        if (Strings.isNullOrEmpty(packageName)) {
            return className;
        }
        else {
            return packageName + "." + className;
        }
    }

    public static String[] getImports(JistSource source) {
        HashSet<String> imports = getImports(source, false);
        if (imports == null) {
            return new String[0];
        }

        String[] names = new String[imports.size()];

        imports.toArray(names);
        Arrays.sort(names);

        return names;
    }

    @SuppressWarnings("unchecked")
    private static HashSet<String> getImports(JistSource source, boolean create) {
        HashSet<String> imports = (HashSet<String>)source.getMetadata(IMPORTS_KEY);
        if (create && (imports == null)) {
            imports = new HashSet<String>();
            source.setMetadata(IMPORTS_KEY, imports);
        }

        return imports;
    }

    public static String[] getModules(JistSource source) {
        HashSet<String> modules = getModules(source, false);
        if (modules == null) {
            return new String[0];
        }

        String[] names = new String[modules.size()];

        modules.toArray(names);
        Arrays.sort(names);

        return names;
    }

    @SuppressWarnings("unchecked")
    private static HashSet<String> getModules(JistSource source, boolean create) {
        HashSet<String> modules = (HashSet<String>)source.getMetadata(MODULES_KEY);
        if (create && (modules == null)) {
            modules = new HashSet<String>();
            source.setMetadata(MODULES_KEY, modules);
        }

        return modules;
    }

    public static String getPackageName(JistSource source) {
        return (String)source.getMetadata(PACKAGE_KEY);
    }

    public static void setClassName(JistSource source, String name) {
        source.setMetadata(CLASS_KEY, name);
    }

    public static void setPackageName(JistSource source, String name) {
        source.setMetadata(PACKAGE_KEY, name);
    }
}
