package com.example.copyandtranslate;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Alexander on 3/30/2015.
 */



public class CtController extends Application {

    private CtModel mCtModel = null;
    private ArrayList<HistoryListModel> mHistoryListModelArray = null;

    private SharedPreferences mSharedPrefs;

    private Button mSourceLanguageButton;
    private Button mTargetLanguageButton;
    private Button mToButton;
    private Switch mActiveSwitch;
    private ListView mHistoryListView;
    private BaseAdapter mHistoryListViewAdapter = null;
    private Notification.Builder mNotificationBuilder;

    final private int mHistorySizeLimit = 100; // Limit translation history

    public void Initialize()
    {
        if (mCtModel != null)
            return;

        mSharedPrefs = this.getSharedPreferences("CopyAndTranslatePrefs", Context.MODE_PRIVATE);
        boolean active = mSharedPrefs.getBoolean("Active", true);
        String sourceLanguage = mSharedPrefs.getString("SourceLanguage", "English");
        String targetLanguage = mSharedPrefs.getString("TargetLanguage", "English");
        String historyListModelJson = mSharedPrefs.getString("HistoryList", "");

        if (true) {
            Gson gson = new Gson();
            HistoryListModel[] historyListModelArray = gson.fromJson(historyListModelJson, HistoryListModel[].class);
            mHistoryListModelArray = new ArrayList<HistoryListModel>();
            if (historyListModelArray != null)
                mHistoryListModelArray.addAll(Arrays.asList(historyListModelArray));
        }
        else
        {
            mHistoryListModelArray = new ArrayList<HistoryListModel>();
        }

        mCtModel = new CtModel(active, sourceLanguage, targetLanguage);
     }

    public void UpdateAllViews()
    {
        mActiveSwitch.setChecked(mCtModel.getActive());
        mSourceLanguageButton.setText(mCtModel.getSourceLanguageName());
        mTargetLanguageButton.setText(mCtModel.getTargetLanguageName());
    }

    public ArrayList<String> getLanguageNames() { return mCtModel.getLanguageNames(); }

    public int getSourceLanguageIndex() { return mCtModel.getSourceLanguageIndex(); }
    public void setSourceLanguageIndex(int index)
    {
        mCtModel.setSourceLanguageIndex(index);

        String languageName =  mCtModel.getSourceLanguageName();

        // Change preferences
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString("SourceLanguage", languageName);
        prefsEditor.apply();

        // Notify view of CtModelData change
        mSourceLanguageButton.setText(languageName);
    }

    public int getTargetLanguageIndex() { return mCtModel.getTargetLanguageIndex(); }
    public void setTargetLanguageIndex(int index)
    {
        mCtModel.setTargetLanguageIndex(index);

        String languageName =  mCtModel.getTargetLanguageName();

        // Change preferences
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString("TargetLanguage", languageName);
        prefsEditor.apply();

        // Notify view of CtModelData change
        mTargetLanguageButton.setText(languageName);
    }

    public void swapLanguages()
    {
        int sourceLanguageIndex = mCtModel.getTargetLanguageIndex();
        int targetLanguageIndex = mCtModel.getSourceLanguageIndex();

        mCtModel.setSourceLanguageIndex(sourceLanguageIndex);
        mCtModel.setTargetLanguageIndex(targetLanguageIndex);


        // Change preferences
        String sourceLanguageName =  mCtModel.getSourceLanguageName();
        String targetLanguageName =  mCtModel.getTargetLanguageName();
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString("SourceLanguage", sourceLanguageName);
        prefsEditor.putString("TargetLanguage", targetLanguageName);
        prefsEditor.apply();

        // Notify views of CtModelData change
        mSourceLanguageButton.setText(sourceLanguageName);
        mTargetLanguageButton.setText(targetLanguageName);
    }

    public boolean getActive() { return mCtModel.getActive(); }
    public void setActive(boolean active)
    {
        mCtModel.setActive(active);

        // Change preferences
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putBoolean("Active", mCtModel.getActive() );
        prefsEditor.apply();

        mActiveSwitch.setChecked( mCtModel.getActive());
    }

    public ArrayList<HistoryListModel> getHistoryListModelArray() { return mHistoryListModelArray; }
    public void addTranslationToHistory(String sourceText, String translatedText)
    {
        HistoryListModel m = new HistoryListModel();
        m.setSourceText(sourceText);
        m.setTranslatedText(translatedText);

        // add data to history
        mHistoryListModelArray.add(0, m);
        if (mHistoryListModelArray.size() > mHistorySizeLimit)
            mHistoryListModelArray.remove(mHistorySizeLimit);

        // Build json string from mHistoryListModelArray to add later to SharedPreferences
        Gson gson = new Gson();
        String historyListModelJson = gson.toJson(mHistoryListModelArray.toArray(), HistoryListModel[].class);

        // Change preferences
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString("HistoryList", historyListModelJson);
        prefsEditor.apply();

        // Notify ListView about data change
        if (mHistoryListViewAdapter != null)
            mHistoryListViewAdapter.notifyDataSetChanged();

        // Update Notification
        if (mNotificationBuilder != null) {
            mNotificationBuilder.setContentTitle(sourceText);
            mNotificationBuilder.setContentText(translatedText);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, mNotificationBuilder.build());
        }
    }

    public void clearHistory()
    {
        mHistoryListModelArray.clear();

        // Build json string from mHistoryListModelArray to add later to SharedPreferences
        Gson gson = new Gson();
        String historyListModelJson = gson.toJson(mHistoryListModelArray.toArray(), HistoryListModel[].class);

        // Change preferences
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString("HistoryList", historyListModelJson);
        prefsEditor.apply();

        // Notify ListView about data change
        if (mHistoryListViewAdapter != null)
            mHistoryListViewAdapter.notifyDataSetChanged();
    }

    public void setSourceLanguageButton(Button b)
    {
        mSourceLanguageButton = b;
    }
    public void setTargetLanguageButton(Button b)
    {
        mTargetLanguageButton = b;
    }
    public void setToButton(Button b)
    {
        mToButton = b;
    }
    public void setActiveSwitch(Switch activeSwitch) { mActiveSwitch = activeSwitch; }
    public void setHistoryListViewAdapter(BaseAdapter historyListViewAdapter) { mHistoryListViewAdapter = historyListViewAdapter; }
    public void setNotificationBuilder(Notification.Builder notificationBuilder) { mNotificationBuilder = notificationBuilder; }

}
