package com.shurkovsky.copyandtranslate;

import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.os.IBinder;

public class ManagerService extends Service {

    private CtController mCtController;

    @Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        mCtController = (CtController) getApplicationContext();
        mCtController.Initialize();

		ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {

            @Override
        	public void onPrimaryClipChanged() {
        		showTranslation();
        	}

        });
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        // here to show that your service is running foreground
        Intent bIntent = new Intent(this, MainActivity.class);
        PendingIntent pbIntent = PendingIntent.getActivity(this, 0 , bIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);

        CharSequence title = getText(R.string.app_name);
        CharSequence subTitle = "Shortcut to " + title;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(subTitle)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentIntent(pbIntent);
/*
        Notification notification = builder.build();

        this.startForeground(1, notification);
*/

        mCtController.setNotificationBuilder(builder);
        mCtController.updateNotification();

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        mCtController.hideNotification();

    }

    // called on copy to clipboard event
  	public void showTranslation() {
		if (!mCtController.getActive())
            // do nothing if not active (translation is not turned on)
            return;

        // Show popup window with translation
        Intent intent = new Intent(this, ShowTranslationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		startActivity(intent);
	}

}
