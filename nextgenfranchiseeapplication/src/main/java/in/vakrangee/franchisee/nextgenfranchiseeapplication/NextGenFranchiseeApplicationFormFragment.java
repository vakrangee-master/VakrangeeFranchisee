package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.NextGenFranchiseeApplicationFormDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

@SuppressLint("ValidFragment")
public class NextGenFranchiseeApplicationFormFragment extends BaseFragment {

    public NextGenFranchiseeApplicationFormDto applicationFormDto;
    private View view;
    private Context context;
    private FranchiseeDetails franchiseeDetails;

    //All Child Fragments
    FAPFranchiseeDetailFragment fapFranchiseeDetailFragment;
    FAPAddressFragment fragmentFAPAddress;
    FAPContactFragment fragmentFAPContact;
    FAPGeneralFragment fragmentFAPGeneral;
    FAPBankDetailFragment fragmentFAPBankDetails;
    FAPProposedKendraDetailFragment fragmentFAPProposedKendraDetails;
    FAPReferencesFragment fragmentFAPReferences;
    FAPCriteriaFragment fragmentFAPCriteria;

    private LinearLayout layoutParentFranchiseeDetail;
    private LinearLayout layoutParentAddress;
    private LinearLayout layoutParentContact;
    private LinearLayout layoutParentGeneral;
    private LinearLayout layoutParentBankDetails;
    private LinearLayout layoutParentProposedDetails;
    private LinearLayout layoutParentReferences;
    private LinearLayout layoutParentCriteria;
    private ViewFlipper viewFlipper;

    private int pos = 0;
    public final int MAX_COUNT = 8;
    private GestureDetector mGestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ISwipeAndClicks iSwipeAndClicks;

    private static final String FAP_FRANCHISEE_DETAILS = "1";
    private static final String FAP_ADDRESS = "2";
    private static final String FAP_CONTACT_INFO = "3";
    private static final String FAP_GENERAL_INFO = "4";
    private static final String FAP_BANK_DETAILS = "5";
    private static final String FAP_PROPOSED_KENDRA_DETAILS = "6";
    private static final String FAP_REFERENCES = "7";
    private static final String FAP_CRITERIA = "8";
    private static final String FAP_CRITERIA_FULL_SUBMIT = "9";

    private static final int MOVED_NEXT = 100;
    private static final int MOVED_PREVIOUS = 101;
    private static final int MOVED_TAB_CLICKED = 102;
    private boolean IsEditable = false;
    public boolean isCIBILPoliceEditable = false, isOwnershipProofEditable = false,
            isBankStmtEditable = false, isKendraAddressEditable = false, isMSMEEditable = false,isGSTINEditable = false;
    public static int statusCode;
    private Typeface font;

    private AsyncSaveFranchiseeApplication asyncSaveFranchiseeApplication = null;
    private AsyncPanCardValidation asyncPanCardValidation;

    public interface ISwipeAndClicks {

        /**
         * STATUS: -1 - Error(Validation failed)
         * STATUS: 0 - Pending
         * STATUS: 1 - All Entered and Validated
         * STATUS: 2 - Partial
         *
         * @param position
         * @param status
         */
        public void onPosChange(int position, int status);

    }

    public NextGenFranchiseeApplicationFormFragment() {
    }

    public NextGenFranchiseeApplicationFormFragment(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_franchisee_application_form, container, false);

        bindViewID(view);
        //Initialize data
        this.context = getContext();
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        ButterKnife.bind(this, view);

        //All Child Fragments
        fapFranchiseeDetailFragment = (FAPFranchiseeDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPFranchiseeDetails);
        fragmentFAPAddress = (FAPAddressFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPAddress);
        fragmentFAPContact = (FAPContactFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPContact);
        fragmentFAPGeneral = (FAPGeneralFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPGeneral);
        fragmentFAPBankDetails = (FAPBankDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPBankDetails);
        fragmentFAPProposedKendraDetails = (FAPProposedKendraDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPProposedKendraDetails);
        fragmentFAPReferences = (FAPReferencesFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPReferences);
        fragmentFAPCriteria = (FAPCriteriaFragment) getChildFragmentManager().findFragmentById(R.id.fragmentFAPCriteria);

