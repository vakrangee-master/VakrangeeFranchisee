package in.vakrangee.franchisee.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.task.AsyncGetmyVakrangeeKendraTimingsResponseParticular;
import in.vakrangee.franchisee.task.AsyncgetLocationDetails;
import in.vakrangee.supercore.franchisee.model.LocationKendraDataModel;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
//

/**
 * Created by Nileshd on 1/31/2017.
 */
public class AdapterRecyclerViewVakrangeeKendraLocationDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    ProgressDialog progress;
    TextView nameset1, nameset2, nameset3, nameset4, nameset5;
    private ArrayList<LocationKendraDataModel> dataSet;
    LocationKendraDataModel locationKendraDataModel;
    String diplayServerResopnse;
    ArrayList<LocationKendraDataModel> dataModels;
    String[] myVakrangeeKendraLocation;
    //View result;

    // create constructor to initialise context and data sent from MainActivity
    public AdapterRecyclerViewVakrangeeKendraLocationDetails(final Context context, ArrayList<LocationKendraDataModel> data,
                                                             TextView txtname1, TextView txtname2, TextView txtname3,
                                                             TextView txtname4, TextView txtname5, final String[] myVakrangeeKendraLocations) {
        this.context = context;

        this.dataSet = data;
        this.nameset1 = txtname1;
        this.nameset2 = txtname2;
        this.nameset3 = txtname3;
        this.nameset4 = txtname4;
        this.nameset5 = txtname5;

        this.myVakrangeeKendraLocation = myVakrangeeKendraLocations;
        System.out.println("Initialize");
        //Third


        if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                && myVakrangeeKendraLocation[2] != null && myVakrangeeKendraLocation[3] != null &&
                myVakrangeeKendraLocation[4] != null) {

            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            String name1 = st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            String name2 = st2.nextToken();

            StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocation[2], "|");
            String name3 = st3.nextToken();

            StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocation[3], "|");
            String name4 = st4.nextToken();

            StringTokenizer st5 = new StringTokenizer(myVakrangeeKendraLocation[4], "|");
            String name5 = st5.nextToken();

            // nameset4.setText(name1 + " > " + name2 + " > " + name3 + " > " + name4 + " > " + name5);
            nameset5.setText(name5);
            System.out.println("Initialize 5" + myVakrangeeKendraLocation[4]);


            //Second
        } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                && myVakrangeeKendraLocation[2] != null && myVakrangeeKendraLocation[3] != null) {

            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            String name1 = st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            String name2 = st2.nextToken();

            StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocation[2], "|");
            String name3 = st3.nextToken();

            StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocation[3], "|");
            String name4 = st4.nextToken();

            //nameset3.setText(name1 + " > " + name2 + " > " + name3 + " > " + name4);
            nameset4.setText(name4 + " > ");
            System.out.println("Initialize 4" + myVakrangeeKendraLocation[3]);


            nameset5.setText("");

            //Second
        } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null
                && myVakrangeeKendraLocation[2] != null) {

            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            String name1 = st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            String name2 = st2.nextToken();

            StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocation[2], "|");
            String name3 = st3.nextToken();

            // nameset2.setText(name1 + " > " + name2 + " > " + name3);
            nameset3.setText(name3 + " > ");
            System.out.println("Initialize 3" + myVakrangeeKendraLocation[2]);

            nameset4.setText("");
            nameset5.setText("");


            //Second
        } else if (myVakrangeeKendraLocation[0] != null && myVakrangeeKendraLocation[1] != null) {
            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            String name1 = st1.nextToken();
            String scope1 = st1.nextToken();

            StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocation[1], "|");
            String name2 = st2.nextToken();

            nameset2.setText(name2 + " > ");
            // nameset1.setText(name2 + " > ");
            System.out.println("Initialize 2" + myVakrangeeKendraLocation[1]);
            nameset3.setText("");
            nameset4.setText("");
            nameset5.setText("");


            //First
        } else if (myVakrangeeKendraLocation[0] != null) {
            StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocation[0], "|");
            String name1 = st1.nextToken();

            nameset1.setText(name1 + " > ");

            System.out.println("Initialize 1" + myVakrangeeKendraLocation[0]);
            nameset2.setText("");
            nameset3.setText("");
            nameset4.setText("");
            nameset5.setText("");

        }


    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewadpaterkendralocationdetails, null);
        MyHolder holder = new MyHolder(view);


        return holder;
    }

    private int lastPosition = -1;

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            dataModels = new ArrayList<>();
            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder = (MyHolder) holder;
            locationKendraDataModel = dataSet.get(position);


            myHolder.txtType.setText(locationKendraDataModel.getId() + locationKendraDataModel.getScope());
            //myHolder.txtName.setText(locationKendraDataModel.getType());


            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final Connection connection = new Connection(context);


            final String getVkid = EncryptionUtil.encryptString(connection.getVkid(), context);
            final String getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), context);
            String deviceIdget = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String getdeviceid = EncryptionUtil.encryptString(deviceIdget, context);

            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
            String getimei = EncryptionUtil.encryptString(deviceIDAndroid, context);

            String simSerial = CommonUtils.getSimSerialNumber(context);
            String getsimserialnumber = EncryptionUtil.encryptString(simSerial, context);

            if (locationKendraDataModel.getScope().equals("BE") && locationKendraDataModel.getId().length() == 9) {
                myHolder.txtName.setText(locationKendraDataModel.getType() + " - " + locationKendraDataModel.getId());
                myHolder.txtName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String getid = dataSet.get(position).getId();
                        String getname = dataSet.get(position).getType();
                        String getscop = dataSet.get(position).getScope();


                        String userId = EncryptionUtil.encryptString(connection.getVkid(), context);
                        String vkid = EncryptionUtil.encryptString(getid, context);
                        Toast.makeText(context, getid, Toast.LENGTH_SHORT).show();
                        // System.out.println("Item click Adapter" + getid + getname + getscop);
                        new AsyncGetmyVakrangeeKendraTimingsResponseParticular(v.getContext(), getid).execute(userId, vkid, getTokenId, getimei, getdeviceid, getsimserialnumber);

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

                    System.out.println("myVakrangeeKendraLocation[0]: " + myVakrangeeKendraLocation[0]);
                    System.out.println("myVakrangeeKendraLocation[1]: " + myVakrangeeKendraLocation[1]);
                    System.out.println("myVakrangeeKendraLocation[2]: " + myVakrangeeKendraLocation[2]);
                    System.out.println("myVakrangeeKendraLocation[3]: " + myVakrangeeKendraLocation[3]);
                    System.out.println("myVakrangeeKendraLocation[4]: " + myVakrangeeKendraLocation[4]);
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
                        // Toast.makeText(context, "Sorry", Toast.LENGTH_SHORT).show();
                    }

                    System.out.println("myVakrangeeKendraLocation[0]: " + myVakrangeeKendraLocation[0]);
                    System.out.println("myVakrangeeKendraLocation[1]: " + myVakrangeeKendraLocation[1]);
                    System.out.println("myVakrangeeKendraLocation[2]: " + myVakrangeeKendraLocation[2]);
                    System.out.println("myVakrangeeKendraLocation[3]: " + myVakrangeeKendraLocation[3]);
                    System.out.println("myVakrangeeKendraLocation[4]: " + myVakrangeeKendraLocation[4]);

                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    Connection connection = new Connection(context);
                    final String getVkid = EncryptionUtil.encryptString(connection.getVkid(), context);
                    final String getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), context);

                    String deviceIdget = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    String getdeviceid = EncryptionUtil.encryptString(deviceIdget, context);

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
                    String getimei = EncryptionUtil.encryptString(deviceIDAndroid, context);

                    String simSerial = CommonUtils.getSimSerialNumber(context);
                    String getsimserialnumber = EncryptionUtil.encryptString(simSerial, context);

                    String getid = dataSet.get(itpos).getId();
                    String getname = dataSet.get(itpos).getType();
                    String getscop = dataSet.get(itpos).getScope();
                    final String scop = EncryptionUtil.encryptString(getscop, context);
                    final String id = EncryptionUtil.encryptString(getid, context);

//
                    new AsyncgetLocationDetails(v.getContext(), myVakrangeeKendraLocation).execute(getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber, scop, id);


                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        // myHolder.balance.setText("" + constants.BALANCE);


    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtType;
        private String mItem;
        private TextView mTextView;
        RelativeLayout realtivelayout;

        public void setText(String item) {
            mItem = item;
            mTextView.setText(item);
        }


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.name);
            txtType = (TextView) itemView.findViewById(R.id.type);
            realtivelayout = (RelativeLayout) itemView.findViewById(R.id.realtivelayout);

            //;


        }


    }


}
