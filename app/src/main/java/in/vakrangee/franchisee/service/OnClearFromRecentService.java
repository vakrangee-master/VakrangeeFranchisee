package in.vakrangee.franchisee.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.bumptech.glide.Glide;

import in.vakrangee.supercore.franchisee.utils.Connection;

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");

        clearGlildeCache();

        // Clear Token Id
        Connection c = new Connection(this);
        c.openDatabase();
        c.setTokenIdnull();

        //TODO: Below code is only for testing purpose
        /*Connection c = new Connection(this);
        c.openDatabase();
        String tokenId = c.getTokenId();
        Log.e("ClearFromRecentService", "Token : "+tokenId);
        c.setTokenIdnull();
        tokenId = c.getTokenId();
        Log.e("ClearFromRecentService", "Token : "+tokenId);*/

        stopSelf();
    }

    //region Clear Glide Cache
    public void clearGlildeCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(OnClearFromRecentService.this).clearDiskCache();
            }
        });
    }
    //endregion
}