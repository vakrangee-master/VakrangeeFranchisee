package in.vakrangee.franchisee.sitelayout.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.recyclerview.widget.RecyclerView;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncNextGenFranchiseeDetails;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncgetLocationDetailsNextGen;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.model.LocationKendraDataModel;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;

//

public class AdapterRecyclerViewNextGen extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    ProgressDialog progress;
    TextView nameset1, nameset2, nameset3, nameset4, nameset5;
    private ArrayList<LocationKendraDataModel> dataSet;
    LocationKendraDataModel locationKendraDataModel;
    String diplayServerResopnse;
    ArrayList<LocationKendraDataModel> dataModels;
    String[] myVakrangeeKendraLocation;
    String modeType = null;

    int NEXTGEN_SITE_TYPE;
    private boolean isAdhoc = false;
    private ICallBack iCallBack;
    private DeprecateHandler deprecateHandler;

    public AdapterRecyclerViewNextGen(final Context context, ArrayList<LocationKendraDataModel> data,
                                      TextView txtname1, TextView txtname2, TextView txtname3,
                                      TextView txtname4, TextView txtname5, final String[] myVakrangeeKendraLocations) {

        init(null, context, data, txtname1, txtname2, txtname3, txtname4, txtname5, myVakrangeeKendraLocations, null);
    }

    // create constructor to initialise context and data sent from MainActivity
    public AdapterRecyclerViewNextGen(int type, String modeType, final Context context, ArrayList<LocationKendraDataModel> data,
                                      TextView txtname1, TextView txtname2, TextView txtname3,
                                      TextView txtname4, TextView txtname5, final String[] myVakrangeeKendraLocations, ICallBack iCallBack) {

        init(modeType, context, data, txtname1, txtname2, txtname3, txtname4, txtname5, myVakrangeeKendraLocations, iCallBack);
        NEXTGEN_SITE_TYPE = type;
    }

    private void init(String modeType, final Context context, ArrayList<LocationKendraDataModel> data,
                      TextView txtname1, TextView txtname2, TextView txtname3,
                      TextView txtname4, TextView txtname5, final String[] myVakrangeeKendraLocations, ICallBack iCallBack) {

        this.modeType = modeType;
        this.context = context;
        this.dataSet = data;
        this.nameset1 = txtname1;
        this.nameset2 = txtname2;
        this.nameset3 = txtname3;
        this.nameset4 = txtname4;
        this.nameset5 = txtname5;
        this.iCallBack = iCallBack;

        //Get App Mode
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;

        this.myVakrangeeKendraLocation = myVakrangeeKendraLocations;
        deprecateHandler = new DeprecateHandler(context);

        if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                && myVakrangeeKendraLocation[2] != null && myVakrangeeKendraLocation[3] != null &&
                myVakrangeeKendraLocation[4] != null) {

            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            st2.nextToken();

            StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocation[2], "|");
            st3.nextToken();

            StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocation[3], "|");
            st4.nextToken();

            StringTokenizer st5 = new StringTokenizer(myVakrangeeKendraLocation[4], "|");
            String name5 = st5.nextToken();

            nameset5.setText(name5);

            //Second
        } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                && myVakrangeeKendraLocation[2] != null && myVakrangeeKendraLocation[3] != null) {

            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            st2.nextToken();

            StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocation[2], "|");
            st3.nextToken();

            StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocation[3], "|");
            String name4 = st4.nextToken();

            nameset4.setText(name4 + " > ");
            nameset5.setText("");

            //Second
        } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                && myVakrangeeKendraLocation[2] != null) {

            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            st2.nextToken();

            StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocation[2], "|");
            String name3 = st3.nextToken();
            nameset3.setText(name3 + " > ");

            nameset4.setText("");
            nameset5.setText("");
            //Second
        } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null) {
            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            st1.nextToken();
            st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            String name2 = st2.nextToken();

            nameset2.setText(name2 + " > ");
            nameset3.setText("");
            nameset4.setText("");
            nameset5.setText("");
            //First
        } else if (myVakrangeeKendraLocation[0] != null) {
            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            String name1 = st1.nextToken();

            nameset1.setText(name1 + " > ");

            nameset2.setText("");
            nameset3.setText("");
            nameset4.setText("");
            nameset5.setText("");

        }
    }

    public interface ICallBack {
        public void iCallClick(String mobNo);

        public void iMailClick(String emailId);
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_readiness, null);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            dataModels = new ArrayList<>();
            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder = (MyHolder) holder;
            locationKendraDataModel = dataSet.get(position);

            myHolder.txtType.setText(locationKendraDataModel.getId() + locationKendraDataModel.getScope());
            myHolder.txtName.setText(locationKendraDataModel.getType() + " - " + locationKendraDataModel.getId());

            if (modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
                boolean IsBE = (locationKendraDataModel.getScope().equals("BE") && locationKendraDataModel.getId().length() == 16) ? true : false;
                hideOtherViews(myHolder, locationKendraDataModel, IsBE);
            }else if (modeType.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
                boolean IsBE = (locationKendraDataModel.getScope().equals("BE") && locationKendraDataModel.getId().length() == 16) ? true : false;
                hideOtherViews(myHolder, locationKendraDataModel, IsBE);
            } else {
                hideOtherViews(myHolder, locationKendraDataModel, false);
            }

            final Connection connection = new Connection(context);

            EncryptionUtil.encryptString(connection.getVkid(), context);
            final String getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), context);

            final String getimei = EncryptionUtil.encryptString(CommonUtils.getAndroidUniqueID(context), context);
            final String getdeviceid = EncryptionUtil.encryptString(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID), context);
            final String getsimserialnumber = EncryptionUtil.encryptString(CommonUtils.getSimSerialNumber(context), context);

            if (locationKendraDataModel.getScope().equals("BE") && locationKendraDataModel.getId().length() == 16) {
                myHolder.txtName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String getid = dataSet.get(position).getId();
                        dataSet.get(position).getType();
                        dataSet.get(position).getScope();

                        String tempVKID = connection.getVkid();
                        String userId = EncryptionUtil.encryptString(tempVKID, context);
                        String vkid = EncryptionUtil.encryptString(getid, context);

                        if (isAdhoc) {
                            new AsyncNextGenFranchiseeDetails(modeType, v.getContext(), getid).execute(tempVKID, getid);
                        } else {
                            new AsyncNextGenFranchiseeDetails(modeType, v.getContext(), getid).execute(userId, vkid, getTokenId, getimei, getdeviceid, getsimserialnumber);
                        }
                    }
                });
            } else {
                myHolder.txtName.setText(locationKendraDataModel.getType());
            }

            myHolder.realtivelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itpos = position;
                    final LocationKendraDataModel locationKendraDataModel = dataSet.get(itpos);

                    if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] == null) {
                        myVakrangeeKendraLocation[1] = locationKendraDataModel.getType() + "|"
                                + locationKendraDataModel.getScope() + "|" + locationKendraDataModel.getId();


                    } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                            && myVakrangeeKendraLocation[2] == null) {
                        myVakrangeeKendraLocation[2] = locationKendraDataModel.getType() + "|"
                                + locationKendraDataModel.getScope() + "|" + locationKendraDataModel.getId();


                    } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                            && myVakrangeeKendraLocation[2] != null && myVakrangeeKendraLocation[3] == null) {
                        myVakrangeeKendraLocation[3] = locationKendraDataModel.getType() + "|"
                                + locationKendraDataModel.getScope() + "|" + locationKendraDataModel.getId();


                    } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                            && myVakrangeeKendraLocation[2] != null && myVakrangeeKendraLocation[3] != null
                            && myVakrangeeKendraLocation[4] == null) {
                        myVakrangeeKendraLocation[4] = locationKendraDataModel.getType() + "|"
                                + locationKendraDataModel.getScope() + "|" + locationKendraDataModel.getId();


                    } else {
                        //Do Nothing
                    }

                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    Connection connection = new Connection(context);
                    final String getVkid = EncryptionUtil.encryptString(connection.getVkid(), context);
                    final String getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), context);
                    @SuppressLint("MissingPermission") final String getimei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), context);
                    String deviceIdget = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    final String getdeviceid = EncryptionUtil.encryptString(deviceIdget, context);
                    @SuppressLint("MissingPermission") final String getsimserialnumber = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(), context);

                    String getid = dataSet.get(itpos).getId();
                    String getname = dataSet.get(itpos).getType();
                    String getscop = dataSet.get(itpos).getScope();
                    final String scop = EncryptionUtil.encryptString(getscop, context);
                    final String id = EncryptionUtil.encryptString(getid, context);

                    String tempVKID = connection.getVkid();
                    String userId = EncryptionUtil.encryptString(tempVKID, context);
                    String vkid = EncryptionUtil.encryptString(getid, context);

                    if (locationKendraDataModel.getScope().equals("BE") && locationKendraDataModel.getId().length() == 16) {
                        if (isAdhoc) {
                            new AsyncNextGenFranchiseeDetails(modeType, v.getContext(), getid).execute(tempVKID, getid);
                        } else {
                            new AsyncNextGenFranchiseeDetails(modeType, v.getContext(), getid).execute(userId, vkid, getTokenId, getimei, getdeviceid, getsimserialnumber);
                        }
                    } else {
                        new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, v.getContext(), myVakrangeeKendraLocation).execute(getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber, scop, id);

                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView txtNameLbl, txtName;
        TextView txtType;
        private TextView mTextView;
        RelativeLayout realtivelayout;
        TextView txtApplicationIdLbl, txtApplicationId, txtStatus, txtMobileNo, txtEmailId;
        LinearLayout layoutStatusIndicator;
        LinearLayout layoutName, layoutApplicationId, layoutStatus, layoutMobileNo, layoutEmailId;
        TextView txtStatusIndicatorIcon, txtMobileIcon, txtEmailIcon;

        public void setText(String item) {
            mTextView.setText(item);
        }


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.name);
            txtType = (TextView) itemView.findViewById(R.id.type);
            realtivelayout = (RelativeLayout) itemView.findViewById(R.id.realtivelayout);
            txtApplicationIdLbl = itemView.findViewById(R.id.txtApplicationIdLbl);
            txtApplicationId = itemView.findViewById(R.id.txtApplicationId);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtMobileNo = itemView.findViewById(R.id.txtMobileNo);
            txtEmailId = itemView.findViewById(R.id.txtEmailId);
            layoutStatusIndicator = itemView.findViewById(R.id.layoutStatusIndicator);
            layoutName = itemView.findViewById(R.id.layoutName);
            txtNameLbl = itemView.findViewById(R.id.txtNameLbl);
            layoutApplicationId = itemView.findViewById(R.id.layoutApplicationId);
            layoutStatus = itemView.findViewById(R.id.layoutStatus);
            layoutMobileNo = itemView.findViewById(R.id.layoutMobileNo);
            layoutEmailId = itemView.findViewById(R.id.layoutEmailId);
            txtStatusIndicatorIcon = itemView.findViewById(R.id.txtStatusIndicator);
            txtMobileIcon = itemView.findViewById(R.id.txtMobileIcon);
            txtEmailIcon = itemView.findViewById(R.id.txtEmail);

        }
    }

    public void hideOtherViews(MyHolder myHolder, LocationKendraDataModel locationKendraDataModel, boolean IsVisible) {

        if (!IsVisible) {
            myHolder.txtNameLbl.setVisibility(View.GONE);
            myHolder.layoutApplicationId.setVisibility(View.GONE);
            myHolder.layoutStatus.setVisibility(View.GONE);
            myHolder.layoutMobileNo.setVisibility(View.GONE);
            myHolder.layoutEmailId.setVisibility(View.GONE);
            myHolder.layoutStatusIndicator.setVisibility(View.GONE);

        } else {

            //Set Data and visibility
            String name = locationKendraDataModel.getType();
            myHolder.txtName.setText(name);

            //Set VkId if exist, else set applicationID
            if (TextUtils.isEmpty(locationKendraDataModel.getVkId())) {
                String applicationId = locationKendraDataModel.getId();
                myHolder.txtApplicationIdLbl.setText("Application No");
                myHolder.txtApplicationId.setText(applicationId);
            } else {
                myHolder.txtApplicationIdLbl.setText("VKID");
                myHolder.txtApplicationId.setText(locationKendraDataModel.getVkId());
            }

            String status = locationKendraDataModel.getStatus();
            myHolder.txtStatus.setText(status);

            //Mobile No
            String mobileNo = locationKendraDataModel.getMobileNo();
            if (TextUtils.isEmpty(mobileNo)) {
                myHolder.layoutMobileNo.setVisibility(View.INVISIBLE);
            } else {
                setFontawesomeIcon(myHolder.txtMobileIcon, context.getResources().getString(R.string.fa_call));
                myHolder.txtMobileNo.setText(mobileNo);
                myHolder.layoutMobileNo.setTag(mobileNo);
                myHolder.layoutMobileNo.setVisibility(View.VISIBLE);
            }

            //Email Id
            String emailId = locationKendraDataModel.getEmailId();
            if (TextUtils.isEmpty(emailId)) {
                myHolder.layoutEmailId.setVisibility(View.INVISIBLE);
            } else {
                setFontawesomeIcon(myHolder.txtEmailIcon, context.getResources().getString(R.string.fa_mail));
                myHolder.txtEmailId.setText(emailId);
                myHolder.layoutEmailId.setTag(emailId);
                myHolder.layoutEmailId.setVisibility(View.VISIBLE);
            }

            myHolder.txtNameLbl.setVisibility(View.VISIBLE);
            myHolder.layoutApplicationId.setVisibility(View.VISIBLE);
            myHolder.layoutStatus.setVisibility(View.VISIBLE);
            myHolder.layoutStatusIndicator.setVisibility(View.VISIBLE);

            //Set Work Status Color
            GradientDrawable bgWorkShape = (GradientDrawable) myHolder.txtStatus.getBackground();

            //Set StatusIndicator Color
            GradientDrawable bgShape = (GradientDrawable) myHolder.txtStatusIndicatorIcon.getBackground();
            switch (locationKendraDataModel.getStatusNo()) {
                case "0":
                    changeDrawable(bgShape, deprecateHandler.getColor(R.color.red), 0, deprecateHandler.getColor(R.color.red));
                    changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.red), 0, deprecateHandler.getColor(R.color.red));
                    myHolder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                    myHolder.txtStatusIndicatorIcon.setText(locationKendraDataModel.getfUploadedCount());
                    myHolder.txtStatusIndicatorIcon.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    break;

                case "1":
                    changeDrawable(bgShape, deprecateHandler.getColor(R.color.white), 2, deprecateHandler.getColor(R.color.orange));
                    changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.white), 2, deprecateHandler.getColor(R.color.orange));
                    myHolder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.iGrey));
                    myHolder.txtStatusIndicatorIcon.setText(locationKendraDataModel.getfUploadedCount());
                    myHolder.txtStatusIndicatorIcon.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    break;

                case "2":
                    changeDrawable(bgShape, deprecateHandler.getColor(R.color.orange), 0, deprecateHandler.getColor(R.color.orange));
                    changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.orange), 0, deprecateHandler.getColor(R.color.orange));
                    myHolder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                    myHolder.txtStatusIndicatorIcon.setText(R.string.fa_check);
                    myHolder.txtStatusIndicatorIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                    break;

                case "3":
                    changeDrawable(bgShape, deprecateHandler.getColor(R.color.green), 2, deprecateHandler.getColor(R.color.green));
                    changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.green), 2, deprecateHandler.getColor(R.color.green));
                    myHolder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.iGrey));
                    myHolder.txtStatusIndicatorIcon.setText(R.string.fa_check);
                    myHolder.txtStatusIndicatorIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                    break;

                case "4":
                    changeDrawable(bgShape, deprecateHandler.getColor(R.color.green), 0, deprecateHandler.getColor(R.color.green));
                    changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.green), 0, deprecateHandler.getColor(R.color.green));
                    myHolder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                    myHolder.txtStatusIndicatorIcon.setText(R.string.fa_check);
                    myHolder.txtStatusIndicatorIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                    break;

                default:
                    break;
            }

            //Mobile No
            myHolder.layoutMobileNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (iCallBack == null)
                        return;

                    String mobileNo = (String) view.getTag();
                    iCallBack.iCallClick(mobileNo);
                }
            });

            //Email Id
            myHolder.layoutEmailId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (iCallBack == null)
                        return;

                    String emailId = (String) view.getTag();
                    iCallBack.iMailClick(emailId);
                }
            });
        }
    }

    public void changeDrawable(GradientDrawable drawable, int solidColor, int strokeWidth, int strokeColor) {
        if (drawable != null) {

            drawable.setColor(solidColor);
            if (strokeWidth > 0)
                drawable.setStroke(strokeWidth, strokeColor);

        }
    }

    public void setFontawesomeIcon(TextView textView, String icon) {
        textView.setText(icon);
        textView.setTextSize(20);
        textView.setTextColor(deprecateHandler.getColor(R.color.iGrey));
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }

}
