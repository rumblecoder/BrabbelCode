package com.brabbelcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private VoiceRecognizer recognizer;
    private EditText codeEditor;
    private EditText debugBox;
    private TextView modeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codeEditor = (EditText) findViewById(R.id.codeEditor);
        debugBox = (EditText) findViewById(R.id.debugBox);
        modeLabel = (TextView) findViewById(R.id.modeText);

        SyntaxHighlighter.watchTextView(codeEditor);
        CodeHistory.getInstance().watchTextView(codeEditor, -1);

        recognizer = new VoiceRecognizer(this);

        if(!recognizer.getIsPresent())
            Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();
        else{
            recognizer.setOutput(codeEditor);
            recognizer.setDebug(debugBox);
            recognizer.setModeBox(modeLabel);
            recognizer.start();
        }
    }

    /**
     * Events
     */
    public void onMicCheckClicked(View view) {
        if(((CheckBox)view).isChecked())
            recognizer.stop();
        else
            recognizer.start();
    }

    public void onThemeCheckClicked(View view) {
        if(((CheckBox)view).isChecked()){
            Theme.setDark(this);
            SyntaxHighlighter.applySyntaxHighlighting(codeEditor.getText());
        }
        else{
            Theme.setLight(this);
            SyntaxHighlighter.applySyntaxHighlighting(codeEditor.getText());
        }
    }

}
