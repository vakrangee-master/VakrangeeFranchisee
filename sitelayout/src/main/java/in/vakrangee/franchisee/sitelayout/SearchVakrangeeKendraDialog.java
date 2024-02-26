package in.vakrangee.franchisee.sitelayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;

import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.InputFilterBuilder;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class SearchVakrangeeKendraDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "SearchVakrangeeKendraDialog";
    private Context context;

    //Views
    LinearLayout layoutSearch;
    EditText editTextFranchiseeApplicationNumber;
    Button btnSearch;
    Button btnCancel;

    private ISearchVakrangeeKendra iSearchVakrangeeKendra;
    private AsyncNextGenFranchiseeDetails asyncNextGenFranchiseeDetails;

    private String modeType;

    //region Callback Interface
    public interface ISearchVakrangeeKendra {
        public void getFranchiseeDetail(FranchiseeDetails franchiseeDetails, String errorMsg);
    }
    //endregion

    //region Constructor
    public SearchVakrangeeKendraDialog(@NonNull Context context, @NonNull ISearchVakrangeeKendra iSearchVakrangeeKendra) {
        super(context);
        this.context = context;
        this.iSearchVakrangeeKendra = iSearchVakrangeeKendra;
    }

    public SearchVakrangeeKendraDialog(String modeType, @NonNull Context context, @NonNull ISearchVakrangeeKendra iSearchVakrangeeKendra) {
        super(context);
        this.context = context;
        this.iSearchVakrangeeKendra = iSearchVakrangeeKendra;
        this.modeType = modeType;
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
        getWindow().setLayout(width, height);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_search_vakrangee_kendra);

        //Initialize Views
        layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        editTextFranchiseeApplicationNumber = (EditText) findViewById(R.id.editTextFranchiseeApplicationNumber);
        editTextFranchiseeApplicationNumber.setFilters(new InputFilter[]{InputFilterBuilder.getCharacterAndDigitFilter(), new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + context.getResources().getString(R.string.cancel)));

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setTypeface(font);
        btnSearch.setText(new SpannableStringBuilder(new String(new char[]{0xf002}) + " " + context.getResources().getString(R.string.search)));

        btnSearch.setOnClickListener(SearchVakrangeeKendraDialog.this);
        btnCancel.setOnClickListener(SearchVakrangeeKendraDialog.this);

        editTextFranchiseeApplicationNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSearch.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    public void searchApplicationNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            editTextFranchiseeApplicationNumber.setText(number);
            btnSearch.performClick();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSearch) {// Validate Franchisee Application Number
            String faId = editTextFranchiseeApplicationNumber.getText().toString();

            if (TextUtils.isEmpty(faId)) {
                editTextFranchiseeApplicationNumber.setError(context.getResources().getString(R.string.error_msg_search1));
                editTextFranchiseeApplicationNumber.requestFocus();
                return;
            }

            // Service Call
            asyncNextGenFranchiseeDetails = new AsyncNextGenFranchiseeDetails(context, faId);
            asyncNextGenFranchiseeDetails.execute("");
        } else if (id == R.id.btnCancel) {
            cancelSearchTask();
            dismiss();
        }
    }

    //region Search Frenchisee Based on Application Number

    public class AsyncNextGenFranchiseeDetails extends AsyncTask<String, Void, FranchiseeDetails> {

        private Context mContext;
        private TelephonyManager telephonyManager;
        private String errorMsg;
        String tempFAId;

        public AsyncNextGenFranchiseeDetails(Context context, String faId) {
            super();
            this.mContext = context;
            telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            this.tempFAId = faId;
            errorMsg = mContext.getString(R.string.Warning);
        }

        @Override
        protected void onPreExecute() {
            Log.e("TAG", "onPreExecute");
            setSearchEnable(false);
        }

        @SuppressLint("LongLogTag")
        @Override
        protected FranchiseeDetails doInBackground(String... values) {
            // If you want to use 'values' string in here
            Log.i(TAG, "doInBackground");
            try {
                // Preparing Data
                Connection connection = new Connection(mContext);
                String tempVKId = connection.getVkid();
                String vkId = EncryptionUtil.encryptString(connection.getVkid(), mContext);
                String tokenId = EncryptionUtil.encryptString(connection.getTokenId(), mContext);

                String deviceid = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceId = EncryptionUtil.encryptString(deviceid, mContext);

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(mContext);
                String imeiNo = EncryptionUtil.encryptString(deviceIDAndroid, mContext);

                String simSerial = CommonUtils.getSimSerialNumber(mContext);
                String simNo = EncryptionUtil.encryptString(simSerial, mContext);

                String nextgenFranchiseeApplicationId = EncryptionUtil.encryptString(tempFAId, mContext);

                String displayServerResopnse = null;
               /* if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
                    displayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
                    displayServerResopnse = WebService.getAllNextGenSiteWorkInProgressDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
                    displayServerResopnse = WebService.getWorkCompletionIntimationDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
                    displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                } else {
                    displayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId, nextgenFranchiseeApplicationId, tokenId, imeiNo, deviceId, simNo);
                }*/

                // Check Mode Type To get NextGen Site Detail
                if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
                    if (Constants.ENABLE_FRANCHISEE_MODE) {
                        displayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(tempVKId, tempFAId);
                    } else {
                        displayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                    }
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
                    if (Constants.ENABLE_FRANCHISEE_MODE) {
                        displayServerResopnse = WebService.getAllNextGenSiteWorkInProgressDetail(tempVKId, tempFAId);
                    } else {
                        displayServerResopnse = WebService.getAllNextGenSiteWorkInProgressDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                    }
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
                    displayServerResopnse = WebService.getWorkCompletionIntimationDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_VISIT_ADHOC)) {
                    displayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(tempVKId);
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_VISIT)) {
                    if (Constants.ENABLE_FRANCHISEE_MODE) {
                        displayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(tempVKId);
                    } else {
                        displayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId, nextgenFranchiseeApplicationId, tokenId, imeiNo, deviceId, simNo);
                    }
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
                    if (Constants.ENABLE_FRANCHISEE_MODE) {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(tempVKId, tempFAId);
                    } else {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                    }
                } else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
                    if (Constants.ENABLE_FRANCHISEE_MODE) {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(tempVKId, tempFAId);
                    } else {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                    }
                }else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED)) {
                    if (Constants.ENABLE_FRANCHISEE_MODE) {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(tempVKId, tempFAId);
                    } else {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                    }
                }else if (!TextUtils.isEmpty(modeType) && modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED)) {
                    if (Constants.ENABLE_FRANCHISEE_MODE) {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(tempVKId, tempFAId);
                    } else {
                        displayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId);
                    }
                } else {
                    displayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId, nextgenFranchiseeApplicationId, tokenId, imeiNo, deviceId, simNo);
                }


                if (displayServerResopnse.startsWith("ERROR|")) {

                    StringTokenizer tokens = new StringTokenizer(displayServerResopnse, "|");
                    tokens.nextToken();     // Jump to next Token
                    errorMsg = tokens.nextToken();

                    return null;
                }

                StringTokenizer tokens1 = new StringTokenizer(displayServerResopnse, "|");
                tokens1.nextToken();    // Jump to next Token
                String franchiseeData = tokens1.nextToken();

                Log.d(TAG, franchiseeData);
                String ab = franchiseeData.substring(1, franchiseeData.length() - 1);
                String franchiseeJsonData = ab.replace("\r\n", "");

                FranchiseeDetails franchiseeDetails = JSONUtils.toJson(FranchiseeDetails.class, franchiseeJsonData);

                try {
                    //Compress Franchisee Profile Pic
                    if (franchiseeDetails != null && !TextUtils.isEmpty(franchiseeDetails.getFranchiseePicFile())) {

                        String picFile = franchiseeDetails.getFranchiseePicFile();
                        Bitmap bitmap = CommonUtils.StringToBitMap(picFile);
                        bitmap = ImageUtils.getResizedBitmap(bitmap);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        String profilePic = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                        franchiseeDetails.setFranchiseePicFile(profilePic);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Fail in compressing Profile pic. \n " + e.toString());
                }

                return franchiseeDetails;//JSONUtils.toJson(FranchiseeDetails.class, franchiseeJsonData); // returning FranchiseeDetails Object

            } catch (Exception e) {
                Log.e("TAG", "Error:in Search Vakrangee Kendra " + e.toString());
                //AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));

            }
            return null;
        }

        @Override
        protected void onPostExecute(FranchiseeDetails franchiseeDetails) {

            setSearchEnable(true);      // Set Search Enable

            if (franchiseeDetails != null) {

                Toast.makeText(mContext, "Franchisee Detail Found.", Toast.LENGTH_SHORT).show();
                iSearchVakrangeeKendra.getFranchiseeDetail(franchiseeDetails, null);
            } else {
                iSearchVakrangeeKendra.getFranchiseeDetail(null, errorMsg);
                editTextFranchiseeApplicationNumber.setError("No Data Found.");
            }
        }

        // Enable and Disable Search Layout
        private void setSearchEnable(boolean enable) {

            editTextFranchiseeApplicationNumber.setEnabled(enable);
            btnSearch.setEnabled(enable);

            if (enable)
                layoutSearch.setVisibility(View.GONE);
            else
                layoutSearch.setVisibility(View.VISIBLE);

        }
    }

    //endregion

    //region Cancel Search Task
    public void cancelSearchTask() {
        if (asyncNextGenFranchiseeDetails != null && !asyncNextGenFranchiseeDetails.isCancelled()) {
            asyncNextGenFranchiseeDetails.cancel(true);
        }
    }
    //endregion

}
