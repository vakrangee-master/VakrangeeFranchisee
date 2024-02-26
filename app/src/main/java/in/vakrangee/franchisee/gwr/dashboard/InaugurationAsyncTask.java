package in.vakrangee.franchisee.gwr.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

public class InaugurationAsyncTask extends AsyncTask<String, Void, String> {

    private String response;
    private Context context;
    private ProgressDialog progressDialog;
    private InaugurationRepository inaugurationRepository;
    private IInaugurationResult iInaugurationResult;

    public interface IInaugurationResult {
        public void resposne(String result, String error);
    }

    private InaugurationAsyncTask() {
    }

    public InaugurationAsyncTask(Context context, IInaugurationResult iInaugurationResult) {
        this.context = context;
        inaugurationRepository = new InaugurationRepository(context);
        this.iInaugurationResult = iInaugurationResult;
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

        //Get Franchisee Application Details
        String macAddress = strings[0];       //"ec:3d:fd:ea:be:6a";
        try {
            response = inaugurationRepository.getServerDateTime(macAddress);

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
        // Check Response is null or Some error.
        if (TextUtils.isEmpty(response)) {
            iInaugurationResult.resposne(null, "ERROR|");
            return;
        }

        //
        if (response.startsWith("ERROR")) {
            String errMsg = response.replace("ERROR|", "");
            iInaugurationResult.resposne(null, errMsg);
        } else {
            String result = response.replace("OKAY|","");
            iInaugurationResult.resposne(result, null);
        }
    }
}