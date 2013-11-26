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

    public int setStartIndex(int start) {
        this.start = start;
        this.calculateLength();
    }
    public int setEndIndex(int end) {
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
        int lineNumber = Integer.parseInt(line[0]);

        String text = this.textBox.toString();
        //TODO: find nth \n und nth-1 \n in String, save index to start/end
        this.textBox.setSelection(SelectionHandler.getInstance().getStartIndex(), SelectionHandler.getInstance().getEndIndex());
    }

    private void calculateLength() {
        this.length = this.end - this.start;
    }
}
