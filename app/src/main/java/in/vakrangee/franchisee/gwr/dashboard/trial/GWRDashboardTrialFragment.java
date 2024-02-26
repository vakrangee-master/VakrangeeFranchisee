package in.vakrangee.franchisee.gwr.dashboard.trial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.mapzen.speakerbox.Speakerbox;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.dashboard.AsyncSaveGWRDashboardData;
import in.vakrangee.franchisee.gwr.dashboard.AsyncSaveInaugurationPhoto;
import in.vakrangee.franchisee.gwr.dashboard.CheckGuinnessWorldRecordAsyncTask;
import in.vakrangee.franchisee.gwr.dashboard.GWRDashboardDto;
import in.vakrangee.franchisee.gwr.dashboard.InaugurationAsyncTask;
import in.vakrangee.franchisee.gwr.dashboard.PlayMusicService;
import in.vakrangee.franchisee.gwr.evidence.EvidenceRepository;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class GWRDashboardTrialFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "GWRDashboardTrialFragment";
    private View view;
    private Connection connection;
    private Context context;

    @BindView(R.id.txtGuinnesRecord)
    LinearLayout txtGuinnesRecord;

    private Vibrator vibrator;
    private GWRDashboardDto gwrDashboardDto;
    private AsyncSaveGWRDashboardData asyncSaveGWRDashboardData = null;
    private InaugurationAsyncTask inaugurationAsyncTask = null;
    private CheckGuinnessWorldRecordAsyncTask checkGuinnessWorldRecordAsyncTask = null;
    private AsyncSaveInaugurationPhoto asyncSaveInaugurationPhoto = null;

    // Update Server Date Time manually
    private Timer timerDateTime;
    private long serverDateTime;
    private long inaugurationTime;

    private Animation marquee;

    private TextView textViewFooter;

    private LinearLayout layoutDay, layoutHours, layoutMinutes, layoutSec;
    private LinearLayout layoutCoundDown, layoutInaugurationImage, layoutHeaderContent, layoutDateTime;
    private TextView days, hours, mins, seconds;
    private TextView txtCurrentDateTime;
    private TextView textTimeOver;
    private LinearLayout layoutGWROfficialAttempt;
    private LinearLayout layoutGWREvent;
    private LinearLayout layoutInAuguraionButton;
    private TextView textCongrats;
    private TextView txtViewInaugurationDateTime;
    private ImageView imgCaputreImage;
    private static final int CAMERA_REQUEST = 1888;
    private PermissionHandler permissionHandler;
    private Uri picUri;                 //Picture URI

    private static final String UNMUTE = "1";
    private static final String MUTE = "2";
    private DeprecateHandler deprecateHandler;
    private EvidenceRepository evidenceRepository;

    private String mCurrentPhotoPath;

    private Speakerbox speakerbox;
    private VakrangeeKendraApplication vkApp;
    private CountDownTimer startCountDown = null;
    private CountDownTimer finishCountDownT = null;
    private ImageView imgGifImage;

    public GWRDashboardTrialFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_gwr_dashboard_trial, container, false);
        final Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        //Initialize data
        this.context = getContext();
        connection = new Connection(context);
        ButterKnife.bind(this, view);
        permissionHandler = new PermissionHandler(getActivity());
        deprecateHandler = new DeprecateHandler(context);
        vkApp = (VakrangeeKendraApplication) context.getApplicationContext();
        speakerbox = new Speakerbox(vkApp);

        evidenceRepository = new EvidenceRepository(context);
        // Layout
        timerDateTime = new Timer();

        //Animation
        marquee = AnimationUtils.loadAnimation(context, R.anim.marquee);

        // Header and Footer
        textViewFooter = view.findViewById(R.id.textViewFooter);
        textViewFooter.startAnimation(marquee);

        // Layout
        layoutDay = (LinearLayout) view.findViewById(R.id.layoutDay);
        layoutHours = (LinearLayout) view.findViewById(R.id.layoutHours);
        layoutMinutes = (LinearLayout) view.findViewById(R.id.layoutMinutes);
        layoutSec = (LinearLayout) view.findViewById(R.id.layoutSec);
        layoutCoundDown = (LinearLayout) view.findViewById(R.id.layoutCoundDown);
        layoutDateTime = (LinearLayout) view.findViewById(R.id.layoutDateTime);
        layoutHeaderContent = (LinearLayout) view.findViewById(R.id.layoutHeaderContent);
        layoutInaugurationImage = (LinearLayout) view.findViewById(R.id.layoutInaugurationImage);
        textTimeOver = (TextView) view.findViewById(R.id.textTimeOver);
        layoutGWROfficialAttempt = view.findViewById(R.id.layoutGWROfficialAttempt);
        layoutGWREvent = view.findViewById(R.id.layoutGWREvent);
        textCongrats = view.findViewById(R.id.textCongrats);
        txtViewInaugurationDateTime = view.findViewById(R.id.txtViewInaugurationDateTime);
        imgGifImage = view.findViewById(R.id.imgGifImage);

        // CountDown Timer
        days = (TextView) view.findViewById(R.id.days);
        hours = (TextView) view.findViewById(R.id.hours);
        mins = (TextView) view.findViewById(R.id.minutes);
        seconds = (TextView) view.findViewById(R.id.seconds);
        txtCurrentDateTime = (TextView) view.findViewById(R.id.txtCurrentDateTime);
        layoutInAuguraionButton = view.findViewById(R.id.layoutInAuguraionButton);

        //Widgets
        txtGuinnesRecord = view.findViewById(R.id.txtGuinnesRecord);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);


        // nilesh  - image capture
        imgCaputreImage = (ImageView) view.findViewById(R.id.imgCaptureImage);

        return view;
    }

    public void refresh(GWRDashboardDto gwrDashboardDto) {
        this.gwrDashboardDto = gwrDashboardDto;

        /*if (!TextUtils.isEmpty(gwrDashboardDto.getTime_up()) && gwrDashboardDto.getTime_up().equalsIgnoreCase("1")) {
            layoutGWREvent.setVisibility(View.GONE);
            layoutGWROfficialAttempt.setVisibility(View.VISIBLE);
            textCongrats.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55);
            textCongrats.setText("!!! Time UP !!!");

            //Start Service to upload Raw Images
            //context.startService(new Intent(context, UploadEvidenceFilesService.class));

        } else if (gwrDashboardDto.getIsInAugurationDone().equalsIgnoreCase("1")) {
            layoutGWREvent.setVisibility(View.GONE);
            layoutGWROfficialAttempt.setVisibility(View.VISIBLE);

            String vkId = gwrDashboardDto.getVkid();        //jsonObject.optString("vkid", "");
            String frName = gwrDashboardDto.getFr_name();   //jsonObject.optString("fr_name", null);
            String msg = "";
            Log.e(TAG, "VKID: " + vkId + " FR Name: " + frName);
            if (TextUtils.isEmpty(frName)) {
                msg = "!!! Congratulation !!! <br> " + vkId;
            } else {
                msg = "!!! Congratulation !!! <br> " + frName + " <br> (" + vkId + ")";
            }

            Log.e(TAG, "Msg : " + msg);
            // Show Congratulations Message.
            textCongrats.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            textCongrats.setText(Html.fromHtml(msg));

            //Start Service to upload Raw Images
            //context.startService(new Intent(context, UploadEvidenceFilesService.class));

        } else */
        {
            layoutGWREvent.setVisibility(View.VISIBLE);
            layoutGWROfficialAttempt.setVisibility(View.GONE);

            init();
        }
    }

    @OnClick({R.id.txtGuinnesRecord, R.id.imgCaptureImage})
    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.txtGuinnesRecord) {
            AnimationHanndler.bubbleAnimation(context, view);
            vibrator.vibrate(80);

            //Save GWR InAuguration Data
            //save();

            //Open Camera and post photo
            imgCapture();
        } /*else if (Id == R.id.imgCaptureImage) {
            //image capture
            imgCapture();

        } else if (Id == R.id.layoutVolumeOnOff) {
            String tag = (String) imgVolumeOnOff.getTag();
            tag = TextUtils.isEmpty(tag) ? UNMUTE : tag;

            if (tag.equalsIgnoreCase(UNMUTE)) {
                imgVolumeOnOff.setTag(MUTE);
                imgVolumeOnOff.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_volume_off_white_24dp));
                speakerbox.mute();

            } else {
                imgVolumeOnOff.setTag(UNMUTE);
                imgVolumeOnOff.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_volume_up_white_24dp));
                speakerbox.unmute();
            }

        } else if (Id == R.id.parentLinearlytTrialButton) {
            showInAugurationTrialDialog("");
        }*/
    }

    private void save() {

        // Get Device Date Time in
        gwrDashboardDto.setDeviceDateTime(System.currentTimeMillis() + "");

        Gson gson = new Gson();
        String jsonData = gson.toJson(gwrDashboardDto, GWRDashboardDto.class);

        asyncSaveGWRDashboardData = new AsyncSaveGWRDashboardData(context, connection.getVkid(), jsonData, new AsyncSaveGWRDashboardData.Callback() {
            @Override
            public void onResult(String result) {
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    return;
                }

                // Handle Error Response From Server.
                if (result.startsWith("ERROR|")) {

                    StringTokenizer tokens = new StringTokenizer(result, "|");
                    tokens.nextToken();     // Jump to next Token
                    String errMsg = tokens.nextToken();

                    if (!TextUtils.isEmpty(errMsg)) {
                        AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                    }
                    return;
                }

                //Process response
                if (result.startsWith("OKAY|")) {
                    StringTokenizer st1 = new StringTokenizer(result, "|");
                    String key = st1.nextToken();
                    String data = st1.nextToken();

                    if (TextUtils.isEmpty(data)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process Response
                    Gson gson = new Gson();
                    gwrDashboardDto = gson.fromJson(data, GWRDashboardDto.class);
                    refresh(gwrDashboardDto);
                }
            }
        });
        asyncSaveGWRDashboardData.execute("");
    }

    public void init() {

        // Step1: Check Internet Connection
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        //Step2: Send Request to Server to get Current Server Date Time.
        String macAddress = TextUtils.isEmpty(gwrDashboardDto.getMac_address()) ? gwrDashboardDto.getVkid() : gwrDashboardDto.getMac_address();
        inaugurationAsyncTask = new InaugurationAsyncTask(context, new InaugurationAsyncTask.IInaugurationResult() {
            @Override
            public void resposne(String result, String error) {
                if (!TextUtils.isEmpty(error)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), error);
                    return;
                } else if (!TextUtils.isEmpty(result)) {
                    try {
                        // Process Response
                        JSONObject jsonObject = new JSONObject(result);
                        String isActive = jsonObject.optString("is_active", "N"); // Y : Event Remaining | N : Event Closed
                        if (isActive.equals("Y")) {

                            // Extract Response Data
                            serverDateTime = jsonObject.getLong("server_time");
                            inaugurationTime = jsonObject.getLong("inauguration_time");
                            txtViewInaugurationDateTime.setText("on " + formatedServerDate(inaugurationTime));
                            long countDownTimer = inaugurationTime - serverDateTime;
                            Log.e(TAG, "Server TimeStamp: " + serverDateTime + " | Inauguration TimeStamp " + inaugurationTime + " | Count Down Timer : " + countDownTimer);

                            // Show CountDown Timer View
                            layoutCoundDown.setVisibility(View.VISIBLE);

                            // Start Date Time and CountDown Timer
                            startDateTimeTask();
                            startCountDown(countDownTimer);
                        } else {
                            //TODO: Show Message of Event Completion.
                            Log.e(TAG, "Inauguration Completed.");
                        }
                    } catch (Exception e) {
                        //speakerbox.play("Something went wrong. Please contact Support Team.");
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        e.printStackTrace();
                    }
                } else {
                    //speakerbox.play("Something went wrong. Please contact support team.");
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        inaugurationAsyncTask.execute(macAddress);
        // Step3: Start Count Down Time
    }

    /**
     * Start Count Down Timer
     */
    public void startCountDown(long countDownTimer) {
        /*
        172800000 milliseconds = 5days
        1000(1sec) is time interval to call onTick method
        millisUntilFinished is amount of until finished
        */

        Log.e(TAG, "Count Down Timer Started.");

        // 172800000
        // countDownTimer = 24 * 60 * 60 * 1000; //  Day
        //countDownTimer = 90 * 60 * 1000; // Hours
        //countDownTimer = 2 * 60 * 1000; // Minutes
        countDownTimer = 75 * 1000;  // 1 Minute
        //countDownTimer = 10 * 1000;  // 10 Seconds
        startCountDown = new CountDownTimer(countDownTimer, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                /*converting the milliseconds into days, hours, minutes and seconds and displaying it in textviews */
                long d = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                days.setText(d + "");

                long h = (TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)));
                if (d == 0) {
                    layoutDay.setVisibility(View.GONE);
                    hours.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    mins.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    seconds.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                }
                hours.setText(h + "");

                long m = (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                if (d == 0 && h == 0) {
                    layoutDay.setVisibility(View.GONE);
                    layoutHours.setVisibility(View.GONE);
                    mins.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
                    seconds.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
                }
                mins.setText(m + "");

                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                if (d == 0 && h == 0 && m == 0) {
                    seconds.setTextSize(TypedValue.COMPLEX_UNIT_SP, 82);
                }
                seconds.setText(sec + "");

                //Set Current Date Time
                txtCurrentDateTime.setText(formatedServerDate(serverDateTime));

                // Start Speaking Before 5 minutes
                if (d == 0 && h == 0 && (m < 6 && m != 0)) {
                    layoutDay.setVisibility(View.GONE);
                    layoutHours.setVisibility(View.GONE);
                    if (sec == 0) {
                        //speakerbox.play(m + " minutes to go.");
                    }
                }

                // Start Speaking Time (1min)
                if (d == 0 && h == 0 && m == 0) {
                    layoutDay.setVisibility(View.GONE);
                    layoutHours.setVisibility(View.GONE);
                    layoutMinutes.setVisibility(View.GONE);
                    layoutDateTime.setVisibility(View.GONE);
                    layoutHeaderContent.setVisibility(View.GONE);

                    //speakerbox.play(sec + "");
                    // Only 10 Seconds Left
                    /*if (sec == 11)
                        speakerbox.play("Only 10 seconds left.");*/
                }

                // Start Air Horn
                if (d == 0 && h == 0 && m == 0 && sec == 0) {

                    // Need to play music.

                }
            }

            @Override

            public void onFinish() {
                /*            clearing all fields and displaying countdown finished message             */

                days.setText("0");
                hours.setText("0");
                mins.setText("0");
                seconds.setText("0");

                /*// Start Service to Play Air Horn
                Intent objIntent = new Intent(context, PlayMusicService.class);
                objIntent.putExtra("RAW_MUSIC", R.raw.air_horn_mp3);
                startService(objIntent);

                // Start Service to Play Fireworks with Crowds
                Intent objIntent1 = new Intent(InaugurationActivity.this, PlayMusicService.class);
                objIntent1.putExtra("RAW_MUSIC", R.raw.fireworks_and_crowd_mp3);
                startService(objIntent1);*/

                // Hide Seconds
                layoutSec.setVisibility(View.GONE);

                // Show Inauguration Image
                layoutInaugurationImage.setVisibility(View.VISIBLE);

                // Start Another Finish CountDown Timer
                finishCountDownTimer();
            }

        };
        startCountDown.start();

    }

    //region Inauguration Count Down
    long finishCountDownTimer = 59 * 1000;

    public void finishCountDownTimer() {
        finishCountDownT = new CountDownTimer(finishCountDownTimer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                textTimeOver.setText(sec + " Seconds To Go...");

                /*if (sec == 10) {
                    speakerbox.playAndOnDone("10 seconds to go.", new Runnable() {
                        @Override
                        public void run() {
                            speakerbox.play("Hurry     ");
                        }
                    });
                }*/
            }

            @Override
            public void onFinish() {

                // Hide Inauguration Image and Show Time
                layoutInAuguraionButton.setVisibility(View.GONE);
                textTimeOver.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55);
                textTimeOver.setText("!!! Time UP !!!");
                textTimeOver.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                //speakerbox.play("Time up    Time up     Time up");
                //speakerbox.play("Congratulations Congratulations Congratulations");

                // Start Service to Play Air Horn
                Intent objIntent = new Intent(context, PlayMusicService.class);
                objIntent.putExtra("RAW_MUSIC", R.raw.air_horn_mp3);
                context.startService(objIntent);

                // Fetch Result
                //fetchInaugurationResult();

            }
        };
        finishCountDownT.start();
    }
    //endregion

    //region Fetch Result Via
    public void fetchInaugurationResult() {

        String macAddress = TextUtils.isEmpty(gwrDashboardDto.getMac_address()) ? gwrDashboardDto.getVkid() : gwrDashboardDto.getVkid();
        checkGuinnessWorldRecordAsyncTask = new CheckGuinnessWorldRecordAsyncTask(context, new CheckGuinnessWorldRecordAsyncTask.IGuinnessWorldRecordResult() {
            @Override
            public void resposne(String result, String error) {

                if (!TextUtils.isEmpty(error)) {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    //speakerbox.play(error);
                } else if (!TextUtils.isEmpty(result)) {

                    try {
                        Log.e(TAG, "fetchInaugurationResult Response : " + result);
                        // Process Response
                        // {"is_activity_over":0,"is_gwr_vkid":1,"is_inauguration_done":0,"guinness_franchisee_id":4,"vkid":"CG2207006","mac_address":"ec:3d:fd:ea:be:6a"}
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("is_inauguration_done")) {
                            int status = jsonObject.getInt("is_inauguration_done");
                            if (status == 1) {

                                String vkId = jsonObject.optString("vkid", "");
                                String frName = jsonObject.optString("fr_name", null);
                                String msg = "";
                                Log.e(TAG, "VKID: " + vkId + " FR Name: " + frName);
                                if (TextUtils.isEmpty(frName)) {
                                    msg = "!!! Congratulation !!! <br> " + vkId;
                                    //speakerbox.play("Congratulation ");
                                } else {
                                    msg = "!!! Congratulation !!! <br> " + frName + " <br> (" + vkId + ")";
                                    //speakerbox.play("Congratulation " + frName);
                                }

                                Log.e(TAG, "Msg : " + msg);
                                // Show Congratulations Message.
                                textTimeOver.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                textTimeOver.setText(Html.fromHtml(msg));

                                // Start Service to Play Air Horn
                                Intent objIntent = new Intent(context, PlayMusicService.class);
                                objIntent.putExtra("RAW_MUSIC", R.raw.fireworks_and_crowd_mp3);
                                context.startService(objIntent);

                            }
                        }

                    } catch (Exception e) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        //speakerbox.play("Something went wrong. Please contact Support Team.");
                        e.printStackTrace();
                    }
                }

            }
        });

        checkGuinnessWorldRecordAsyncTask.execute(macAddress);
    }
    //endregion

    //region Show Current Date Time

    /**
     * Start Date Time Task
     */
    public void startDateTimeTask() {

        txtCurrentDateTime.setText(formatedServerDate(serverDateTime));

        timerDateTime.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                serverDateTime = serverDateTime + (1000); //(60 * 1000)
                /*try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //txtCurrentDateTime.setText(formatedServerDate(serverDateTime));
                            //String currentDateTime = formatedServerDate(serverDateTime);
                            //Log.e(TAG, "Current Date: "+currentDateTime);
                            //txtCurrentDateTime.setText(currentDateTime);
                        }
                    });
                }
                catch (Exception e) {
                    Log.e(TAG, "Exception occurred into Set Current DateTIme.");
                    e.printStackTrace();
                }*/
            }
        }, 0, 1000); //60 * 1000

    }

    /**
     * Format Server Date Time
     *
     * @param datetime
     * @return
     */
    public String formatedServerDate(long datetime) {
        Date date = new Date(datetime);
        DateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
        return formatter.format(date);
    }

    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void stopGWR() {
        // Close GWR Async Task
        if (inaugurationAsyncTask != null && !inaugurationAsyncTask.isCancelled()) {
            inaugurationAsyncTask.cancel(true);
        }

        // Close GWR Async Task
        if (checkGuinnessWorldRecordAsyncTask != null && !checkGuinnessWorldRecordAsyncTask.isCancelled()) {
            checkGuinnessWorldRecordAsyncTask.cancel(true);
        }

        if (timerDateTime != null) {
            timerDateTime.cancel();
            timerDateTime.purge();
        }

        Log.e(TAG, "onDestroy: Finish Count Down Timer.");
        if (finishCountDownT != null) {
            finishCountDownT.cancel();
        }

        Log.e(TAG, "onDestroy: Start Count Down Timer.");
        if (startCountDown != null) {
            startCountDown.cancel();
        }
    }

    //region caputr image when event start - last 1 min
    private void imgCapture() {
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    try {
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    //endregion

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(); //CommonUtils.getOutputMediaFile(1);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //picUri = Uri.fromFile(createImageFile());
                picUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                takePictureIntent.putExtra("ImageId", picUri); // set the image file
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    //region onActivity result image set
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {

                Toast.makeText(context, "Inauguration Demo Photo Submitted Successfully", Toast.LENGTH_SHORT).show();
                layoutGWREvent.setVisibility(View.GONE);
                layoutGWROfficialAttempt.setVisibility(View.VISIBLE);

                String vkId = gwrDashboardDto.getVkid();        //jsonObject.optString("vkid", "");
                String frName = gwrDashboardDto.getFr_name();   //jsonObject.optString("fr_name", null);
                String msg = "";
                Log.e(TAG, "VKID: " + vkId + " FR Name: " + frName);
                if (TextUtils.isEmpty(frName)) {
                    msg = "!!! Congratulation !!! <br> " + vkId;
                } else {
                    msg = "!!! Congratulation !!! <br> " + frName + " <br> (" + vkId + ")";
                }

                Log.e(TAG, "Msg : " + msg);
                // Show Congratulations Message.
                textCongrats.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                textCongrats.setText(Html.fromHtml(msg));

                setBalloonGIF();

            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            //Open Camera and post photo
            Toast toast = Toast.makeText(context, "Please capture Demo Inauguration photo.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            imgCapture();
        }
    }
    //endregion

    //region save data to server
    private void SaveInaugurationPhoto() {
        Gson gson = new Gson();
        String jsonData = gson.toJson(gwrDashboardDto, GWRDashboardDto.class);

        asyncSaveInaugurationPhoto = new AsyncSaveInaugurationPhoto(context, connection.getVkid(), jsonData, new AsyncSaveInaugurationPhoto.Callback() {
            @Override
            public void onResult(String result) {
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    return;
                }

                // Handle Error Response From Server.
                if (result.startsWith("ERROR|")) {
                    Toast.makeText(context, "failed to sumbitted photo,Please retry to capture photo.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Process response
                if (result.startsWith("OKAY|")) {
                    Toast.makeText(context, "Inauguration Photo Submit Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "failed to sumbitted photo,Please retry to capture photo.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        asyncSaveInaugurationPhoto.execute("");
    }
    //endregion

    private void setBalloonGIF() {
        try {
            imgGifImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.fire)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (resource instanceof GifDrawable) {
                                ((GifDrawable) resource).setLoopCount(GifDrawable.LOOP_FOREVER);
                            }
                            return false;
                        }
                    })
                    .into(imgGifImage);

            textCongrats.setTextColor(deprecateHandler.getColor(R.color.white));
            textCongrats.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
