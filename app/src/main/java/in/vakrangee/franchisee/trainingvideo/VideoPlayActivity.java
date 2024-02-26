package in.vakrangee.franchisee.trainingvideo;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import in.vakrangee.franchisee.R;

public class VideoPlayActivity extends YouTubeBaseActivity {
    private String videolink, youtube_id;
    private YouTubePlayerView myouTubePlayerView;
    private YouTubePlayer.OnInitializedListener monInitializedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

      /*  try {
            Bundle urllink = getIntent().getExtras();
            if (urllink != null) {
                videolink = urllink.getString("videolink");
                youtube_id = urllink.getString("youtube_id");
                myouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
                monInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(youtube_id);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };
                myouTubePlayerView.initialize("AIzaSyB2ksZwGPDMzq0OOjqNS3yGwUhe3djSNbQ", monInitializedListener);

            } else {
                AlertDialogBoxInfo.showOkDialog(getApplicationContext(), "Sorry! Link not open");
            }

        } catch (Exception e) {
            e.getMessage();
        }
*/

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
