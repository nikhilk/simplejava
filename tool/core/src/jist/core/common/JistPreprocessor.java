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

    private final static Pattern pragmaPattern =
        Pattern.compile("^(?<pragma>(import)|(class)|(package)|(require))\\s+(?<name>[a-zA-Z0-9_\\.\\*:]+)\\s*;$");

    private JistSession _session;

    private BufferedReader _codeReader;
    private CodeWriter _codeWriter;

    public JistPreprocessor(JistSession session) {
        _session = session;
    }

    public String preprocessCode(String code) throws IOException {
        _codeReader = new BufferedReader(new StringReader(code));
        _codeWriter = new CodeWriter();

        int lineNumber = 0;
        try {
            String line = null;
            while ((line = _codeReader.readLine()) != null) {
                lineNumber++;

                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("%")) {
                    int linesConsumed = processMacro(trimmedLine);
                    lineNumber += linesConsumed;

                    continue;
                }

                Matcher matcher = pragmaPattern.matcher(trimmedLine);
                if (matcher.matches()) {
                    processPragma(matcher.group("pragma"), matcher.group("name"));
                }
                else {
                    _codeWriter.writeLine(line);
                }
            }

            code = _codeWriter.toString();
        }
        catch (JistErrorException e) {
            System.out.println("Error: " + e.getMessage() + "[" + lineNumber + "]");
        }
        finally {
            _codeReader.close();
        }

        return code;
    }

    private int processMacro(String startLine) throws IOException, JistErrorException {
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

        return lineCount;
    }

    private void processPragma(String pragma, String name) throws JistErrorException {
        // TODO: Some useful validation...

        if (pragma.equals("import")) {
            _session.addImport(name);
        }
        else if (pragma.equals("class")) {
            _session.specifyClassName(name);
        }
        else if (pragma.equals("package")) {
            _session.specifyPackageName(name);;
        }
        else if (pragma.equals("require")) {
            throw new JistErrorException("Support for require has not been implemented yet.");
        }
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
