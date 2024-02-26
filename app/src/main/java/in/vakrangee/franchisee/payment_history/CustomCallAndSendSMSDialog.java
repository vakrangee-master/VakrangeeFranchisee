package in.vakrangee.franchisee.payment_history;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class CustomCallAndSendSMSDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomAttributeDialog";
    private Context context;
    private LinearLayout parentLayout;
    private LinearLayout layoutCall, layoutSMS;
    private RadioGroup radioGroupMobileNo;
    private RadioButton radioButtonMobileNo, radioButtonAltMobileNo;
    private Button btnClose, btnCall, btnSendSMS;
    private CheckBox checkboxMobileNo, checkboxAltMobileNo;
    private TextView txtTitle;
    private String title = null;
    private PaymentHistoryDto paymentHistoryDto;
    private int TYPE;
    public static final int TYPE_CALL = 1;
    public static final int TYPE_SMS = 2;
    private ICallAndSMSClicks iCallAndSMSClicks;
    private String callingNumber;
    private String smsNumber;

    public interface ICallAndSMSClicks {

        public void callNumber(PaymentHistoryDto paymentHistoryDto, String callingNumber);

        public void sendSMS(PaymentHistoryDto paymentHistoryDto, String selectedNumber);
    }

    public CustomCallAndSendSMSDialog(@NonNull Context context, PaymentHistoryDto paymentHistoryDto, int TYPE, ICallAndSMSClicks iCallAndSMSClicks) {
        super(context);
        this.context = context;
        this.paymentHistoryDto = paymentHistoryDto;
        this.TYPE = TYPE;
        this.iCallAndSMSClicks = iCallAndSMSClicks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_call_sms_send);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        CommonUtils.setDialogWidth(context, parentLayout);
        txtTitle = findViewById(R.id.txtTitle);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);


        layoutCall = findViewById(R.id.layoutCall);
        radioGroupMobileNo = findViewById(R.id.radioGroupMobileNo);
        radioButtonMobileNo = findViewById(R.id.radioButtonMobileNo);
        radioButtonAltMobileNo = findViewById(R.id.radioButtonAltMobileNo);
        btnCall = findViewById(R.id.btnCall);
        btnCall.setTypeface(font);
        btnCall.setText(new SpannableStringBuilder(new String(new char[]{0xf095}) + "  Call "));

        layoutSMS = findViewById(R.id.layoutSMS);
        checkboxMobileNo = findViewById(R.id.checkboxMobileNo);
        checkboxAltMobileNo = findViewById(R.id.checkboxAltMobileNo);
        btnSendSMS = findViewById(R.id.btnSendSMS);
        btnSendSMS.setTypeface(font);
        btnSendSMS.setText(new SpannableStringBuilder(new String(new char[]{0xf27a}) + "  Send "));

        btnCall.setOnClickListener(this);
        btnSendSMS.setOnClickListener(this);

        refresh(paymentHistoryDto, TYPE);

    }

    public void refresh(PaymentHistoryDto paymentHistoryDto, int TYPE) {
        this.paymentHistoryDto = paymentHistoryDto;
        this.TYPE = TYPE;

        if (TYPE == TYPE_CALL) {
            layoutCall.setVisibility(View.VISIBLE);
            layoutSMS.setVisibility(View.GONE);

        } else if (TYPE == TYPE_SMS) {
            layoutCall.setVisibility(View.GONE);
            layoutSMS.setVisibility(View.VISIBLE);
        }

        radioButtonMobileNo.setText(paymentHistoryDto.getMobileNo());
        radioButtonAltMobileNo.setText(paymentHistoryDto.getAlterMobileNo());
        radioGroupMobileNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Get Text
                int selectedId = radioGroupMobileNo.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                callingNumber = radioButton.getText().toString();
            }
        });

        checkboxMobileNo.setText(paymentHistoryDto.getMobileNo());
        checkboxAltMobileNo.setText(paymentHistoryDto.getAlterMobileNo());

        callingNumber = paymentHistoryDto.getMobileNo();
        smsNumber = paymentHistoryDto.getMobileNo();

    }

    // Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        /*if (!TextUtils.isEmpty(title)) {
            this.title = title;
            txtTitle.setText(title);
        }*/
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnClose) {
            dismiss();

        } else if (Id == R.id.btnCall) {

            if (TextUtils.isEmpty(callingNumber)) {
                Toast.makeText(context, "Please select Mobile No. to call", Toast.LENGTH_SHORT).show();
                return;
            }

            iCallAndSMSClicks.callNumber(paymentHistoryDto, callingNumber);
            dismiss();

        } else if (Id == R.id.btnSendSMS) {
            smsNumber = "";
            if (checkboxMobileNo.isChecked()) {
                smsNumber = checkboxMobileNo.getText().toString() + ",";
            }

            if (checkboxAltMobileNo.isChecked()) {
                smsNumber = smsNumber + checkboxAltMobileNo.getText().toString();
            }

            if (TextUtils.isEmpty(smsNumber)) {
                Toast.makeText(context, "Please select atleast 1 Mobile No.", Toast.LENGTH_SHORT).show();
                return;
            }

            iCallAndSMSClicks.sendSMS(paymentHistoryDto, smsNumber);
            dismiss();
        }
    }
}
