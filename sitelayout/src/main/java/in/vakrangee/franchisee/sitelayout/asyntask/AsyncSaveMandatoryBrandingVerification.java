package in.vakrangee.franchisee.sitelayout.asyntask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.model.MandatoryBrandingList;
import in.vakrangee.franchisee.sitelayout.model.SiteReadinessCategoryDto;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncSaveMandatoryBrandingVerification extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveMandatoryBrandingVerification";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private AsyncSaveMandatoryBrandingVerification.Callback callback;
    private boolean isAdhoc = false;
    //private List<SiteReadinessCategoryDto> categoryDtoList;
    private List<MandatoryBrandingList> categoryDtoList;
    private FranchiseeDetails franchiseeDetails;
    private String type;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveMandatoryBrandingVerification(Context context) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
    }

    public AsyncSaveMandatoryBrandingVerification(Context context, List<MandatoryBrandingList> categoryDtoList, FranchiseeDetails franchiseeDetails, String type, AsyncSaveMandatoryBrandingVerification.Callback callback) {
        super();
        this.mContext = context;
        this.categoryDtoList = categoryDtoList;
        this.franchiseeDetails = franchiseeDetails;
        this.type = type;
        logger = Logger.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;
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
            if (categoryDtoList == null) {
                franchiseeDetails.setDesignElements(null);
                franchiseeDetails.setBranding_element_details(null);
            } else {

                Gson gson = new GsonBuilder().create();
                String dataArray = gson.toJson(categoryDtoList, new TypeToken<ArrayList<MandatoryBrandingList>>() {
                }.getType());
                if (type.equalsIgnoreCase(Constants.SITE_MANDATORY_INTERIOR_BRANDING_TYPE)) {
                    franchiseeDetails.setBranding_element_details(dataArray);
                } else {
                    franchiseeDetails.setDesignElements(dataArray);
                }
            }

            String jsonData = JSONUtils.toString(franchiseeDetails);

            String response = null;
            String data = jsonData;   // Work In Progress Data - JSON

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();

            if (isAdhoc) {

                if (!TextUtils.isEmpty(tmpVkId)) {
                    response = WebService.getMandatoryVerificationDetail(mContext);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(mContext);
                    response = WebService.getMandatoryVerificationDetail(mContext);
                }
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
        logger.writeError(TAG, "response: " + response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Site Readiness and Verification Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update Site Readiness and Verification. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
