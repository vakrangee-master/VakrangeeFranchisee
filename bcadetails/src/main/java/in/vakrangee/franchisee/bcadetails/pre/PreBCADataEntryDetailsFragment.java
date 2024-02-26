package in.vakrangee.franchisee.bcadetails.pre;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import in.vakrangee.franchisee.bcadetails.R;
import in.vakrangee.franchisee.bcadetails.pre.personal.PreBCAPersonalDetailsDto;
import in.vakrangee.franchisee.bcadetails.pre.personal.PreBCAPersonalDetailsFragment;
import in.vakrangee.franchisee.bcadetails.pre.smsretrieval.IOTPReceiveListener;
import in.vakrangee.franchisee.bcadetails.pre.smsretrieval.SMSRetrievalReceiver;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("LongLogTag")
public class PreBCADataEntryDetailsFragment extends BaseFragment implements IOTPReceiveListener {

    private static final String TAG = "PreBCADataEntryDetailsFragment";
    private View view;
    private int pos = 0;
    public final static int MAX_COUNT = 1;
    private static final String BCA_PERSONAL_DETAILS = "1";
    private static final String BCA_LOCATION_DETAILS = "2";
    private static final String BCA_PRE_FULL_SUBMIT = "3";
    private boolean IsEditable = false;
    private Typeface font;
    private static final int MOVED_NEXT = 100;
    private static final int MOVED_PREVIOUS = 101;
    private static final int MOVED_TAB_CLICKED = 102;
    private Context context;
    private PermissionHandler permissionHandler;
    private LinearLayout layoutPersonalDetail, layoutLocationDetail;
    private PreBCAPersonalDetailsFragment fragmentPersonalDetail;
    //private PreBCALocationDetailFragment fragmentLocationDetail;
    private ViewFlipper viewFlipper;
    public PreBCADetailsDto preBCADetailsDto;
    private AsyncSaveBCADetails asyncSaveBCADetails = null;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private AsyncValidateMobileNoUsingOTP asyncValidateMobileNoUsingOTP = null;
    private VerifyOTPDialog verifyOTPDialog = null;
    private SMSRetrievalReceiver smsReceiver;

