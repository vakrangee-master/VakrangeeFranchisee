package in.vakrangee.franchisee.hardwareacknowledgement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncSaveKendraAckDetails extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveKendraAckDetails";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private KendraAcknowledgementRepository kendraAckRepo;
    private Callback callback;
    private KendraAcknowledgementDto kendraAckDto;
    private List<ImageDto> imagesList;
    private String response;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveKendraAckDetails(Context context, KendraAcknowledgementDto acknowledgementDto, List<ImageDto> imagesList, AsyncSaveKendraAckDetails.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        kendraAckRepo = new KendraAcknowledgementRepository(context);
        this.kendraAckDto = acknowledgementDto;
        this.imagesList = imagesList;
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

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(String... strings) {

        try {
            response = null;
            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to Update Kendra Ack Details. [VKId is null].");
                return null;
            }

            //STEP 1: Validate Serial No
           /* String srRes = kendraAckRepo.getSrNoExistsStatus(kendraAckDto.getBrandName(), kendraAckDto.getSerialNo());
            if (TextUtils.isEmpty(srRes) || srRes.startsWith("ERROR")) {
                return response;
            }

            StringTokenizer st1 = new StringTokenizer(srRes, "|");
            String key = st1.nextToken();
            String sco1 = st1.nextToken();
            if(key.equalsIgnoreCase("OKAY") && sco1.equalsIgnoreCase("true")){
                response = "FAILED";
                return response;
            }*/

            //STEP 2: Save Kendra Acknowledgement details
            Gson gson = new Gson();               //Equipment Images
            String data = gson.toJson(imagesList, new TypeToken<ArrayList<ImageDto>>() {
            }.getType());
            kendraAckDto.setEquipmentImages(data);

            response = kendraAckRepo.saveKendraAckDetail(tmpVkId, kendraAckDto);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            logger.writeError(TAG, "Exception: " + e.toString());
        }
        return null;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Kendra Acknowledgement Details Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update Kendra Acknowledgement Details. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
