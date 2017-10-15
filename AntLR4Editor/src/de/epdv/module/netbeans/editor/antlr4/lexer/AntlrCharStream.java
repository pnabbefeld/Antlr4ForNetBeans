/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.lexer;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.misc.Interval;
import org.netbeans.spi.lexer.LexerInput;

/**
 *
 * @author peter
 */
public class AntlrCharStream implements CharStream {

    private class CharStreamState {

        int index;
        int line;
        int charPositionInLine;
    }

    private final String name;
    private final LexerInput input;
    private int line = 1;
    private int charPositionInLine = 0;
    private int index = 0;
    private List<CharStreamState> markers;
    private int markDepth = 0;

    public AntlrCharStream(LexerInput input, String name) {
        this.input = input;
        this.name = name;
    }

    @Override
    public void consume() {
        int c = input.read();
        index++;
        charPositionInLine++;

        if (c == '\n') {
            line++;
            charPositionInLine = 0;
        }
    }

    @Override
    public int LA(int i) {
        if (i == 0) {
            return 0; // undefined
        }

        int c = 0;
        for (int j = 0; j < i; j++) {
            c = read();
        }
        backup(i);
        return c;
    }

    @Override
    public int mark() {
        if (markers == null) {
            markers = new ArrayList<>();
            markers.add(null); // depth 0 means no backtracking, leave blank
        }
        markDepth++;
        CharStreamState state;
        if (markDepth >= markers.size()) {
            state = new CharStreamState();
            markers.add(state);
        } else {
            state = (CharStreamState)markers.get(markDepth);
        }
        state.index = index;
        state.line = line;
        state.charPositionInLine = charPositionInLine;
        return markDepth;
    }

    @Override
    public void release(int marker) {
        // unwind any other markers made after m and release m
        markDepth = marker;
        // release this marker
        markDepth--;
    }

    @Override
    public void seek(int index) {
        if (index < this.index) {
            backup(this.index - index);
            this.index = index; // just jump; don't update stream state (line, ...)
            return;
        }

        // seek forward, consume until p hits index
        while (this.index < index) {
            consume();
        }
    }

    @Override
    public int index() {
        return index;
    }

    /**
     * Size of input stream cannot be determined here, so throw an
     * UnsupportedOperationException.
     *
     * @return size of input stream.
     */
    @Override
    public int size() {
        return -1;
//        throw new UnsupportedOperationException();
    }

    @Override
    public String getSourceName() {
        return name;
    }

    /**
     * Get a text interval.
     *
     * @param intrvl
     * @return
     */
    @Override
    public String getText(Interval intrvl) {
        int base = getLastMarkedPosition();

//        base = 0;
        int a = intrvl.a - base;
        int b = intrvl.b - base + 1;
        String text = input.readText(a, b).toString();
        return text;
    }

    private int read() {
        int result = input.read();
        if (result == LexerInput.EOF) {
            result = CharStream.EOF;
        }

        return result;
    }

    private void backup(int count) {
        input.backup(count);
    }

    private int getLastMarkedPosition() {
        int n = markDepth;
        if (n < 1) {
            return 0;
        } else {
            return markers.get(n).index;
        }
    }
}
