/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.braces;

import de.epdv.module.netbeans.editor.antlr4.lexer.Antlr4TokenId;
import de.epdv.module.netbeans.editor.antlr4.lexer.TokenType;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.spi.editor.bracesmatching.BracesMatcher;
import org.netbeans.spi.editor.bracesmatching.MatcherContext;

/**
 *
 * @author peter
 */
public class Antlr4BracesMatcher implements BracesMatcher {

    private final MatcherContext context;

    private class BracePair {

        int open;       // Lexer ordinal of opening brace eg { or [
        int close;      // Lexer ordinal of closing brace eg } or }

        private BracePair(int op, int cl) {
            open = op;
            close = cl;
        }
    }
    BracePair[] bracePairs = {
        new BracePair(TokenType.LPAREN.id, TokenType.RPAREN.id),
        new BracePair(TokenType.LBRACE.id, TokenType.RBRACE.id)
    };

    public Antlr4BracesMatcher(MatcherContext context) {
        this.context = context;
    }

    @Override
    public int[] findOrigin() throws InterruptedException, BadLocationException {
        int[] ret = null;
        ((AbstractDocument)context.getDocument()).readLock();
        try {
            BaseDocument doc = (BaseDocument)context.getDocument();
            int offset = context.getSearchOffset();
            TokenSequence<? extends Antlr4TokenId> ts = LexUtilities.getUnoTokenSequence(doc, offset);

            if (ts != null) {
                ts.move(offset);

                if (ts.moveNext()) {

                    Token<? extends Antlr4TokenId> token = ts.token();

                    if (token != null) {
                        TokenId id = token.id();
                        int ordinal = id.ordinal();

                        for (BracePair bp : bracePairs) {
                            if (ordinal == bp.open) {
                                ret = new int[]{ts.offset(), ts.offset() + token.length()};
                                break;
                            } else if (ordinal == bp.close) {
                                ret = new int[]{ts.offset(), ts.offset() + token.length()};
                                break;
                            }
                        }
                    }
                }
            }

        } finally {
            ((AbstractDocument)context.getDocument()).readUnlock();
        }
        return ret;
    }

    @Override
    public int[] findMatches() throws InterruptedException, BadLocationException {
        int[] ret = null;
        ((AbstractDocument)context.getDocument()).readLock();
        try {
            BaseDocument doc = (BaseDocument)context.getDocument();
            int offset = context.getSearchOffset();
            TokenSequence<? extends Antlr4TokenId> ts = LexUtilities.getUnoTokenSequence(doc, offset);

            if (ts != null) {
                ts.move(offset);

                if (ts.moveNext()) {

                    Token<? extends Antlr4TokenId> token = ts.token();

                    if (token != null) {
                        TokenId id = token.id();
                        int ordinal = id.ordinal();
                        OffsetRange r;

                        for (BracePair bp : bracePairs) {
                            if (ordinal == bp.open) {
                                r = LexUtilities.findFwd(ts, bp.open, bp.close);
                                ret = new int[]{r.getStart(), r.getEnd()};
                                break;
                            } else if (ordinal == bp.close) {
                                r = LexUtilities.findBwd(ts, bp.open, bp.close);
                                ret = new int[]{r.getStart(), r.getEnd()};
                                break;

                            }
                        }
                    }
                }
            }

        } finally {
            ((AbstractDocument)context.getDocument()).readUnlock();
        }
        return ret;

    }

}
