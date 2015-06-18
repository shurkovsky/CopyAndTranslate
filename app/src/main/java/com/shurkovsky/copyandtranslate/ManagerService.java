package com.shurkovsky.copyandtranslate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

public class ManagerService extends Service {

    private CtController mCtController;

    @Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
        Log.i("SHURIK", "ManagerService.onCreate");
		super.onCreate();

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        mCtController = (CtController) getApplicationContext();
        mCtController.setManagerservice(this);
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

        Log.i("SHURIK", "ManagerService.onStartCommand");
        super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            String action = intent.getAction();
            if (action == "OnCheckBoxClicked") {
                Log.i("SHURIK", "ManagerService.onStartCommand - OnCheckBoxClicked");
                mCtController.toggleActive();
                return START_STICKY;
            } else if (action == "StartUpBroadcast" && !mCtController.getRunAtStartup())
                mCtController.quit();
        }

        CharSequence title = getText(R.string.app_name);
        CharSequence subTitle = "Shortcut to " + title;

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_copy_and_translate_app);
        remoteViews.setTextViewText(R.id.notification_source_text, title);
        remoteViews.setTextViewText(R.id.notification_translated_text, subTitle);

        // Create intent for "On" check box click
        Intent resultIntent = new Intent(this, ManagerService.class);
        resultIntent.setAction("OnCheckBoxClicked");
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.check_box_on_imageButton, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.check_box_on_layout, pendingIntent);

        // Create intent for starting main activity on notification click
        resultIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_copy_and_translate_app)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setContent(remoteViews);

        mCtController.setNotificationBuilder(builder);
        mCtController.setNotificationRemoteViews(remoteViews);
        mCtController.refreshActive();


 /*       // here to show that your service is running foreground
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
*//*
        Notification notification = builder.build();

        this.startForeground(1, notification);
*//*

        mCtController.setNotificationBuilder(builder);
        mCtController.updateNotification();
*/

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Log.i("SHURIK", "ManagerService.onDestroy");
        mCtController.hideNotification();
    }

    // called on copy to clipboard event
  	public void showTranslation() {
		if (!mCtController.getActive())
            // do nothing if not active (translation is not turned on)
            return;

        // Show popup window with translation
        Intent intent = new Intent(this, ShowTranslationActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
