package in.vakrangee.franchisee.gwr.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

public class CheckGuinnessWorldRecordAsyncTask extends AsyncTask<String, Void, String> {

    private String response;
    private Context context;
    private ProgressDialog progressDialog;
    private InaugurationRepository inaugurationRepository;
    private IGuinnessWorldRecordResult iGuinnessWorldRecordResult;

    public interface IGuinnessWorldRecordResult {
        public void resposne(String result, String error);
    }

    private CheckGuinnessWorldRecordAsyncTask() {
    }

    public CheckGuinnessWorldRecordAsyncTask(Context context, IGuinnessWorldRecordResult iGuinnessWorldRecordResult) {
        this.context = context;
        inaugurationRepository = new InaugurationRepository(context);
        this.iGuinnessWorldRecordResult = iGuinnessWorldRecordResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        progressDialog = new ProgressDialog(context);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        //Get Franchisee Application Details
        String macAddress = strings[0];       //"ec:3d:fd:ea:be:6a";
        try {
            response = inaugurationRepository.checkGuinnessWorldRecord(macAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
        // Check Response is null or Some error.
        if (TextUtils.isEmpty(response)) {
            iGuinnessWorldRecordResult.resposne(null, "ERROR|");
            return;
        }

        //
        if (response.startsWith("ERROR")) {
            String errMsg = response.replace("ERROR|", "");
            iGuinnessWorldRecordResult.resposne(null, errMsg);
        } else {
            String result = response.replace("OKAY|","");
            iGuinnessWorldRecordResult.resposne(result, null);
        }
    }
}