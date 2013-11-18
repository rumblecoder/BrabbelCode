package com.brabbelcode;

import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter {

    static Collection<String> KEYWORDS = Arrays.asList("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends",
            "false", "final", "finally", "float", "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native", "new", "null",
            "package", "private", "protected", "public", "return",
            "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "true",
            "transient", "try", "void", "volatile", "while");

    static Pattern WORD_PATTERN = Pattern.compile("\\w+");

    public static void applySyntaxHighlighting(Spannable spannable) {
        for(ForegroundColorSpan span : spannable.getSpans(0,  spannable.length(), ForegroundColorSpan.class)) {
            spannable.removeSpan(span);
        }

        Matcher matcher = WORD_PATTERN.matcher(spannable);
        while (matcher.find()) {
            Integer color = null;
            String match = matcher.group();

            if (KEYWORDS.contains(match)) {
                color = Theme.getKeywordColor();
            }

            if (color!=null) {
                spannable.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), 0);
            }
        }
    }

    public static void watchTextView(final EditText editText) {
        final SyntaxHighlighter highlighter = new SyntaxHighlighter();

        editText.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                highlighter.applySyntaxHighlighting(editText.getText());
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }
}
