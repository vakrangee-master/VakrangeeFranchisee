package in.vakrangee.core.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Nileshd on 6/8/2016.
 */
public class AlertDialogBoxInfo {

    public static void alertDialogShow(Context context, String message) {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(Html.fromHtml(message));
            alertDialog.setCancelable(false);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
            textView.setTextSize(12);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public static void showOkDialog(Context context, String message) {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
