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
import java.util.Arrays;
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
    private void init(){
        if(isPresent){
            speech = SpeechRecognizer.createSpeechRecognizer(context);
            speech.setRecognitionListener(this);

            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            isSpeaking = false;
            handler = new Handler();
            this.replacer = PlaceholderReplacer.getInstance();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!isSpeaking)
                speech.startListening(intent);

            handler.postDelayed(this, 2000);
        }
    };

    /**
     * Public Methods
     */
    public void start(){
        status.setText("Starting voice recognizer...");
        handler.post(runnable);
    }

    public void stop(){
        speech.cancel();
        handler.removeCallbacks(runnable);
        isSpeaking = false;
        status.setText("Voice recognizer is off!");
    }

    public void cancel(){
        speech.cancel();
    }

    public void destroy(){
        speech.destroy();
    }

    public void setOutput(EditText editText){
        this.output = editText;

        SelectionHandler.getInstance().init(editText);
        DeletionHandler.getInstance().init(editText);
        PlaceholderReplacer.getInstance().init(editText);
    }

    public void setDebug(EditText editText){
        this.debugBox = editText;
    }

    public void setStatus(TextView textView){
        status = textView;
    }

    public boolean getIsPresent(){
        return isPresent;
    }

    /**
     * Events
     */
    @Override
    public void onBeginningOfSpeech(){
        isSpeaking = true;
        status.setText("You are speaking...");
    }

    @Override
    public void onBufferReceived(byte[] arg0){
    }

    @Override
    public void onEndOfSpeech(){
        isSpeaking = false;
        status.setText("As you wish, my lord.");
    }

    @Override
    public void onError(int e){
        if(e == SpeechRecognizer.ERROR_AUDIO)
            status.setText("Audio Error");
        else if(e == SpeechRecognizer.ERROR_CLIENT)
            status.setText("Client Error");
        else if(e == SpeechRecognizer.ERROR_NETWORK)
            status.setText("Network Error");
        else if(e == SpeechRecognizer.ERROR_NO_MATCH){
            status.setText("No Match");
            speech.startListening(intent);
        }
        else if(e == SpeechRecognizer.ERROR_SERVER)
            status.setText("Server Error");
    }

    @Override
    public void onEvent(int arg0, Bundle arg1){
    }

    @Override
    public void onPartialResults(Bundle arg0){
    }

    @Override
    public void onReadyForSpeech(Bundle arg0){
        isSpeaking = false;
        status.setText("I´m listening...");
    }

    @Override
    public void onResults(Bundle data){
        ArrayList<String> matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        InputTranslator translator = new InputTranslator(false);

        String translatorResult = "";
        if(matches != null && !this.replacer.getReadystate())
        {
            boolean matchString = false;

            for(String s:matches)
            {
                String[] speechResultArray = s.split(" ");
                speechResultArray = Mode.getInstance().extractMode(speechResultArray);
                Enums.MODE mode = Mode.getInstance().getMode();

                if(mode == Enums.MODE.CREATE) {
                    translatorResult = translator.translateCreate(speechResultArray);
                    if(translatorResult.equals("comment")){
                        StringBuilder builder = new StringBuilder();
                        for(int i = 1; i < speechResultArray.length; i++) {
                            builder.append(" " + speechResultArray[i]);
                        }
                        output.getText().insert(output.getSelectionStart(), "//" + builder.toString() + " \n");
                        this.setSelection();
                    }
                    else{
                        output.getText().insert(output.getSelectionStart(), translatorResult + "\n");
                        this.replacer.setCommandToModify(translatorResult);
                        this.replacer.setReadyState(true);
                        this.setSelection();
                    }
                    break;
                } else if(mode == Enums.MODE.SELECT) {
                    translatorResult = translator.translateSelect(speechResultArray);
                    if(translatorResult.equals("all")) {
                        SelectionHandler.getInstance().selectAll();
                    } else if(translatorResult.equals("word")) {
                        SelectionHandler.getInstance().selectNextWord(speechResultArray);
                    } else if(translatorResult.equals("next")) {
                        SelectionHandler.getInstance().selectNext();
                    } else if(translatorResult.equals("line")) {
                        SelectionHandler.getInstance().selectLine(speechResultArray);
                    } else if(translatorResult.equals("none")) {
                        SelectionHandler.getInstance().selectNone();
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
                    output.getText().insert(output.getSelectionStart(), translatorResult + "\n");
                    break;
                } else if(mode == Enums.MODE.UNDO) {
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

        } else if (matches != null && this.replacer.getReadystate()) {
            String s = matches.get(0);
            String[] speechResultArray = s.split(" ");
            this.replacer.replace(speechResultArray);
            this.setSelection();
        }

        speech.startListening(intent);
    }

    @Override
    public void onRmsChanged(float arg0){
    }

    /**
     * Checks if there are placeholders or accessors to select and selects the next one.
     */
    private void setSelection() {
        if (SelectionHandler.getInstance().selectWord("XAccessorX")) {
            this.replacer.setAccessorIsSelected(true);
            this.replacer.setReturnValueIsSelected(false);
        } else if (SelectionHandler.getInstance().selectWord("XReturnValueX")) {
            this.replacer.setAccessorIsSelected(false);
            this.replacer.setReturnValueIsSelected(true);
        } else if (SelectionHandler.getInstance().selectWord("XPlaceholderX")) {
            this.replacer.setAccessorIsSelected(false);
            this.replacer.setReturnValueIsSelected(false);
        } else if (SelectionHandler.getInstance().selectWord("XLastPlaceholderX")) {
            int start = SelectionHandler.getInstance().getStartIndex();
            this.replacer.replaceSelection("");
            output.setSelection(start);
            this.replacer.setReadyState(false);
        } else {
            this.replacer.setReadyState(false);
            output.setSelection(output.getSelectionEnd());
            SelectionHandler.getInstance().setStartIndex(output.getSelectionEnd());
            SelectionHandler.getInstance().setEndIndex(output.getSelectionEnd());
        }
    }
}

