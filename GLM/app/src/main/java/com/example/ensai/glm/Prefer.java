package com.example.ensai.glm;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Locale;

import static com.example.ensai.glm.R.*;

/**
 * Created by ensai on 15/05/17.
 */

public class Prefer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( layout.activity_prefer);

        Button boutonFR = (Button) findViewById( id.radioFR );
        Button boutonEN = (Button) findViewById( id.radioEN );
    }


    public void retourAppli(View v){
        this.finish();
    }

    public void changeLocale(String lang, String pays){
        Locale locale = new Locale(lang, pays);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioFR:
                if (checked)
                    changeLocale( "fr", "FR");
                    break;
            case id.radioEN:
                if (checked)
                    changeLocale( "en", "EN");
                    break;
        }
    }
}
