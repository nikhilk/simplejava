// Jist.java
// jist/core
//

package jist.core;

public final class Jist {

    private final JistSession _session;
    private final String _code;
    private final String _compilableCode;

    public Jist(JistSession session, String code, String compilableCode) {
        _session = session;
        _code = code;
        _compilableCode = compilableCode;
    }

    public String getCode() {
        return _code;
    }

    public String getCompilableCode() {
        return _compilableCode;
    }

    public JistSession getSession() {
        return _session;
    }
}
