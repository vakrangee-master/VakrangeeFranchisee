package in.vakrangee.franchisee.sitelayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.StringTokenizer;

import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class OTPDialog extends Dialog {

    private static final String TAG = OTPDialog.class.getCanonicalName();

    private Context context;
    private Dialog d;
    private String getVkid;
    String diplayServerResopnse;
    ProgressDialog progress;
    TextView txtResendOTP, txtResendOTPResponse, txtTimerCount;
    static EditText edtOtpEnter;
    Button btnOptVerified;
    String otpValue;

    private AsyncOtpVerifyUser myRequest;
    private IIsOTPVerified iIsOTPVerified;

    public interface IIsOTPVerified {
        public void IsOTPVerified(boolean IsVerified);
    }

    public OTPDialog(Context a) {
        super(a);
        this.context = a;
    }

    public OTPDialog(Context a, IIsOTPVerified iIsOTPVerified) {
        super(a);
        this.context = a;
        this.iIsOTPVerified = iIsOTPVerified;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_otp);

        init();

    }

    //region Verify Site Visit OTP
    private void init() {

        Connection connection = new Connection(context);
        getVkid = connection.getVkid();
        myRequest = new AsyncOtpVerifyUser();
        myRequest.execute();

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        TextView otpverification = (TextView) findViewById(R.id.otpverification);
        otpverification.setTypeface(Typeface.SANS_SERIF);

        txtResendOTP = (TextView) findViewById(R.id.resendOTP);
        txtResendOTP.setTypeface(Typeface.SANS_SERIF);
        txtResendOTPResponse = (TextView) findViewById(R.id.resendOTPresponse);
        txtResendOTPResponse.setTypeface(Typeface.SANS_SERIF);
        txtResendOTPResponse.setVisibility(View.INVISIBLE);
        txtTimerCount = (TextView) findViewById(R.id.timercount);
        txtTimerCount.setTypeface(Typeface.SANS_SERIF);
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtTimerCount.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                txtTimerCount.setVisibility(View.INVISIBLE);
                txtResendOTP.setVisibility(View.VISIBLE);
            }
        }.start();


        edtOtpEnter = (EditText) findViewById(R.id.inputOtp);
        edtOtpEnter.requestFocus();
        edtOtpEnter.setTypeface(Typeface.SANS_SERIF);

        btnOptVerified = (Button) findViewById(R.id.optcheck);
        btnOptVerified.setTypeface(font);
        btnOptVerified.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " +
                context.getResources().getString(R.string.verifyOTP)));


        txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRequest = new AsyncOtpVerifyUser();
                myRequest.execute();

            }
        });
        edtOtpEnter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (edtOtpEnter.getText().toString().contains(" ")) {
                    edtOtpEnter.setText(edtOtpEnter.getText().toString().replaceAll(" ", ""));
                    edtOtpEnter.setSelection(edtOtpEnter.getText().length());

                    //Toast.makeText(getApplicationContext(), "No Spaces Allowed", Toast.LENGTH_LONG).show();
                }
            }
        });

        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        edtOtpEnter.setOnFocusChangeListener(ofcListener);

        btnOptVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = edtOtpEnter.getText().toString();
                if (otp.trim().length() == 0) {
                    edtOtpEnter.setError(context.getResources().getString(R.string.enterOTP));
                } else {
                    if (edtOtpEnter.getText().toString().equalsIgnoreCase(otpValue)) {
                        iIsOTPVerified.IsOTPVerified(true);

                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.invalidOTP));
                        edtOtpEnter.setText("");
                    }
                }
            }
        });
    }

    //endregion

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.inputOtp && !hasFocus) {

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }

    public static void updateMessageBox(String s) {

        try {
            //  messageBox.append(s);
            StringTokenizer tokens = new StringTokenizer(s, ".");
            String first = tokens.nextToken();
            String second = tokens.nextToken();

            String substring = first.substring(Math.max(first.length() - 8, 0));
            edtOtpEnter.setText(substring);

        } catch (Exception e) {

            e.printStackTrace();

            Log.e(" AsyncUpdateMPin  catch", e.getMessage());

        }
    }

    private class AsyncOtpVerifyUser extends AsyncTask<Void, Void, Void> {
        //String Otp = edtOtpEnter.getText().toString();
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = new ProgressDialog(context);
            progress.setTitle(R.string.resendOTP);
            progress.setMessage(context.getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                    if (!TextUtils.isEmpty(getVkid)) {
                        diplayServerResopnse = WebService.SiteotpVerfiy(getVkid);
                    } else {
                        String enquiryId = CommonUtils.getEnquiryId(getContext());
                        diplayServerResopnse = WebService.getOTPSiteReviewViaEnquiryId(enquiryId);
                    }
                } else {
                    diplayServerResopnse = WebService.SiteotpVerfiy(getVkid);
                }

            } catch (Exception e) {
                Log.e("diplayServerResopnse", e.getMessage());
                String message = null;
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            progress.dismiss();
            try {
                if (diplayServerResopnse == null) {
                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));
                } else {
                    StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                    String first = tokens.nextToken();
                    otpValue = tokens.nextToken();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("catchBlock- OTP", diplayServerResopnse);
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (myRequest != null && !myRequest.isCancelled()) {
            myRequest.cancel(true);
        }
    }
}
