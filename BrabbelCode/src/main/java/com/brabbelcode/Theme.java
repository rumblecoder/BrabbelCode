package com.brabbelcode;

import android.app.Activity;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Theme {

    private static boolean isDark = false;

    public static void setLight(Activity activity){
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.container);
        linearLayout.setBackgroundColor(Color.argb(255, 255, 255, 255));
        LineNumbers lineNumbers = (LineNumbers) activity.findViewById(R.id.codeEditor);
        lineNumbers.setTextColor(Color.argb(255, 0, 0, 0));
        lineNumbers.nPaintNumbers.setColor(Color.argb(255, 0, 0, 0));
        CheckBox checkBox0 = (CheckBox) activity.findViewById(R.id.micCheck);
        checkBox0.setTextColor(Color.argb(255, 0, 0, 0));
        checkBox0.setHighlightColor(Color.argb(255, 0, 0, 0));
        checkBox0.setButtonDrawable(R.drawable.mic_light);
        CheckBox checkBox1 = (CheckBox) activity.findViewById(R.id.themeCheck);
        checkBox1.setTextColor(Color.argb(255, 0, 0, 0));
        checkBox1.setHighlightColor(Color.argb(255, 0, 0, 0));
        TextView textView0 = (TextView) activity.findViewById(R.id.modeText);
        textView0.setTextColor(Color.argb(255, 0, 0, 0));
        TextView textView1 = (TextView) activity.findViewById(R.id.statusText);
        textView1.setTextColor(Color.argb(255, 255, 255, 255));
        textView1.setBackgroundColor(Color.argb(255, 30, 30, 30));

        isDark = false;
    }

    public static void setDark(Activity activity){
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.container);
        linearLayout.setBackgroundColor(Color.argb(255, 30, 30, 30));
        LineNumbers lineNumbers = (LineNumbers) activity.findViewById(R.id.codeEditor);
        lineNumbers.setTextColor(Color.argb(255, 255, 255, 255));
        lineNumbers.nPaintNumbers.setColor(Color.argb(255, 255, 255, 255));
        CheckBox checkBox0 = (CheckBox) activity.findViewById(R.id.micCheck);
        checkBox0.setTextColor(Color.argb(255, 255, 255, 255));
        checkBox0.setHighlightColor(Color.argb(255, 255, 255, 255));
        checkBox0.setButtonDrawable(R.drawable.mic_dark);
        CheckBox checkBox1 = (CheckBox) activity.findViewById(R.id.themeCheck);
        checkBox1.setTextColor(Color.argb(255, 255, 255, 255));
        checkBox1.setHighlightColor(Color.argb(255, 255, 255, 255));
        TextView textView0 = (TextView) activity.findViewById(R.id.modeText);
        textView0.setTextColor(Color.argb(255, 255, 255, 255));
        TextView textView1 = (TextView) activity.findViewById(R.id.statusText);
        textView1.setTextColor(Color.argb(255, 0, 0, 0));
        textView1.setBackgroundColor(Color.argb(255, 255, 255, 255));

        isDark = true;
    }

    public static int getKeywordsColor(){
        if(!isDark)
            return Color.argb(255, 0, 0, 255);
        else
            return Color.argb(255, 86, 156, 214);
    }

    public static int getCommentsColor(){
        if(!isDark)
            return Color.argb(255, 0, 128, 0);
        else
            return Color.argb(255, 87, 166, 74);
    }

    public static int getCharsColor(){
        if(!isDark)
            return Color.argb(255, 163, 21, 21);
        else
            return Color.argb(255, 214, 157, 133);
    }
}
