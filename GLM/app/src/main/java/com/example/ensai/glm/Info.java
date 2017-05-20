package com.example.ensai.glm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ensai on 20/05/17.
 */

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }


    public void retourAppli(View v){
        this.finish();
    }

}