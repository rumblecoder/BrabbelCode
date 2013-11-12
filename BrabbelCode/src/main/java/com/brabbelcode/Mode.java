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
        if(arr[0].equals("create")) {
            this.mode = "create";
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(arr[0].equals("select")) {
            this.mode = "select";
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else if(arr[0].equals("delete")) {
            this.mode = "delete";
            out = Arrays.copyOfRange(arr, 1, arr.length);
        } else {
            this.mode = "free";
            out = Arrays.copyOfRange(arr, 0, arr.length);
        }
        this.textView.setText(this.mode);
        return out;
    }
    public String getMode() {
        return this.mode;
    }
}

/*
public class Mode {

    private static TextView output;
    private static String mode = "none";
    private static List<String> createKeywords = Arrays.asList("class", "var", "function");
    private static List<String> selectKeywords = Arrays.asList("line", "word", "scope", "all");
    private static List<String> freeKeywords = Arrays.asList("");

    public static void setOutput(TextView textView){
        output = textView;
        output.setText(mode);
    }

    public static String getMode(){
        return mode;
    }

    public static List<String> getKeywords(){
        if(mode == "none")
            return null;
        else if(mode == "create")
            return createKeywords;
        else if(mode == "select")
            return selectKeywords;
        else if(mode == "free")
            return freeKeywords;
        else
            return null;
    }

    public static void switchToCreate(){
        mode = "create";
        output.setText("Mode: " + mode);
    }

    public static void switchToSelect(){
        mode = "select";
        output.setText("Mode: " + mode);
    }

    public static void switchToFree(){
        mode = "free";
        output.setText("Mode: " + mode);
    }
}
*/
