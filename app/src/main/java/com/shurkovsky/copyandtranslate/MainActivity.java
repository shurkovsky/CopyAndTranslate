package com.shurkovsky.copyandtranslate;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MainActivity extends ListActivity implements OnCheckedChangeListener
 {
    private CtController mCtController;

    private Switch mActiveSwitch = null;
    private int mSourceLanguageIndex = -1;
    private int mTargetLanguageIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	ActionBar actionBar = getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.action_bar_layout);
        actionBar.setDisplayShowCustomEnabled(true);

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        mCtController = (CtController) getApplicationContext();
        mCtController.Initialize();

        // Setup Language buttons
        Button mSourceLanguageButton = (Button) this.findViewById(R.id.buttonSourceLanguage);
        mSourceLanguageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSelectLanguageDialog("source");
            }
        });
        mSourceLanguageButton.setTextColor(Color.WHITE);
        mCtController.setSourceLanguageButton(mSourceLanguageButton);

        Button mTargetLanguageButton = (Button)this.findViewById(R.id.buttonTargetLanguage);
        mTargetLanguageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSelectLanguageDialog("target");
            }
        });
        mTargetLanguageButton.setTextColor(Color.WHITE);
        mCtController.setTargetLanguageButton(mTargetLanguageButton);

        Button toButton = (Button) this.findViewById(R.id.buttonTo);
        toButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCtController.swapLanguages();
            }
        });
        toButton.setTextColor(Color.WHITE);
        mCtController.setToButton(toButton);

        mActiveSwitch = (Switch)actionBar.getCustomView().findViewById(R.id.switchActive);
        mActiveSwitch.setOnCheckedChangeListener(this);
        mCtController.setActiveSwitch(mActiveSwitch);

        // HistoryListView
        HistoryListAdapter adapter = new HistoryListAdapter(this, mCtController.getHistoryListModelArray(), getResources());
        setListAdapter(adapter);
        mCtController.setHistoryListViewAdapter(adapter);

        // Update language buttons and ActiveSwitch
        mCtController.UpdateAllViews();

        // Start service
        Intent intent = new Intent(this, ManagerService.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intent);
    }

    @Override
    public void onDestroy()
    {
        mCtController.setHistoryListViewAdapter(null);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {

         switch (item.getItemId()) {
             // Respond to the action bar buttons
             case R.id.action_clear_history:
                 clearHistoryWithConfirmation();
                 return true;
             case R.id.action_help:
                 // Show help activity
                 Intent intent = new Intent(this, HelpActivity.class);
                 startActivity(intent);

                 return true;
         }

         return super.onOptionsItemSelected(item);
     }

     private void clearHistoryWithConfirmation()
     {
         if (mCtController.getHistoryListModelArray().size() == 0)
             return;

         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("Clear Translation History?");
         builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 mCtController.clearHistory();
                 dialog.dismiss();
             }
         });
         builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 dialog.dismiss();
             }
         });
         AlertDialog dialog = builder.create();
         dialog.show();
     }

    public void showSelectLanguageDialog(String sourceOrTargetLanguage) {
        SelectLanguageDialogFragment newFragment = new SelectLanguageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SourceOrTargetLanguage", sourceOrTargetLanguage);
        newFragment.setArguments(bundle);
        newFragment.setCtController(mCtController);
        newFragment.show(getFragmentManager(), "SelectLanguage");
    }

    @Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.switchActive)
		{
            mCtController.setActive(arg1);
		}
	}
	
}
