package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.newexistingfranchisee.AsyncNewExistingFranchiseeChecks;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class CustomNextGenApplicationFirstScreenDialog extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private LinearLayout parentLayout, layoutNextButton, layoutVKID;
    private Button btnClose;
    private Button btnNext;
    private RadioGroup radioGroupNewExistingApplicant;
    private EditText editTextVKID, editTextPassword;
    private int type = 0;
    private AsyncNewExistingFranchiseeChecks asyncNewExistingFranchiseeChecks = null;

    public CustomNextGenApplicationFirstScreenDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_nextgen_application_first_screen);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        layoutNextButton = findViewById(R.id.layoutNextButton);
        layoutVKID = findViewById(R.id.layoutVKID);
        layoutVKID.setVisibility(View.INVISIBLE);
        radioGroupNewExistingApplicant = findViewById(R.id.radioGroupNewExistingApplicant);
        CommonUtils.setDialogWidth(context, parentLayout);
        editTextVKID = findViewById(R.id.editTextVKID);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        radioGroupNewExistingApplicant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonNewApplicant) {
                    type = 0;
                    layoutVKID.setVisibility(View.INVISIBLE);
                    layoutNextButton.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonExistingApplicant) {
                    type = 1;
                    layoutVKID.setVisibility(View.VISIBLE);
                    layoutNextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        //VKID
        editTextVKID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 9) {
                    editTextVKID.setTextColor(Color.parseColor("#000000"));
                    editTextVKID.setError(context.getResources().getString(R.string.EnterCharacterVKID));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editTextVKID.setText(s);
                    editTextVKID.setSelection(s.length());
                }

                if (editTextVKID.getText().toString().contains(" ")) {
                    editTextVKID.setText(editTextVKID.getText().toString().replaceAll(" ", ""));
                    editTextVKID.setSelection(editTextVKID.getText().length());
                }

                if (editTextVKID.length() <= 8) {
                    //Do Nothing
                } else {
                    editTextVKID.setTextColor(Color.parseColor("#468847"));
                    editTextVKID.setError(null);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnClose) {
            dismiss();

        } else if (Id == R.id.btnNext) {
            if (type == 1) {
                String vkId = editTextVKID.getText().toString().trim();
                String pwd = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(vkId) || vkId.length() < 9) {
                    editTextVKID.setError("Please enter VKID.");
                    return;
                }

                if (TextUtils.isEmpty(pwd) || pwd.length() < 1) {
                    editTextPassword.setError("Please enter Password.");
                    return;
                }

                //Authenticate VKID
                authenticateVKID(vkId, pwd);
            } else {
                dismiss();
                //Start NextGen Application Form Activity
                String enquiryId = CommonUtils.getEnquiryId(context);
                new AsyncGetFranchiseeApplicationByEnquiryId(context).execute(enquiryId);
            }
        }
    }

    private void authenticateVKID(String vkId, String pwd) {
        String enquiryId = CommonUtils.getEnquiryId(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vk_id", vkId);
            jsonObject.put("password", pwd);
            jsonObject.put("enquiryId", enquiryId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        authenticateExistingFranchisee(context, jsonObject.toString(), enquiryId);
    }

    private void authenticateExistingFranchisee(final Context context, String jsonData, final String enquiryId) {
        asyncNewExistingFranchiseeChecks = new AsyncNewExistingFranchiseeChecks(context, Constants.FROM_AUTHENTICATE_EXISTING_FRANCHISEE, jsonData, new AsyncNewExistingFranchiseeChecks.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("ERROR")) {
                        String msg = result.replace("ERROR|", "");
                        msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                        AlertDialogBoxInfo.alertDialogShow(context, msg);
                        return;
                    }

                    dismiss();
                    if (result.startsWith("OKAY")) {
                        new AsyncGetFranchiseeApplicationByEnquiryId(context).execute(enquiryId);

                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncNewExistingFranchiseeChecks.execute("");
    }
}
