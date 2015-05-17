package com.shurkovsky.copyandtranslate;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Alexander on 4/26/2015.
 */
public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);

        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final EditText editText = (EditText) this.findViewById(R.id.editText);
        Button copyToClipboardButton = (Button) this.findViewById(R.id.copyToClipboardButton);
        copyToClipboardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                this.copyTextToClipboard();
            }

            private void copyTextToClipboard() {
                String text = editText.getText().toString();
                ClipData clip = ClipData.newPlainText("Text to translate",text);
                clipboard.setPrimaryClip(clip);
            }

        });
        copyToClipboardButton.setTextColor(Color.WHITE);

    }

}
