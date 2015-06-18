package com.shurkovsky.copyandtranslate;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

// Pops up on top of current application when clipboard contents changes
public class ShowTranslationActivity extends Activity {
	
	SharedPreferences mSharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_layout);
        mSharedPrefs = this.getSharedPreferences("CopyAndTranslatePrefs", Context.MODE_PRIVATE);
	}

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

        class bgStuff extends AsyncTask<Void, Void, Void>{

            String mClipboardText = "";
            String mTranslatedText = "";
            boolean mTranslationError = false;
            
            @Override
            protected Void doInBackground(Void... params) {

                try {

                    Translate.setClientId("CopyAndTranslate");
                    Translate.setClientSecret("oHnqF1qAHSQwU3CeBoyc1SYTa98jESXP75eYW/zPLjk=");

                    String sourceLanguage = mSharedPrefs.getString("SourceLanguage", "English").toUpperCase();
                    String targetLanguage = mSharedPrefs.getString("TargetLanguage", "English").toUpperCase();
                    mTranslatedText = Translate.execute(mClipboardText, Language.valueOf(sourceLanguage), Language.valueOf(targetLanguage));

                } catch (Exception e) {

                    e.printStackTrace();
                    mTranslatedText = e.toString();
                    mTranslationError = true;

                }
                return null;
            }

            @Override
            protected void onPreExecute() {

                try {

                    ClipboardManager cbm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    mClipboardText = cbm.getPrimaryClip().getItemAt(0).getText().toString();
                    mTranslationError = false;

                } catch (Exception e) {
                    e.printStackTrace();
                    mTranslatedText = e.toString();
                    mTranslationError = true;
                }

            }

            @Override
            protected void onPostExecute(Void result) {

                try {

                    ((TextView) findViewById(R.id.textViewTranslation)).setText(mTranslatedText);

                } catch (Exception e) {
                    e.printStackTrace();
                    mTranslatedText = e.toString();
                    mTranslationError = true;
                }

                // add translation to history
                if (!mTranslationError)
                {
                    CtController ctController = (CtController) getApplicationContext();
                    ctController.addTranslationToHistory(mClipboardText, mTranslatedText );
                }

            }

        }

        // Create and execute bgStuff
        new bgStuff().execute();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }


}