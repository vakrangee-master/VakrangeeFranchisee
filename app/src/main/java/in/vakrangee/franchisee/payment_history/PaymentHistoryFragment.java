package in.vakrangee.franchisee.payment_history;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentHistoryFragment extends Fragment {
    private View view;
    private Context context;
    @BindView(R.id.layoutFRDetails)
    LinearLayout layoutFRDetails;
    @BindView(R.id.recyclerViewPaymentHistory)
    RecyclerView recyclerViewPaymentHistory;
    @BindView(R.id.textDataFound)
    TextView textDataFound;
    //FR details
    @BindView(R.id.textFranchiseeName)
    TextView textFranchiseeName;
    @BindView(R.id.textFranchiseeApplicationId)
    TextView textFranchiseeApplicationId;
    @BindView(R.id.textFranchiseeAddress)
    TextView textFranchiseeAddress;
    @BindView(R.id.textTotalPayment)
    TextView textTotalPayment;
    //mobile no
    @BindView(R.id.txtMobileNumber)
    TextView txtMobileNumber;
    @BindView(R.id.txtMobileNumberIcon)
    TextView txtMobileNumberIcon;
    @BindView(R.id.layoutMobileNo)
    LinearLayout layoutMobileNo;
    //more icon
    @BindView(R.id.txtMobileNumberMoreIcon)
    TextView txtMobileNumberMoreIcon;
    //sms
    @BindView(R.id.txtSMS)
    TextView txtSMS;
    @BindView(R.id.txtSMSIcon)
    TextView txtSMSIcon;
    @BindView(R.id.layoutSMS)
    LinearLayout layoutSMS;
    @BindView(R.id.txtSMSIconMoreIcon)
    TextView txtSMSIconMoreIcon;
    //email
    @BindView(R.id.layoutEmailId)
    LinearLayout layoutEmailId;
    @BindView(R.id.txtEmailIdIcon)
    TextView txtEmailIdIcon;
    @BindView(R.id.txtEmailId)
    TextView txtEmailId;
    //model type
    @BindView(R.id.layoutVKModelType)
    LinearLayout layoutVKModelType;
    @BindView(R.id.txtVKModelTypeCircle)
    TextView txtVKModelTypeCircle;
    @BindView(R.id.txtNoDataMsg)
    TextView txtNoDataMsg;
    @BindView(R.id.layoutMain)
    LinearLayout layoutMain;
    @BindView(R.id.layoutHeaderPaymentHistory)
    LinearLayout layoutHeaderPaymentHistory;

    private PermissionHandler permissionHandler;
    private PaymentHistoryDetailsAdapter paymentHistoryDetailsAdapter;
    private PaymentHistoryDto paymentHistoryDtos;
    private DeprecateHandler deprecateHandler;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alert;
    private CustomCallAndSendSMSDialog callAndSendSMSDialog;
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 1;


    public PaymentHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment_history, container, false);
        this.context = getContext();
        ButterKnife.bind(this, view);
        deprecateHandler = new DeprecateHandler(context);
        paymentHistoryDtos = new PaymentHistoryDto();
        permissionHandler = new PermissionHandler(getActivity());


        layoutFRDetails.setVisibility(View.GONE);
        return view;
    }

    public void reload(String result) {
        try {

            layoutFRDetails.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                return;
            }
            //Response
            if (result.startsWith("ERROR|")) {
                result = result.replace("ERROR|", "");
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                } else {
                    AlertDialogBoxInfo.alertDialogShow(context, result);
                }
            } else if (result.startsWith("OKAY|")) {
                result = result.replace("OKAY|", "");
                adapterData(result);

            } else {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    private void adapterData(String result) {
        try {

            Gson gson = new Gson();
            paymentHistoryDtos = gson.fromJson(result, PaymentHistoryDto.class);
            if (paymentHistoryDtos != null) {
                txtNoDataMsg.setVisibility(View.GONE);
                layoutMain.setVisibility(View.VISIBLE);
                layoutHeaderPaymentHistory.setVisibility(View.VISIBLE);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray payment_history_details = jsonObject.getJSONArray("payment_history_details");
                //set Franchisee Details
                setFrachiseeDetails();
                //set adapter data -payment history
                setInstalmentDataAdapter(payment_history_details.toString());
            } else {
                txtNoDataMsg.setVisibility(View.VISIBLE);
                layoutHeaderPaymentHistory.setVisibility(View.GONE);
                layoutMain.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void setInstalmentDataAdapter(String payment_history_details) {
        try {
            Gson gson = new GsonBuilder().create();
            List<PaymentHistoryDto> paymentHistoryD = gson.fromJson(payment_history_details, new TypeToken<ArrayList<PaymentHistoryDto>>() {
            }.getType());

            if (paymentHistoryD != null && paymentHistoryD.size() > 0) {
                textDataFound.setVisibility(View.GONE);
                recyclerViewPaymentHistory.setVisibility(View.VISIBLE);

                paymentHistoryDetailsAdapter = new PaymentHistoryDetailsAdapter(context, paymentHistoryD, new PaymentHistoryDetailsAdapter.ifcItemClick() {
                    @Override
                    public void clickPosition(int position, PaymentHistoryDto paymentHistoryDto) {

                    }
                });
                @SuppressLint("WrongConstant") LinearLayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewPaymentHistory.setLayoutManager(layoutManager1);
                recyclerViewPaymentHistory.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPaymentHistory.setAdapter(paymentHistoryDetailsAdapter);

            } else {
                textDataFound.setVisibility(View.VISIBLE);
                recyclerViewPaymentHistory.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setFrachiseeDetails() {
        try {
            if (TextUtils.isEmpty(paymentHistoryDtos.getModelType())) {
                layoutVKModelType.setVisibility(View.GONE);
            } else {
                layoutVKModelType.setVisibility(View.VISIBLE); // CFB53B       C0C0C0
                String color;
                //String color = dto.getVkModelName().equalsIgnoreCase("Gold") ? "#e2c009" : "#e0dfdf";
                if (paymentHistoryDtos.getModelType().equalsIgnoreCase("GOLD")) {
                    color = "#e2c009";
                } else if (paymentHistoryDtos.getModelType().equalsIgnoreCase("SILVER")) {
                    color = "#e0dfdf";
                } else {
                    color = "#c5ab84";
                }
                //myViewHolder.txtVKModelTypeCircle.setText(dto.getVkModelName().substring(0, 1));
                GradientDrawable drawable = (GradientDrawable) txtVKModelTypeCircle.getBackground();
                if (drawable != null) {
                    drawable.setColor(Color.parseColor(color));
                }
            }

            //set data
            textFranchiseeName.setText(TextUtils.isEmpty(paymentHistoryDtos.getFrachiseeName()) ? "" : paymentHistoryDtos.getFrachiseeName());
            textFranchiseeApplicationId.setText(TextUtils.isEmpty(paymentHistoryDtos.getFrachiseeApplicationNo()) ? "" : paymentHistoryDtos.getFrachiseeApplicationNo());
            textFranchiseeAddress.setText(TextUtils.isEmpty(paymentHistoryDtos.getAddress()) ? "" : paymentHistoryDtos.getAddress());
            // textTotalPayment.setText(TextUtils.isEmpty(paymentHistoryDtos.getTotalFee()) ? "" : paymentHistoryDtos.getTotalFee());

            if (!TextUtils.isEmpty(paymentHistoryDtos.getTotalFee())) {
                String amount = paymentHistoryDtos.getTotalFee().substring(0, paymentHistoryDtos.getTotalFee().length() - 3);
                CommonUtils.setRupeeToSpeator(textTotalPayment, amount);
            } else {
                textTotalPayment.setText("0.00 ₹");
            }
            //Mobile No
            if (TextUtils.isEmpty(paymentHistoryDtos.getMobileNo())) {
                txtMobileNumber.setVisibility(View.INVISIBLE);
            } else {
                setFontawesomeIcon(txtMobileNumberIcon, getResources().getString(R.string.fa_call));
                layoutMobileNo.setTag(paymentHistoryDtos.getMobileNo());
                layoutMobileNo.setVisibility(View.VISIBLE);
            }
            //click on mobile
            layoutMobileNo.setTag(paymentHistoryDtos);
            layoutMobileNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimationHanndler.bubbleAnimation(context, v);
                    PaymentHistoryDto dto1 = (PaymentHistoryDto) v.getTag();
                    callNow(dto1);
                }
            });

            //more mobile number icon -- if alter number
            if (!TextUtils.isEmpty(paymentHistoryDtos.getAlterMobileNo())) {
                txtMobileNumberMoreIcon.setVisibility(View.VISIBLE);
                setFontawesomeIcon(txtMobileNumberMoreIcon, context.getResources().getString(R.string.fa_down_arrow_drak));
            } else {
                txtMobileNumberMoreIcon.setVisibility(View.GONE);
            }

            //SMS
            if (TextUtils.isEmpty(paymentHistoryDtos.getMobileNo())) {
                txtSMS.setVisibility(View.INVISIBLE);
            } else {
                setFontawesomeIcon(txtSMSIcon, getResources().getString(R.string.fa_msg));
                layoutSMS.setTag(paymentHistoryDtos.getMobileNo());
                layoutSMS.setVisibility(View.VISIBLE);
            }

            //more sms  icon -- if two number
            if (!TextUtils.isEmpty(paymentHistoryDtos.getAlterMobileNo())) {
                txtSMSIconMoreIcon.setVisibility(View.VISIBLE);
                setFontawesomeIcon(txtSMSIconMoreIcon, context.getResources().getString(R.string.fa_down_arrow_drak));
            } else {
                txtSMSIconMoreIcon.setVisibility(View.GONE);
            }

            //click on SMS

            layoutSMS.setTag(paymentHistoryDtos);
            layoutSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AnimationHanndler.bubbleAnimation(context, v);
                    PaymentHistoryDto dto1 = (PaymentHistoryDto) v.getTag();
                    performSMSClick(dto1);


                    /*AnimationHanndler.bubbleAnimation(context, v);
                    if (null != paymentHistoryDtos.getMobileNo()) {
                        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + paymentHistoryDtos.getMobileNo()));
                        if (i.resolveActivity(context.getPackageManager()) != null) {
                            startActivity(i);
                        } else {
                            Toast.makeText(context, "SMS App not found", Toast.LENGTH_LONG).show();
                        }
                    }*/
                }
            });

            //Email Id
            if (TextUtils.isEmpty(paymentHistoryDtos.getEmailId())) {
                txtEmailIdIcon.setVisibility(View.INVISIBLE);
            } else {
                setFontawesomeIcon(txtEmailIdIcon, getResources().getString(R.string.fa_mail));
                layoutEmailId.setTag(paymentHistoryDtos.getEmailId());
                layoutEmailId.setVisibility(View.VISIBLE);

            }

            //click email id icon
            layoutEmailId.setTag(paymentHistoryDtos);
            layoutEmailId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimationHanndler.bubbleAnimation(context, v);
                    PaymentHistoryDto dto1 = (PaymentHistoryDto) v.getTag();
                    sendEmail(dto1);

                   /* AnimationHanndler.bubbleAnimation(context, v);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", paymentHistoryDtos.getEmailId(), null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Payment History");
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));*/
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void sendEmail(PaymentHistoryDto dto) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", dto.getEmailId(), null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Payment History Details");
        context.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    private void performSMSClick(PaymentHistoryDto dto) {
        //If Alt Mobile No is not present
        if (TextUtils.isEmpty(dto.getAlterMobileNo())) {
            composeSmsMessage("", dto.getMobileNo());
            return;
        }
        //Show Dialog to choose
        showCallAndSendDialog(dto, CustomCallAndSendSMSDialog.TYPE_SMS);
    }

    private void callNow(PaymentHistoryDto dto1) {
        checkMicrophonePermission(dto1);
    }

    private void checkMicrophonePermission(final PaymentHistoryDto dto) {
        try {

            //If Alt Mobile No is not present
            if (TextUtils.isEmpty(dto.getAlterMobileNo())) {
                //dto.setMobileNo(dto.getMobileNo());
                showMessage("Do you want to call " + dto.getMobileNo() + " ?", dto, dto.getMobileNo());
                return;
            }

            //Show Dialog to choose
            showCallAndSendDialog(dto, CustomCallAndSendSMSDialog.TYPE_CALL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCallAndSendDialog(PaymentHistoryDto dto, int TYPE) {

        if (callAndSendSMSDialog != null && callAndSendSMSDialog.isShowing()) {
            callAndSendSMSDialog.setDialogTitle("");
            callAndSendSMSDialog.refresh(dto, TYPE);
            return;
        }

        if (dto != null) {
            callAndSendSMSDialog = new CustomCallAndSendSMSDialog(context, dto, TYPE, new CustomCallAndSendSMSDialog.ICallAndSMSClicks() {
                @Override
                public void callNumber(PaymentHistoryDto paymentHistoryDto, String callingNumber) {
                    //leadMgtLocationDto.setMobileNo(callingNumber);
                    showMessage("Do you want to call " + callingNumber + " ?", paymentHistoryDto, callingNumber);
                }

                @Override
                public void sendSMS(PaymentHistoryDto leadMgtLocationDto, String selectedNumber) {
                    composeSmsMessage("", selectedNumber);
                }
            });
            callAndSendSMSDialog.show();
            callAndSendSMSDialog.setCancelable(true);
            callAndSendSMSDialog.setDialogTitle("");

        } else {
            Toast toast = Toast.makeText(context, "No Information Found.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void composeSmsMessage(String message, String phoneNumber) {
        if (null != phoneNumber) {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + phoneNumber));
            i.putExtra("sms_body", message);
            if (i.resolveActivity(context.getPackageManager()) != null) {
                startActivity(i);
            } else {
                Toast.makeText(context, "SMS App not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showMessage(String msg, final PaymentHistoryDto dto1, String callingNumber) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            alert = null;
                        }
                    })
                    .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            startCalling(callingNumber);
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void startCalling(String dto) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + dto));
            startActivity(intent);
        }

    }

    private void setFontawesomeIcon(TextView textView, String icon) {
        textView.setText(icon);
        textView.setTextSize(12);
        textView.setTextColor(deprecateHandler.getColor(R.color.colorGreyL5));
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }
}
