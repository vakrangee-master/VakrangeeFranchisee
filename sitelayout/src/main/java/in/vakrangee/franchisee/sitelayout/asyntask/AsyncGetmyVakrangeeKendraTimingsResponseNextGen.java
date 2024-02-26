package in.vakrangee.franchisee.sitelayout.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetailsPojo;
import in.vakrangee.supercore.franchisee.webservice.WebService;

import static in.vakrangee.supercore.franchisee.utils.Main.franchiseeDetailsPojo;

public class AsyncGetmyVakrangeeKendraTimingsResponseNextGen extends AsyncTask<String, Void, FranchiseeDetailsPojo> {
    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    ProgressDialog progress;
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
    private static final String NINE_AM_CONST = "09:00 AM";
    private static final String NINE_PM_CONST = "09:00 PM";
    private static final String UNCHECK_CONST = "uncheck";
    private static final String CHECK_CONST = "check";

    public AsyncGetmyVakrangeeKendraTimingsResponseNextGen(Context context) {
        super();
        this.mContext = context;
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

    @Override
    protected FranchiseeDetailsPojo doInBackground(String... values) {
        // If you want to use 'values' string in here
        Log.i("TAG", "doInBackground");
        try {
            String vkid = values[0];
            String TokenID = values[1];

            String imei = values[2];
            String deviceid = values[3];
            String simserialnumber = values[4];

            diplayServerResopnse = WebService.myVakrangeeKendraTimingsResponse(vkid, TokenID, imei, deviceid, simserialnumber);
            String diplayServerResopnse1 = WebService.myVakrangeeKendraGeoCordinates(vkid, TokenID, imei, deviceid, simserialnumber);
            String displayRes1 = diplayServerResopnse1;
            String strGeo = displayRes1.replace("OKAY|", "");
            JSONObject jObject1 = new JSONObject(strGeo);
            latitude = jObject1.getString("myModelObject1");
            longtitude = jObject1.getString("myModelObject2");


            String strJson = diplayServerResopnse;
            strJson = strJson.replace("OKAY|", "");

            strJson = strJson.replace("\r\n", "");


            JSONObject jObject = new JSONObject(strJson);
            String mondayTimings = jObject.getString("mondayTimings");

            if (mondayTimings.equals("null")) {
                strOpenmMon = NINE_AM_CONST;
                strCloseMon = NINE_PM_CONST;
                mondayTimingscheck = UNCHECK_CONST;

            } else {
                StringTokenizer mon = new StringTokenizer(mondayTimings, "|");
                strOpenmMon = mon.nextToken();
                strCloseMon = mon.nextToken();
                mondayTimingscheck = CHECK_CONST;
            }
            String tuesdayTimings = jObject.getString("tuesdayTimings");
            if (tuesdayTimings.equals("null")) {

                strOpenTue = NINE_AM_CONST;
                strCloseTue = NINE_PM_CONST;
                tuesdayTimingscheck = UNCHECK_CONST;
            } else {
                StringTokenizer tue = new StringTokenizer(tuesdayTimings, "|");
                strOpenTue = tue.nextToken();
                strCloseTue = tue.nextToken();
                tuesdayTimingscheck = CHECK_CONST;
            }
            String wednesdayTimings = jObject.getString("wednesdayTimings");
            if (wednesdayTimings.equals("null")) {

                strOpenWed = NINE_AM_CONST;
                strCloseWed = NINE_PM_CONST;
                wednesdayTimingscheck = UNCHECK_CONST;
            } else {
                StringTokenizer wed = new StringTokenizer(wednesdayTimings, "|");
                strOpenWed = wed.nextToken();
                strCloseWed = wed.nextToken();
                wednesdayTimingscheck = CHECK_CONST;
            }
            String thursdayTimings = jObject.getString("thursdayTimings");
            if (thursdayTimings.equals("null")) {
                strOpenThu = NINE_AM_CONST;
                strCloseThu = NINE_PM_CONST;
                thursdayTimingscheck = UNCHECK_CONST;
            } else {
                StringTokenizer thu = new StringTokenizer(thursdayTimings, "|");
                strOpenThu = thu.nextToken();
                strCloseThu = thu.nextToken();
                thursdayTimingscheck = CHECK_CONST;
            }
            String fridayTimings = jObject.getString("fridayTimings");
            if (fridayTimings.equals("null")) {
                strOpenFri = NINE_AM_CONST;
                strCloseFri = NINE_PM_CONST;
                fridayTimingscheck = UNCHECK_CONST;
            } else {
                StringTokenizer fri = new StringTokenizer(fridayTimings, "|");
                strOpenFri = fri.nextToken();
                strCloseFri = fri.nextToken();
                fridayTimingscheck = CHECK_CONST;
            }
            String saturdayTimings = jObject.getString("saturdayTimings");
            if (saturdayTimings.equals("null")) {
                strOpenSat = NINE_AM_CONST;
                strCloseSat = NINE_PM_CONST;
                saturdayTimingscheck = UNCHECK_CONST;
            } else {
                StringTokenizer Sat = new StringTokenizer(saturdayTimings, "|");
                strOpenSat = Sat.nextToken();
                strCloseSat = Sat.nextToken();
                saturdayTimingscheck = CHECK_CONST;
            }
            String sundayTimings = jObject.getString("sundayTimings");
            if (sundayTimings.equals("null")) {
                strOpenSun = NINE_AM_CONST;
                strCloseSun = NINE_PM_CONST;
                sundayTimingscheck = UNCHECK_CONST;
            } else {
                StringTokenizer str = new StringTokenizer(sundayTimings, "|");
                strOpenSun = str.nextToken();
                strCloseSun = str.nextToken();
                sundayTimingscheck = CHECK_CONST;
            }

            setImageData(jObject);

            return new FranchiseeDetailsPojo();

        } catch (Exception e) {
            Log.e("TAG", "Error:in LoginPage " + e.getMessage());
            Log.e(" AsyncLogin  catch", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void setImageData(JSONObject jObject) throws JSONException {
        String frontageImage = jObject.getString("frontageImage");
        if (frontageImage.equals("null")) {
            //Do Nothing
        }
        String leftWallImage = jObject.getString("leftWallImage");
        if (leftWallImage.equals("null")) {
            //Do Nothing
        }
        String frontWallImage = jObject.getString("frontWallImage");
        if (frontWallImage.equals("null")) {
            //Do Nothing
        }
        String rightWallImage = jObject.getString("rightWallImage");
        if (rightWallImage.equals("null")) {
            //Do Nothing
        }
        String backWallImage = jObject.getString("backWallImage");
        if (backWallImage.equals("null")) {
            //Do Nothing
        }
        String ceilingImage = jObject.getString("ceilingImage");
        if (ceilingImage.equals("null")) {
            //Do Nothing
        }
        String floorImage = jObject.getString("floorImage");
        if (floorImage.equals("null")) {
            //Do Nothing
        }
        String extraImage1 = jObject.getString("extraImage1");
        if (extraImage1.equals("null")) {
            //Do Nothing
        }
        String extraImage2 = jObject.getString("extraImage2");
        if (extraImage2.equals("null")) {
            //Do Nothing
        }
        String extraImage3 = jObject.getString("extraImage3");
        if (extraImage3.equals("null")) {
            //Do Nothing
        }
        frontageThumb = jObject.getString("frontageThumb");
        if (frontageThumb.equals("null")) {
            //Do Nothing
        }
        leftWallThumb = jObject.getString("leftWallThumb");
        if (leftWallThumb.equals("null")) {
            //Do Nothing
        }
        frontWallThumb = jObject.getString("frontWallThumb");
        if (frontWallThumb.equals("null")) {
            //Do Nothing
        }
        rightWallThumb = jObject.getString("rightWallThumb");
        if (rightWallThumb.equals("null")) {
            //Do Nothing
        }
        backWallThumb = jObject.getString("backWallThumb");
        if (backWallThumb.equals("null")) {
            //Do Nothing
        }
        ceilingThumb = jObject.getString("ceilingThumb");
        if (ceilingThumb.equals("null")) {
            //Do Nothing
        }
        floorThumb = jObject.getString("floorThumb");
        if (floorThumb.equals("null")) {
            //Do Nothing
        }
        extraThumb1 = jObject.getString("extraThumb1");
        if (extraThumb1.equals("null")) {
            //Do Nothing
        }
        extraThumb2 = jObject.getString("extraThumb2");
        if (extraThumb2.equals("null")) {
            //Do Nothing
        }
        extraThumb3 = jObject.getString("extraThumb3");
        if (extraThumb3.equals("null")) {
            //Do Nothing
        }
    }

    protected void onPostExecute(FranchiseeDetailsPojo myVKMaster) {
        progress.dismiss();
        Intent intent = new Intent(mContext, NextGenPhotoViewPager.class);
        franchiseeDetailsPojo = myVKMaster;
        intent.putExtra("FranchiseeDetailsPojo", (Serializable) myVKMaster);
        mContext.startActivity(intent);

    }
}



