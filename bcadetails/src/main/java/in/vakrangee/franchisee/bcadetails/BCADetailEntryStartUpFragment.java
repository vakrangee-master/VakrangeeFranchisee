package in.vakrangee.franchisee.bcadetails;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class BCADetailEntryStartUpFragment extends BaseFragment {

    private static final String TAG = "BCADetailEntryStartUpFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private Logger logger;
    private DeprecateHandler deprecateHandler;

    public BCAEntryDetailsDto bcaEntryDetailsDto;


    //All Child Fragments
    BCABankDetailsFragment bcaBankDetailsFragment;
    BCABasicInformationFragment bcaBasicInformationFragment;
    BCAOutletInformationFragment bcaOutletInformationFragment;
    BCASupportingInfoFragment bcaSupportingInfoFragment;
    BCAOtherInformationFragment bcaOtherInformationFragment;


    private float startX;
    private int pos = 0;
    public final int MAX_COUNT = 5;
    private GestureDetector mGestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private BCADetailEntryStartUpFragment.ISwipeAndClicks iSwipeAndClicks;

    private static final String BCA_BANK_INFO = "1";
    private static final String BCA_BASIC_INFO = "2";
    private static final String BCA_OUTLET_INFO = "3";
    private static final String BCA_SUPPORTING_INFO = "4";
    private static final String BCA_OTHER_INFO = "5";
    private static final String BCA_FULL_SUBMIT = "6";

    private static final int MOVED_NEXT = 100;
    private static final int MOVED_PREVIOUS = 101;
    private static final int MOVED_TAB_CLICKED = 102;
    private boolean IsEditable = false;
    private Typeface font;
    private AsyncSaveBCAEntryDetails asyncSaveBCAEntryDetails = null;
    private LinearLayout layoutBCABankDetail;
    private LinearLayout layoutBCABasicInfo;
    private LinearLayout layoutBCASupportingInfo;
    private LinearLayout layoutBCAOtherInfo;
    private ViewFlipper viewFlipper;

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

    public BCADetailEntryStartUpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_bca_detail_startup, container, false);

        bindViewId(view);
        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        ButterKnife.bind(this, view);

        //All Child Fragments
        bcaBankDetailsFragment = (BCABankDetailsFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBCABankDetail);
        bcaBasicInformationFragment = (BCABasicInformationFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBCABasicInfo);
        bcaOutletInformationFragment = (BCAOutletInformationFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBCAOutletInfo);
        bcaSupportingInfoFragment = (BCASupportingInfoFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBCASupportingInfo);
        bcaOtherInformationFragment = (BCAOtherInformationFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBCAOtherInfo);

        //View Flipper
        mGestureDetector = new GestureDetector(context, new CustomGestureDetector());
        viewFlipper.setMeasureAllChildren(false);

        return view;
    }

    private void bindViewId(View view) {
        layoutBCABankDetail = view.findViewById(R.id.layoutBCABankDetail);
        layoutBCABasicInfo = view.findViewById(R.id.layoutBCABasicInfo);
        layoutBCASupportingInfo = view.findViewById(R.id.layoutBCASupportingInfo);
        layoutBCAOtherInfo = view.findViewById(R.id.layoutBCAOtherInfo);
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
        if (!IsEditable)
            handleJump(MOVED_NEXT);
        else
            saveData(MOVED_NEXT);
    }

    public void moveNext() {
        if (pos < (MAX_COUNT - 1)) {
            int status = getStatus(pos);
            pos++;
            setBottomLayoutVisibility(((BCADetailEntryStartUpActivity) getActivity()).getBackButton(), ((BCADetailEntryStartUpActivity) getActivity()).getNextButton());
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
            setBottomLayoutVisibility(((BCADetailEntryStartUpActivity) getActivity()).getBackButton(), ((BCADetailEntryStartUpActivity) getActivity()).getNextButton());
            viewFlipper.setInAnimation(context, R.anim.right_in);
            viewFlipper.setOutAnimation(context, R.anim.right_out);
            viewFlipper.showPrevious();
            iSwipeAndClicks.onPosChange(pos, status);
        }
    }

    public void showPrevious() {
        if (!IsEditable)
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
        setBottomLayoutVisibility(((BCADetailEntryStartUpActivity) getActivity()).getBackButton(), ((BCADetailEntryStartUpActivity) getActivity()).getNextButton());
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
            /*btnNext.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            btnNext.setPadding(10,0,10,0);*/
        } else {

            btnNext.setTypeface(font);
            btnNext.setText(new SpannableStringBuilder(context.getResources().getString(R.string.next) + " " + " " + new String(new char[]{0xf054})));

            /*btnNext.setText("Next");
            btnNext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);*/
        }

        ((BCADetailEntryStartUpActivity) getActivity()).setPagingText((pos + 1) + " of " + MAX_COUNT);

        ((BCADetailEntryStartUpActivity) getActivity()).setScrollViewScrolltop();
        ((BCADetailEntryStartUpActivity) getActivity()).headerStepsFragment.notifyAdapter(pos);
    }

    public void refresh(BCAEntryDetailsDto bcaEntryDetailsDto, BCADetailEntryStartUpFragment.ISwipeAndClicks iSwipeAndClicks) {
        this.bcaEntryDetailsDto = bcaEntryDetailsDto;
        refreshOtherFragments();
        this.iSwipeAndClicks = iSwipeAndClicks;
    }

    public String IsAllStepsValidated() {

        //STEP 1: Bank Information
        int step1 = bcaBankDetailsFragment.IsBankInformationValidated();
        if (step1 != 0)
            return BCA_BANK_INFO;

        //STEP 2: Basic Information
        int step2 = bcaBasicInformationFragment.IsBasicInformationValidated();
        if (step2 != 0)
            return BCA_BASIC_INFO;

        //STEP 3: Outlet Information
        int step3 = bcaOutletInformationFragment.IsOutletInformationValidated();
        if (step3 != 0)
            return BCA_OUTLET_INFO;

        //STEP 4: Supporting Information
        int step4 = bcaSupportingInfoFragment.IsSupportingInformationValidated();
        if (step4 != 0)
            return BCA_SUPPORTING_INFO;

        //STEP 5: Other Information
        int step5 = bcaOtherInformationFragment.IsOtherInformationValidated();
        if (step5 != 0)
            return BCA_OTHER_INFO;

        return "0";
    }

    public int getStatus(int pos) {
        String status = "0";

        switch (String.valueOf(pos)) {

            case BCA_BANK_INFO:
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getBankInfoStatus()) ? "0" : bcaEntryDetailsDto.getBankInfoStatus();
                break;

            case BCA_BASIC_INFO:
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getBasicInfoStatus()) ? "0" : bcaEntryDetailsDto.getBasicInfoStatus();
                break;

            case BCA_OUTLET_INFO:
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getOutletInfoStatus()) ? "0" : bcaEntryDetailsDto.getOutletInfoStatus();
                break;

            case BCA_SUPPORTING_INFO:
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getSupportingInfoStatus()) ? "0" : bcaEntryDetailsDto.getSupportingInfoStatus();
                break;

            case BCA_OTHER_INFO:
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getOtherInfoStatus()) ? "0" : bcaEntryDetailsDto.getOtherInfoStatus();
                break;

            default:
                break;
        }

        int value = Integer.parseInt(status);
        return value;
    }

    public void refreshOtherFragments() {

        handleBCAEntryStatus();
        if (((BCADetailEntryStartUpActivity) getActivity()).getBackButton() != null && ((BCADetailEntryStartUpActivity) getActivity()).getNextButton() != null) {
            setBottomLayoutVisibility(((BCADetailEntryStartUpActivity) getActivity()).getBackButton(), ((BCADetailEntryStartUpActivity) getActivity()).getNextButton());
        }

        bcaBankDetailsFragment.reloadData(bcaEntryDetailsDto.getBankInfoDetail(), IsEditable);
        bcaBasicInformationFragment.reloadData(bcaEntryDetailsDto.getBasicInfoDetail(), IsEditable);
        bcaOutletInformationFragment.reloadData(bcaEntryDetailsDto.getOutletInfoDetail(), IsEditable);
        bcaSupportingInfoFragment.reloadData(bcaEntryDetailsDto.getSupportingInfoDetail(), IsEditable);
        bcaOtherInformationFragment.reloadData(bcaEntryDetailsDto.getOtherInfoDetail(), IsEditable);
    }

    public void handleBCAEntryStatus() {
        //Handle Status
        IsEditable = false;
        try {
            if (!TextUtils.isEmpty(bcaEntryDetailsDto.getStatusDetail())) {

                JSONObject jsonObject = new JSONObject(bcaEntryDetailsDto.getStatusDetail());

                String status = jsonObject.optString("bca_status");
                int IsMagShown = jsonObject.optInt("bca_is_msg_shown");
                String msg = jsonObject.optString("bca_status_msg");
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

        if (type.equalsIgnoreCase(BCA_FULL_SUBMIT)) {
            String status = IsAllStepsValidated();
            if (!status.equalsIgnoreCase("0")) {

                int position = getPosByType(status);
                if (pos != position) {
                    displayLayout(position);
                }
                return;
            }
        }

        //partial save to server
        saveBCAEntryData(movedFrom, type);

    }

    //save all BCA Entry data -
    private void saveBCAEntryData(final int movedFrom, String type) {
        //base on type save data
        BCAEntryDetailsDto formDto = getBCAEntryDetailsDtoToSave(type);
        asyncSaveBCAEntryDetails = new AsyncSaveBCAEntryDetails(context, type, formDto, new AsyncSaveBCAEntryDetails.Callback() {
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
                        String key = st1.nextToken();
                        String data = st1.nextToken();
                        try {
                            Gson gson = new GsonBuilder().create();
                            List<BCAEntryDetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<BCAEntryDetailsDto>>() {
                            }.getType());
                            if (applicationList.size() > 0) {

                                bcaEntryDetailsDto = applicationList.get(0);
                                ((BCADetailEntryStartUpActivity) getActivity()).setBcaEntryDetailsDto(bcaEntryDetailsDto);
                                refreshOtherFragments();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
        asyncSaveBCAEntryDetails.execute("");
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

        String txt = ((BCADetailEntryStartUpActivity) getActivity()).getNextButton().getText().toString();
        if (txt.equalsIgnoreCase("Submit"))
            return BCA_FULL_SUBMIT;

        switch (pos) {

            case 0:
                type = BCA_BANK_INFO;
                break;

            case 1:
                type = BCA_BASIC_INFO;
                break;

            case 2:
                type = BCA_OUTLET_INFO;
                break;

            case 3:
                type = BCA_SUPPORTING_INFO;
                break;

            case 4:
                type = BCA_OTHER_INFO;
                break;

            case 5:
                type = BCA_FULL_SUBMIT;
                break;

            default:
                break;
        }
        return type;
    }

    private int getPosByType(String type) {
        int pos = 0;

        switch (type) {

            case BCA_BANK_INFO:
                pos = 0;
                break;

            case BCA_BASIC_INFO:
                pos = 1;
                break;

            case BCA_OUTLET_INFO:
                pos = 2;
                break;

            case BCA_SUPPORTING_INFO:
                pos = 3;
                break;

            case BCA_OTHER_INFO:
                pos = 4;
                break;

            default:
                break;

        }

        return pos;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncSaveBCAEntryDetails != null && !asyncSaveBCAEntryDetails.isCancelled()) {
            asyncSaveBCAEntryDetails.cancel(true);
        }

    }

    private BCAEntryDetailsDto getBCAEntryDetailsDtoToSave(String type) {
        BCAEntryDetailsDto detailsDto = bcaEntryDetailsDto;
        Gson gson = new Gson();

        switch (type) {

            case BCA_BANK_INFO:
                BCABankDetailsDto bcaBankDetailsDto = bcaBankDetailsFragment.getBCABankDetailsDto();
                String bankJson = gson.toJson(bcaBankDetailsDto, BCABankDetailsDto.class);

                detailsDto.setBankInfoDetail(bankJson);
                detailsDto.setBasicInfoDetail(null);
                detailsDto.setOutletInfoDetail(null);
                detailsDto.setSupportingInfoDetail(null);
                detailsDto.setOtherInfoDetail(null);

                break;

            case BCA_BASIC_INFO:
                BCABasicInformationDto basicInformationDto = bcaBasicInformationFragment.getBCABasicInformationDto();
                String basicJson = gson.toJson(basicInformationDto, BCABasicInformationDto.class);

                detailsDto.setBankInfoDetail(null);
                detailsDto.setBasicInfoDetail(basicJson);
                detailsDto.setOutletInfoDetail(null);
                detailsDto.setSupportingInfoDetail(null);
                detailsDto.setOtherInfoDetail(null);

                break;

            case BCA_OUTLET_INFO:
                BCAOutletInformationDto bcaOutletInformationDto = bcaOutletInformationFragment.getBCAOutletInformationDto();
                String outletJson = gson.toJson(bcaOutletInformationDto, BCAOutletInformationDto.class);

                detailsDto.setBankInfoDetail(null);
                detailsDto.setBasicInfoDetail(null);
                detailsDto.setOutletInfoDetail(outletJson);
                detailsDto.setSupportingInfoDetail(null);
                detailsDto.setOtherInfoDetail(null);

                break;

            case BCA_SUPPORTING_INFO:
                BCASupportingInformationDto bcaSupportingInformationDto = bcaSupportingInfoFragment.getBCASupportingInformationDto();
                String supportingJson = gson.toJson(bcaSupportingInformationDto, BCASupportingInformationDto.class);

                detailsDto.setBankInfoDetail(null);
                detailsDto.setBasicInfoDetail(null);
                detailsDto.setOutletInfoDetail(null);
                detailsDto.setSupportingInfoDetail(supportingJson);
                detailsDto.setOtherInfoDetail(null);

                break;

            case BCA_OTHER_INFO:
                BCAOtherInformationDto otherInformationDto = bcaOtherInformationFragment.getBCAOtherInformationDto();
                String otherJson = gson.toJson(otherInformationDto, BCAOtherInformationDto.class);

                detailsDto.setBankInfoDetail(null);
                detailsDto.setBasicInfoDetail(null);
                detailsDto.setOutletInfoDetail(null);
                detailsDto.setSupportingInfoDetail(null);
                detailsDto.setOtherInfoDetail(otherJson);

                break;

            default:

                String bankJson1 = gson.toJson(bcaBankDetailsFragment.getBCABankDetailsDto(), BCABankDetailsDto.class);
                String basicJson1 = gson.toJson(bcaBasicInformationFragment.getBCABasicInformationDto(), BCABasicInformationDto.class);
                String outletJson1 = gson.toJson(bcaOutletInformationFragment.getBCAOutletInformationDto(), BCAOutletInformationDto.class);
                String supportingJson1 = gson.toJson(bcaSupportingInfoFragment.getBCASupportingInformationDto(), BCASupportingInformationDto.class);
                String otherJson1 = gson.toJson(bcaOtherInformationFragment.getBCAOtherInformationDto(), BCAOtherInformationDto.class);

                detailsDto.setBankInfoDetail(bankJson1);
                detailsDto.setBasicInfoDetail(basicJson1);
                detailsDto.setOutletInfoDetail(outletJson1);
                detailsDto.setSupportingInfoDetail(supportingJson1);
                detailsDto.setOtherInfoDetail(otherJson1);
                break;
        }

        return detailsDto;
    }

    public void updateIsFranchiseeDetail(String status) {
        bcaEntryDetailsDto.setSameAsFranchisee(status);

        String kycDetails = bcaEntryDetailsDto.getKycDetail();
        if (TextUtils.isEmpty(kycDetails))
            return;

        Gson gson = new Gson();
        KYCDetailsDto kycDetailsDto = gson.fromJson(kycDetails, KYCDetailsDto.class);
        boolean IsChecked = (!TextUtils.isEmpty(status) && status.equalsIgnoreCase("1")) ? true : false;

        //Basic Information
        bcaBasicInformationFragment.updateBasicKYCDetails(IsChecked, kycDetailsDto);
        //Supporting Information
        bcaSupportingInfoFragment.updateSupportingKYCDetails(IsChecked, kycDetailsDto);

    }
}
