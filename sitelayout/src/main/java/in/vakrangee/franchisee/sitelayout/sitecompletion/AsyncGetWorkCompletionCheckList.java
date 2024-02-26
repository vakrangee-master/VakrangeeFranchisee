package in.vakrangee.franchisee.sitelayout.sitecompletion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncGetWorkCompletionCheckList extends AsyncTask<String, Void, String> {

    private final static String TAG = AsyncGetWorkCompletionCheckList.class.getCanonicalName();
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private Callback callback;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetWorkCompletionCheckList(Context context, Callback callback) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
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

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to Get Work Completion Check List. [VKId is null].");
                return null;
            }

            String tmpTokenId = connection.getTokenId();
            if (TextUtils.isEmpty(tmpTokenId)) {
                Log.e(TAG, "Failed to Get Work Completion Check List. [TokenId is null].");
                return null;
            }

            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);
            String tokenId = EncryptionUtil.encryptString(tmpTokenId, mContext);
            String response = "OKAY|[{\"work_completion_checklist_id\":1,\"element_name\":\"Frontage glass & door\",\"sub_element_name\":\"\"},{\"work_completion_checklist_id\":2,\"element_name\":\"Space for ATM/UPS/NVR/Router\",\"sub_element_name\":\"\"},{\"work_completion_checklist_id\":3,\"element_name\":\"Furniture as per specification\",\"sub_element_name\":\"\"},{\"work_completion_checklist_id\":4,\"element_name\":\"Flooring as per specification\",\"sub_element_name\":\"\"},{\"work_completion_checklist_id\":5,\"element_name\":\"Wall Paint finish\",\"sub_element_name\":\"\"},{\"work_completion_checklist_id\":6,\"element_name\":\"Ceiling as per specification\",\"sub_element_name\":\"\"},{\"work_completion_checklist_id\":7,\"element_name\":\"Lighting as per specification\",\"sub_element_name\":\"\"},{\"work_completion_checklist_id\":8,\"element_name\":\"Electricity fitting as per specification\",\"sub_element_name\":\"\"}]";
            //WebService.getWorkCompletionCheckList(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo());

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
                Log.d(TAG, "Successfully Received Work Completion Intimation CheckList. " + response);
            else
                Log.e(TAG, "Failed to Get Work Completion Intimation CheckList. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
