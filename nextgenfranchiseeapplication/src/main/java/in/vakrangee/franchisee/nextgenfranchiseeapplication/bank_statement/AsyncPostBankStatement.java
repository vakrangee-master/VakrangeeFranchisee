package in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsyncPostBankStatement extends AsyncTask<String, Void, String> {

    private Context mContext;
    private ProgressDialog progress;
    private BankStatementRepository bankStatementRepos;
    private Callback callback;
    private BankStatementDto bankStatementDto;
    private String appId;
    private String response;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncPostBankStatement(Context context,String appId, BankStatementDto bankStatementDto, Callback icallback) {
        super();
        this.mContext = context;
        this.appId = appId;
        bankStatementRepos = new BankStatementRepository(context);
        this.bankStatementDto = bankStatementDto;
        this.callback = icallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            response = null;

            response = bankStatementRepos.saveSpecificBankStatementDetail(appId, bankStatementDto);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
