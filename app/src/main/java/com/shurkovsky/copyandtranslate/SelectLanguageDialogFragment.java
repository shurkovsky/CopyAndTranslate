package com.shurkovsky.copyandtranslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Alexander on 3/28/2015.
 */
public class SelectLanguageDialogFragment extends DialogFragment {

    private CtController mCtController;
    ArrayList<String> mLanguageNames = new ArrayList<String>();

    public void setCtController(CtController ctController)
    {
            mCtController = ctController;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle=getArguments();

        //here is your list array
        final ArrayList<String> languageNames = mCtController.getLanguageNames();
        final String sourceOrTargetLanguage = bundle.getString("SourceOrTargetLanguage");

        final CharSequence[] chars = languageNames.toArray(new CharSequence[mLanguageNames.size()]);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int index = sourceOrTargetLanguage == "source"
                ? mCtController.getSourceLanguageIndex()
                : mCtController.getTargetLanguageIndex();

        builder.setTitle("Choose " + sourceOrTargetLanguage + " language")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected

                .setSingleChoiceItems(chars, index,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (sourceOrTargetLanguage == "source")
                                    mCtController.setSourceLanguageIndex(which);
                                else
                                    mCtController.setTargetLanguageIndex(which);

                                dismiss();
                            }
                        });


        // Create the AlertDialog object and return it
        return builder.create();
    }
}

