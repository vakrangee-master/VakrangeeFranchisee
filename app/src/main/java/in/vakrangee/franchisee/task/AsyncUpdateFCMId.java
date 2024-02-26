package in.vakrangee.franchisee.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncUpdateFCMId extends AsyncTask<String, Void, String> {

    static final String TAG = AsyncUpdateFCMId.class.getCanonicalName();
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;

    private AsyncUpdateFCMId() {
    }

    public AsyncUpdateFCMId(@NonNull Context context) {
        this.mContext = context;
        logger = Logger.getInstance(context);
        deviceInfo = DeviceInfo.getInstance(context);
    }


    @Override
    protected String doInBackground(String... strings) {

        try {

            String tmpFCMId = SharedPrefUtils.getInstance(mContext).getFCMId();
            if (TextUtils.isEmpty(tmpFCMId)) {
                Log.e(TAG, "Failed to update FCMId. [FCMId is null].");
                return null;
            }

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to update FCMId. [VKId is null].");
                return null;
            }

            String tmpTokenId = connection.getTokenId();
            if (TextUtils.isEmpty(tmpTokenId)) {
                Log.e(TAG, "Failed to update FCMId. [TokenId is null].");
                //return null;
            }

            String fcmId = EncryptionUtil.encryptString(tmpFCMId, mContext);
            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);
            String tokenId = EncryptionUtil.encryptString(tmpTokenId, mContext);
            String response = WebService.updateFCMId(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo(), fcmId);
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("OKAY"))
                    logger.writeInfo(TAG, "FCMId updated successfully. " + response);
                else
                    logger.writeError(TAG, "Failed to update FCMId. " + response);
            }

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in updating FCMId: " + e.getMessage());
            logger.writeError(TAG, "Error in updating FCMId: " + e.toString());
        }
        return null;
    }

}
