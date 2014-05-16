// JistClassFactory.java
// jist/core
//

package jist.core;

public interface JistClassFactory {

    @SuppressWarnings("rawtypes")
    public Class compile(Jist jist, String source) throws Exception;
}
