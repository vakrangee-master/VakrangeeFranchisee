package in.vakrangee.franchisee.franchiseelogin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class VerifyOTPDialog extends Dialog {

    private static final String TAG = "VerifyOTPDialog";
    private Context context;
    private WebView webView;
    TextView txtResendOTPResponse, txtTimerCount;
    static TextInputEditText edtOtpEnter;
    private String otpValue;
    private IVerifyOTP iVerifyOTP;
    private ProgressDialog progress;
    private MaterialButton txtResendOTP, btnOptVerified;

    public VerifyOTPDialog(@NonNull Context context, IVerifyOTP iVerifyOTP) {
        super(context);
        this.context = context;
        this.iVerifyOTP = iVerifyOTP;
    }

    public interface IVerifyOTP {
        public void isOTPVerified();

        public void resendOTP();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_verify_otp);

        init();

    }

    private void init() {

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        TextView otpverification = (TextView) findViewById(R.id.otpverification);
        otpverification.setTypeface(Typeface.SANS_SERIF);

        webView = (WebView) findViewById(R.id.webView);

        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        String str = "<font color='gray', font style ='Typeface.SANS_SERIF'><html><body><ul><li>An OTP has been send your Vakrangee Email ID and Registered Mobile Number.</li><li>For dual SIM phone, Please keep the Default SIM as the one which is registered for Mobile Access.</li><li>Please keep your mobile data/ internet connection on.</li></ul></body></html></font>";
        webView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);

        txtResendOTP = (MaterialButton) findViewById(R.id.resendOTP);
        txtResendOTP.setTypeface(Typeface.SANS_SERIF);
        txtResendOTPResponse = (TextView) findViewById(R.id.resendOTPresponse);
        txtResendOTPResponse.setTypeface(Typeface.SANS_SERIF);
        txtResendOTPResponse.setVisibility(View.GONE);
        txtTimerCount = (TextView) findViewById(R.id.timercount);
        txtTimerCount.setTypeface(Typeface.SANS_SERIF);
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtTimerCount.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                txtTimerCount.setVisibility(View.INVISIBLE);
                txtResendOTP.setVisibility(View.VISIBLE);
            }
        }.start();

        edtOtpEnter = (TextInputEditText) findViewById(R.id.inputOtp);
        edtOtpEnter.requestFocus();
        edtOtpEnter.setTypeface(Typeface.SANS_SERIF);

        btnOptVerified = (MaterialButton) findViewById(R.id.optcheck);
        //btnOptVerified.setTypeface(font);
        //btnOptVerified.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + context.getResources().getString(R.string.verifyOTP)));

        //Resend OTP
        txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iVerifyOTP.resendOTP();
            }
        });

        edtOtpEnter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (edtOtpEnter.getText().toString().contains(" ")) {
                    edtOtpEnter.setText(edtOtpEnter.getText().toString().replaceAll(" ", ""));
                    edtOtpEnter.setSelection(edtOtpEnter.getText().length());
                }
            }
        });
        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        edtOtpEnter.setOnFocusChangeListener(ofcListener);

        //Verify OTP
        btnOptVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = edtOtpEnter.getText().toString();
                if (otp.trim().length() == 0) {
                    edtOtpEnter.setError(context.getResources().getString(R.string.enterOTP));
                } else {
                    if (edtOtpEnter.getText().toString().equalsIgnoreCase(otpValue)) {
                        dismiss();
                        iVerifyOTP.isOTPVerified();

                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.invalidOTP));
                        edtOtpEnter.setText("");
                    }
                }
            }
        });
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.inputOtp && !hasFocus) {

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    // OTP Value
    public void OTPValue(String otpValue) {
        this.otpValue = otpValue;
    }

    //Set OTP Value
    public void SetOTPValue(String otpValue) {
        if (edtOtpEnter != null || !TextUtils.isEmpty(otpValue)) {
            edtOtpEnter.setText(otpValue);
            showProgressDialog();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                    btnOptVerified.performClick();
                }
            }, 3000);
        }
    }

    private void showProgressDialog() {
        progress = new ProgressDialog(context);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(context.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }
}
