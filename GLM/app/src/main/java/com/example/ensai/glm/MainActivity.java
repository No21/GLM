package com.example.ensai.glm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;

    private Button launchPrompt, readText;
    private Lecteur speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readText = (Button) findViewById(R.id.readText);
        readText.setOnClickListener(readListener);

        checkTTS();
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    View.OnClickListener readListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            speakOut();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        checkTTS();
    }

    private void speakOut() {
        if(!speaker.isSpeaking()) {
            speaker.speak(readFile(R.raw.martin_luther_king));
            speaker.pause(SHORT_DURATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHECK_CODE: {
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    speaker = new Speaker(this);
                } else {
                    Intent install = new Intent();
                    install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(install);
                }
            }
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        speaker.destroy();
        super.onStop();
    }

    private String readFile(int rawfile) {
        String result = new String();
        try {
            Resources res = getResources();
            InputStream input_stream = res.openRawResource(rawfile);

            byte[] b = new byte[input_stream.available()];
            input_stream.read(b);
            result = new String(b);
        } catch (Exception e) {
            Log.e("readFile", e.getMessage());
        }
        return result;
    }

}