    public PreBCADataEntryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_pre_bca_details, container, false);

        bindViewId(view);

        //Initialize data
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        //All Child Fragments
        fragmentPersonalDetail = (PreBCAPersonalDetailsFragment) getChildFragmentManager().findFragmentById(R.id.fragmentPersonalDetail);
        //fragmentLocationDetail = (PreBCALocationDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragmentLocationDetail);
        viewFlipper.setMeasureAllChildren(false);

        configureSMSRetreiver();

        return view;
    }

    private void bindViewId(View view) {
        layoutPersonalDetail = view.findViewById(R.id.layoutPersonalDetail);
        //layoutLocationDetail = view.findViewById(R.id.layoutLocationDetail);
        viewFlipper = view.findViewById(R.id.viewFlipper);
    }

    public void refresh(PreBCADetailsDto bcaDetailsDto) {
        this.preBCADetailsDto = bcaDetailsDto;
        if (preBCADetailsDto == null) {
            this.preBCADetailsDto = new PreBCADetailsDto();
        }

        refreshOtherFragments();
    }

    public void showNext() {

        if (pos == (MAX_COUNT - 1)) {
            saveData();
            return;
        }

        //Current Tab
        int status = IsCurrentTabValidated(pos);
        if (status == 0) {
            handleJump(MOVED_NEXT);
        }
    }

    public void showPrevious() {
        handleJump(MOVED_PREVIOUS);

    }

    public void moveNext() {
        if (pos < (MAX_COUNT - 1)) {
            pos++;
            setBottomLayoutVisibility(((PreBCADataEntryDetailsActivity) getActivity()).getBackButton(), ((PreBCADataEntryDetailsActivity) getActivity()).getNextButton());
            viewFlipper.setInAnimation(context, R.anim.left_in);
            viewFlipper.setOutAnimation(context, R.anim.left_out);
            viewFlipper.showNext();
        }
    }

    public void moveBack() {
        if (pos > 0) {
            pos--;
            setBottomLayoutVisibility(((PreBCADataEntryDetailsActivity) getActivity()).getBackButton(), ((PreBCADataEntryDetailsActivity) getActivity()).getNextButton());
            viewFlipper.setInAnimation(context, R.anim.right_in);
            viewFlipper.setOutAnimation(context, R.anim.right_out);
            viewFlipper.showPrevious();
        }
    }

    public void displayLayout(int position) {
        boolean IsPrev = false;
        if (position < pos)
            IsPrev = true;

        this.pos = position;

        if (IsPrev) {
            viewFlipper.setInAnimation(context, R.anim.right_in);
            viewFlipper.setOutAnimation(context, R.anim.right_out);
        } else {
            viewFlipper.setInAnimation(context, R.anim.left_in);
            viewFlipper.setOutAnimation(context, R.anim.left_out);
        }
        viewFlipper.setDisplayedChild(pos);
        setBottomLayoutVisibility(((PreBCADataEntryDetailsActivity) getActivity()).getBackButton(), ((PreBCADataEntryDetailsActivity) getActivity()).getNextButton());
    }

    public void setBottomLayoutVisibility(TextView btnBack, TextView btnNext) {

        //For Back Button
        if (pos == 0) {
            btnBack.setVisibility(View.GONE);
        } else {
            btnBack.setVisibility(View.VISIBLE);
        }

        //For Next Button
        btnNext.setVisibility(View.VISIBLE);
        if (pos == (MAX_COUNT - 1)) {
            if (IsEditable)
                btnNext.setVisibility(View.VISIBLE);
            else
                btnNext.setVisibility(View.GONE);
            btnNext.setText(" " + " " + "Submit" + " " + " ");
        } else {

            btnNext.setTypeface(font);
            btnNext.setText(new SpannableStringBuilder(" " + " " + context.getResources().getString(R.string.next) + " " + " " + new String(new char[]{0xf054}) + " " + " "));

        }

        ((PreBCADataEntryDetailsActivity) getActivity()).setPagingText((pos + 1) + " of " + MAX_COUNT);

    }

    private String getType() {
        String type = null;

        String txt = ((PreBCADataEntryDetailsActivity) getActivity()).getNextButton().getText().toString();
        if (txt.trim().equalsIgnoreCase("Submit"))
            return BCA_PRE_FULL_SUBMIT;

        switch (pos) {

            case 0:
                type = BCA_PERSONAL_DETAILS;
                break;

            case 1:
                type = BCA_LOCATION_DETAILS;
                break;

            case 2:
                type = BCA_PRE_FULL_SUBMIT;
                break;

            default:
                break;
        }
        return type;
    }

    private int getPosByType(String type) {
        int pos = 0;

        switch (type) {

            case BCA_PERSONAL_DETAILS:
                pos = 0;
                break;

            case BCA_LOCATION_DETAILS:
                pos = 1;
                break;

            default:
                break;

        }

        return pos;
    }

    public void saveData() {

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        String status = IsAllStepsValidated();
        if (!status.equalsIgnoreCase("0")) {

            int position = getPosByType(status);
            if (pos != position) {
                displayLayout(position);
            }
            return;
        }

        //Validate OTP
        validateMobileNoUsingOTP(preBCADetailsDto.getFrMobileNumber());

    }

    public String IsAllStepsValidated() {

        //STEP 1: PERSONAL
        int step1 = fragmentPersonalDetail.IsPersonalDetailsValidated();
        if (step1 != 0)
            return BCA_PERSONAL_DETAILS;

        //STEP 2: LOCATION
       /* int step2 = fragmentLocationDetail.IsLocationDetailsValidated();
        if (step2 != 0)
            return BCA_LOCATION_DETAILS;*/

        return "0";
    }

    public void refreshOtherFragments() {

        handleFranchiseeStatus();
        if (((PreBCADataEntryDetailsActivity) getActivity()).getBackButton() != null && ((PreBCADataEntryDetailsActivity) getActivity()).getNextButton() != null) {
            setBottomLayoutVisibility(((PreBCADataEntryDetailsActivity) getActivity()).getBackButton(), ((PreBCADataEntryDetailsActivity) getActivity()).getNextButton());
        }

        fragmentPersonalDetail.reloadData(preBCADetailsDto.getPersonalDetails(), IsEditable);
        //fragmentLocationDetail.reloadData(preBCADetailsDto.getLocationDetails(), IsEditable);
    }

    public void handleFranchiseeStatus() {
        //Handle Status
        IsEditable = false;
        int statusCode = -1;
        try {
            if (!TextUtils.isEmpty(preBCADetailsDto.getStatusDetail())) {

                JSONObject jsonObject = new JSONObject(preBCADetailsDto.getStatusDetail());

                String status = jsonObject.optString("bca_status");
                int IsMagShown = jsonObject.optInt("bca_is_msg_shown");
                String msg = jsonObject.optString("bca_status_msg");

                statusCode = jsonObject.optInt("bca_status_code", -1);
                IsEditable = (!TextUtils.isEmpty(status) && status.equalsIgnoreCase("E")) ? true : false;
                boolean IsMsgBeShowned = IsMagShown == 1 ? true : false;

                if (IsMsgBeShowned)
                    showMessage(msg);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PreBCADetailsDto getPreBCADetailsDtoToSave() {
        PreBCADetailsDto detailsDto = new PreBCADetailsDto();
        Gson gson = new Gson();

        //Personal
        String basicJson = gson.toJson(fragmentPersonalDetail.getPreBCAPersonalDetailsDto(), PreBCAPersonalDetailsDto.class);
        detailsDto.setPersonalDetails(basicJson);

        /*//Location
        String locationJson = gson.toJson(fragmentLocationDetail.getPreBCALocationDetailsDto(), PreBCALocationDetailsDto.class);
        detailsDto.setLocationDetails(locationJson);*/

        return detailsDto;
    }

    private void saveAllPreBCAData() {
        PreBCADetailsDto preBCADetailsDto = getPreBCADetailsDtoToSave();

        //Save
        asyncSaveBCADetails = new AsyncSaveBCADetails(context, preBCADetailsDto, new AsyncSaveBCADetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });

        asyncSaveBCADetails.execute("");
    }

    private void processResult(String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                return;
            }

            //Handle Response
            if (result.startsWith("ERROR|")) {
                result = result.replace("ERROR|", "");

                String msg = TextUtils.isEmpty(result) ? context.getString(R.string.Warning) : result;
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return;
            }

            if (result.startsWith("OKAY|")) {
                result = result.replace("OKAY|", "");
                String msg = TextUtils.isEmpty(result) ? "BCA Details saved Successfully." : result;
                showOKMessageToReload(msg);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    public void showOKMessageToReload(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(Html.fromHtml(msg))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            getActivity().finish();

                            //Reload
                            //new AsyncGetPreBCADetails(context).execute("");
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void handleJump(int from) {

        switch (from) {

            case MOVED_NEXT:
                moveNext();
                break;

            case MOVED_PREVIOUS:
                moveBack();
                break;

            case MOVED_TAB_CLICKED:
                break;

            default:
                break;
        }

    }

    private int IsCurrentTabValidated(int pos) {
        int status = 0;

        switch (pos) {

            case 0:
                status = fragmentPersonalDetail.IsPersonalDetailsValidated();
                break;

          /*  case 1:
                status = fragmentLocationDetail.IsLocationDetailsValidated();
                break;*/

            default:
                break;
        }

        return status;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncSaveBCADetails != null && !asyncSaveBCADetails.isCancelled()) {
            asyncSaveBCADetails.cancel(true);
        }

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(smsReceiver);
        }

    }

    public void updateIsFranchiseeDetail(String status) {
        preBCADetailsDto.setSameAsFranchisee(status);
        fragmentPersonalDetail.performSameAsFranchiseeActivity(status);


    }

    private void displayOTPDialog(String otpValue, final String expiryTime, String mobileNo) {

        if (verifyOTPDialog != null && verifyOTPDialog.isShowing()) {
            verifyOTPDialog.OTPValue(otpValue);
            return;
        }

        //Display Verify OTP Dialog
        verifyOTPDialog = new VerifyOTPDialog(context, mobileNo, new VerifyOTPDialog.IVerifyOTP() {
            @Override
            public void isOTPVerified() {

                //Save
                saveAllPreBCAData();
            }

            @Override
            public void resendOTP() {
                validateMobileNoUsingOTP(preBCADetailsDto.getFrMobileNumber());
            }
        });
        verifyOTPDialog.show();
        verifyOTPDialog.setCancelable(false);
        verifyOTPDialog.OTPValue(otpValue);
    }

    public void validateMobileNoUsingOTP(final String mobileNo) {

        if (TextUtils.isEmpty(mobileNo)) {
            showMessage("Mobile No. not exists.");
            return;
        }

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        asyncValidateMobileNoUsingOTP = new AsyncValidateMobileNoUsingOTP(context, mobileNo, new AsyncValidateMobileNoUsingOTP.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    //Handle Response
                    if (result.startsWith("OKAY")) {
                        StringTokenizer st1 = new StringTokenizer(result, "\\|");
                        String key = st1.nextToken();
                        final String OTP = st1.nextToken();
                        final String expiryTime = st1.nextToken();
                        displayOTPDialog(OTP, expiryTime, mobileNo);

                    } else {
                        StringTokenizer st1 = new StringTokenizer(result, "\\|");
                        String key = st1.nextToken();
                        String sco1 = st1.nextToken();
                        if (!TextUtils.isEmpty(sco1)) {
                            showMessage(sco1);
                            return;
                        }

                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncValidateMobileNoUsingOTP.execute("");
    }

    private void configureSMSRetreiver() {

        smsReceiver = new SMSRetrievalReceiver();
        smsReceiver.setOTPListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        context.registerReceiver(smsReceiver, intentFilter);

        SmsRetrieverClient client = SmsRetriever.getClient(context);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Android will provide message once receive. Start your broadcast receiver.
                IntentFilter filter = new IntentFilter();
                filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
                context.registerReceiver(new SMSRetrievalReceiver(), filter);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onOTPReceived(String otp) {
        String code = parseCode(otp);//Parse verification code
        showToast("OTP Received: " + code);
        Log.e(TAG, "OTP: " + code);
        if (verifyOTPDialog != null && verifyOTPDialog.isShowing())
            verifyOTPDialog.SetOTPValue(code);

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(smsReceiver);
        }
    }

    @Override
    public void onOTPTimeOut() {
        showToast("OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        showToast(error);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
}
