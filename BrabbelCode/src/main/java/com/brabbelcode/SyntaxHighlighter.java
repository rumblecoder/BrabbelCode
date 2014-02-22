package com.brabbelcode;

import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter {

    private static final Pattern KEYWORDS = Pattern.compile(
            "\\b(abstract|assert|boolean|break|byte|case|catch|char|"+
                "class|const|continue|default|do|double|else|enum|extends|"+
                "false|final|finally|float|for|goto|if|implements|import|"+
                "instanceof|int|interface|long|native|new|null|package|"+
                "private|protected|public|return|short|static|strictfp|"+
                "String|super|switch|synchronized|this|throw|throws|true|"+
                "transient|try|void|volatile|while)\\b" );

    private static final Pattern CHARS = Pattern.compile(
            "'.*'|\".*\"" );

    private static final Pattern COMMENTS = Pattern.compile(
            "/\\*(?:.|[\\n\\r])*?\\*/|//.*" );

    public static void applySyntaxHighlighting(Spannable spannable) {
        for(ForegroundColorSpan span : spannable.getSpans(0,  spannable.length(), ForegroundColorSpan.class)) {
            spannable.removeSpan(span);
        }

        Matcher matcher = KEYWORDS.matcher(spannable);
        while (matcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(Theme.getKeywordsColor()), matcher.start(), matcher.end(), 0);
        }

        matcher = CHARS.matcher(spannable);
        while (matcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(Theme.getCharsColor()), matcher.start(), matcher.end(), 0);
        }

        matcher = COMMENTS.matcher(spannable);
        while (matcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(Theme.getCommentsColor()), matcher.start(), matcher.end(), 0);
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
