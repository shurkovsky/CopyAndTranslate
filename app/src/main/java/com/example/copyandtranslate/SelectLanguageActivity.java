package com.example.copyandtranslate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Alexander on 3/27/2015.
 */
public class SelectLanguageActivity  extends Activity {
    SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_language_layout);

        mSharedPrefs = this.getSharedPreferences("CopyAndTranslatePrefs", Context.MODE_PRIVATE);

        // Set title
        Intent intent = getIntent();
        String languageType = intent.getExtras().getString("languageType");
        String title = (languageType == "sourceLanguage") ? "Select source language" : "Select target language";
        setTitle(title);
    }
}
