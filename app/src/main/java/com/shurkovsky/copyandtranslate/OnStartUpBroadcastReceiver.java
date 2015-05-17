package com.shurkovsky.copyandtranslate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnStartUpBroadcastReceiver extends BroadcastReceiver {

    @Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

	        Intent i = new Intent(context, ManagerService.class);
            context.startService(i);

	    }
		
	}

}
