package com.brabbelcode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias on 01.11.13.
 */
public class VoiceRecognizer implements RecognitionListener {

    /**
     * Members
     */
    private Context context;
    private SpeechRecognizer speech;
    private Intent intent;
    private boolean isPresent;
    private EditText output;

    /**
     * Ctor
     */
    public VoiceRecognizer(Context context){
        this.context = context;
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        if(activities.size() == 0)
            isPresent = false;
        else
            isPresent = true;

        init();
    }

    /**
     * Init
     */
    private void init()
    {
        if(isPresent){
            speech = SpeechRecognizer.createSpeechRecognizer(context);
            speech.setRecognitionListener(this);

            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        }
    }

    /**
     * Methods
     */
    public void start(){
        speech.startListening(intent);
    }

    public void stop(){
        speech.stopListening();
    }

    public void cancel(){
        speech.cancel();
    }

    public void destroy(){
        speech.destroy();
    }

    /**
     * Setter
     */
    public void setOutput(EditText editText){
        output = editText;
    }

    /**
     * Getter
     */
    public boolean getIsPresent(){
        return isPresent;
    }

    /**
     * Events
     */
    @Override
    public void onBeginningOfSpeech()
    {
    }

    @Override
    public void onBufferReceived(byte[] arg0)
    {
    }

    @Override
    public void onEndOfSpeech()
    {
    }

    @Override
    public void onError(int e)
    {
        if(e == SpeechRecognizer.ERROR_AUDIO)
            Toast.makeText(context, "Audio Error", Toast.LENGTH_SHORT).show();
        else if(e == SpeechRecognizer.ERROR_CLIENT)
            Toast.makeText(context, "Client Error", Toast.LENGTH_SHORT).show();
        else if(e == SpeechRecognizer.ERROR_NETWORK)
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
        else if(e == SpeechRecognizer.ERROR_NO_MATCH){
            Toast.makeText(context, "No Match", Toast.LENGTH_SHORT).show();
            start();
        }
        else if(e == SpeechRecognizer.ERROR_SERVER)
            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEvent(int arg0, Bundle arg1)
    {
    }

    @Override
    public void onPartialResults(Bundle arg0)
    {
    }

    @Override
    public void onReadyForSpeech(Bundle arg0)
    {
    }

    @Override
    public void onResults(Bundle data)
    {
        ArrayList<String> matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        InputTranslator translator = new InputTranslator();


        if(!matches.isEmpty())
        {
            output.setText(output.getText() + "\n"+translator.translateInput(matches.get(0)));

            /*if(matches.get(0).contains("new line"))
                output.setText(output.getText() + "\n");
            else if(matches.get(0).contains("tab"))
                output.setText(output.getText() + "\t");
            else if(matches.get(0).contains("open round bracket"))
                output.setText(output.getText() + "(");
            else if(matches.get(0).contains("closed round bracket"))
                output.setText(output.getText() + ")");
            else if(matches.get(0).contains("open curly bracket"))
                output.setText(output.getText() + "{");
            else if(matches.get(0).contains("closed curly bracket"))
                output.setText(output.getText() + "}");
            else if(matches.get(0).contains("make class person"))
                output.setText(output.getText() + "public class person{\n\n\tpublic person(){\n\t}\n\n}");
            else
                output.setText(output.getText() + " " + matches.get(0) + "\n");
                */
        }

        start();

    }

    @Override
    public void onRmsChanged(float arg0)
    {
    }
}