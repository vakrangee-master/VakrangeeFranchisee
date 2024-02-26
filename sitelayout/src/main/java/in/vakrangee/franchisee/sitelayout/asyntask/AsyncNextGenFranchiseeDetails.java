package in.vakrangee.franchisee.sitelayout.asyntask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.ResultIPC;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.MandatoryBrandingActivity;
import in.vakrangee.franchisee.sitelayout.sitecommencement.NextGenSiteCommencementActivity;
import in.vakrangee.franchisee.sitelayout.sitecompletion.WorkCompletionIntimationActivity;
import in.vakrangee.franchisee.sitelayout.sitereadiness.SiteReadinessActivity;
import in.vakrangee.franchisee.workinprogress.WorkInProgressDetailActivity;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncNextGenFranchiseeDetails extends AsyncTask<String, Void, FranchiseeDetails> {

    Context mContext;
    String diplayServerResopnse;
    ProgressDialog progress;
    private FranchiseeDetails franchiseeDetails;
    String userIdP;
    String modetype;

    public AsyncNextGenFranchiseeDetails(String modetype, Context context, String id) {
        super();
        this.mContext = context;
        this.userIdP = id;
        this.modetype = modetype;
    }

    public AsyncNextGenFranchiseeDetails(Context context, String id) {
        super();
        this.mContext = context;
        this.userIdP = id;
    }

    @Override
    protected void onPreExecute() {


        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected FranchiseeDetails doInBackground(String... values) {
        // If you want to use 'values' string in here
        Log.i("TAG", "doInBackground---------------------" + modetype);
        try {
            String vkId = null, nextgenFranchiseeApplicationNo = null, TokenID = null, imei = null, deviceid = null, simserialnumber = null;
            if ((!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_VISIT_ADHOC)) || Constants.ENABLE_FRANCHISEE_MODE) {
                vkId = values[0];
                if (values.length > 1) {
                    nextgenFranchiseeApplicationNo = values[1];
                } else {
                    nextgenFranchiseeApplicationNo = vkId;
                }
            } else {
                vkId = values[0];
                nextgenFranchiseeApplicationNo = values[1];
                TokenID = values[2];

                imei = values[3];
                deviceid = values[4];
                simserialnumber = values[5];
            }
            // Check Mode Type To get NextGen Site Detail
            if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
                checkCommencementData(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);

            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
                checkWorkInProgress(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);

            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
                checkWokrCompletionIntimation(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);

            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_VISIT_ADHOC)) {
                checkSiteVisitADHOC(vkId);

            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_VISIT)) {
                checkSiteVisit(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);

            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
                checkSiteReadinessVerification(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
                checkSiteVisit(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED)) {
                checkSiteVisit(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED)) {
               // checkMandatoryBrandingVerification(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);
                checkSiteVisit(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);
            } else {
                diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);
            }

            StringTokenizer tokens1 = new StringTokenizer(diplayServerResopnse, "|");
            tokens1.nextToken();
            String second1 = tokens1.nextToken();

            String ab = second1.substring(1, second1.length() - 1);
            String strJson = ab.replace("\r\n", "");
            franchiseeDetails = JSONUtils.toJson(FranchiseeDetails.class, strJson);

            //Compress Franchisee Profile Pic

            if (franchiseeDetails != null && !TextUtils.isEmpty(franchiseeDetails.getFranchiseePicFile())) {

                String picFile = franchiseeDetails.getFranchiseePicFile();
                Bitmap bitmap = CommonUtils.StringToBitMap(picFile);
                bitmap = ImageUtils.getResizedBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String profilePic = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                franchiseeDetails.setFranchiseePicFile(profilePic);

            }

            //Compress GST Pic
            if (franchiseeDetails != null && !TextUtils.isEmpty(franchiseeDetails.getGstImage())) {

                String picFile = franchiseeDetails.getGstImage();
                Bitmap bitmap = CommonUtils.StringToBitMap(picFile);
                bitmap = ImageUtils.getResizedBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String gstPic = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                franchiseeDetails.setGstImage(gstPic);

            }

            // Compress Layout Sketch
            if (franchiseeDetails != null && !TextUtils.isEmpty(franchiseeDetails.getSiteLayoutSketch())) {

                String picFile = franchiseeDetails.getSiteLayoutSketch();
                Bitmap bitmap = CommonUtils.StringToBitMap(picFile);
                bitmap = ImageUtils.getResizedBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String siteLayoutPic = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                franchiseeDetails.setSiteLayoutSketch(siteLayoutPic);

            }
            return franchiseeDetails;
        } catch (Exception e) {
            Log.e("TAG", "Error:in LoginPage " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void checkSiteReadinessVerification(String vkId, String nextgenFranchiseeApplicationNo, String TokenID, String imei, String deviceid, String simserialnumber) {
        if (Constants.ENABLE_FRANCHISEE_MODE) {
            if (Constants.ENABLE_FRANCHISEE_LOGIN) {

                if (!TextUtils.isEmpty(vkId)) {
                    diplayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, nextgenFranchiseeApplicationNo);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(mContext);
                    diplayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(enquiryId, nextgenFranchiseeApplicationNo);
                }
            } else {
                diplayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, nextgenFranchiseeApplicationNo);
            }
        } else {
            diplayServerResopnse = WebService.getSiteReadinessAndVerificationDetail(vkId, TokenID, imei, deviceid, simserialnumber, nextgenFranchiseeApplicationNo);
        }
    }

    private void checkMandatoryBrandingVerification(String vkId, String nextgenFranchiseeApplicationNo, String TokenID, String imei, String deviceid, String simserialnumber) {
        diplayServerResopnse = WebService.getMandatoryVerificationDetail(mContext);
        Log.d("rrr", "------: " + diplayServerResopnse);
    }

    private void checkSiteVisit(String vkId, String nextgenFranchiseeApplicationNo, String TokenID, String imei, String deviceid, String simserialnumber) {
        if (Constants.ENABLE_FRANCHISEE_MODE) {
            if (Constants.ENABLE_FRANCHISEE_LOGIN) {

                if (!TextUtils.isEmpty(vkId)) {
                    diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(mContext);
                    diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen2(enquiryId);
                }
            } else {
                diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId);
            }
        } else {
            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId, nextgenFranchiseeApplicationNo, TokenID, imei, deviceid, simserialnumber);
        }
    }

    private void checkSiteVisitADHOC(String vkId) {
        if (Constants.ENABLE_FRANCHISEE_LOGIN) {

            if (!TextUtils.isEmpty(vkId)) {
                diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId);
            } else {
                String enquiryId = CommonUtils.getEnquiryId(mContext);
                diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen2(enquiryId);
            }
        } else {
            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId);
        }
    }

    private void checkWokrCompletionIntimation(String vkId, String nextgenFranchiseeApplicationNo, String TokenID, String imei, String deviceid, String simserialnumber) {
        if (Constants.ENABLE_FRANCHISEE_MODE) {
            diplayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(vkId, nextgenFranchiseeApplicationNo);
        } else {
            diplayServerResopnse = WebService.getWorkCompletionIntimationDetail(vkId, TokenID, imei, deviceid, simserialnumber, nextgenFranchiseeApplicationNo);
        }
    }

    private void checkWorkInProgress(String vkId, String nextgenFranchiseeApplicationNo, String TokenID, String imei, String deviceid, String simserialnumber) {
        if (Constants.ENABLE_FRANCHISEE_MODE) {
            diplayServerResopnse = WebService.getAllNextGenSiteWorkInProgressDetail(vkId, nextgenFranchiseeApplicationNo);
        } else {
            diplayServerResopnse = WebService.getAllNextGenSiteWorkInProgressDetail(vkId, TokenID, imei, deviceid, simserialnumber, nextgenFranchiseeApplicationNo);
        }
    }

    private void checkCommencementData(String vkId, String nextgenFranchiseeApplicationNo, String TokenID, String imei, String deviceid, String simserialnumber) {
        if (Constants.ENABLE_FRANCHISEE_MODE) {
            if (Constants.ENABLE_FRANCHISEE_LOGIN) {

                if (!TextUtils.isEmpty(vkId)) {
                    diplayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(vkId, nextgenFranchiseeApplicationNo);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(mContext);
                    diplayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(enquiryId, nextgenFranchiseeApplicationNo);
                }
            } else {
                diplayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(vkId, nextgenFranchiseeApplicationNo);
            }
        } else {
            diplayServerResopnse = WebService.getNextGenSiteWorkCommencementDetail(vkId, TokenID, imei, deviceid, simserialnumber, nextgenFranchiseeApplicationNo);
        }
    }

    @Override
    protected void onPostExecute(FranchiseeDetails franchiseeDetails) {
        progress.dismiss();

        if (diplayServerResopnse.startsWith("ERROR|")) {
            // Handle Error Response From Server.

            StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
            tokens.nextToken();     // Jump to next Token
            String errMsg = tokens.nextToken();

            if (!TextUtils.isEmpty(errMsg)) {
                AlertDialogBoxInfo.alertDialogShow(mContext, errMsg);
            }
        } else if (franchiseeDetails == null) {
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
        } else {

            if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
                Intent intent = new Intent(mContext, NextGenSiteCommencementActivity.class);
                intent.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_WORK_COMMENCEMENT);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);

            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
                Intent intent = new Intent(mContext, WorkInProgressDetailActivity.class);
                intent.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
                Intent intent = new Intent(mContext, WorkCompletionIntimationActivity.class);
                intent.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                intent.putExtra("MODE", Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
                Intent intent = new Intent(mContext, MandatoryBrandingActivity.class);
                intent.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                //int sync = ResultIPC.get().setLargeData(franchiseeDetails);
                //intent.putExtra("BIG_DATA:SYNC_CODE", sync);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED)) {
                Intent intent = new Intent(mContext, MandatoryBrandingActivity.class);
                intent.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                //intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
               /* int sync = ResultIPC.get().setLargeData(franchiseeDetails);
                intent.putExtra("BIG_DATA:SYNC_CODE", sync);*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED)) {
                Intent intent = new Intent(mContext, MandatoryBrandingActivity.class);
                intent.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                // int sync = ResultIPC.get().setLargeData(franchiseeDetails);
                // intent.putExtra("BIG_DATA:SYNC_CODE", sync);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
                Intent intent = new Intent(mContext, SiteReadinessActivity.class);
                intent.putExtra("FROM", "");
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
                // int sync = ResultIPC.get().setLargeData(franchiseeDetails);
                // intent.putExtra("BIG_DATA:SYNC_CODE", sync);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_VISIT_ADHOC)) {
                Intent intent = new Intent(mContext, NextGenPhotoViewPager.class);
                intent.putExtra("FROM", "");
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_VISIT_ADHOC);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_VISIT)) {
                Intent intent = new Intent(mContext, NextGenPhotoViewPager.class);
                intent.putExtra("FROM", "");
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_VISIT);
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, NextGenPhotoViewPager.class);
                this.franchiseeDetails = franchiseeDetails;
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                mContext.startActivity(intent);
            }
        }
    }

}
