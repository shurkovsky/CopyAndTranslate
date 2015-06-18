package com.shurkovsky.copyandtranslate;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by Alexander on 6/11/2015.
 */
class NoYesAdapter {
    public void NoHandler(Dialog d) {};
    public void YesHandler(Dialog d) {};
}

public class NoYesDialog extends Dialog implements View.OnClickListener {

    Activity mCallingActivity;
    Button btNo;
    Button btYes;
    NoYesAdapter mNoYesAdapter;

    CtController mCtController;


    NoYesDialog(Activity a, NoYesAdapter i) {
        super(a);

        mCallingActivity = a;
        mNoYesAdapter = i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_yes_dialog_layout);

        btYes = (Button) findViewById(R.id.button_yes);
        btNo = (Button) findViewById(R.id.button_no);
        btYes.setOnClickListener(this);
        btNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_yes:
                mNoYesAdapter.YesHandler(this);
                dismiss();
                break;
            case R.id.button_no:
                mNoYesAdapter.NoHandler(this);
                dismiss();
                break;
            default:
                break;
        }
    }

}