// JistPreprocessor.java
// jist/core
//

package jist.core.common;

import java.io.*;
import java.util.regex.*;
import jist.core.*;

public final class JistPreprocessor {

    // Macros consist of a macro identifier, a variable declaration
    // and optionally a "heredoc" style end marker (useful if ';' is needed within the text).
    private final static Pattern macroPattern =
        Pattern.compile("^%(?<m>[a-z]+)\\s+->\\s+(?<decl>[a-z_][a-z_0-9]*)(\\s+<<(?<trim>-)?\\s*(?<end>[^\\s]+))?$",
                        Pattern.CASE_INSENSITIVE);

    private JistSession _session;

    private int _lineNumber;
    private BufferedReader _codeReader;
    private CodeWriter _codeWriter;

    public JistPreprocessor(JistSession session) {
        _session = session;
    }

    public String preprocessCode(String code) throws IOException {
        _codeReader = new BufferedReader(new StringReader(code));
        _codeWriter = new CodeWriter();
        _lineNumber = 0;

        try {
            String line = null;
            while ((line = _codeReader.readLine()) != null) {
                _lineNumber++;

                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("#")) {
                    processPragma(trimmedLine);
                }
                else if (trimmedLine.startsWith("%")) {
                    processMacro(trimmedLine);
                }
                else {
                    _codeWriter.writeLine(line);
                }
            }

            code = _codeWriter.toString();
        }
        catch (JistErrorException e) {
            System.out.println("Error: " + e.getMessage() + "[" + _lineNumber + "]");
        }
        finally {
            _codeReader.close();
        }

        return code;
    }

    private void processMacro(String startLine) throws IOException, JistErrorException {
        int lineCount = 0;

        Matcher m = macroPattern.matcher(startLine);
        if (!m.matches()) {
            throw new JistErrorException("Invalid macro syntax.");
        }

        String macro = m.group("m");
        String declaration = m.group("decl");
        boolean trim = m.group("trim") != null;

        String endMarker = m.group("end");
        if (endMarker == null) {
            endMarker = ";";
        }

        JistExpander macroExpander = _session.getExpander(macro);
        if (macroExpander == null) {
            throw new JistErrorException("Could not find a matching expander for " + macro + " macro.");
        }

        MacroTextBuilder macroText = new MacroTextBuilder(trim);

        String line;
        while ((line = _codeReader.readLine()) != null) {
            lineCount++;

            String trimmedLine = line.trim();
            if (trimmedLine.endsWith(endMarker)) {
                int delimiterIndex = line.indexOf(endMarker);
                if (delimiterIndex != 0) {
                    line = line.substring(0, delimiterIndex);
                    macroText.appendLine(line);
                }

                break;
            }
            else {
                macroText.appendLine(line);
            }
        }

        String code = macroExpander.expand(_session, macro, declaration, macroText.toString());
        _codeWriter.writeLine(code);

        _lineNumber += lineCount;
    }

    private void processPragma(String line) {
        // TODO: Implement this
    }


    private final class CodeWriter extends StringWriter {

        public void writeLine(String text) {
            write(text);
            write(System.lineSeparator());
        }
    }


    private final class MacroTextBuilder {

        private StringBuilder _sb;
        private boolean _trim;
        private boolean _firstLine;

        public MacroTextBuilder(boolean trim) {
            _sb = new StringBuilder();

            _trim = trim;
            _firstLine = true;
        }

        public void appendLine(String text) {
            if (!_firstLine) {
                _sb.append('\n');
            }

            if (_trim) {
                text = text.trim();
            }

            _sb.append(text);
            _firstLine = false;
        }

        @Override
        public String toString() {
            return _sb.toString();
        }
    }
}
