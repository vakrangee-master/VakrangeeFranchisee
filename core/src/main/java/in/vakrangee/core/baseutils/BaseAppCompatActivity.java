package in.vakrangee.core.baseutils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.vakrangee.core.R;
import in.vakrangee.core.utils.DeprecateHandler;
import in.vakrangee.core.utils.network.NetworkHealthHandler;

/**
 * Created by Vasundhara on 06/22/2018.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private static final String TAG = "BaseAppCompatActivity";
    private GestureLibrary gestureLib;
    protected GestureOverlayView gestureOverlayView;
    private static final double MAX_SCORE = 2.0;
    private DeprecateHandler deprecateHandler;
    private ProgressDialog progressDialog;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deprecateHandler = new DeprecateHandler(this);
        //setGestureOnCreate();

    }

   /* public void setGestureOnCreate() {
        //For Gesture
        addGestureOverlay();
        setContentView(gestureOverlayView);
    }

    public void addGestureOverlay() {
        gestureOverlayView = new GestureOverlayView(this);
        gestureOverlayView.setOrientation(GestureOverlayView.ORIENTATION_VERTICAL);
        gestureOverlayView.setGestureVisible(true);
        gestureOverlayView.setGestureColor(getResources().getColor(R.color.orange));
        gestureOverlayView.setUncertainGestureColor(getResources().getColor(R.color.orange));
        gestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
                ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
                for (Prediction prediction : predictions) {
                    if (prediction.score > MAX_SCORE) {
                        Intent intent = new Intent(BaseAppCompatActivity.this, SupportTicketActivity.class);
                        intent.putExtra("IsBackVisible", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        });
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLib.load()) {
            finish();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //super.setContentView(layoutResID);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layoutResID, null, false);

        if (gestureOverlayView != null)
            gestureOverlayView.addView(contentView, 0);

    }*/

    //region Check Network Health
    public void displayConnectivitySnackbar(String msg) {

        View v = getRootView();
        String actionMsg = getString(R.string.close_text);
        NetworkHealthHandler.displaySnackBar(v, msg, actionMsg, deprecateHandler);
    }

    private View getRootView() {
        final ViewGroup contentViewGroup = (ViewGroup) findViewById(android.R.id.content);
        View rootView = null;

        if (contentViewGroup != null)
            rootView = contentViewGroup.getChildAt(0);

        if (rootView == null)
            rootView = getWindow().getDecorView().getRootView();

        return rootView;
    }

    //endregion

    public void showProgressSpinner(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void dismissProgressSpinner() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showMessageWithFinish(String msg, final IHandleOkButton iHandleOkButton) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            iHandleOkButton.onOkClick();
                            //finish();
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }
}

