package com.example.copyandtranslate;

/**
 * Created by Alexander on 4/6/2015.
 */
public class HistoryListModel {

    private  String mSourceText="";
    private  String mTranslatedText="";

    /*********** Set Methods ******************/

    public void setSourceText(String sourceText)
    {
        mSourceText = sourceText;
    }

    public void setTranslatedText(String translatedText)
    {
        mTranslatedText = translatedText;
    }

    /*********** Get Methods ****************/

    public String getSourceText()
    {
        return mSourceText;
    }

    public String getTranslatedText()
    {
        return mTranslatedText;
    }

}
