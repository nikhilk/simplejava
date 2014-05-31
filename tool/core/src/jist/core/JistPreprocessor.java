// JavaPreprocessor.java
// jist/core
//

package jist.core;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public abstract class JistPreprocessor {

    // Macros consist of a macro identifier, a variable declaration
    // and optionally a "heredoc" style end marker (useful if ';' is needed within the text).
    private final static Pattern macroPattern =
        Pattern.compile("^%(?<m>[a-z]+)\\s+->\\s+(?<decl>[a-z_][a-z_0-9]*)(\\s+<<(?<trim>-)?\\s*(?<end>[^\\s]+))?$",
                        Pattern.CASE_INSENSITIVE);

    private final JistRuntime _runtime;
    private final HashMap<String, JistExpander> _expanders;

    private BufferedReader _textReader;
    private TextWriter _textWriter;

    protected JistPreprocessor(JistRuntime runtime) {
        _runtime = runtime;
        _expanders = new HashMap<String, JistExpander>();
    }

    public void addExpander(String name, JistExpander expander) {
        _expanders.put(name, expander);
    }

    public String preprocess(JistSource source) throws IOException {
        String text = source.getText();

        _textReader = new BufferedReader(new StringReader(text));
        _textWriter = new TextWriter();

        int lineNumber = 0;
        try {
            String line = null;
            while ((line = _textReader.readLine()) != null) {
                lineNumber++;

                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("%")) {
                    int linesConsumed = processMacro(source, trimmedLine);
                    lineNumber += linesConsumed;

                    continue;
                }

                line = processLine(source, line);
                if (line != null) {
                    _textWriter.writeLine(line);
                }
            }

            text = _textWriter.toString();
        }
        catch (JistErrorException e) {
            System.out.println("Error: " + e.getMessage() + "[" + lineNumber + "]");
        }
        finally {
            _textWriter.close();
        }

        return text;
    }

    protected String processLine(JistSource source, String line) throws JistErrorException {
        return line;
    }

    private int processMacro(JistSource source, String startLine) throws IOException, JistErrorException {
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

        JistExpander macroExpander = _expanders.get(macro);
        if (macroExpander == null) {
            throw new JistErrorException("Could not find a matching expander for " + macro + " macro.");
        }

        MacroTextBuilder macroText = new MacroTextBuilder(trim);

        String line;
        while ((line = _textReader.readLine()) != null) {
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

        String code = macroExpander.expand(source, macro, declaration, macroText.toString());
        _textWriter.writeLine(code);

        return lineCount;
    }


    private final class TextWriter extends StringWriter {

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
