/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4;

import de.epdv.module.netbeans.editor.antlr4.parser.Antlr4Parser;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Document;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;
import org.openide.util.Exceptions;

public class SyntaxErrorsHighlightingTask extends ParserResultTask<Antlr4Parser.Antlr4EditorParserResult> {

    public SyntaxErrorsHighlightingTask() {
    }

    @Override
    public void run(Antlr4Parser.Antlr4EditorParserResult result, SchedulerEvent event) {
        try {
            Antlr4Parser.Antlr4EditorParserResult parserResult = result;
            ANTLRErrorListener listener = parserResult.getAntlr4Parser().getErrorListeners().get(0);
            List<SyntaxError> syntaxErrors = ((Antlr4ErrorListener)listener).getSyntaxErrors();
            Document document = result.getSnapshot().getSource().getDocument(false);
            List<ErrorDescription> errors = new ArrayList<>();
            for (SyntaxError syntaxError : syntaxErrors) {
//                RecognitionException exception = syntaxError.getException();
                String message = syntaxError.getMessage();

                int line = syntaxError.getLine();
                if (line <= 0) {
                    continue;
                }
                ErrorDescription errorDescription = ErrorDescriptionFactory.createErrorDescription(
                        Severity.ERROR,
                        message,
                        document,
                        line);
                errors.add(errorDescription);
            }
            HintsController.setErrors(document, "antlr4", errors);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }
}
