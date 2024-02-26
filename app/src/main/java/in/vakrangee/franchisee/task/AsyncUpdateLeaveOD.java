package in.vakrangee.franchisee.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.MyVKMaster;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by Nileshd on 1/10/2017.
 */
public class AsyncUpdateLeaveOD extends AsyncTask<String, Void, MyVKMaster> {
    String diplayServerResopnse;

    Context mContext;

    ProgressDialog dialog;

    public AsyncUpdateLeaveOD(Context context) {
        super();
        this.mContext = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        dialog = ProgressDialog.show(mContext, "Title", "Message", true);


    }

    @Override
    protected MyVKMaster doInBackground(String... values) {
        // If you want to use 'values' string in here
        Log.i("TAG", "doInBackground");
        try {
            String vkid = values[0];
            String TokenID = values[1];

            String imei = values[2];
            String deviceid = values[3];
            String simserialnumber = values[4];
            String leaveTypeId = values[5];
            String leaveOdStatus = values[6];
            String reason = values[7];

            diplayServerResopnse = WebService.updateStatusLeaveOD(vkid, TokenID, imei, deviceid, simserialnumber, leaveTypeId, leaveOdStatus, reason);


        } catch (Exception e) {
            Log.e("TAG", "Error:in LoginPage " + e.getMessage());


            Log.e(" AsyncLogin  catch", e.getMessage());
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));

        }


        return null;
    }

    @Override
    protected void onPostExecute(MyVKMaster myVKMaster) {
        dialog.dismiss();


        String display = diplayServerResopnse;

        if (diplayServerResopnse.equals("OKAY")) {


            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.odapply));
            //getBalance.setText(getResources().getString(R.string.rs) + second + "");

        }
        // new MyVakrangeeKendraTimingFragment().updatedata(result);


    }
}
