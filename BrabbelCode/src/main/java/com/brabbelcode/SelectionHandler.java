package com.brabbelcode;

import android.widget.EditText;

public class SelectionHandler {
    private static final SelectionHandler instance = new SelectionHandler();
    private EditText textBox;

    private SelectionHandler() {

    }
    public void init(EditText textBox) {
        this.textBox = textBox;
    }
    public static SelectionHandler getInstance() {
        return instance;
    }

    public void selectAll() {
        this.textBox.selectAll();
    }
    public void selectNone() {
        this.textBox.setSelection(this.textBox.getText().length());
    }
}
