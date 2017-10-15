/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.lexer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author peter
 */
public class Antlr4LanguageHierarchy extends LanguageHierarchy<Antlr4TokenId> {

    private static final List<Antlr4TokenId> TOKENS = new ArrayList<>();
    private static final Map<Integer, Antlr4TokenId> ID_TO_TOKEN = new HashMap<>();

    static {
        TokenType[] tokenTypes = TokenType.values();
        for (TokenType tokenType : tokenTypes) {
            TOKENS.add(new Antlr4TokenId(tokenType.name(), tokenType.category, tokenType.id));
        }
        for (Antlr4TokenId token : TOKENS) {
            ID_TO_TOKEN.put(token.ordinal(), token);
        }
    }

    static synchronized Antlr4TokenId getToken(int id) {
        return ID_TO_TOKEN.get(id);
    }

    @Override
    protected synchronized Collection<Antlr4TokenId> createTokenIds() {
        return TOKENS;
    }

    @Override
    protected synchronized Lexer<Antlr4TokenId> createLexer(LexerRestartInfo<Antlr4TokenId> info) {
        return new Antlr4Lexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-antlr4";
    }
}