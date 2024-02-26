package in.vakrangee.franchisee.bcadetails.pre;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.bcadetails.BCAEntryDetailsRepository;
import in.vakrangee.franchisee.bcadetails.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsyncGetPreBCADetails extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private PreBCADetailsDto bcaEntryDetailsDto;
    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private boolean IsSuccessful = false;
    private String errMsg;

    public AsyncGetPreBCADetails(Context context) {
        this.context = context;
        bcaEntryDetailsRepo = new BCAEntryDetailsRepository(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        Connection connection = new Connection(context);
        String vkId = connection.getVkid();
        String enquiryId = CommonUtils.getEnquiryId(context);

        response = bcaEntryDetailsRepo.getPreBCAEntryDetailsByEnquiryId(enquiryId);
        processResponse();
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        //Handle Response
        if (!IsSuccessful) {
            AlertDialogBoxInfo.alertDialogShow(context, errMsg);
            return;
        }

        //Validate Response
        if (bcaEntryDetailsDto == null) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            return;
        }

        //Start BCA Entry Activity
        startBCAEntryForm(bcaEntryDetailsDto);
    }

    public void startBCAEntryForm(PreBCADetailsDto entryDetailsDto) {
        Intent intent = new Intent(context, PreBCADataEntryDetailsActivity.class);
        if (entryDetailsDto != null)
            intent.putExtra("PRE_BCA_DETAILS", (Serializable) entryDetailsDto);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }

    private void processResponse() {
        try {

            if (TextUtils.isEmpty(response)) {
                IsSuccessful = false;
                errMsg = context.getResources().getString(R.string.Warning);
                return;
            }

            // Handle Error Response From Server.
            if (response.startsWith("ERROR|")) {

                IsSuccessful = false;
                StringTokenizer tokens = new StringTokenizer(response, "|");
                tokens.nextToken();     // Jump to next Token
                errMsg = tokens.hasMoreElements() ? tokens.nextToken() : context.getResources().getString(R.string.Warning);
                return;
            }

            //Process response
            if (response.startsWith("OKAY|")) {
                StringTokenizer st1 = new StringTokenizer(response, "|");
                String key = st1.nextToken();
                String data = st1.nextToken();

                if (TextUtils.isEmpty(data)) {
                    IsSuccessful = false;
                    errMsg = context.getResources().getString(R.string.Warning);
                    return;
                }

                //Process Data
                IsSuccessful = true;
                Gson gson = new GsonBuilder().create();
                List<PreBCADetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<PreBCADetailsDto>>() {
                }.getType());
                if (applicationList.size() > 0)
                    bcaEntryDetailsDto = applicationList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            IsSuccessful = false;
            errMsg = context.getResources().getString(R.string.Warning);
        }
    }
}
