// Application.java
// jist/app
//

package jist;

import jist.core.*;
import jist.core.common.*;
import jist.core.java.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        String code =
"System.out.println(\"Hello World!\");\n" +
"System.out.println(\"jist completed...\");\n" +
"\n" +
"%text -> foo\n" +
"some text;\n" +
"%text -> bar << ---\n" +
"some more bar text\n" +
"2nd line as well\n" +
"---\n\n" +
"%text -> baz <<- END\n" +
"    blah blah blah\n" +
"    blah blue bleeEND\n" +
"%text -> buz << END\n" +
"    blah blah blah\n" +
"    blah blue bleeEND\n" +
"System.out.println(buz);";

        JistClassFactory classFactory = new JavaClassFactory();
        JistRuntime runtime = new JavaRuntime(classFactory);

        JistSession session = runtime.createSession();
        JistPreprocessor preprocessor = new JistPreprocessor(session);

        Jist jist = new Jist(session, preprocessor.preprocessCode(code));
        runtime.execute(jist);
    }
}
