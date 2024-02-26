package in.vakrangee.core.baseutils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import in.vakrangee.core.utils.DeprecateHandler;
import in.vakrangee.core.utils.network.NetworkHealthHandler;

/**
 * Created by Vasundhara on 06/22/2018.
 */

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    private Context context;
    private ProgressDialog progressDialog;
    private DeprecateHandler deprecateHandler;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
    }

    public void showProgressSpinner(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void dismissProgressSpinner(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    //region Check Network Health

    public void checkNetworkHealth(View v) {
        NetworkHealthHandler.checkNetworkHealth(v, context, deprecateHandler);

    }

    //endregion

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(Html.fromHtml(msg))
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

    public void showMessageWithFinish(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            getActivity().finish();
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }
}
