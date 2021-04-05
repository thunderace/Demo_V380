package com.macrovideo.demo;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class RecordActivity extends FragmentActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(LocalDefines.notificationID);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_record);

        PlaybackFragment playbackFragment = new PlaybackFragment();

        fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fl_root, playbackFragment);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }
}
