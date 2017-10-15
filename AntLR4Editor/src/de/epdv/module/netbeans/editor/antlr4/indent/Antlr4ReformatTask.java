/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.indent;

import javax.swing.text.BadLocationException;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

/**
 *
 * @author peter
 */
public class Antlr4ReformatTask implements ReformatTask {

    private final Context context;

    public Antlr4ReformatTask(Context context) {
        this.context = context;
    }

    @Override
    public void reformat() throws BadLocationException {
        System.out.println("we will format this now");
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }
}
