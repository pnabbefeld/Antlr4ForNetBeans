/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.indent;

import javax.swing.text.BadLocationException;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.IndentTask;

/**
 *
 * @author peter
 */
public class Antlr4IndentTask implements IndentTask {

    private final Context context;

    Antlr4IndentTask(Context context) {
        this.context = context;
    }

    @Override
    public void reindent() throws BadLocationException {
        System.out.println("we will indent this now");
    }

    @Override
    public ExtraLock indentLock() {
        return null;
    }
}
