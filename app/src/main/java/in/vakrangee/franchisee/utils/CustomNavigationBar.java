package in.vakrangee.franchisee.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.franchisee.activity.MyhelpLineActivity;
import in.vakrangee.franchisee.atmloading.ATMActivity;
import in.vakrangee.franchisee.atmtechlivechecklist.ATMTechLiveCheckListActivity;
import in.vakrangee.franchisee.delivery_address.DeliveryAddressActivity;
import in.vakrangee.franchisee.documentmanager.DocumentManagerActivity;
import in.vakrangee.franchisee.gstdetails.GSTDetailsActivity;
import in.vakrangee.franchisee.loandocument.LoanDocumentActivity;
import in.vakrangee.franchisee.networktesting.simstrength.NetworkTestingActivity;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.AsyncGetFranchiseeApplicationByEnquiryId;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.CustomNextGenApplicationFirstScreenDialog;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.newexistingfranchisee.AsyncNewExistingFranchiseeChecks;
import in.vakrangee.franchisee.payment_details.PaymentDetailsActivity;
import in.vakrangee.franchisee.phasechecks.AsyncGetFranchiseePhaseInfo;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncIsDrawerItemAllowed;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncNextGenFranchiseeDetails;
import in.vakrangee.franchisee.tracking_hardware.TrackingHardwareActivity;
import in.vakrangee.franchisee.workinprogress.wipchatview.AsyncGetWIPChatViewStatus;
import in.vakrangee.simcarddetail.simcarddetails.SimcardDetailsActivity;
import in.vakrangee.supercore.franchisee.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.phasechecks.PhaseInfoDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.team.franchiseeexit.exit_evidence.FrExitEvidenceCollectionActivity;

public class CustomNavigationBar {

    private static final String TAG = CustomNavigationBar.class.getSimpleName();

    private Context context;
    private Toolbar toolbar;
    private List<IDrawerItem> iDrawerItems;
    private Drawer drawer;
    private Logger logger;
    private AsyncIsDrawerItemAllowed asyncIsDrawerItemAllowed;
    private AsyncGetFranchiseePhaseInfo asyncGetFranchiseePhaseInfo;
    private AsyncNewExistingFranchiseeChecks asyncNewExistingFranchiseeChecks;
    private Connection connection;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alert;
    private boolean IsVKIDAvailable = false;
    private String vkIdOrEnquiryId;

    public CustomNavigationBar(@NonNull Context context, @NonNull Toolbar toolbar) {
        this.context = context;
        this.toolbar = toolbar;
        connection = new Connection(context);
        IsVKIDAvailable = !TextUtils.isEmpty(connection.getVkid()) ? true : false;
        // Initialize Navigation Item
        initNavigation();
    }

