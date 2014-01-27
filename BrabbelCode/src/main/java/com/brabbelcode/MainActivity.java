package com.brabbelcode;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private VoiceRecognizer recognizer;
    private LineNumbers codeEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        recognizer = new VoiceRecognizer(this);
        codeEditor = (LineNumbers) findViewById(R.id.codeEditor);

        SyntaxHighlighter.watchTextView(codeEditor);
        CodeHistory.getInstance().watchTextView(codeEditor, -1);
        Mode.getInstance().init((TextView) findViewById(R.id.modeText));

        if(!recognizer.getIsPresent())
            Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();
        else{
            recognizer.setOutput(codeEditor);
            recognizer.setDebug((EditText) findViewById(R.id.debugBox));
            recognizer.setStatus((TextView) findViewById(R.id.statusText));
            recognizer.start();
        }

        Theme.setLight(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //here you can handle orientation change
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
