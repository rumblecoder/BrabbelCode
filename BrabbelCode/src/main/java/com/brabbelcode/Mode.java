package com.brabbelcode;

import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class Mode {
    private static final Mode instance = new Mode();
    private TextView textView;
    private String mode;
    private String[] out;

    private Mode() {

    }
    public void init(TextView textView) {
        this.textView = textView;
    }
    public static Mode getInstance() {
        return instance;
    }

    public String[] extractMode(String[] arr) {
        if(arr[0].equals(ModeEnum.CREATE)) {
            this.mode = ModeEnum.CREATE;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(arr[0].equals(ModeEnum.SELECT)) {
            this.mode = ModeEnum.SELECT;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(arr[0].equals(ModeEnum.DELETE)) {
            this.mode = ModeEnum.DELETE;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else {
            this.mode = ModeEnum.FREE;
            out = Arrays.copyOfRange(arr, 0, arr.length);
        }
        this.textView.setText(this.mode);
        return out;
    }
    public String getMode() {
        return this.mode;
    }
}