    private void initNavigation() {
        String tmpVkId = connection.getVkid();
        String enquiryId = CommonUtils.getEnquiryId(context);
        vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

        //init i Drawer items
        iDrawerItems = new LinkedList<>();

        //if product code VF - then visible menu
        if (Constants.PRODUCT_CODE.equals("VF")) {
            iDrawerItems.add(new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1));
            iDrawerItems.add(new SecondaryDrawerItem().withName("Nextgen Franchisee Application").withIdentifier(209));
            iDrawerItems.add(new SecondaryDrawerItem().withName("NextGen Site Detail").withIdentifier(5));
            //iDrawerItems.add(new SecondaryDrawerItem().withName("Work Commencement").withIdentifier(201));     // 201 - Code
            iDrawerItems.add(new SecondaryDrawerItem().withName("Work In Progress").withIdentifier(202));     // 202 - Code
            iDrawerItems.add(new SecondaryDrawerItem().withName("Kendra Work Completion Intimation").withIdentifier(204));
            // Commented - Now GSTIN is getting updated from Document Manager.
            //iDrawerItems.add(new SecondaryDrawerItem().withName("Update GST Details").withIdentifier(206));
            iDrawerItems.add(new SecondaryDrawerItem().withName("Document Manager").withIdentifier(211));
            iDrawerItems.add(new SecondaryDrawerItem().withName(context.getResources().getString(R.string.tracking_hardware)).withIdentifier(220));
            iDrawerItems.add(new SecondaryDrawerItem().withName(context.getResources().getString(R.string.atm_loading)).withIdentifier(224));
            iDrawerItems.add(new SecondaryDrawerItem().withName(context.getResources().getString(R.string.delivery_address)).withIdentifier(225));
            iDrawerItems.add(new SecondaryDrawerItem().withName(context.getResources().getString(R.string.simcardDetails)).withIdentifier(208));
            iDrawerItems.add(new SecondaryDrawerItem().withName(context.getResources().getString(R.string.payment_details)).withIdentifier(300));
            iDrawerItems.add(new SecondaryDrawerItem().withName(context.getResources().getString(R.string.atm_checklist)).withIdentifier(302));

            if (!TextUtils.isEmpty(tmpVkId)) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(context.getResources().getString(R.string.title_network_testing)).withIdentifier(227));
            }

            iDrawerItems.add(new SecondaryDrawerItem().withName("My HelpLine").withIdentifier(207));
            iDrawerItems.add(new SecondaryDrawerItem().withName("Privacy Policy").withIdentifier(1003));     // 202 - Code

        }

        //drawer open -screen density width
        int pixels = CommonUtils.getScreenDensityWidth(context);
        int width = (int) (pixels * 0.65);
        //init Drawer
        drawer = new DrawerBuilder()
                .withActivity((Activity) context).withSliderBackgroundColorRes(R.color.navigationcolor)
                .withToolbar(toolbar).withDrawerWidthDp(width)
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        ((Activity) context).finish();
                        //return true if we have consumed the event
                        return true;
                    }
                })
                .addStickyDrawerItems(
                        //bottom singout drawer set
                        new SecondaryDrawerItem().withName(R.string.SignOut).withIcon(FontAwesome.Icon.faw_sign_out)
                )
                .addDrawerItems(
                        // add drawer item list of data - using list
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    //darwer on item click position go to- activity
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :Drawer item
                        long idIdentifier = drawerItem.getIdentifier();
                        switchToIntent((int) idIdentifier);
                        return false;
                    }
                })
                .build();

        // add of drawer item in navigation drawer list
        for (IDrawerItem iDrawerItem : iDrawerItems) {
            drawer.addItem(iDrawerItem);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Handle Phase wise Menus
                handlePhaseWiseMenus();

                //if VKID show Kendra Acknowledge
                checkVKIDFound();

                //menu drawer allowed
                isDrawerItemAllowed();
            }
        }, 800); //1250


    }

    private void isDrawerItemAllowed() {
        asyncIsDrawerItemAllowed = new AsyncIsDrawerItemAllowed(context, new AsyncIsDrawerItemAllowed.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }
                    //Response
                    if (result.startsWith("ERROR|")) {
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(context, result);
                        }
                    } else if (result.startsWith("OKAY|")) {
                        result = result.replace("OKAY|", "");

                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("name");
                            String display = jsonobject.getString("display");
                            String menu_id = jsonobject.getString("menu_id");
                            if (!TextUtils.isEmpty(name)) {
                                drawer.updateName(Long.parseLong(menu_id), new com.mikepenz.materialdrawer.holder.StringHolder(name));
                            }
                            if (!TextUtils.isEmpty(display) && display.equalsIgnoreCase("N")) {
                                drawer.removeItem(Long.parseLong(menu_id));
                            }
                        }
                        /*JSONObject jsonObject = new JSONObject(result);
                        Iterator iterator = jsonObject.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            JSONObject insideObject = jsonObject.getJSONObject(key);
                            String name = insideObject.optString("name");
                            String display = insideObject.optString("display");
                            if (!TextUtils.isEmpty(name)) {
                                drawer.updateName(Long.parseLong(key), new com.mikepenz.materialdrawer.holder.StringHolder(name));
                            }
                            if (!TextUtils.isEmpty(display) && display.equalsIgnoreCase("N")) {
                                drawer.removeItem(Long.parseLong(key));
                            }
                        }*/
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncIsDrawerItemAllowed.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

    }

    private void handlePhaseWiseMenus() {
        asyncGetFranchiseePhaseInfo = new AsyncGetFranchiseePhaseInfo(context, new AsyncGetFranchiseePhaseInfo.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        executePhase0();
                        return;
                    }

                    Gson gson = new Gson();
                    PhaseInfoDto phaseInfoDto = gson.fromJson(result, PhaseInfoDto.class);
                    if (phaseInfoDto == null) {
                        executePhase0();
                        return;
                    }
                    // txtPhaseName.setText(phaseInfoDto.getNextGenPhaseName());

                    //Handle Menus
                    CommonUtils.setFranchiseePhaseInfoIntoPreferences(context, phaseInfoDto);
                    if (TextUtils.isEmpty(phaseInfoDto.getNextGenPhaseCode()) || phaseInfoDto.getNextGenPhaseCode().equalsIgnoreCase(Constants.PHASE_0)) {
                        //FOR Phase-0: Remove "Work Commencement" and "Work In Progress", Rename "Site Readiness Updation"
                        executePhase0();
                    }

                    if (phaseInfoDto.getNextGenPhaseCode().equalsIgnoreCase(Constants.PHASE_1)) {
                        //FOR Phase-1: Add "Work Commencement" and "Work In Progress", Rename "Work Completion Intimation"

                        drawer.updateName(204, new com.mikepenz.materialdrawer.holder.StringHolder("Work Completion Intimation"));
                      /*  int readinessPos = drawer.getPosition(204);
                        List<IDrawerItem> iDrawerItems = drawer.getDrawerItems();
                        iDrawerItems.set(readinessPos, new SecondaryDrawerItem().withName("Work Completion Intimation").withIdentifier(204));*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        asyncGetFranchiseePhaseInfo.execute("");
    }

    private void executePhase0() {
        //FOR Phase-0: Remove "Work Commencement" and "Work In Progress", Rename "Site Readiness Updation"
        drawer.removeItem(201);
        drawer.removeItem(202);
        drawer.updateName(204, new com.mikepenz.materialdrawer.holder.StringHolder("Site Readiness Updation"));
    }

    //region if VKID found -show Kendra Acknowledge barcode data
    private void checkVKIDFound() {
        if (!TextUtils.isEmpty(connection.getVkid())) {
            drawer.updateName(221, new com.mikepenz.materialdrawer.holder.StringHolder(context.getResources().getString(R.string.next_gen_kendra_acknowledgement)));
        } else {
            drawer.removeItem(221);
        }
    }
    //endregion

    private void switchToIntent(int i) {
        Intent intent;
        switch (i) {
            case 1:
                intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 209:
                getNextGenFranhiseeApp(context);
                break;
            case 5:
                getNextGenSiteDeatils(context);
                break;
            case 201:
                Globals sharedData = Globals.getInstance();
                sharedData.setValue(0);
                new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT, context, connection.getVkid()).execute(connection.getVkid());
                break;
            case 202:
                new AsyncGetWIPChatViewStatus(context).execute("");
                break;
            case 204:
                switcingTOLocationDetails(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
                break;
            case 2012:
                switcingTOLocationDetails(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED);
                break;
            case 2011:
                switcingTOLocationDetails(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED);
                break;
            case 2010:
                switcingTOLocationDetails(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION);
                break;
            case 211:
                intent = new Intent(context, DocumentManagerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 206:
                intent = new Intent(context, GSTDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 220:
                intent = new Intent(context, TrackingHardwareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 224:
                intent = new Intent(context, ATMActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 225:
                intent = new Intent(context, DeliveryAddressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 207:
                intent = new Intent(context, MyhelpLineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 208:
                intent = new Intent(context, SimcardDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 227:
                intent = new Intent(context, NetworkTestingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 300:
                intent = new Intent(context, PaymentDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case 302:
                intent = new Intent(context, ATMTechLiveCheckListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;

            case 303:
                intent = new Intent(context, LoanDocumentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;

            case 305:
                String FrEnquiryId = CommonUtils.getEnquiryId(context);
                getSpecificFrEvidenceList(null, FrEnquiryId);
                break;
        }
    }

    private void getSpecificFrEvidenceList(String userVKId, String FrEnquiryId) {
        Intent intent = new Intent(context, FrExitEvidenceCollectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("USER_VKID", userVKId);
        intent.putExtra("FR_ENQUIRY_ID", FrEnquiryId);
        context.startActivity(intent);
    }

    private void switcingTOLocationDetails(String nextgenSiteReadinessAndVerification) {
        Globals sharedData = Globals.getInstance();
        sharedData.setValue(0);
        new AsyncNextGenFranchiseeDetails(nextgenSiteReadinessAndVerification, context, connection.getVkid()).execute(connection.getVkid());
    }

    public void getNextGenFranhiseeApp(Context context) {

        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(context);
        if (loginChecksDto != null && !TextUtils.isEmpty(loginChecksDto.getNextGenEnquiryId()) && !loginChecksDto.getNextGenEnquiryId().equalsIgnoreCase("0")) {
            //Commented for next release--- Vassundhara 21st Feb 2019
            boolean IsNewExistingCheckDone = false;
            if (IsNewExistingCheckDone) {
                new AsyncGetFranchiseeApplicationByEnquiryId(context).execute(loginChecksDto.getNextGenEnquiryId());
            } else {
                //Display Dialog
                displayNewOrExistingFranchiseeDialog(context, loginChecksDto.getNextGenEnquiryId());
            }
        } else {
            showMessage("Something went wrong. Please logOut and try it again.");
        }
    }

    private void displayNewOrExistingFranchiseeDialog(final Context context, final String enquiryId) {
        asyncNewExistingFranchiseeChecks = new AsyncNewExistingFranchiseeChecks(context, Constants.FROM_ISREQUIRE_CHECK, null, new AsyncNewExistingFranchiseeChecks.Callback() {
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

                    if (result.equalsIgnoreCase("OKAY|Y")) {
                        CustomNextGenApplicationFirstScreenDialog dialog = new CustomNextGenApplicationFirstScreenDialog(context);
                        dialog.setCancelable(false);
                        dialog.show();

                    } else if (result.equalsIgnoreCase("OKAY|N")) {
                        new AsyncGetFranchiseeApplicationByEnquiryId(context).execute(enquiryId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncNewExistingFranchiseeChecks.execute("");
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    public void getNextGenSiteDeatils(Context context) {
        Connection connection = new Connection(context);
        String vkid = connection.getVkid();
        IsVKIDAvailable = !TextUtils.isEmpty(vkid) ? true : false;
        if (!InternetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.internetCheck));

        } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
            // Intent intent = new Intent(DashboardActivity.this, NextGenPhotoViewPager.class);
            Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXTGEN_SITE_VISIT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            //finish();

        } else {
            Globals sharedData = Globals.getInstance();
            sharedData.setValue(0);
            if (!IsVKIDAvailable) {
                new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_VISIT, context, "").execute("");
            } else {
                new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_VISIT, context, vkid).execute(vkid);
            }
            // new AsyncGetmyVakrangeeKendraTimingsResponseNextGen(DashboardActivity.this).execute(vkidd, TokenID, imei, deviceid, simserialnumber);
        }

    }

    public void openDrawer() {
        drawer.openDrawer();
    }
}
