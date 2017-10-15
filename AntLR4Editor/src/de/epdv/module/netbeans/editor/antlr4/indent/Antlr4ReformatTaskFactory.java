/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.indent;

import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

/**
 *
 * @author peter
 */
public class Antlr4ReformatTaskFactory implements ReformatTask.Factory {

    @Override
    public ReformatTask createTask(Context context) {
        return new Antlr4ReformatTask(context);
    }
}
