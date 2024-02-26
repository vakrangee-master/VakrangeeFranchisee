package in.vakrangee.franchisee.gwr.dashboard;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class PlayMusicService extends Service {

    private static final String LOGCAT = null;
    private MediaPlayer objPlayer;

    public void onCreate() {
        super.onCreate();
        Log.d(LOGCAT, "PlayMusicService Service Started!");

    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            int rawMusic = intent.getIntExtra("RAW_MUSIC", -1);

            if (rawMusic != -1) {
                objPlayer = MediaPlayer.create(this, rawMusic);
                objPlayer.start();
                Log.d(LOGCAT, "Media Player started!");
                if (objPlayer.isLooping() != true) {
                    Log.d(LOGCAT, "Problem in Playing Audio");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void onStop() {
        if (objPlayer != null) {
            objPlayer.stop();
            objPlayer.release();
        }
    }

    public void onPause() {
        if (objPlayer != null) {
            objPlayer.stop();
            objPlayer.release();
        }
    }

    public void onDestroy() {
        if (objPlayer != null) {
            objPlayer.stop();
            objPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent objIndent) {
        return null;
    }
}
