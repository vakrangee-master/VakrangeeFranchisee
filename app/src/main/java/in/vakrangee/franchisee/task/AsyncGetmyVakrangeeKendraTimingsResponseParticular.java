package in.vakrangee.franchisee.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.MyVakrangeeKendraPhotoViewPager;
import in.vakrangee.supercore.franchisee.model.MyVKMaster;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.webservice.WebService;

import static in.vakrangee.supercore.franchisee.utils.Main.myVKMasterMain;

/**
 * Created by Nileshd on 2/3/2017.
 */
public class AsyncGetmyVakrangeeKendraTimingsResponseParticular extends AsyncTask<String, Void, MyVKMaster> {
    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    ProgressDialog progress;
    private MyVKMaster myVKMaster;
    String strOpenmMon;
    String strCloseMon;
    String strOpenTue;
    String strCloseTue;
    String strOpenWed;
    String strCloseWed;
    String strOpenThu;
    String strCloseThu;
    String strOpenFri;
    String strCloseFri;
    String strOpenSat;
    String strCloseSat;
    String strOpenSun;
    String strCloseSun;
    String latitude;
    String longtitude;
    String frontageThumb;
    String leftWallThumb;
    String frontWallThumb;
    String rightWallThumb;
    String backWallThumb;
    String ceilingThumb;
    String floorThumb;
    String extraThumb1;
    String extraThumb2;
    String extraThumb3;

    String mondayTimingscheck;
    String tuesdayTimingscheck;
    String wednesdayTimingscheck;
    String thursdayTimingscheck;
    String fridayTimingscheck;
    String saturdayTimingscheck;
    String sundayTimingscheck;

    String userIdP;


    public AsyncGetmyVakrangeeKendraTimingsResponseParticular(Context context, String id) {
        super();
        this.mContext = context;
        this.userIdP = id;

    }


