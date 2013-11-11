package com.brabbelcode;

import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tobias on 11.11.13.
 */
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
