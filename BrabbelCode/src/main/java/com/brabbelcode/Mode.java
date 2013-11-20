package com.brabbelcode;

import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class Mode {
    private static final Mode instance = new Mode();
    private TextView textView;
    private Enums.MODE mode;
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
        if(arr[0].equals("create")) {
            this.mode = Enums.MODE.CREATE;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(arr[0].equals("select")) {
            this.mode = Enums.MODE.SELECT;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(arr[0].equals("delete")) {
            this.mode = Enums.MODE.DELETE;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(arr[0].equals("undo")) {
            this.mode = Enums.MODE.UNDO;
            //out = Arrays.copyOfRange(arr, 1, arr.length);
            out = null;
        } else if(arr[0].equals("redo")) {
            this.mode = Enums.MODE.REDO;
            //out = Arrays.copyOfRange(arr, 1, arr.length);
            out = null;
        }
        else {
            this.mode = Enums.MODE.FREE;
            out = Arrays.copyOfRange(arr, 0, arr.length);
        }
        this.textView.setText(mode.toString());
        return out;
    }

    public Enums.MODE getMode() {
        return this.mode;
    }
}