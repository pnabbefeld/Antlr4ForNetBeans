/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.lexer;

import java.util.List;
import org.antlr.v4.runtime.Token;
import org.netbeans.api.lexer.TokenUtilities;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author peter
 */
public class Antlr4Lexer implements Lexer<Antlr4TokenId> {

    private final LexerRestartInfo<Antlr4TokenId> info;
    private final ANTLRv4Lexer antlr4Lexer;

    private boolean debugFirst = true;

    public Antlr4Lexer(LexerRestartInfo<Antlr4TokenId> info) {
        this.info = info;

        AntlrCharStream charStream = new AntlrCharStream(info.input(), "Antlr4Editor");
        antlr4Lexer = new ANTLRv4Lexer(charStream);
    }

    @Override
    public org.netbeans.api.lexer.Token<Antlr4TokenId> nextToken() {
        Token token = antlr4Lexer.nextToken();
        if (token.getType() != ANTLRv4Lexer.EOF) {
            Antlr4TokenId tokenId = Antlr4LanguageHierarchy.getToken(token.getType());
            org.netbeans.api.lexer.Token<Antlr4TokenId> nToken = info.tokenFactory().createToken(tokenId);
            return nToken;
        }
        return null;
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
