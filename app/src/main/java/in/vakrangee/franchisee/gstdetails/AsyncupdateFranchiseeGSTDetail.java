package in.vakrangee.franchisee.gstdetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncupdateFranchiseeGSTDetail extends AsyncTask<String, Void, String> {
    private final static String TAG = "AsyncUpdateWorkInProgress";
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private Callback callback;
    private GSTINDTO gstindto;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncupdateFranchiseeGSTDetail(Context context, GSTINDTO gstindto, Callback okay) {
        super();
        this.mContext = context;
        this.gstindto = gstindto;
        this.callback = okay;
        this.deviceInfo = DeviceInfo.getInstance(context);
        this.logger = Logger.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        // Get App MODE
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

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);

            String response = null;
            if (!TextUtils.isEmpty(tmpVkId)) {
                HashMap<String, String> parameters = prepareData(gstindto, tmpVkId);
                response = WebService.updateFranchiseeGSTDetail(parameters);
            } else {
                String enquiryId = CommonUtils.getEnquiryId(mContext);
                HashMap<String, String> parameters = prepareData(gstindto, enquiryId);
                response = WebService.updateFranchiseeGSTDetail(parameters);
            }

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
                Log.d(TAG, "GSTIN Details Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update GSTIN Details. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }

    private HashMap<String, String> prepareData(GSTINDTO gstindto, String VKIDOrEnquiryId) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", VKIDOrEnquiryId);
        parameters.put("gstEntityName", gstindto.getGstEntityName());
        parameters.put("gstNumber", gstindto.getGstNumber());
        parameters.put("gstImage", gstindto.getGstImage());
        parameters.put("gstImageId", gstindto.getGstImageId());
        parameters.put("gstImageExt", gstindto.getGstImageExt());
        parameters.put("gstAddressLine1", gstindto.getGstAddressLine1());
        parameters.put("gstAddressLine2", gstindto.getGstAddressLine2());
        parameters.put("gstLandmark", gstindto.getGstLandmark());
        parameters.put("gstLocality", gstindto.getGstLocality());
        parameters.put("gstArea", gstindto.getGstArea());
        parameters.put("gstState", gstindto.getGstState());
        parameters.put("gstDistrict", gstindto.getGstDistrict());
        parameters.put("gstVtc", gstindto.getGstVtc());
        parameters.put("gstPinCode", gstindto.getGstPinCode());
        parameters.put("is_gstin_applied", gstindto.getAppliedForGSTIN());
        parameters.put("gstin_trn_number", gstindto.getTrnNumber());
        parameters.put("gstin_ack_receipt_img_id", gstindto.getGstinAckReceiptImgId());
        parameters.put("gstin_ack_receipt_img_base64", gstindto.getGstinAckReceiptImgBase64());
        parameters.put("gstin_ack_receipt_img_ext", gstindto.getGstinAckReceiptImgExt());
        parameters.put("gstin_applied_date", gstindto.getGstApplicationDate());
        parameters.put("gstin_expected_date", gstindto.getExpectedDateOfGSTApplication());

        return parameters;
    }
}
