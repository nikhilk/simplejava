// JistClassFactory.java
// jist/core
//

package jist.core;

public interface JistClassFactory {

    @SuppressWarnings("rawtypes")
    public Class compile(String name, String source) throws Exception;
}
