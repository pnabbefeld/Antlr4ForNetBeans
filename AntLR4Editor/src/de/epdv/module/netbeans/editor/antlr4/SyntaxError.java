/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4;

import org.antlr.v4.runtime.RecognitionException;

/**
 *
 * @author peter
 */
public class SyntaxError {

    private final RecognitionException exception;
    private final String message;
    private final int line;
    private final int charPositionInLine;

    public SyntaxError(RecognitionException exception, String message, int line, int charPositionInLine) {
        this.exception = exception;
        this.message = message;
        this.line = line;
        this.charPositionInLine = charPositionInLine;
    }

   
    public RecognitionException getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getCharPositionInLine() {
        return charPositionInLine;
    }
}
