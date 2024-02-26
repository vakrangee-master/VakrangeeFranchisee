package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.model.NextGenFranchiseeApplicationFormDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;

public class AsyncGetFranchiseeApplicationByEnquiryId extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private NextGenFranchiseeApplicationFormDto applicationFormDto;
    private FranchiseeApplicationRepository franchiseeAppRepo;

    public AsyncGetFranchiseeApplicationByEnquiryId(Context context) {
        this.context = context;
        franchiseeAppRepo = new FranchiseeApplicationRepository(context);
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

        String nextGenEnquiryId = strings[0];
        response = franchiseeAppRepo.getFranchiseeApplicationDetailsByNextGenEnquiryId(nextGenEnquiryId);

        try {
            Gson gson = new GsonBuilder().create();
            List<NextGenFranchiseeApplicationFormDto> applicationList = gson.fromJson(response, new TypeToken<ArrayList<NextGenFranchiseeApplicationFormDto>>() {
            }.getType());
            if (applicationList.size() > 0)
                applicationFormDto = applicationList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        //Validate Response
        if (applicationFormDto == null) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            return;
        }

        //Start Application Activity
        startApplicationForm(applicationFormDto);
    }

    public void startApplicationForm(NextGenFranchiseeApplicationFormDto applicationFormDto) {
        Intent intent = new Intent(context, NextGenFranchiseeApplicationActivity.class);
        if (applicationFormDto != null)
            intent.putExtra("NextGenFranchiseeApplicationDetail", (Serializable) applicationFormDto);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }
}
