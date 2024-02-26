package in.vakrangee.franchisee.gwr.witness_statement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.GWRRepository;
import in.vakrangee.franchisee.gwr.attendance.AttendanceDetailsDto;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncSaveUploadWitnessStatement extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveUploadWitnessStatement";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private GWRRepository gwrRepository;
    private AsyncSaveUploadWitnessStatement.Callback callback;
    private String response;
    private int type;
    private List<AttendanceDetailsDto> attendanceDetailsList;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveUploadWitnessStatement(Context context, int type, List<AttendanceDetailsDto> attendanceDetailsList, AsyncSaveUploadWitnessStatement.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        this.type = type;
        gwrRepository = new GWRRepository(context);
        this.attendanceDetailsList = attendanceDetailsList;
        this.callback = icallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(mContext);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            response = null;
            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to Update Witness Statement Details. [VKId is null].");
                return null;
            }

            //STEP 2: Save Witness Statement Details
            Gson gson = new Gson();
            String data = gson.toJson(attendanceDetailsList, new TypeToken<ArrayList<AttendanceDetailsDto>>() {
            }.getType());

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray(data);

            jsonObject.put("camera_man_list", new JSONArray());
            jsonObject.put("witness_man_list", jsonArray);

            response = gwrRepository.saveWitnessStatement(tmpVkId, jsonObject.toString());

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            logger.writeError(TAG, "Exception: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Witness statement Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update Witness Statement Details. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
