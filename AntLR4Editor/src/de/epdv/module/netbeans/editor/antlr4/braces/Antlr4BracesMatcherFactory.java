/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.braces;

import org.netbeans.spi.editor.bracesmatching.BracesMatcher;
import org.netbeans.spi.editor.bracesmatching.BracesMatcherFactory;
import org.netbeans.spi.editor.bracesmatching.MatcherContext;

/**
 *
 * @author peter
 */
public class Antlr4BracesMatcherFactory implements BracesMatcherFactory {

    @Override
    public BracesMatcher createMatcher(MatcherContext context) {
        return new Antlr4BracesMatcher(context);
    }
}
