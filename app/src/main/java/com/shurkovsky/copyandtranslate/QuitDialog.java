package com.shurkovsky.copyandtranslate;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by Alexander on 6/11/2015.
 */
public class QuitDialog extends Dialog implements View.OnClickListener {
    Activity mCallingActivity;
    Button btNo;
    Button btYes;
    CheckBox checkBoxRunAtStartup;

    CtController mCtController;

    QuitDialog(Activity a) {
        super(a);

        mCallingActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quit_dialog_layout);

        setTitle(R.string.quitTitle);

        btYes = (Button) findViewById(R.id.button_quit_yes);
        btNo = (Button) findViewById(R.id.button_quit_no);
        checkBoxRunAtStartup = (CheckBox) findViewById(R.id.checkBox_runAtStartup);
        btYes.setOnClickListener(this);
        btNo.setOnClickListener(this);
        checkBoxRunAtStartup.setOnClickListener(this);

        mCtController = (CtController) mCallingActivity.getApplicationContext();
        mCtController.setRunAtStartupCheckBox(checkBoxRunAtStartup);
        mCtController.refreshRunAtStartup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox_runAtStartup:
                CheckBox c = (CheckBox) v;
                mCtController.setRunAtStartup(c.isChecked());
                break;
            case R.id.button_quit_yes:
                mCtController.quit();
                break;
            case R.id.button_quit_no:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

}