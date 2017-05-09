package com.example.ensai.glm;

/**
 * Created by ensai on 09/05/17.
 */

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.RequiresApi;

public class Lecteur implements OnInitListener {

    private TextToSpeech tts;

    private boolean ready = false;

    private boolean allowed = false;

    public Lecteur(Context context){
        tts = new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            // Change this to match your
            // locale
            tts.setLanguage(Locale.US);
            ready = true;
        }else{
            ready = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speak(String text){

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if(ready && allowed) {
            HashMap<String, String> hash = new HashMap<String,String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pause(int duration){
        tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null );
    }

    // Free up resources
    public void destroy(){
        tts.shutdown();
    }


}
