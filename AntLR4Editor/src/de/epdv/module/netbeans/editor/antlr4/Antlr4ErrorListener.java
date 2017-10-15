/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

/**
 *
 * @author peter
 */
public class Antlr4ErrorListener implements ANTLRErrorListener {

    public List<SyntaxError> syntaxErrors = new ArrayList<>();

    public Antlr4ErrorListener() {
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException rex) {
        SyntaxError syntaxError = new SyntaxError(rex, msg, line, charPositionInLine);
        syntaxErrors.add(syntaxError);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        System.out.println("reportAmbiguity: " + recognizer + ", " + dfa + ", " + startIndex + ", " + stopIndex + ", " + exact + ", " + ambigAlts + ", " + configs);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        System.out.println("reportAttemptingFullContext: " + recognizer + ", " + dfa + ", " + startIndex + ", " + stopIndex + ", " + conflictingAlts + ", " + configs);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        System.out.println("reportContextSensitivity: " + recognizer + ", " + dfa + ", " + startIndex + ", " + stopIndex + ", " + prediction + ", " + configs);
    }

    public List<SyntaxError> getSyntaxErrors() {
        return Collections.unmodifiableList(syntaxErrors);
    }
}
