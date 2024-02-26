package in.vakrangee.franchisee.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.LeaveOD;
import in.vakrangee.supercore.franchisee.model.ODSpinner;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.webservice.WebService;
//

/**
 * Created by Nileshd on 12/8/2016.
 */
public class ODApplyAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    TelephonyManager telephonyManager;
    List<LeaveOD> data;
    private ArrayList<LeaveOD> arraylist;

    ProgressDialog progress;
    String Datef, Datet;

    String diplayServerResopnse;
    TextView edtODReasonpop;

    Long Spnitem;
    String leaveOdId;
    GPSTracker gps;
    private List<ODSpinner> ODSpinnerEntityList = new ArrayList<ODSpinner>();
    Place place;
    final int PLACE_PICKER_REQUEST = 1;

    // create constructor to innitilize context and data sent from MainActivity
    public ODApplyAdpater(Context context, List<LeaveOD> data) {
        this.context = context;
        this.data = data;
        this.arraylist = new ArrayList<LeaveOD>();
        this.arraylist.addAll(data);
    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewadpaterodapply, parent, false);
        MyHolder holder = new MyHolder(view);
        gps = new GPSTracker(context);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        final LeaveOD constants = data.get(position);

        try {

            String s = constants.getLeaveOdStatus();
            Datef = constants.getStartDate();
            Datet = constants.getEndDate();
            if (s.equals("Rejected")) {

                myHolder.odstatus.setImageDrawable(context.getResources().getDrawable(R.drawable.rejectod));
            } else if (s.equals("Withdrawn")) {
                myHolder.odstatus.setImageDrawable(context.getResources().getDrawable(R.drawable.rejectod));
            } else if (s.equals("Recommened")) {
                myHolder.odstatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pendingo));
            } else if (s.equals("Approved")) {
                // myHolder.Status.setText(constants.Status);
                // myHolder.Status.setTextColor(Color.GREEN);
                myHolder.odstatus.setImageDrawable(context.getResources().getDrawable(R.drawable.approved));

            } else if (s.equals("On Hold")) {
                // myHolder.Status.setText(constants.Status);
                // myHolder.Status.setTextColor(Color.GREEN);
                myHolder.odstatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pendingo));

            } else if (s.equals("Pending")) {
                // myHolder.Status.setText(constants.Status);
                //myHolder.Status.setTextColor(Color.BLACK);
                myHolder.odstatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pendingo));

            } else {
                // myHolder.Status.setText(constants.Status);
                // myHolder.Status.setTextColor(Color.BLACK);
                myHolder.odstatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pendingo));

            }
        } catch (Exception e) {
            e.getMessage();
        }


        myHolder.OutdoorID.setText(constants.getLeaveOdId());
        myHolder.OutdoorStartDate.setText("Start Date :" + constants.getStartDate());
        myHolder.OutdoorEndDate.setText("End Date :" + constants.getEndDate());
        myHolder.DateTime.setText("Apply Date :" + constants.getTime());
        // myHolder.Status.setText(constants.Status);
        myHolder.Reason.setText(constants.getReason());

        myHolder.rel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(context, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    // myHolder.btnCheckIn.setBackgroundColor(context.getResources().getColor(R.color.md_green_400));
                    myHolder.btnCheckOut.setVisibility(View.VISIBLE);
                    myHolder.rel2.setVisibility(View.VISIBLE);
                    myHolder.btnCheckIn.setClickable(false);
                    myHolder.rel1.setClickable(false);
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    Toast.makeText(context, "Please turn on GPS network/settings", Toast.LENGTH_LONG).show();
                    myHolder.swt.setChecked(false);
                    //myHolder.btnCheckIn.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    myHolder.btnCheckOut.setClickable(false);
                    myHolder.rel2.setClickable(false);

                }
            }
        });

        myHolder.rel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(context, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    // myHolder.btnCheckOut.setBackgroundColor(context.getResources().getColor(R.color.md_green_400));
                    myHolder.btnCheckIn.setClickable(false);
                    myHolder.btnCheckOut.setClickable(false);
                    myHolder.rel1.setClickable(false);
                    myHolder.rel2.setClickable(false);
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    Toast.makeText(context, "Please turn on GPS network/settings", Toast.LENGTH_LONG).show();
                    myHolder.swt.setChecked(false);
                    //  myHolder.btnCheckOut.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    myHolder.btnCheckOut.setClickable(false);


                }
            }
        });

        myHolder.swt.setChecked(false);
        myHolder.swt.setTextOn("On"); // displayed text of the Switch whenever it is in checked or on state
        myHolder.swt.setTextOff("Off");
        myHolder.swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {

                    case R.id.switch1:

                        if (!isChecked) {
                            // Toast.makeText(getApplicationContext(), "check out - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                            Toast.makeText(context, " Switch is off!!", Toast.LENGTH_SHORT).show();
                        } else {
//
                            //Toast.makeText(context, "Yes Switch is on!!", Toast.LENGTH_SHORT).show();

                            if (gps.canGetLocation()) {

                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();

                                // \n is for new line
                                Toast.makeText(context, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                            } else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                Toast.makeText(context, "Please turn on GPS network/settings", Toast.LENGTH_LONG).show();
                                myHolder.swt.setChecked(false);


                            }

                        }
                        break;

                    default:
                        break;
                }


            }
        });


        myHolder.btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    //  intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build((Activity) context.getApplicationContext());
                    ((Activity) context.getApplicationContext()).startActivityForResult(intent, PLACE_PICKER_REQUEST);


                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }










       /*         myHolder.linerODClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {


                        //  Toast.makeText(context, "" + constants.getLeaveOdId() , Toast.LENGTH_SHORT).show();
                        //Addleavepopup(v);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        //inflate layout from xml. you must create an xml layout file in res/layout first
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                        View layout = inflater.inflate(R.layout.popupodadpterdata, null);
                        builder.setView(layout);

                        fromdateODpop = (TextView) layout.findViewById(R.id.fromdateOD);
                        todateODpop = (TextView) layout.findViewById(R.id.todateOD);
                        edtODReasonpop = (TextView) layout.findViewById(R.id.edtODReason);
                        ODspinnerpop = (Spinner) layout.findViewById(R.id.spinner);
                        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                        //  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, loadDummyCities());

                        // Drop down layout style - list view with radio button
                        //  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        //spinner.setAdapter(dataAdapter);

                        ODSpnAdapter = new ODSpnAdapter(context, android.R.layout.simple_spinner_dropdown_item, loadDummyCities());
                        ODSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ODspinnerpop.setAdapter(ODSpnAdapter);
                        leaveOdId = constants.getLeaveOdId();
                        fromdateODpop.setText(constants.getStartDate());
                        todateODpop.setText(constants.getEndDate());
                        edtODReasonpop.setText(constants.getReason());
                        ODspinnerpop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                Spnitem = ODSpnAdapter.getItem(position).getId();
                                //Toast.makeText(parent.getContext(), "", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                        builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                        final AlertDialog dialog = builder.create();


                        dialog.show();

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String from = fromdateODpop.getText().toString();
                                String to = todateODpop.getText().toString();
                                String textString = edtODReasonpop.getText().toString();

                                Connection connection = new Connection(context);
                                String vkid = connection.getVkid();
                                String tokenId = connection.getTokenId();

                                String sa = String.valueOf(Spnitem);

                                String odid = leaveOdId;
                                String vkidd = EncryptionUtil.encryptString(vkid, context);
                                String TokenId = EncryptionUtil.encryptString(tokenId, context);
                                String imei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), context);
                                String deviceIdget = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                                String deviceid = EncryptionUtil.encryptString(deviceIdget, context);
                                String simserialnumber = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(), context);
                                String leaveTypeId = EncryptionUtil.encryptString(odid, context);
                                String leaveOdStatus = EncryptionUtil.encryptString(sa, context);
                                String reason = EncryptionUtil.encryptString(textString, context);


                                progress = new ProgressDialog(context.getApplicationContext());
                                progress.setTitle("Initiating ");
                                progress.setMessage(context.getApplicationContext().getResources().getString(R.string.pleaseWait));
                                progress.setCancelable(false);
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.show();

                                // new AsyncUpdateLeaveOD(getA).execute(vkidd, TokenId, imei, deviceid, simserialnumber, leaveTypeId, leaveOdStatus, reason);
                                AsyncGetUpdateLeaveODPopup myRequest = new AsyncGetUpdateLeaveODPopup();
                                myRequest.execute();


                                // dialog.dismiss();


                            }
                        });


//                builder.show(); */
            }


        });


    }


    private List<ODSpinner> loadDummyCities() {
        ODSpinnerEntityList = new ArrayList<ODSpinner>();


        ODSpinner ODSpinner1 = new ODSpinner();
        ODSpinner1.setId(5);
        ODSpinner1.setCity("Withdrawn");
        ODSpinnerEntityList.add(ODSpinner1);
        ODSpinner ODSpinner2 = new ODSpinner();
        ODSpinner2.setId(1);
        ODSpinner2.setCity("Update");
        ODSpinnerEntityList.add(ODSpinner2);


        return ODSpinnerEntityList;
    }


    public static boolean CheckDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = true;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public static boolean isDateAfter(String startDate, String endDate) {
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);


                // SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.US);
                Date date1 = sdf.parse(endDate);
                Date startingDate = sdf.parse(startDate);

                if (date1.after(startingDate)) {
                    return true;
                } else if (date1.equals(startingDate)) {
                    return true;
                } else if (startingDate.after(date1)) {
                    return false;
                } else
                    return false;
            } catch (Exception e) {

                return false;
            }
        }
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    public Locale getFilter() {
        return null;
    }

    public void filter(String charText) {
        charText = charText;
        data.clear();
        if (charText.equals("ALL")) {
            data.addAll(arraylist);
        } else if (charText.length() == 0) {
            data.addAll(arraylist);
        } else {
            for (LeaveOD wp : arraylist) {
                if (wp.getLeaveOdStatus().contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView OutdoorID;
        TextView OutdoorStartDate;
        TextView OutdoorEndDate;
        TextView DateTime;
        TextView Status;
        TextView Reason;
        LinearLayout linerODClick;
        ImageView odstatus;
        Switch swt;
        ImageView btnCheckIn, btnCheckOut, btnShowMap;
        RelativeLayout rel1, rel2;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardView);

            OutdoorID = (TextView) itemView.findViewById(R.id.OutdoorID);
            OutdoorStartDate = (TextView) itemView.findViewById(R.id.OutdoorStartDate);
            OutdoorEndDate = (TextView) itemView.findViewById(R.id.OutdoorEndDate);
            DateTime = (TextView) itemView.findViewById(R.id.DateTime);
            Status = (TextView) itemView.findViewById(R.id.Status);
            Reason = (TextView) itemView.findViewById(R.id.ReasonOD);
            linerODClick = (LinearLayout) itemView.findViewById(R.id.linerODClick);

            odstatus = (ImageView) itemView.findViewById(R.id.icodstatus);

            swt = (Switch) itemView.findViewById(R.id.switch1);
            btnCheckIn = (ImageView) itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = (ImageView) itemView.findViewById(R.id.btnCheckOut);
            btnShowMap = (ImageView) itemView.findViewById(R.id.btnShowMap);
            rel1 = (RelativeLayout) itemView.findViewById(R.id.rel1);
            rel2 = (RelativeLayout) itemView.findViewById(R.id.rel2);

        }

    }


    private class AsyncGetUpdateLeaveODPopup extends AsyncTask<Void, Void, Void> {


        String ODreason = edtODReasonpop.getText().toString();

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i("TAG", "onPreExecute");


        }


        @Override
        protected Void doInBackground(Void... params) {
            Log.i("TAG", "doInBackground");
            try {

                Connection connection = new Connection(context);
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

                String sa = String.valueOf(Spnitem);

                String odid = leaveOdId;
                String vkidd = EncryptionUtil.encryptString(vkid, context);
                String TokenId = EncryptionUtil.encryptString(tokenId, context);

                String deviceIdget = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, context);

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);

                String simSerial = CommonUtils.getSimSerialNumber(context);
                String simserialnumber = EncryptionUtil.encryptString(simSerial, context);

                String leaveTypeId = EncryptionUtil.encryptString(odid, context);
                String leaveOdStatus = EncryptionUtil.encryptString(sa, context);
                String reason = EncryptionUtil.encryptString(ODreason, context);

                diplayServerResopnse = WebService.updateStatusLeaveOD(vkidd, TokenId, imei, deviceid, simserialnumber, leaveTypeId, leaveOdStatus, reason);

                Log.d("TAG", "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                String message = null;
                Log.e("TAG" + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");

            progress.dismiss();
            try {


                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));

                    //  Log.e(TAG + "Please Null error", diplayServerResopnse);
                } else if (diplayServerResopnse.equals("OKAY")) {


                    // AsyncGetMyLeaveODList myRequest = new AsyncGetMyLeaveODList();
                    // myRequest.execute();


                } else {
                    Log.e("Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

                e.printStackTrace();
            }


        }


    }

}
