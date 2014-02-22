package com.brabbelcode;

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
        if(LevenshteinDistance.computeLevenshteinDistanceWithTolerance(arr[0],"create")){
            this.mode = Enums.MODE.CREATE;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(LevenshteinDistance.computeLevenshteinDistanceWithTolerance(arr[0],"select")) {
            this.mode = Enums.MODE.SELECT;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(LevenshteinDistance.computeLevenshteinDistanceWithTolerance(arr[0],"delete")) {
            this.mode = Enums.MODE.DELETE;
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(LevenshteinDistance.computeLevenshteinDistanceWithTolerance(arr[0],"undo")) {
            this.mode = Enums.MODE.UNDO;
            out = null;
        } else if(LevenshteinDistance.computeLevenshteinDistanceWithTolerance(arr[0],"redo")) {
            this.mode = Enums.MODE.REDO;
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