package com.brabbelcode;

import android.widget.EditText;

public class SelectionHandler {
    private static final SelectionHandler instance = new SelectionHandler();
    private EditText textBox;
    private int start = 0;
    private int end = 0;
    private int length = 0;

    private SelectionHandler() {

    }
    public void init(EditText textBox) {
        this.textBox = textBox;
    }
    public static SelectionHandler getInstance() {
        return instance;
    }

    public void setStartIndex(int start) {
        this.start = start;
        this.calculateLength();
    }
    public void setEndIndex(int end) {
        this.end = end;
        this.calculateLength();
    }
    public int getStartIndex() {
        return this.start;
    }
    public int getEndIndex() {
        return this.end;
    }
    public int getSelectionLength() {
        return this.length;
    }
    public void selectAll() {
        this.textBox.selectAll();
    }
    public void selectNone() {
        this.textBox.setSelection(this.textBox.getText().length());
    }
    public void selectLine(String[] line) {
        if(Util.isInteger(line[1])) {
            int lineNumber = Integer.parseInt(line[1]);

            String[] lines = this.textBox.getText().toString().split("\r?\n");

            if(lines.length > lineNumber) {
                int end = 0;
                int start = 0;
                for(int i = 0; i < lineNumber; i++) {
                    start = end;
                    end += lines[i].length() + 1; // +1 for \n
                }
                if(start < end) {
                    this.setStartIndex(start);
                    this.setEndIndex(end);
                    this.textBox.setSelection(start, end);
                }
            }
        }
        //TODO: unable to select last line!
    }

    private void calculateLength() {
        this.length = this.end - this.start;
    }
}
