package in.vakrangee.franchisee.sitelayout;

import android.content.Context;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.json.JSONObject;

import java.util.List;

import in.vakrangee.franchisee.sitelayout.fragment.NextGenKendraLocationFragment;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenKendraPhotoFragment;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenProfileFragment;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenSiteVisitFragment;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeRemarkDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeTimeLineDetails;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class NextGenViewPager extends FragmentStatePagerAdapter {


    static final String TAG = NextGenViewPager.class.getCanonicalName();
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    List<FranchiseeTimeLineDetails> franchiseeTimeLineDetailsList;
    Logger logger;

    public static final int SITE_VERIFIED_AND_APPROVED = 0;
    public static final int SITE_REJECTED = 1;
    public static final int SITE_HOLD = 2;
    public static final int SITE_SEND_BACK_FOR_CORRECTION = 3;
    public static final int SITE_REESUBMITTED_FOR_VERIFICATION = 4;

    public static final int SITE_VISIT_FULL_UPDATE = 7;
    public static final int SITE_I_WILL_EXIT = 8;
    public static final int SITE_YET_TO_BE_IDENTIFIED = 9;
    public static final int SITE_INORMATION_REVIEWED_AND_CONFIRMED = 10;

    //4 - Resubmit
    // 5 - Geo
    // 6 - Photo
    // 7 - Site Visit Full Update
    // 8 - I will Exit
    // 9 - Yet to be identified
    // 10 - Information Reviewed and Confirmed

    public static boolean needToShowAlert;

    public NextGenViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, FranchiseeDetails franchiseeDetails) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.franchiseeDetails = franchiseeDetails;
        logger = Logger.getInstance(context);

        needToShowAlert = true;
        initStatusAndRemarks();
    }

    // Extract NextGen Site Visit Status and its Description
    private void initStatusAndRemarks() {
        try {
            franchiseeTimeLineDetailsList = franchiseeDetails.getTimeLineList();
            if (franchiseeTimeLineDetailsList != null &&
                    franchiseeTimeLineDetailsList.size() > 0) {

                int status = -1;
                FranchiseeTimeLineDetails franchiseeTimeLineDetails = franchiseeTimeLineDetailsList.get(franchiseeTimeLineDetailsList.size() - 1);
                status = Integer.parseInt(franchiseeTimeLineDetails.getNextgenSiteVisitStatus());
                String desc = franchiseeTimeLineDetails.getNextgenSiteVisitDescription();

                franchiseeDetails.setStatus(status);
                franchiseeDetails.setStatusDesc(desc);

                // Based on status allow to edit site detail
                //status = SITE_VISIT_FULL_UPDATE;       //For testing
                if (status == SITE_VERIFIED_AND_APPROVED || status == SITE_REJECTED || status == SITE_HOLD) {
                    franchiseeDetails.setAllowToEdit(false);
                } else if (status == SITE_VISIT_FULL_UPDATE || status == SITE_I_WILL_EXIT || status == SITE_YET_TO_BE_IDENTIFIED) {
                    franchiseeDetails.setAllowToEdit(true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            franchiseeDetails.setNeedToBeReviewed(true);
                            refreshAllFragments(franchiseeDetails.isNeedToBeReviewed());
                        }
                    }, 3500);


                   /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.alert_reviewed_confirmation_msg))
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    franchiseeDetails.setNeedToBeReviewed(true);
                                    refreshAllFragments(franchiseeDetails.isNeedToBeReviewed());

                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();*/

                } else {
                    franchiseeDetails.setAllowToEdit(true);
                    //franchiseeDetails.setNeedToBeReviewed(true);
                    //refreshAllFragments(franchiseeDetails.isNeedToBeReviewed());

                }

                // Extract Remarks Data Based On Status
                if (status == SITE_SEND_BACK_FOR_CORRECTION) {
                    JSONObject jsonObject = JSONUtils.convertStringToJSONObject(franchiseeTimeLineDetails.getNextgenSiteVisitRemarks(), "\\r?\\n", ":");
                    FranchiseeRemarkDetails franchiseeRemarkDetails = JSONUtils.toJson(FranchiseeRemarkDetails.class, jsonObject.toString());
                    franchiseeDetails.setFranchiseeRemarkDetails(franchiseeRemarkDetails);

                    AlertDialogBoxInfo.alertDialogShow(context, "There is an issue with Site Detail. Please re-submit the detail.");
                }
                return;
            } else {
                franchiseeDetails.setAllowToEdit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "Error in initializing status and remarks : " + e.toString());
        }
    }

    NextGenProfileFragment tab1;
    NextGenKendraLocationFragment tab2;
    NextGenKendraPhotoFragment tab3;
    NextGenSiteVisitFragment tab4;

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new NextGenProfileFragment(franchiseeDetails);
                return tab1;
            case 1:
                tab2 = new NextGenKendraLocationFragment(franchiseeDetails);
                return tab2;
            case 2:
                tab3 = new NextGenKendraPhotoFragment(franchiseeDetails);
                return tab3;
            case 3:
                tab4 = new NextGenSiteVisitFragment(franchiseeDetails);
                return tab4;

            default:
                return null;
        }
    }

    public void refreshAllFragments(boolean IsReviewed) {
//        tab1.IsReviewed(IsReviewed);
//        tab2.IsReviewed(IsReviewed);
//        tab3.IsReviewed(IsReviewed);
//        tab4.IsReviewed(IsReviewed);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}
