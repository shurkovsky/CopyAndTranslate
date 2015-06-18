package com.shurkovsky.copyandtranslate;

import com.memetix.mst.language.Language;

import java.util.ArrayList;

/**mSou
 * Created by Alexander on 3/30/2015.
 */
public class CtModel {
    private final ArrayList<String> mLanguageNames = new ArrayList<String>();
    private int mSourceLanguageIndex = -1;
    private int mTargetLanguageIndex = -1;
    private boolean mActive = true;
    private boolean mRunAtStartup = true;

    public CtModel(boolean runAtStartup, boolean active, String sourceLanguage, String targetLanguage)
    {
        Language.setClientId("CopyAndTranslate");
        Language.setClientSecret("oHnqF1qAHSQwU3CeBoyc1SYTa98jESXP75eYW/zPLjk=");

        mActive = active;
        mRunAtStartup = runAtStartup;

        Language[] languages = Language.values();
        int index = 0;
        for(Language lang : languages ) {
            try {
                String langName = lang.name();
                langName = langName.substring(0, 1).toUpperCase() + langName.substring(1).toLowerCase();
                mLanguageNames.add(langName);
                if (langName.equals(sourceLanguage))
                    mSourceLanguageIndex = index;
                if (langName.equals(targetLanguage))
                    mTargetLanguageIndex = index;
                index ++;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getLanguageNames() { return mLanguageNames; }

    public String getSourceLanguageName() {
        return mSourceLanguageIndex < 0 || mSourceLanguageIndex >= mLanguageNames.size()
                ? "" : mLanguageNames.get(mSourceLanguageIndex);
    }
    public String getTargetLanguageName() {
        return mTargetLanguageIndex < 0 || mTargetLanguageIndex >= mLanguageNames.size()
                ? "" : mLanguageNames.get(mTargetLanguageIndex);
    }

    public int getSourceLanguageIndex() { return mSourceLanguageIndex; }
    public void setSourceLanguageIndex(int index) { mSourceLanguageIndex = index; }

    public int getTargetLanguageIndex() { return mTargetLanguageIndex; }
    public void setTargetLanguageIndex(int index) { mTargetLanguageIndex = index; }

    public boolean getActive() { return mActive; }
    public void setActive(boolean active) { mActive = active; }

    public boolean getRunAtStartup() { return mRunAtStartup; }
    public void setRunAtStartup(boolean runAtStartup) { mRunAtStartup = runAtStartup; }
}
