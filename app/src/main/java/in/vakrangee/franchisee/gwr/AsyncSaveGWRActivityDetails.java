package in.vakrangee.franchisee.gwr;

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

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncSaveGWRActivityDetails extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveGWRActivityDetails";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private AsyncSaveGWRActivityDetails.Callback callback;
    private List<GWRCategoryDto> categoryDtoList;
    private String response;
    private GWRRepository gwrRepository;
    private String type;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveGWRActivityDetails(Context context, List<GWRCategoryDto> categoryDtoList, String type, AsyncSaveGWRActivityDetails.Callback callback) {
        super();
        this.mContext = context;
        this.categoryDtoList = categoryDtoList;
        logger = Logger.getInstance(context);
        this.callback = callback;
        gwrRepository = new GWRRepository(context);
        this.type = type;
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
            Gson gson = new GsonBuilder().create();
            String jsonData = gson.toJson(categoryDtoList, new TypeToken<ArrayList<GWRCategoryDto>>() {
            }.getType());

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to Update GWR Activity Details. [VKId is null].");
                return null;
            }

            response = gwrRepository.saveGWRActivityDetail(jsonData, tmpVkId, type);

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
                Log.d(TAG, "GWR Activity Details Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update GWR Activity Details. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
