package in.vakrangee.franchisee.franchiseelogin;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;

public class AsyncUpdateFranchiseeFCMIdViaEnquiryId extends AsyncTask<String, Void, String> {

    static final String TAG = "AsyncUpdateFranchiseeFCMIdViaEnquiryId";
    private Context mContext;
    private Logger logger;
    private FranchiseeAuthenticationRepository franchiseeAuthRepo;

    private AsyncUpdateFranchiseeFCMIdViaEnquiryId() {
    }

    public AsyncUpdateFranchiseeFCMIdViaEnquiryId(@NonNull Context context) {
        this.mContext = context;
        logger = Logger.getInstance(context);
        franchiseeAuthRepo = new FranchiseeAuthenticationRepository(context);
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            String tmpFCMId = SharedPrefUtils.getInstance(mContext).getFCMId();
            if (TextUtils.isEmpty(tmpFCMId)) {
                Log.e(TAG, "Failed to update FCMId. [FCMId is null].");
                return null;
            }

            //STEP 2: Get Enquiry ID
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            if (TextUtils.isEmpty(enquiryId)) {
                Log.e(TAG, "Failed to update enquiryId. [EnquiryId is null].");
                return null;
            }

            String response = franchiseeAuthRepo.updateFCMIdViaEnquiryId(enquiryId, tmpFCMId);
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("OKAY"))
                    logger.writeInfo(TAG, "Franchisee FCMId updated successfully. " + response);
                else
                    logger.writeError(TAG, "Failed to update Franchisee FCMId. " + response);
            }

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in updating Franchisee FCMId: " + e.getMessage());
            logger.writeError(TAG, "Error in updating Franchisee FCMId: " + e.toString());
        }
        return null;
    }
}


