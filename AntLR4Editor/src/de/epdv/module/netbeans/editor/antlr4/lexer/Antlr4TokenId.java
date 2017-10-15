/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author peter
 */
public class Antlr4TokenId implements TokenId {

    private static final Language<Antlr4TokenId> LANGUAGE = new Antlr4LanguageHierarchy().language();

    public static final Language<Antlr4TokenId> getLanguage() {
        return LANGUAGE;
    }

    private final String name;
    private final String primaryCategory;
    private final int id;

    public Antlr4TokenId(String name, String primaryCategory, int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }
}
