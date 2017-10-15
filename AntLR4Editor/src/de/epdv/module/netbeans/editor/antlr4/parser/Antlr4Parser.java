package de.epdv.module.netbeans.editor.antlr4.parser;

import de.epdv.module.netbeans.editor.antlr4.Antlr4ErrorListener;
import de.epdv.module.netbeans.editor.antlr4.lexer.ANTLRv4Lexer;
import javax.swing.event.ChangeListener;

//import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;

import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.openide.util.Exceptions;

public class Antlr4Parser extends Parser {

    // <editor-fold defaultstate="collapsed" desc="Antlr4EditorParserResult">
    public static class Antlr4EditorParserResult extends Result {

        private final ANTLRv4Parser antlr4Parser;
        private boolean valid = true;

        Antlr4EditorParserResult(Snapshot snapshot, ANTLRv4Parser antlr4Parser) {
            super(snapshot);
            this.antlr4Parser = antlr4Parser;
        }

        public ANTLRv4Parser getAntlr4Parser() throws ParseException {
            if (valid) {
                return antlr4Parser;
            } else {
                throw new ParseException();
            }
        }

        @Override
        protected void invalidate() {
            valid = false;
        }
    }// </editor-fold>

    private ANTLRv4Parser antlr4Parser;
    private Snapshot snapshot;

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) {
        this.snapshot = snapshot;
        CodePointCharStream input = CharStreams.fromString(snapshot.getText().toString());
        Lexer lexer = new ANTLRv4Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        antlr4Parser = new ANTLRv4Parser(tokens);
        // remove the default error listener
        antlr4Parser.removeErrorListeners();
        // add own error listener
        antlr4Parser.addErrorListener(new Antlr4ErrorListener());
        try {
            // Grammar entry point
            antlr4Parser.grammarSpec();
        } catch (RecognitionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public Result getResult(Task task) {
        return new Antlr4EditorParserResult(snapshot, antlr4Parser);
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
    }
}
