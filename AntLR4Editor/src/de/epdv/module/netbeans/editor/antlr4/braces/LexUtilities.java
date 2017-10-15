/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.braces;

import de.epdv.module.netbeans.editor.antlr4.lexer.Antlr4TokenId;
import java.util.List;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.csl.api.OffsetRange;

/**
 *
 * @author peter
 */
class LexUtilities {

    @SuppressWarnings("unchecked")
    public static TokenSequence<Antlr4TokenId> getUnoTokenSequence(Document doc, int offset) {
        TokenHierarchy<Document> th = TokenHierarchy.get(doc);
        TokenSequence<Antlr4TokenId> ts = th == null ? null : th.tokenSequence(Antlr4TokenId.getLanguage());

        if (ts == null) {
            // Possibly an embedding scenario such as an RHTML file
            // First try with backward bias true
            List<TokenSequence<?>> list = th.embeddedTokenSequences(offset, true);

            for (TokenSequence<? extends TokenId> t : list) {
                if (t.language() == Antlr4TokenId.getLanguage()) {
                    ts = (TokenSequence<Antlr4TokenId>)t;

                    break;
                }
            }

            if (ts == null) {
                list = th.embeddedTokenSequences(offset, false);

                for (TokenSequence<? extends TokenId> t : list) {
                    if (t.language() == Antlr4TokenId.getLanguage()) {
                        ts = (TokenSequence<Antlr4TokenId>)t;

                        break;
                    }
                }
            }
        }

        return ts;
    }

    /**
     * Search forwards in the token sequence until a token of type
     * <code>down</code> is found
     */
    public static OffsetRange findFwd(BaseDocument doc, TokenSequence<? extends Antlr4TokenId> ts, char up, char down) {
        int balance = 0;

        while (ts.moveNext()) {
            Token<? extends Antlr4TokenId> token = ts.token();

            if (textEquals(token.text(), up)) {
                balance++;
            } else if (textEquals(token.text(), down)) {
                if (balance == 0) {
                    return new OffsetRange(ts.offset(), ts.offset() + token.length());
                }

                balance--;
            }
        }

        return OffsetRange.NONE;
    }

    /**
     * Search forwards in the token sequence until a matching closing token is
     * found so keeps track of nested pairs of up-down eg (()) is ignored if
     * we're searching for a )
     *
     * @param ts the TokenSequence set to the position after an up
     * @param up the opening token eg { or [
     * @param down the closing token eg } or ]
     * @return the Range of closing token in our case 1 char
     */
    public static OffsetRange findFwd(TokenSequence<? extends Antlr4TokenId> ts, int up, int down) {
        int balance = 0;

        while (ts.moveNext()) {
            Token<? extends Antlr4TokenId> token = ts.token();

            if (token.id().ordinal() == up) {
                balance++;
            } else if (token.id().ordinal() == down) {
                if (balance == 0) {
                    return new OffsetRange(ts.offset(), ts.offset() + token.length());
                }
                balance--;
            }
        }

        return OffsetRange.NONE;
    }

    /**
     * Search forwards in the token sequence until a matching closing token is
     * found so keeps track of nested pairs of up-down eg (()) is ignored if
     * we're searching for a )
     *
     * @param ts the TokenSequence set to the position after an up
     * @param up the opening token eg { or [
     * @param down the closing token eg } or ]
     * @return the Range of closing token in our case 1 char
     */
    public static OffsetRange findBwd(TokenSequence<? extends Antlr4TokenId> ts, int up, int down) {
        int balance = 0;

        while (ts.movePrevious()) {
            Token<? extends Antlr4TokenId> token = ts.token();
            TokenId id = token.id();

            if (token.id().ordinal() == up) {
                if (balance == 0) {
                    return new OffsetRange(ts.offset(), ts.offset() + token.length());
                }

                balance++;
            } else if (token.id().ordinal() == down) {
                balance--;
            }
        }

        return OffsetRange.NONE;
    }

    /**
     * Search backwards in the token sequence until a token of type
     * <code>up</code> is found
     */
    public static OffsetRange findBwd(BaseDocument doc, TokenSequence<? extends Antlr4TokenId> ts, char up, char down) {
        int balance = 0;

        while (ts.movePrevious()) {
            Token<? extends Antlr4TokenId> token = ts.token();
            TokenId id = token.id();

            if (textEquals(token.text(), up)) {
                if (balance == 0) {
                    return new OffsetRange(ts.offset(), ts.offset() + token.length());
                }

                balance++;
            } else if (textEquals(token.text(), down)) {
                balance--;
            }
        }

        return OffsetRange.NONE;
    }

    public static boolean textEquals(CharSequence text1, char... text2) {
        int len = text1.length();
        if (len == text2.length) {
            for (int i = len - 1; i >= 0; i--) {
                if (text1.charAt(i) != text2[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
