package com.example.ensai.glm;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

import static com.example.ensai.glm.R.*;
import static java.security.AccessController.getContext;

/**
 * Created by ensai on 15/05/17.
 */

public class Prefer extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton boutonFR;
    private RadioButton boutonEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( layout.activity_prefer);

        radioGroup = (RadioGroup) findViewById( id.radioLanguesGroup );
        boutonFR = (RadioButton) findViewById( id.radioFR );
        boutonEN = (RadioButton) findViewById( id.radioEN );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    public void retourAppli(View v){
        this.finish();
    }

    public void changeLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
    }


    public void onRadioButtonClicked(View view) {

        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId==boutonFR.getId()){
            changeLocale("fr" );
        } else {
            changeLocale( "en" );
        }
    }
}
