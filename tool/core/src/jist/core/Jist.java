// Jist.java
// jist/core
//

package jist.core;

public final class Jist {

    private final JistSession _session;
    private final String _code;

    public Jist(JistSession session, String code) {
        _session = session;
        _code = code;
    }

    public String getCode() {
        return _code;
    }

    public JistSession getSession() {
        return _session;
    }
}
