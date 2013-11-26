package com.brabbelcode;

import android.widget.EditText;

public class DeletionHandler {
    private static final DeletionHandler instance = new DeletionHandler();
    private EditText textBox;

    private DeletionHandler() {

    }
    public void init(EditText textBox) {
        this.textBox = textBox;
    }
    public static DeletionHandler getInstance() {
        return instance;
    }

    public void deleteAll() {
        this.textBox.setText("");
    }
    public void deleteSelection() {
        //TODO: test code
        int start = SelectionHandler.getInstance().getStartIndex();
        int length = SelectionHandler.getInstance().getSelectionLength();
        this.textBox.setText(new char[] {' '}, start, length);
    }
}