        //View Flipper
        mGestureDetector = new GestureDetector(context, new CustomGestureDetector());
        viewFlipper.setMeasureAllChildren(false);
        return view;
    }

    private void bindViewID(View view) {
        layoutParentFranchiseeDetail = view.findViewById(R.id.layoutParentFranchiseeDetail);
        layoutParentAddress = view.findViewById(R.id.layoutParentAddress);
        layoutParentContact = view.findViewById(R.id.layoutParentContact);
        layoutParentGeneral = view.findViewById(R.id.layoutParentGeneral);
        layoutParentBankDetails = view.findViewById(R.id.layoutParentBankDetails);
        layoutParentProposedDetails = view.findViewById(R.id.layoutParentProposedDetails);
        layoutParentReferences = view.findViewById(R.id.layoutParentReferences);
        layoutParentCriteria = view.findViewById(R.id.layoutParentCriteria);
        viewFlipper = view.findViewById(R.id.viewFlipper);
    }

    public void touchViewFlipper(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }

            /* positive value means right to left direction */
            final float distance = e1.getX() - e2.getX();
            final boolean enoughSpeed = Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY;
            if (distance > SWIPE_MIN_DISTANCE && enoughSpeed) {
                // right to left swipe
                showNext();
                return true;
            } else if (distance < -SWIPE_MIN_DISTANCE && enoughSpeed) {
                // left to right swipe
                showPrevious();
                return true;
            } else {
                // oooou, it didn't qualify; do nothing
                return false;
            }
        }
    }

    public void showNext() {
        if (!IsEditable && !isOwnershipProofEditable && !isCIBILPoliceEditable && !isBankStmtEditable && !isKendraAddressEditable && !isGSTINEditable && !isMSMEEditable)
            handleJump(MOVED_NEXT);
        else
            saveData(MOVED_NEXT);
    }

    public void moveNext() {
        if (pos < (MAX_COUNT - 1)) {
            int status = getStatus(pos);
            pos++;
            setBottomLayoutVisibility(((NextGenFranchiseeApplicationActivity) getActivity()).getBackButton(), ((NextGenFranchiseeApplicationActivity) getActivity()).getNextButton());
            viewFlipper.setInAnimation(context, R.anim.left_in);
            viewFlipper.setOutAnimation(context, R.anim.left_out);
            viewFlipper.showNext();
            iSwipeAndClicks.onPosChange(pos, status);
        }
    }

    public void moveBack() {
        if (pos > 0) {
            int status = getStatus(pos);
            pos--;
            setBottomLayoutVisibility(((NextGenFranchiseeApplicationActivity) getActivity()).getBackButton(), ((NextGenFranchiseeApplicationActivity) getActivity()).getNextButton());
            viewFlipper.setInAnimation(context, R.anim.right_in);
            viewFlipper.setOutAnimation(context, R.anim.right_out);
            viewFlipper.showPrevious();
            iSwipeAndClicks.onPosChange(pos, status);
        }
    }

    public void showPrevious() {
        if (!IsEditable && !isOwnershipProofEditable && !isCIBILPoliceEditable  && !isBankStmtEditable && !isKendraAddressEditable && !isGSTINEditable && !isMSMEEditable)
            handleJump(MOVED_PREVIOUS);
        else
            saveData(MOVED_PREVIOUS);
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
        setBottomLayoutVisibility(((NextGenFranchiseeApplicationActivity) getActivity()).getBackButton(), ((NextGenFranchiseeApplicationActivity) getActivity()).getNextButton());
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
            btnNext.setText("Submit");

        } else {

            btnNext.setTypeface(font);
            btnNext.setText(new SpannableStringBuilder(context.getResources().getString(R.string.next) + " " + " " + new String(new char[]{0xf054})));

        }

        ((NextGenFranchiseeApplicationActivity) getActivity()).setPagingText((pos + 1) + " of " + MAX_COUNT);

        ((NextGenFranchiseeApplicationActivity) getActivity()).setScrollViewScrolltop();
        ((NextGenFranchiseeApplicationActivity) getActivity()).headerStepsFragment.notifyAdapter(pos);
    }

    public void refresh(NextGenFranchiseeApplicationFormDto applicationFormDto, ISwipeAndClicks iSwipeAndClicks) {
        this.applicationFormDto = applicationFormDto;
        refreshOtherFragments();
        this.iSwipeAndClicks = iSwipeAndClicks;
    }

    public String IsAllStepsValidated() {

        //STEP 1: Franchisee Details
        int step1 = fapFranchiseeDetailFragment.IsFranchiseeDetailsValidated();
        if (step1 != 0)
            return FAP_FRANCHISEE_DETAILS;

        //STEP 2: Address
        int step2 = fragmentFAPAddress.IsFranchiseeAddressValidated();
        if (step2 != 0)
            return FAP_ADDRESS;

        //STEP 3: Contact Information
        int step3 = fragmentFAPContact.IsFranchiseeContactValidated();
        if (step3 != 0)
            return FAP_CONTACT_INFO;

        //STEP 4: General Information
        int step4 = fragmentFAPGeneral.IsFranchiseeGeneralInfoValidated();
        if (step4 != 0)
            return FAP_GENERAL_INFO;

        //STEP 5: Bank Details
        int step5 = fragmentFAPBankDetails.IsFranchiseeBankDetailsValidated();
        if (step5 != 0)
            return FAP_BANK_DETAILS;

        //STEP 6: Proposed Vakrangee Kendra Detail
        int step6 = fragmentFAPProposedKendraDetails.IsFranchiseeProposedValidated();
        if (step6 != 0)
            return FAP_PROPOSED_KENDRA_DETAILS;

        //STEP 7: References
        int step7 = fragmentFAPReferences.IsFranchiseeReferencesValidated();
        if (step7 != 0)
            return FAP_REFERENCES;

        //STEP 8: Criteria
        int step8 = fragmentFAPCriteria.IsFranchiseeCriteriaValidated();
        if (step8 != 0)
            return FAP_CRITERIA;

        return "0";
    }

    public int getStatus(int pos) {
        String status = "0";

        switch (String.valueOf(pos)) {

            case FAP_FRANCHISEE_DETAILS:
                status = TextUtils.isEmpty(applicationFormDto.getFranchiseeDetailStatus()) ? "0" : applicationFormDto.getFranchiseeDetailStatus();
                break;

            case FAP_ADDRESS:
                status = TextUtils.isEmpty(applicationFormDto.getAddressStatus()) ? "0" : applicationFormDto.getAddressStatus();
                break;

            case FAP_CONTACT_INFO:
                status = TextUtils.isEmpty(applicationFormDto.getContactInfoStatus()) ? "0" : applicationFormDto.getContactInfoStatus();
                break;

            case FAP_GENERAL_INFO:
                status = TextUtils.isEmpty(applicationFormDto.getGeneralInfoStatus()) ? "0" : applicationFormDto.getGeneralInfoStatus();
                break;

            case FAP_BANK_DETAILS:
                status = TextUtils.isEmpty(applicationFormDto.getBankDetailStatus()) ? "0" : applicationFormDto.getBankDetailStatus();
                break;

            case FAP_PROPOSED_KENDRA_DETAILS:
                status = TextUtils.isEmpty(applicationFormDto.getProposedVKDetailStatus()) ? "0" : applicationFormDto.getProposedVKDetailStatus();
                break;

            case FAP_REFERENCES:
                status = TextUtils.isEmpty(applicationFormDto.getReferencesStatus()) ? "0" : applicationFormDto.getReferencesStatus();
                break;

            case FAP_CRITERIA:
                status = TextUtils.isEmpty(applicationFormDto.getCriteriaStatus()) ? "0" : applicationFormDto.getCriteriaStatus();
                break;

            default:
                break;
        }

        int value = Integer.parseInt(status);
        return value;
    }

    public void refreshOtherFragments() {

        handleFranchiseeStatus();
        if (((NextGenFranchiseeApplicationActivity) getActivity()).getBackButton() != null && ((NextGenFranchiseeApplicationActivity) getActivity()).getNextButton() != null) {
            setBottomLayoutVisibility(((NextGenFranchiseeApplicationActivity) getActivity()).getBackButton(), ((NextGenFranchiseeApplicationActivity) getActivity()).getNextButton());
        }

        fapFranchiseeDetailFragment.reloadData(applicationFormDto.getFranchiseeDetails(), IsEditable);
        fragmentFAPAddress.reloadData(applicationFormDto.getAddress(), IsEditable);
        fragmentFAPContact.reloadData(applicationFormDto.getContactInfo(), IsEditable);
        fragmentFAPGeneral.reloadData(applicationFormDto.getGeneralInfo(), IsEditable);
        fragmentFAPBankDetails.reloadData(applicationFormDto.getBankDetails(), IsEditable);
        fragmentFAPProposedKendraDetails.reloadData(applicationFormDto.getProposedVakrangeeKendraDetail(), IsEditable);
        fragmentFAPReferences.reloadData(applicationFormDto.getReferences(), IsEditable);
        fragmentFAPCriteria.reloadData(applicationFormDto.getCriteria(), IsEditable);
    }

    public void handleFranchiseeStatus() {
        //Handle Status
        IsEditable = false;
        statusCode = -1;
        try {
            if (!TextUtils.isEmpty(applicationFormDto.getStatusDetail())) {

                JSONObject jsonObject = new JSONObject(applicationFormDto.getStatusDetail());

                String status = jsonObject.optString("fa_status");
                int IsMagShown = jsonObject.optInt("fa_is_msg_shown");
                String msg = jsonObject.optString("fa_status_msg");
                String is_ownershipEditable = jsonObject.optString("is_ownership_proof_editable");
                String is_cibil_police_editable = jsonObject.optString("is_cibil_police_editable");

                String is_bank_stmt_editable = jsonObject.optString("is_bank_stmt_editable");
                String is_kendra_address_editable = jsonObject.optString("is_kendra_address_editable");
                String is_msme_editable = jsonObject.optString("is_msme_editable");
                String is_gstin_editable = jsonObject.optString("is_gstin_editable");

                isCIBILPoliceEditable = (!TextUtils.isEmpty(is_cibil_police_editable) && is_cibil_police_editable.equalsIgnoreCase("1")) ? true : false;
                isOwnershipProofEditable = (!TextUtils.isEmpty(is_ownershipEditable) && is_ownershipEditable.equalsIgnoreCase("1")) ? true : false;

                isBankStmtEditable = (!TextUtils.isEmpty(is_bank_stmt_editable) && is_bank_stmt_editable.equalsIgnoreCase("1")) ? true : false;
                isKendraAddressEditable = (!TextUtils.isEmpty(is_kendra_address_editable) && is_kendra_address_editable.equalsIgnoreCase("1")) ? true : false;
                isGSTINEditable = (!TextUtils.isEmpty(is_gstin_editable) && is_gstin_editable.equalsIgnoreCase("1")) ? true : false;
                isMSMEEditable = (!TextUtils.isEmpty(is_msme_editable) && is_msme_editable.equalsIgnoreCase("1")) ? true : false;

                statusCode = jsonObject.optInt("fa_status_code", -1);
                IsEditable = (!TextUtils.isEmpty(status) && status.equalsIgnoreCase("E")) ? true : false;
                boolean IsMsgBeShowned = IsMagShown == 1 ? true : false;

                if (IsMsgBeShowned)
                    showMessage(msg);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData(final int movedFrom) {

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        final String type = getType();

        if (TextUtils.isEmpty(type))
            return;

        if (type.equalsIgnoreCase(FAP_CRITERIA_FULL_SUBMIT)) {
            String status = IsAllStepsValidated();
            if (!status.equalsIgnoreCase("0")) {

                int position = getPosByType(status);
                if (pos != position) {
                    displayLayout(position);
                }
                return;
            }
        } else if (!type.equalsIgnoreCase(FAP_CRITERIA_FULL_SUBMIT)) {
            int step1 = fapFranchiseeDetailFragment.IsFranchiseeNameValidated();
            if (step1 != 0) {
                displayLayout(0);
                return;
            }
        }

        //if all step are not completed
        if (!IsAllStepsValidated().equalsIgnoreCase("0")) {
            //partial save to server
            saveFApplicationData(movedFrom, type);
        } else {
            //TODO : change - pancard validation in Franchisee application -Nilesh Dhola -23-04-2019
            //after check all application information - then check pan card validation via web service
            //after validation PAN card - save data from server.
            if (IsAllStepsValidated().equalsIgnoreCase("0")) {
                FAPGeneralInfoDto generalDto = fragmentFAPGeneral.getFapGeneralInfoDto();
                if (generalDto.getIsHavingPANCard().equalsIgnoreCase("0")) {
                    isPanCardValidationCheck(movedFrom, type, generalDto.getPanNumber1(), generalDto.getNameOnPANCard(), "final");
                } else {
                    saveFApplicationData(movedFrom, type);
                }
            } else {
                saveFApplicationData(movedFrom, type);
            }
        }
    }

    //region pan card validation check
    private void isPanCardValidationCheck(final int movedFrom, final String type, String panNumber, String pancardName, final String submit_type) {
        asyncPanCardValidation = new AsyncPanCardValidation(context, panNumber,
                pancardName, new AsyncPanCardValidation.Callback() {
            @Override
            public void onResult(String result) {
                processPANValidationResult(result, submit_type, movedFrom, type);
            }
        });
        asyncPanCardValidation.execute();

    }
    //endregion

    private void processPANValidationResult(String result, String submit_type, int movedFrom, final String type) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                return;
            }
            if (result.startsWith("OKAY|")) {
                if (submit_type.equalsIgnoreCase("final")) {
                    //if submit type- final check validation - partial save
                    saveFApplicationData(movedFrom, type);
                } else {
                    //if next button click - save and check validation - partial save
                    saveFranchiseeApplicationDetails(movedFrom, type);
                }

            } else if (result.startsWith("ERROR|")) {
                result = result.replace("ERROR|", "");
                if (TextUtils.isEmpty(result)) {
                    //if any error
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                } else {
                    //if any error goto - type 4
                    AlertDialogBoxInfo.alertDialogShow(context, result);
                    displayLayout(3);
                }

            } else {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
        }
    }

    //save all Frachisee application data -
    private void saveFApplicationData(final int movedFrom, String type) {
        //TODO : change - pancard validation in Franchisee application -Nilesh Dhola -01-05-2019
        //pan card verification - when next button click - Nilesh dhola- 02-05-2019
        FAPGeneralInfoDto generalDto = fragmentFAPGeneral.getFapGeneralInfoDto();

        if (type.equalsIgnoreCase("4") && !TextUtils.isEmpty(generalDto.getCibilScore())) {
            int status = fragmentFAPGeneral.isCIBILScoreValidated();
            if (status != 0) {
                showMessage(fragmentFAPGeneral.cibilNote);
                return;
            }
        }

        //1. if both are empty - cant check
        if (type.equalsIgnoreCase("4") && !TextUtils.isEmpty(generalDto.getNameOnPANCard()) || !TextUtils.isEmpty(generalDto.getPanNumber1())) {
            if (type.equalsIgnoreCase("4") && TextUtils.isEmpty(generalDto.getNameOnPANCard())) {
                //1.1 - if pan name is available
                AlertDialogBoxInfo.alertDialogShow(getActivity(), "please insert PAN Name");
            } else if (type.equalsIgnoreCase("4") && TextUtils.isEmpty(generalDto.getPanNumber1())) {
                //1.2 - if pan card number  is available
                AlertDialogBoxInfo.alertDialogShow(getActivity(), "please insert PAN Number");
            } else if (type.equalsIgnoreCase("4") && !TextUtils.isEmpty(generalDto.getPanNumber1()) && !TextUtils.isEmpty(generalDto.getNameOnPANCard())) {
                //1.3 - if pan name and pan card number is available  and Next button click
                isPanCardValidationCheck(movedFrom, type, generalDto.getPanNumber1(), generalDto.getNameOnPANCard(), "next");
            } else {
                // 1.4 - not type 4 -direct save data - partial  save
                saveFranchiseeApplicationDetails(movedFrom, type);
            }
        } else {
            // save partial data
            saveFranchiseeApplicationDetails(movedFrom, type);
        }
    }

    private void saveFranchiseeApplicationDetails(final int movedFrom, String type) {
        //base on type save data
        NextGenFranchiseeApplicationFormDto formDto = getApplicationFormDtoToSave(type);
        if (formDto != null) {
            FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(context);
            if (loginChecksDto != null && !TextUtils.isEmpty(loginChecksDto.getNextGenEnquiryId()))
                formDto.setNextgenEnquiryId(loginChecksDto.getNextGenEnquiryId());
        }
        asyncSaveFranchiseeApplication = new AsyncSaveFranchiseeApplication(context, type, formDto, new AsyncSaveFranchiseeApplication.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }
                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        st1.nextToken();
                        String franchiseeData = st1.nextToken();
                        Gson gson = new GsonBuilder().create();
                        List<NextGenFranchiseeApplicationFormDto> applicationList = gson.fromJson(franchiseeData, new TypeToken<ArrayList<NextGenFranchiseeApplicationFormDto>>() {
                        }.getType());
                        if (applicationList.size() > 0) {

                            applicationFormDto = applicationList.get(0);
                            ((NextGenFranchiseeApplicationActivity) getActivity()).setApplicationFormDto(applicationFormDto);
                            refreshOtherFragments();
                        }
                        handleJump(movedFrom);

                    } else {
                        showMessage("Details saving failed.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveFranchiseeApplication.execute("");
    }
    //endregion

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

    private String getType() {
        String type = null;

        String txt = ((NextGenFranchiseeApplicationActivity) getActivity()).getNextButton().getText().toString();
        if (txt.equalsIgnoreCase("Submit"))
            return FAP_CRITERIA_FULL_SUBMIT;

        switch (pos) {

            case 0:
                type = FAP_FRANCHISEE_DETAILS;
                break;

            case 1:
                type = FAP_ADDRESS;
                break;

            case 2:
                type = FAP_CONTACT_INFO;
                break;

            case 3:
                type = FAP_GENERAL_INFO;
                break;

            case 4:
                type = FAP_BANK_DETAILS;
                break;

            case 5:
                type = FAP_PROPOSED_KENDRA_DETAILS;
                break;

            case 6:
                type = FAP_REFERENCES;
                break;

            case 7:
                type = FAP_CRITERIA;
                break;

            case 8:
                type = FAP_CRITERIA_FULL_SUBMIT;
                break;

            default:
                break;
        }
        return type;
    }

    private int getPosByType(String type) {
        int pos = 0;

        switch (type) {

            case FAP_FRANCHISEE_DETAILS:
                pos = 0;
                break;

            case FAP_ADDRESS:
                pos = 1;
                break;

            case FAP_CONTACT_INFO:
                pos = 2;
                break;

            case FAP_GENERAL_INFO:
                pos = 3;
                break;

            case FAP_BANK_DETAILS:
                pos = 4;
                break;

            case FAP_PROPOSED_KENDRA_DETAILS:
                pos = 5;
                break;

            case FAP_REFERENCES:
                pos = 6;
                break;

            case FAP_CRITERIA:
                pos = 7;
                break;

            default:
                break;

        }

        return pos;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncSaveFranchiseeApplication != null && !asyncSaveFranchiseeApplication.isCancelled()) {
            asyncSaveFranchiseeApplication.cancel(true);
        }
        if (asyncPanCardValidation != null && !asyncPanCardValidation.isCancelled()) {
            asyncPanCardValidation.cancel(true);
        }
    }

    private NextGenFranchiseeApplicationFormDto getApplicationFormDtoToSave(String type) {
        NextGenFranchiseeApplicationFormDto formDto = applicationFormDto;
        Gson gson = new Gson();

        FAPFranchiseeDetailsDto franchiseeDto2 = fapFranchiseeDetailFragment.getFranchiseeDetailsDto();
        String franchiseeJson2 = gson.toJson(franchiseeDto2, FAPFranchiseeDetailsDto.class);

        switch (type) {

            case FAP_FRANCHISEE_DETAILS:
                FAPFranchiseeDetailsDto franchiseeDto = fapFranchiseeDetailFragment.getFranchiseeDetailsDto();
                String franchiseeJson = gson.toJson(franchiseeDto, FAPFranchiseeDetailsDto.class);

                formDto.setFranchiseeDetails(franchiseeJson);
                formDto.setAddress(null);
                formDto.setContactInfo(null);
                formDto.setGeneralInfo(null);
                formDto.setBankDetails(null);
                formDto.setProposedVakrangeeKendraDetail(null);
                formDto.setReferences(null);
                formDto.setCriteria(null);

                break;

            case FAP_ADDRESS:
                FAPAddressDto addressDto = fragmentFAPAddress.getFapAddressDto();
                String addressJson = gson.toJson(addressDto, FAPAddressDto.class);

                formDto.setFranchiseeDetails(franchiseeJson2);
                formDto.setAddress(addressJson);
                formDto.setContactInfo(null);
                formDto.setGeneralInfo(null);
                formDto.setBankDetails(null);
                formDto.setProposedVakrangeeKendraDetail(null);
                formDto.setReferences(null);
                formDto.setCriteria(null);

                break;

            case FAP_CONTACT_INFO:
                FAPContactInfoDto contactDto = fragmentFAPContact.getFapContactInfoDto();
                String contactJson = gson.toJson(contactDto, FAPContactInfoDto.class);

                formDto.setFranchiseeDetails(franchiseeJson2);
                formDto.setAddress(null);
                formDto.setContactInfo(contactJson);
                formDto.setGeneralInfo(null);
                formDto.setBankDetails(null);
                formDto.setProposedVakrangeeKendraDetail(null);
                formDto.setReferences(null);
                formDto.setCriteria(null);

                break;

            case FAP_GENERAL_INFO:
                FAPGeneralInfoDto generalDto = fragmentFAPGeneral.getFapGeneralInfoDto();
                String generalJson = gson.toJson(generalDto, FAPGeneralInfoDto.class);

                formDto.setFranchiseeDetails(franchiseeJson2);
                formDto.setAddress(null);
                formDto.setContactInfo(null);
                formDto.setGeneralInfo(generalJson);
                formDto.setBankDetails(null);
                formDto.setProposedVakrangeeKendraDetail(null);
                formDto.setReferences(null);
                formDto.setCriteria(null);

                break;

            case FAP_BANK_DETAILS:
                FAPBankDetailsDto bankDto = fragmentFAPBankDetails.getFapBankDetailsDto();
                String bankJson = gson.toJson(bankDto, FAPBankDetailsDto.class);

                formDto.setFranchiseeDetails(franchiseeJson2);
                formDto.setAddress(null);
                formDto.setContactInfo(null);
                formDto.setGeneralInfo(null);
                formDto.setBankDetails(bankJson);
                formDto.setProposedVakrangeeKendraDetail(null);
                formDto.setReferences(null);
                formDto.setCriteria(null);

                break;

            case FAP_PROPOSED_KENDRA_DETAILS:
                FAPProposedKendraDetailDto kendraDetailDto = fragmentFAPProposedKendraDetails.getFapProposedKendraDetailDto();
                String kendraJson = gson.toJson(kendraDetailDto, FAPProposedKendraDetailDto.class);

                formDto.setFranchiseeDetails(franchiseeJson2);
                formDto.setAddress(null);
                formDto.setContactInfo(null);
                formDto.setGeneralInfo(null);
                formDto.setBankDetails(null);
                formDto.setProposedVakrangeeKendraDetail(kendraJson);
                formDto.setReferences(null);
                formDto.setCriteria(null);

                break;

            case FAP_REFERENCES:
                FAPReferenceDto referenceDto = fragmentFAPReferences.getFapReferenceDto();
                String refJson = gson.toJson(referenceDto, FAPReferenceDto.class);

                formDto.setFranchiseeDetails(franchiseeJson2);
                formDto.setAddress(null);
                formDto.setContactInfo(null);
                formDto.setGeneralInfo(null);
                formDto.setBankDetails(null);
                formDto.setProposedVakrangeeKendraDetail(null);
                formDto.setReferences(refJson);
                formDto.setCriteria(null);

                break;

            case FAP_CRITERIA:
                FAPCriteriaDto criteriaDto = fragmentFAPCriteria.getCriteriaDto();
                String criteriaJson = gson.toJson(criteriaDto, FAPCriteriaDto.class);

                formDto.setFranchiseeDetails(franchiseeJson2);
                formDto.setAddress(null);
                formDto.setContactInfo(null);
                formDto.setGeneralInfo(null);
                formDto.setBankDetails(null);
                formDto.setProposedVakrangeeKendraDetail(null);
                formDto.setReferences(null);
                formDto.setCriteria(criteriaJson);

                break;

            default:
                String franchiseeJson1 = gson.toJson(fapFranchiseeDetailFragment.getFranchiseeDetailsDto(), FAPFranchiseeDetailsDto.class);
                String addressJson1 = gson.toJson(fragmentFAPAddress.getFapAddressDto(), FAPAddressDto.class);
                String contactJson1 = gson.toJson(fragmentFAPContact.getFapContactInfoDto(), FAPContactInfoDto.class);
                String generalJson1 = gson.toJson(fragmentFAPGeneral.getFapGeneralInfoDto(), FAPGeneralInfoDto.class);
                String bankJson1 = gson.toJson(fragmentFAPBankDetails.getFapBankDetailsDto(), FAPBankDetailsDto.class);
                String kendraJson1 = gson.toJson(fragmentFAPProposedKendraDetails.getFapProposedKendraDetailDto(), FAPProposedKendraDetailDto.class);
                String refJson1 = gson.toJson(fragmentFAPReferences.getFapReferenceDto(), FAPReferenceDto.class);
                String criteriaJson1 = gson.toJson(fragmentFAPCriteria.getCriteriaDto(), FAPCriteriaDto.class);

                formDto.setFranchiseeDetails(franchiseeJson1);
                formDto.setAddress(addressJson1);
                formDto.setContactInfo(contactJson1);
                formDto.setGeneralInfo(generalJson1);
                formDto.setBankDetails(bankJson1);
                formDto.setProposedVakrangeeKendraDetail(kendraJson1);
                formDto.setReferences(refJson1);
                formDto.setCriteria(criteriaJson1);
                break;
        }

        return formDto;
    }

}
