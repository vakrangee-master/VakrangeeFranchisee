package in.vakrangee.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import in.vakrangee.core.R;
import in.vakrangee.core.application.VakrangeeKendraApplication;

/**
 * Created by Vasundhara on 6/18/2018
 */

public class PermissionHandler {

    private static final String TAG = "PermissionHandler";
    private Activity activity;
    private Logger logger;
    private DeprecateHandler deprecateHandler;
    private Snackbar snackbar = null;

    public PermissionHandler(Activity activity) {
        this.activity = activity;
        logger = Logger.getInstance(activity);
        deprecateHandler = new DeprecateHandler(activity);

    }

    public void requestPermission(final View view, String permission, final String msg, final IPermission iPermission) {
        Dexter.withActivity(activity)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //Do after Granting permission
                        Log.e(TAG, "RequestPermission: Permission Granted ");
                        iPermission.IsPermissionGranted(true);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                       /* if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                            showSettingsSnackbar(view, activity.getString(R.string.needs_permission_storage_msg));
                        }*/
                        // check for permanent denial of permission
                        showSettingsSnackbar(view, msg);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    public void requestAccountsAndPhonePermission(final View view, final IPermission iPermission) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            iPermission.IsPermissionGranted(true);
                        }

                        int size = report.getDeniedPermissionResponses().size();
                        // check for permanent denial of any permission
                        if (size > 0 || report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsSnackbar(view, activity.getString(R.string.needs_permission_msg));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(activity, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public void requestPhoneLocationAndStoragePermission(final View view, final IPermission iPermission) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            iPermission.IsPermissionGranted(true);
                        }

                        int size = report.getDeniedPermissionResponses().size();
                        // check for permanent denial of any permission
                        if (size > 0 || report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsSnackbar(view, activity.getString(R.string.needs_permission_msg));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(activity, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    public void requestAccountsAndPhonePermissionNotUsed(final View view, final IPermission iPermission) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE)
                .withListener(getCompositeListener(view, iPermission))
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(activity, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public SnackbarOnAnyDeniedMultiplePermissionsListener getSnackBarListener(View view) {
        //SnackBar Listener
        SnackbarOnAnyDeniedMultiplePermissionsListener snackbarPermissionListener = SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                .with(view, R.string.needs_permission_msg)
                .withOpenSettingsButton(R.string.settings_txt)
                .withDuration(Snackbar.LENGTH_INDEFINITE)
                .withCallback(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar snackbar) {
                        // Event handler for when the given Snackbar is visible
                    }

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        // Event handler for when the given Snackbar has been dismissed
                    }
                }).build();

        return snackbarPermissionListener;
    }

    public MultiplePermissionsListener getMultipleListener(final IPermission iPermission) {
        MultiplePermissionsListener multiplePermissionsListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                // check if all permissions are granted
                if (report.areAllPermissionsGranted()) {
                    iPermission.IsPermissionGranted(true);
                }

                /*int size = report.getDeniedPermissionResponses().size();

                // check for permanent denial of any permission
                if (size > 0 || report.isAnyPermissionPermanentlyDenied()) {
                    // show alert dialog navigating to Settings
                    //showSettingsDialog();
                    //showSettingsSnackbar(activity, view);
                }*/
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };
        return multiplePermissionsListener;
    }

    public CompositeMultiplePermissionsListener getCompositeListener(final View view, final IPermission iPermission) {
        CompositeMultiplePermissionsListener permission = new CompositeMultiplePermissionsListener(getSnackBarListener(view), getMultipleListener(iPermission));
        return permission;
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.need_permission_lbl));
        builder.setMessage(activity.getString(R.string.needs_permission_msg));
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.settings_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    /**
     * Showing Snackbar with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    @SuppressLint("WrongConstant")
    private void showSettingsSnackbar(View view, String msg) {

        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

        //Message Text
        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTypeface(VakrangeeKendraApplication.sans_serif);
        tv.setTextSize(14);
        tv.setTextColor(deprecateHandler.getColor(R.color.white));
        tv.setMaxLines(4);

        //Button Text
        Button button = (Button) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_action);
        button.setTypeface(VakrangeeKendraApplication.sans_serif);
        button.setTextSize(14);
        button.setTextColor(deprecateHandler.getColor(R.color.orange));

        //Background Color
        snackbar.getView().setBackgroundColor(deprecateHandler.getColor(R.color.blackTransparent));

        snackbar.setAction(activity.getString(R.string.settings_txt), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                openSettings();
            }
        });

        snackbar.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    public void dismissSnackbar() {
        if (snackbar != null && snackbar.isShownOrQueued()) {
            snackbar.dismiss();
            snackbar = null;

        }
    }

}