    @Override
    protected void onPreExecute() {


        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        //progress.setTitle(R.string.updateTiming);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected MyVKMaster doInBackground(String... values) {
        // If you want to use 'values' string in here
        Log.i("TAG", "doInBackground");
        try {
            String userId = values[0];
            String vkid = values[1];
            String TokenID = values[2];

            String imei = values[3];
            String deviceid = values[4];
            String simserialnumber = values[5];


            diplayServerResopnse = WebService.myVakrangeeKendraTimingsParticular(userId, vkid, TokenID, imei, deviceid, simserialnumber);
            String diplayServerResopnse1 = WebService.myVakrangeeKendraGeoCordinatesParticular(userId, vkid, TokenID, imei, deviceid, simserialnumber);

            StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
            String first = tokens.nextToken();
            String second = tokens.nextToken();

            StringTokenizer tokens1 = new StringTokenizer(diplayServerResopnse1, "|");
            String first1 = tokens1.nextToken();
            String second1 = tokens1.nextToken();

            if (first1.equals("OKAY")) {

                String displayRes1 = diplayServerResopnse1;
                String strGeo = displayRes1.replace("OKAY|", "");
                JSONObject jObject1 = new JSONObject(strGeo);
                latitude = jObject1.getString("myModelObject1");
                longtitude = jObject1.getString("myModelObject2");


            } else if (second1.equals("No data found.")) {

                latitude = "19.112857";
                longtitude = "72.883871";

            } else {
                latitude = "19.112857";
                longtitude = "72.883871";
            }

            if (second.equals("No data found.")) {


            } else {

               /* String displayRes1 = diplayServerResopnse1;
                String strGeo = displayRes1.replace("OKAY|", "");
                JSONObject jObject1 = new JSONObject(strGeo);
                latitude = jObject1.getString("myModelObject1");
                longtitude = jObject1.getString("myModelObject2");
*/

                String strJson = diplayServerResopnse;
                strJson = strJson.replace("OKAY|", "");

                strJson = strJson.replace("\r\n", "");


                JSONObject jObject = new JSONObject(strJson);
                int myVkId = (Integer) jObject.get("myVkId");

                String mondayTimings = jObject.getString("mondayTimings");

                if (mondayTimings.equals("null")) {
                    strOpenmMon = "09:00 AM";
                    strCloseMon = "09:00 PM";
                    mondayTimingscheck = "uncheck";

                } else {
                    StringTokenizer mon = new StringTokenizer(mondayTimings, "|");
                    strOpenmMon = mon.nextToken();
                    strCloseMon = mon.nextToken();
                    mondayTimingscheck = "check";
                }
                String tuesdayTimings = jObject.getString("tuesdayTimings");
                if (tuesdayTimings.equals("null")) {

                    strOpenTue = "09:00 AM";
                    strCloseTue = "09:00 PM";
                    tuesdayTimingscheck = "uncheck";
                } else {
                    StringTokenizer tue = new StringTokenizer(tuesdayTimings, "|");
                    strOpenTue = tue.nextToken();
                    strCloseTue = tue.nextToken();
                    tuesdayTimingscheck = "check";
                }
                String wednesdayTimings = jObject.getString("wednesdayTimings");
                if (wednesdayTimings.equals("null")) {

                    strOpenWed = "09:00 AM";
                    strCloseWed = "09:00 PM";
                    wednesdayTimingscheck = "uncheck";
                } else {
                    StringTokenizer wed = new StringTokenizer(wednesdayTimings, "|");
                    strOpenWed = wed.nextToken();
                    strCloseWed = wed.nextToken();
                    wednesdayTimingscheck = "check";
                }
                String thursdayTimings = jObject.getString("thursdayTimings");
                if (thursdayTimings.equals("null")) {
                    strOpenThu = "09:00 AM";
                    strCloseThu = "09:00 PM";
                    thursdayTimingscheck = "uncheck";
                } else {
                    StringTokenizer thu = new StringTokenizer(thursdayTimings, "|");
                    strOpenThu = thu.nextToken();
                    strCloseThu = thu.nextToken();
                    thursdayTimingscheck = "check";
                }
                String fridayTimings = jObject.getString("fridayTimings");
                if (fridayTimings.equals("null")) {
                    strOpenFri = "09:00 AM";
                    strCloseFri = "09:00 PM";
                    fridayTimingscheck = "uncheck";
                } else {
                    StringTokenizer fri = new StringTokenizer(fridayTimings, "|");
                    strOpenFri = fri.nextToken();
                    strCloseFri = fri.nextToken();
                    fridayTimingscheck = "check";
                }
                String saturdayTimings = jObject.getString("saturdayTimings");
                if (saturdayTimings.equals("null")) {
                    strOpenSat = "09:00 AM";
                    strCloseSat = "09:00 PM";
                    saturdayTimingscheck = "uncheck";
                } else {
                    StringTokenizer Sat = new StringTokenizer(saturdayTimings, "|");
                    strOpenSat = Sat.nextToken();
                    strCloseSat = Sat.nextToken();
                    saturdayTimingscheck = "check";
                }
                String sundayTimings = jObject.getString("sundayTimings");
                if (sundayTimings.equals("null")) {
                    strOpenSun = "09:00 AM";
                    strCloseSun = "09:00 PM";
                    sundayTimingscheck = "uncheck";
                } else {
                    StringTokenizer sun = new StringTokenizer(sundayTimings, "|");
                    strOpenSun = sun.nextToken();
                    strCloseSun = sun.nextToken();
                    sundayTimingscheck = "check";
                }
                String frontageImage = jObject.getString("frontageImage");
                if (frontageImage.equals("null")) {
                }
                String leftWallImage = jObject.getString("leftWallImage");
                if (leftWallImage.equals("null")) {
                }
                String frontWallImage = jObject.getString("frontWallImage");
                if (frontWallImage.equals("null")) {
                }
                String rightWallImage = jObject.getString("rightWallImage");
                if (rightWallImage.equals("null")) {
                }
                String backWallImage = jObject.getString("backWallImage");
                if (backWallImage.equals("null")) {
                }
                String ceilingImage = jObject.getString("ceilingImage");
                if (ceilingImage.equals("null")) {
                }
                String floorImage = jObject.getString("floorImage");
                if (floorImage.equals("null")) {
                }
                String extraImage1 = jObject.getString("extraImage1");
                if (extraImage1.equals("null")) {
                }
                String extraImage2 = jObject.getString("extraImage2");
                if (extraImage2.equals("null")) {
                }
                String extraImage3 = jObject.getString("extraImage3");
                if (extraImage3.equals("null")) {
                }
                frontageThumb = jObject.getString("frontageThumb");
                if (frontageThumb.equals("null")) {
                }
                leftWallThumb = jObject.getString("leftWallThumb");
                if (leftWallThumb.equals("null")) {
                }
                frontWallThumb = jObject.getString("frontWallThumb");
                if (frontWallThumb.equals("null")) {
                }
                rightWallThumb = jObject.getString("rightWallThumb");
                if (rightWallThumb.equals("null")) {
                }
                backWallThumb = jObject.getString("backWallThumb");
                if (backWallThumb.equals("null")) {
                }
                ceilingThumb = jObject.getString("ceilingThumb");
                if (ceilingThumb.equals("null")) {
                }
                floorThumb = jObject.getString("floorThumb");
                if (floorThumb.equals("null")) {
                }
                extraThumb1 = jObject.getString("extraThumb1");
                if (extraThumb1.equals("null")) {
                }
                extraThumb2 = jObject.getString("extraThumb2");
                if (extraThumb2.equals("null")) {
                }
                extraThumb3 = jObject.getString("extraThumb3");
                if (extraThumb3.equals("null")) {
                }


                return new MyVKMaster(strOpenmMon, strCloseMon, strOpenTue, strCloseTue, strOpenWed, strCloseWed, strOpenThu,
                        strCloseThu, strOpenFri, strCloseFri, strOpenSat, strCloseSat, strOpenSun, strCloseSun, latitude, longtitude,
                        frontageThumb, leftWallThumb, frontWallThumb, rightWallThumb, backWallThumb, ceilingThumb, floorThumb, extraThumb1,
                        extraThumb2, extraThumb3, mondayTimingscheck, tuesdayTimingscheck, wednesdayTimingscheck, thursdayTimingscheck,
                        fridayTimingscheck, saturdayTimingscheck, sundayTimingscheck, userIdP);
            }
            return new MyVKMaster("09:00 AM", "09:00 PM", "09:00 AM", "09:00 PM", "09:00 AM", "09:00 PM", "09:00 AM",
                    "09:00 PM", "09:00 AM", "09:00 PM", "09:00 AM", "09:00 PM", "09:00 AM", "09:00 PM", latitude, longtitude,
                    "", "", "", "", "", "", "", "",
                    "", "", "check", "check", "check", "check",
                    "check", "check", "check", userIdP);

        } catch (Exception e) {
            Log.e("TAG", "Error:in LoginPage " + e.getMessage());


            Log.e(" AsyncLogin  catch", e.getMessage());
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));

        }


        return null;
    }

    @Override
    protected void onPostExecute(MyVKMaster myVKMaster) {
        progress.dismiss();


        Intent intent = new Intent(mContext, MyVakrangeeKendraPhotoViewPager.class);
        myVKMasterMain = myVKMaster;
        intent.putExtra("myVKMaster", (Serializable) myVKMaster);
        mContext.startActivity(intent);

        // new MyVakrangeeKendraTimingFragment().updatedata(result);


    }


}
