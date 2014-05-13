// JistClassFactory.java
// jist/core
//

package jist.core.services;

public interface JistClassFactory {

    public Class compile(String name, String source) throws Exception;
}
