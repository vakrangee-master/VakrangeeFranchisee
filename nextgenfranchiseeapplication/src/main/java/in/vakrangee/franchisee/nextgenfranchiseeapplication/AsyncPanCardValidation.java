package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;
import in.vakrangee.supercore.franchisee.utils.Logger;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class AsyncPanCardValidation extends AsyncTask<String, Void, String> {


        private final static String TAG = "AsyncPanCardValidation";
        private Context mContext;
        private Logger logger;
        private ProgressDialog progress;
        private FranchiseeApplicationRepository fapRepo;
        private AsyncPanCardValidation.Callback callback;
        private String pancardNo, pancardName;

        public interface Callback {
            void onResult(String result);
        }

    public AsyncPanCardValidation(Context context, String pancard_no, String pancard_name, AsyncPanCardValidation.Callback icallback) {
            super();
            this.mContext = context;
            logger = Logger.getInstance(context);
            fapRepo = new FranchiseeApplicationRepository(context);
            this.pancardNo = pancard_no;
            this.pancardName = pancard_name;
            this.callback = icallback;
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
                String enquiryId = CommonUtils.getEnquiryId(mContext);

                String response = fapRepo.verifyPanCardDetail(enquiryId, pancardNo, pancardName);

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
                    Log.d(TAG, "Franchisee Application Async PanCard Validation  successfully. " + response);
                else
                    Log.e(TAG, "Failed to Update Async Pan Card Validation. " + response);

                if (callback != null)
                    callback.onResult(response);  // Send Response Back To Caller.
            } else {
                if (callback != null)
                    callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
            }
        }
}
