package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import androidx.annotation.NonNull;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;

@SuppressLint("LongLogTag")
public class CustomRequestCallBackDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomRequestCallBackDialog";
    private Context context;
    private LinearLayout parentLayout;
    private Button btnClose;
    private Button btnRequest;
    private Spinner spinnerRequestType;
    private FranchiseeApplicationRepository fapRepo;
    private ProgressDialog progressDialog;
    private List<CustomFranchiseeApplicationSpinnerDto> requestTypeList;
    private GetRequestCallBackTypeAsync getRequestCallBackTypeAsync = null;

    public CustomRequestCallBackDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        fapRepo = new FranchiseeApplicationRepository(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_request_call_back);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        CommonUtils.setDialogWidth(context, parentLayout);

        spinnerRequestType = findViewById(R.id.spinnerRequestType);
        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(this);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        getRequestCallBackTypeAsync = new GetRequestCallBackTypeAsync();
        getRequestCallBackTypeAsync.execute("");

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnClose) {
            dismiss();

        } else if (Id == R.id.btnRequest) {
            //Do Nothing
        }
    }

    public class GetRequestCallBackTypeAsync extends AsyncTask<String, Void, String> implements AdapterView.OnItemSelectedListener {

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

            //STEP 1: Request Type
            requestTypeList = fapRepo.getRequestCallBackTypeList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            //Set Adapter
            CustomFranchiseeApplicationSpinnerAdapter entityTypeAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, requestTypeList);
            spinnerRequestType.setAdapter(entityTypeAdapter);
            int pos = fapRepo.getSelectedPos(requestTypeList, null);
            spinnerRequestType.setSelection(pos);
            spinnerRequestType.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int Id = parent.getId();

            if (Id == R.id.spinnerRequestType && position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerRequestType.getItemAtPosition(position);
                Log.e(TAG, "Name: " + dto.getName());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //Do Nothing
        }
    }
}
