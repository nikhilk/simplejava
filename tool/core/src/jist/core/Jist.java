// Jist.java
// jist/core
//

package jist.core;

public final class Jist {

    private final JistSession _session;
    private final JistSource _source;

    public Jist(JistSession session, JistSource source) {
        _session = session;
        _source = source;
    }

    public JistSession getSession() {
        return _session;
    }

    public JistSource getSource() {
        return _source;
    }
}
