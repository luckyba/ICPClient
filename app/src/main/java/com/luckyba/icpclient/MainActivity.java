package com.luckyba.icpclient;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import com.lucky.icp.IMusicService;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String MUSIC_ACTION = "com.luckyba.icpserver.service.MusicService.BIND";
    private static final String MUSIC_PACKAGE = "com.luckyba.icpserver";
    private static final String MUSIC_CLASS = "com.luckyba.icpserver.service.MusicService";

    private IMusicService mService;
    private boolean mIsServiceConnected;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IMusicService.Stub.asInterface(iBinder);
            mIsServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
        initViews();
    }

    private void bindService() {
        Intent intent = new Intent(MUSIC_ACTION);
        intent.setClassName(MUSIC_PACKAGE, MUSIC_CLASS);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initViews() {
        findViewById(R.id.button_pause).setOnClickListener(this);
        findViewById(R.id.button_play).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!mIsServiceConnected) {
            return;
        }
        switch (view.getId()) {
            case R.id.button_play:
                try {
                    mService.playSong();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button_pause:
                try {
                    mService.pause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        try {
            mService.stopService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
