package com.brabbelcode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VoiceRecognizer implements RecognitionListener {

    /**
     * Members
     */
    private Context context;
    private SpeechRecognizer speech;
    private Intent intent;
    private boolean isSpeaking;
    private Handler handler;
    private boolean isPresent;
    private EditText output;
    private EditText debugBox;
    private TextView status;
    private PlaceholderReplacer replacer;

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

            isSpeaking = false;
            handler = new Handler();
            handler.postDelayed(runnable, 2000);
            this.replacer = PlaceholderReplacer.getInstance();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(!isSpeaking)
                start();

            handler.postDelayed(this, 2000);
        }
    };

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
        this.output = editText;

        SelectionHandler.getInstance().init(editText);
        DeletionHandler.getInstance().init(editText);
    }

    public void setDebug(EditText editText){
        this.debugBox = editText;
    }

    public void setStatus(TextView textView){
        status = textView;
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
        isSpeaking = true;
        status.setText("You are speaking...");
    }

    @Override
    public void onBufferReceived(byte[] arg0)
    {
    }

    @Override
    public void onEndOfSpeech()
    {
        isSpeaking = false;
        status.setText("As you wish, my lord.");
    }

    @Override
    public void onError(int e)
    {
        if(e == SpeechRecognizer.ERROR_AUDIO)
            status.setText("Audio Error");
        else if(e == SpeechRecognizer.ERROR_CLIENT)
            status.setText("Client Error");
        else if(e == SpeechRecognizer.ERROR_NETWORK)
            status.setText("Network Error");
        else if(e == SpeechRecognizer.ERROR_NO_MATCH){
            status.setText("No Match");
            start();
        }
        else if(e == SpeechRecognizer.ERROR_SERVER)
            status.setText("Server Error");
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
        isSpeaking = false;
        status.setText("I´m listening...");
    }

    @Override
    public void onResults(Bundle data)
    {
        ArrayList<String> matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        InputTranslator translator = new InputTranslator(false);

        String translatorResult = "";
        if(matches != null && !this.replacer.getReadystate())
        {

            boolean matchString = false;

            for(String s:matches)
            {
                String [] speechResultArray = s.split(" ");
                speechResultArray = Mode.getInstance().extractMode(speechResultArray);
                Enums.MODE mode = Mode.getInstance().getMode();

                if(mode == Enums.MODE.CREATE) {
                    translatorResult = translator.translateCreate(speechResultArray);
                    output.setText(output.getText() + "\n"+ translatorResult);
                    break;
                } else if(mode == Enums.MODE.SELECT) {
                    translatorResult = translator.translateSelect(speechResultArray);
                    if(translatorResult.equals("all")) {
                        SelectionHandler.getInstance().selectAll();
                    } else if(translatorResult.equals("none")) {
                        SelectionHandler.getInstance().selectNone();
                    } else if(translatorResult.equals("line")) {
                        SelectionHandler.getInstance().selectLine(speechResultArray);
                    }
                    break;
                } else if(mode == Enums.MODE.DELETE) {
                    translatorResult = translator.translateDelete(speechResultArray);
                    if(translatorResult.equals("all")) {
                        DeletionHandler.getInstance().deleteAll();
                    } else if(translatorResult.equals("selection")) {
                        DeletionHandler.getInstance().deleteSelection();
                    }
                    break;
                } else if(mode == Enums.MODE.FREE) {
                    translatorResult = translator.translateFree(speechResultArray);
                    output.setText(output.getText() + "\n" + translatorResult);
                    break;
                }
                  else if(mode == Enums.MODE.UNDO) {
                    CodeHistory.getInstance().undo();
                    break;
                } else if(mode == Enums.MODE.REDO) {
                    CodeHistory.getInstance().redo();
                    break;
                }
            }

            if(translatorResult != "")
                matchString = true;
            debugBox.setText("match string: " + matchString);
            for(String s:matches){
                debugBox.setText(debugBox.getText() + "\n" + s);
            }

            //TODO: Bug -> wrong position in code, block is also executed on other commands
            if (translatorResult.contains("XPlaceholderX")) {
                if (this.replacer == null) {
                    this.replacer = PlaceholderReplacer.getInstance();
                }
                this.replacer.setCommandToModify(translatorResult);
                this.replacer.setReadyState(true);
            }

        } else if (matches != null && this.replacer.getReadystate()) {
            String s = matches.get(0);
            String [] speechResultArray = s.split(" ");
            translatorResult = this.replacer.replacePlaceholders(speechResultArray);
            this.replacer.setReadyState(false);
            CodeHistory.getInstance().replace(translatorResult);
        }

        start();

    }

    @Override
    public void onRmsChanged(float arg0)
    {
    }
}
