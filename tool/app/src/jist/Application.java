// Application.java
// jist/app
//

package jist;

import jist.core.services.*;
import jist.core.services.java.*;

public final class Application {

    public static void main(String[] args) throws Exception {
        String code = "System.out.println(\"Hello World!\");";
        JistExecutor executor = new JavaExecutor();

        executor.executeJist(code);
    }
}
