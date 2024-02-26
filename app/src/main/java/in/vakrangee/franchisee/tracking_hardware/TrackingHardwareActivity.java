package in.vakrangee.franchisee.tracking_hardware;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.tracking_hardware.CustomTrackingPDFdialog;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class TrackingHardwareActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = TrackingHardwareActivity.class.getSimpleName();
    @BindView(R.id.toolbarAgreement)
    Toolbar toolbarAgreement;
    @BindView(R.id.btnSubmit)
    TextView btnSubmit;
    @BindView(R.id.editTextAwb)
    EditText editTextAwb;

    @BindView(R.id.cardviewBasicInfo)
    CardView cardviewBasicInfo;

    // AWB info
    @BindView(R.id.textOriginName)
    TextView textOriginName;
    @BindView(R.id.textExpDate)
    TextView textExpDate;
    @BindView(R.id.textTotalPackage)
    TextView textTotalPackage;
    @BindView(R.id.textStatus)
    TextView textStatus;
    @BindView(R.id.textDestinationName)
    TextView textDestinationName;
    @BindView(R.id.textCNNumber)
    TextView textCNNumber;
    @BindView(R.id.textTrackingStatus)
    TextView textTrackingStatus;
    @BindView(R.id.textShipmentDate)
    TextView textShipmentDate;


    // delivery status
  /*  @BindView(R.id.textReceivedBy)
    TextView textReceivedBy;*/
    @BindView(R.id.textDeliveryDate)
    TextView textDeliveryDate;
    @BindView(R.id.imgPDF)
    ImageView imgPDF;
    @BindView(R.id.layoutPOD)
    LinearLayout layoutPOD;
    @BindView(R.id.layoutDelivery)
    LinearLayout layoutDelivery;
    @BindView(R.id.textTrackingReason)
    TextView textTrackingReason;
    @BindView(R.id.textTrackingRec)
    TextView textTrackingRec;
    @BindView(R.id.layoutReason)
    LinearLayout layoutReason;
    @BindView(R.id.layoutDeliveryStatus)
    LinearLayout layoutDeliveryStatus;
    @BindView(R.id.textTrackingDetails)
    TextView textTrackingDetails;
    @BindView(R.id.layoutTrackingDetails)
    LinearLayout layoutTrackingDetails;


    private Typeface font;
    private AWBTrackingDto awbTrackingDto;
    private AsyncGetTrackingHardwareDetails asyncGetTrackingHardwareDetails;
    private CustomTrackingPDFdialog customTrackingPDFdialog;
    // private CustomNavigationBar customNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_hardware);

        ButterKnife.bind(this);

        //customNavigationBar = new CustomNavigationBar(TrackingHardwareActivity.this, toolbarAgreement);

        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        btnSubmit.setTypeface(font);
        // btnSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.go)));
        btnSubmit.setOnClickListener(this);
        imgPDF.setOnClickListener(this);
        //CommonUtils.InputFiletrWithMaxLength(editTextAwb, "\"~#^|$%&*!'", 25);

        onChangeListenerEditext();

        init();
    }


    //region - edit text on changeListener
    private void onChangeListenerEditext() {
        editTextAwb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start < 5) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    editTextAwb.setTextColor(Color.parseColor("#000000"));
                    editTextAwb.setError("Enter minimumm 5 character AWB number.");
                    //btnSubmit.setVisibility(View.GONE);
                    btnSubmit.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String s = editable.toString();
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editTextAwb.setText(s);
                    editTextAwb.setSelection(s.length());
                }

                if (editTextAwb.getText().toString().contains(" ")) {
                    editTextAwb.setText(editTextAwb.getText().toString().replaceAll(" ", ""));
                    editTextAwb.setSelection(editTextAwb.getText().length());


                }


                if (editTextAwb.length() <= 4) {
                    btnSubmit.setClickable(false);
                    // btnSubmit.setVisibility(View.GONE);
                } else {
                    btnSubmit.setClickable(true);
                    // btnSubmit.setVisibility(View.VISIBLE);
                    editTextAwb.setTextColor(Color.parseColor("#468847"));
                    editTextAwb.setError(null);

                }
            }
        });
    }
    //endregion

    //region init  toolbar
    private void init() {
        //set toolbar name
        setSupportActionBar(toolbarAgreement);
        if (getSupportActionBar() != null) {
            toolbarAgreement.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.tracking_hardware) + "</small>"));
        }
    }
    //endregion

    @Override
    public void onBackPressed() {
        backPressed();
    }

    //region on Back press
    public void backPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_serach, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_main);
        searchItem.setVisible(false);
        MenuItem home = menu.findItem(R.id.action_home_dashborad);
        home.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //customNavigationBar.openDrawer();
                backPressed();
                break;
            case R.id.action_home_dashborad:
                Intent intent = new Intent(TrackingHardwareActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSubmit:
                btnSubmitClick();
                break;
            case R.id.imgPDF:
                AnimationHanndler.bubbleAnimation(TrackingHardwareActivity.this, v);
                openPDFDialog(awbTrackingDto.getPOD());
             /*   Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(awbTrackingDto.getPOD()));
                intent.setData(Uri.parse(awbTrackingDto.getPOD()));
                startActivity(intent);*/
                break;
        }
    }

    private void openPDFDialog(String pod) {
        customTrackingPDFdialog = new CustomTrackingPDFdialog(TrackingHardwareActivity.this, pod);
        customTrackingPDFdialog.show();
        customTrackingPDFdialog.setDialogTitle("POD Status");
        customTrackingPDFdialog.setCancelable(false);
    }

    //region button submit click - AWB button
    private void btnSubmitClick() {

        if (editTextAwb.getText().toString().length() <= 4) {
            editTextAwb.setError("Please enter 5 character AWB number");
        } else if (TextUtils.isEmpty(editTextAwb.getText().toString())) {
            Toast.makeText(TrackingHardwareActivity.this, "Please enter AWB number.", Toast.LENGTH_SHORT).show();
        } else {
            editTextAwb.setError(null);
            asyncGetTrackingHardwareDetails = new AsyncGetTrackingHardwareDetails(TrackingHardwareActivity.this,
                    editTextAwb.getText().toString(), new AsyncGetTrackingHardwareDetails.Callback() {
                @Override
                public void onResult(String result) {
                    try {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        //result = getJsonResponse();
                        btnSubmit.setClickable(true);
                        // btnSubmit.setVisibility(View.VISIBLE);
                        if (result.startsWith("ERROR")) {
                            cardviewBasicInfo.setVisibility(View.GONE);
                            result = result.replace("ERROR|", "");
                            if (TextUtils.isEmpty(result)) {
                                cardviewBasicInfo.setVisibility(View.GONE);
                                AlertDialogBoxInfo.alertDialogShow(TrackingHardwareActivity.this, getResources().getString(R.string.Warning));
                                return;
                            }

                            String[] result_split = result.split("\\|");
                            if (result_split[1] != null) {
                                AlertDialogBoxInfo.alertDialogShow(TrackingHardwareActivity.this, result_split[1]);
                            } else {
                                cardviewBasicInfo.setVisibility(View.GONE);
                                AlertDialogBoxInfo.alertDialogShow(TrackingHardwareActivity.this, getResources().getString(R.string.Warning));
                            }
                        } else if (result.startsWith("OKAY")) {
                            editTextAwb.setText(null);
                            editTextAwb.setError(null);
                            btnSubmit.setClickable(false);
                            //btnSubmit.setVisibility(View.GONE);
                            result = result.replace("OKAY|", "");
                            Gson gson = new Gson();
                            awbTrackingDto = gson.fromJson(result, AWBTrackingDto.class);

                            //if result OKAY
                            cardviewBasicInfo.setVisibility(View.VISIBLE);

                            textOriginName.setText(TextUtils.isEmpty(awbTrackingDto.getOrigin()) ? "- " : awbTrackingDto.getOrigin());
                            textDestinationName.setText(awbTrackingDto.getDestination());

                            textShipmentDate.setText(awbTrackingDto.getCNDate());
                            textExpDate.setText(TextUtils.isEmpty(awbTrackingDto.getExpectedDeliveryDate()) ? "- " : awbTrackingDto.getExpectedDeliveryDate());
                            textTotalPackage.setText(TextUtils.isEmpty(awbTrackingDto.getNoOfPackages()) ? "- " : awbTrackingDto.getNoOfPackages());
                            textCNNumber.setText(TextUtils.isEmpty(awbTrackingDto.getCN()) ? "-" : awbTrackingDto.getCN());

                            textStatus.setText(getStatusCode(awbTrackingDto.getStatus()));

                            if (awbTrackingDto.getStatus().equalsIgnoreCase("DEL")) {
                                layoutDelivery.setVisibility(View.VISIBLE);
                                //  textReceivedBy.setText("-");
                                textDeliveryDate.setText(TextUtils.isEmpty(awbTrackingDto.getDeliveryDate()) ? "- " : awbTrackingDto.getDeliveryDate());
                                DeliveryStatusSplit(awbTrackingDto.getDocketTrackingStatus());
                                layoutDeliveryStatus.setVisibility(View.VISIBLE);
                                layoutTrackingDetails.setVisibility(View.GONE);

                            } else {
                                layoutDelivery.setVisibility(View.GONE);
                                layoutDeliveryStatus.setVisibility(View.GONE);
                                layoutTrackingDetails.setVisibility(View.VISIBLE);
                                textTrackingDetails.setText(awbTrackingDto.getDocketTrackingStatus());
                            }
                            //String Podstatus = TextUtils.isEmpty(awbTrackingDto.getPOD()) ? "- " : awbTrackingDto.getPOD();
                            if (TextUtils.isEmpty(awbTrackingDto.getPOD())) {
                                layoutPOD.setVisibility(View.GONE);
                            } else {
                                layoutPOD.setVisibility(View.VISIBLE);
                            }

                        } else {
                            AlertDialogBoxInfo.alertDialogShow(TrackingHardwareActivity.this, getResources().getString(R.string.Warning));
                        }
                    } catch (Exception e) {
                        e.getMessage();
                        cardviewBasicInfo.setVisibility(View.GONE);
                        AlertDialogBoxInfo.alertDialogShow(TrackingHardwareActivity.this, getResources().getString(R.string.Warning));
                    }
                }
            });

            asyncGetTrackingHardwareDetails.execute();
        }

    }

    private void DeliveryStatusSplit(String docketTrackingStatus) {
        try {
            String[] arrayString = docketTrackingStatus.split(":");
            String DeliveredBy = arrayString[0];
            DeliveredBy = DeliveredBy.substring(DeliveredBy.indexOf("Delivered By") + 12, DeliveredBy.length());
            DeliveredBy = DeliveredBy.replace("Reason", " ");
            textTrackingStatus.setText(DeliveredBy);


            String getReason = getSubString1(docketTrackingStatus, ".", ":");
            getReason = getReason.replace("- Rec", " ");
            getReason = getReason.replace(": - ", "").trim();
            if (TextUtils.isEmpty(getReason)) {
                layoutReason.setVisibility(View.GONE);
            } else {
                layoutReason.setVisibility(View.VISIBLE);
                textTrackingReason.setText(getReason);
            }


            String receivedBy = docketTrackingStatus.substring(docketTrackingStatus.lastIndexOf(".") + 1);
            textTrackingRec.setText(receivedBy);

        } catch (Exception e) {
            e.getMessage();
        }
    }
    //endregion

    //region status code
    private String getStatusCode(String status) {
        String name = "-";
        switch (status) {
            case "DEL":
                textStatus.setTextColor(getResources().getColor(R.color.md_green_500));
                return "Delivered";
            case "ARR":
                textStatus.setTextColor(getResources().getColor(R.color.md_red_500));
                return "Arrived";
            case "UNDEL":
                textStatus.setTextColor(getResources().getColor(R.color.md_red_500));
                return "undelivered";
            case "INTRANSIT":
                textStatus.setTextColor(getResources().getColor(R.color.md_red_500));
                return "in transit";
            case "BKG":
                textStatus.setTextColor(getResources().getColor(R.color.md_red_500));
                return "booking";
            case "OFD":
                textStatus.setTextColor(getResources().getColor(R.color.md_red_500));
                return "out for delivery";
            case "PARTDEL":
                textStatus.setTextColor(getResources().getColor(R.color.md_red_500));
                return "part delivered";
            case "HOLD":
                textStatus.setTextColor(getResources().getColor(R.color.md_red_500));
                return "hold";


        }
        return name;
    }
    //endregion

    private String getJsonResponse() {
        String repsonse = "OKAY|{\"CN\":\"1220004433\",\"CNDate\":\"13 Mar 2019\",\"DeliveryDate\":\"26 Mar 2019\",\"DeliveryPincode\":\"311012\",\"Destination\":\"BHILWARA\",\"DocketTrackingStatus\":\" Delivered By UDRF On 26 Mar 19 - DS\\/UDRF\\/18_19\\/00753 Reason : - SHIPMENT RECEIVED LATE FOR ATTEMPTING DELIVERY TODAY  - Rec. By ok\",\"ExecutionMessage\":\"Success\",\"ExecutionStatus\":\"200\",\"ExpectedDeliveryDate\":\"26 Mar 2019\",\"InvoiceDetailList\":[{\"EWBNumber\":\"\",\"InvoiceNo\":\"0005\",\"InvoiceValues\":\"40.00\"},{\"EWBNumber\":\"\",\"InvoiceNo\":\"0004\",\"InvoiceValues\":\"40.00\"},{\"EWBNumber\":\"\",\"InvoiceNo\":\"0003\",\"InvoiceValues\":\"100.00\"},{\"EWBNumber\":\"\",\"InvoiceNo\":\"0002\",\"InvoiceValues\":\"500.00\"},{\"EWBNumber\":\"\",\"InvoiceNo\":\"0001\",\"InvoiceValues\":\"8000.00\"},{\"EWBNumber\":\"251089125078\",\"InvoiceNo\":\"9075\",\"InvoiceValues\":\"140000.00\"}],\"NoOfPackages\":\"6\",\"Origin\":\"BHIWANDI\",\"POD\":\"http:\\/\\/tms.futuresupplychains.com\\/LivePODVIEWER\\/2019\\/Mar\\/P_1220004433.PDF\",\"PickupPincode\":\"421101\",\"Status\":\"DEL\",\"StatusDate\":\"26 Mar 2019\",\"StatusLocation\":\"UDRF\",\"StatusTime\":\"15:01:52\",\"TotalActualWight\":\"126.00\",\"TransportMode\":\"Standard\"}";
        // repsonse = "ERROR|200|AWB No is invalid";

        return repsonse;
    }

    public static String getSubString1(String mainString, String lastString, String startString) {
        String endString = "";
        int endIndex = mainString.indexOf(lastString);
        int startIndex = mainString.indexOf(startString);
        Log.d("message", "" + mainString.substring(startIndex, endIndex));
        endString = mainString.substring(startIndex, endIndex);
        return endString;
    }


}
