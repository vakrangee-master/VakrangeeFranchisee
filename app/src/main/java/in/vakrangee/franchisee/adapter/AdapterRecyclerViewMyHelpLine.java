package in.vakrangee.franchisee.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.helpline.MyHelpLineDto;
import in.vakrangee.supercore.connect.Constants;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.model.WalletData;
import in.vakrangee.supercore.franchisee.utils.CircleTransformRecyclerviewImage;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class AdapterRecyclerViewMyHelpLine extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<WalletData> data = Collections.emptyList();
    in.vakrangee.supercore.connect.Constants current;
    int currentPos = 0;
    private DeprecateHandler deprecateHandler;
    private IActionHandler iActionHandler;

    public interface IActionHandler {

        public void callNow(String mobileNo);

        public void sendSMS(String mobileNo);

        public void sendEmail(String emailId);

    }

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterRecyclerViewMyHelpLine(Context context, List<WalletData> data,IActionHandler iActionHandler) {
        this.context = context;
        deprecateHandler = new DeprecateHandler(context);
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.iActionHandler = iActionHandler;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewadpatermyhelpline, parent, false);
        //view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewadpatermyhelpline, null);
        MyHolder holder = new MyHolder(view);


        //return new MyHolder(parent);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        final WalletData constants = data.get(position);
        myHolder.type.setText(constants.TYPE);
        myHolder.name.setText(TextUtils.isEmpty(constants.NAME) ? " - " : constants.NAME);

        setFontawesomeIcon(myHolder.txtMobileNumberIcon, context.getResources().getString(R.string.fa_call));
        setFontawesomeIcon(myHolder.txtSMSIcon, context.getResources().getString(R.string.fa_msg));
        setFontawesomeIcon(myHolder.txtEmailIdIcon, context.getResources().getString(R.string.fa_mail));

        //Mobile Number
        if (!TextUtils.isEmpty(constants.MOBILENUMBER)) {
            myHolder.layoutMobileNo.setVisibility(View.VISIBLE);
        } else {
            myHolder.layoutMobileNo.setVisibility(View.GONE);
        }

        //SMS
        if (!TextUtils.isEmpty(constants.Is_SMS) && constants.Is_SMS.equalsIgnoreCase("1")) {
            myHolder.layoutSMS.setVisibility(View.VISIBLE);
        } else {
            myHolder.layoutSMS.setVisibility(View.GONE);
        }

        //email id
        if (!TextUtils.isEmpty(constants.EMAILID)) {
            myHolder.layoutEmailId.setVisibility(View.VISIBLE);
        } else {
            myHolder.layoutEmailId.setVisibility(View.GONE);
        }

        myHolder.layoutMobileNo.setTag(constants.MOBILENUMBER);
        myHolder.layoutMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHanndler.bubbleAnimation(context, view);

                String mobNo = (String) view.getTag();
                iActionHandler.callNow(mobNo);
            }
        });

        myHolder.layoutSMS.setTag(constants.MOBILENUMBER);
        myHolder.layoutSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHanndler.bubbleAnimation(context, view);

                String mobNo = (String) view.getTag();
                iActionHandler.sendSMS(mobNo);
            }
        });

        myHolder.layoutEmailId.setTag(constants.EMAILID);
        myHolder.layoutEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHanndler.bubbleAnimation(context, view);

                String emaIlId = (String) view.getTag();
                iActionHandler.sendEmail(emaIlId);
            }
        });

        final String imageUrl = Constants.DownloadImageUrl + constants.IMAGE;
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.proflie).transform(new CircleTransformRecyclerviewImage())
                .error(R.drawable.proflie)      // optional
                .resize(200, 200)                        // optional
                //.rotate(90)                             // optional
                .into(myHolder.image);

    }

    public void setFontawesomeIcon(TextView textView, String icon) {

        textView.setText(icon);
        textView.setTextSize(14);
        textView.setTextColor(deprecateHandler.getColor(R.color.text_color));
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.NORMAL);


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView type;

        TextView name;
        TextView dr, txtMobileNumberIcon, txtSMSIcon, txtEmailIdIcon;
        ImageView image;
        private LinearLayout layoutMobileNo, layoutSMS, layoutEmailId;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardViewhelpline);
            type = (TextView) itemView.findViewById(R.id.type);

            name = (TextView) itemView.findViewById(R.id.name);
            txtMobileNumberIcon = itemView.findViewById(R.id.txtMobileNumberIcon);
            txtSMSIcon = itemView.findViewById(R.id.txtSMSIcon);
            txtEmailIdIcon = itemView.findViewById(R.id.txtEmailIdIcon);
            layoutMobileNo = itemView.findViewById(R.id.layoutMobileNo);
            layoutSMS = itemView.findViewById(R.id.layoutSMS);
            layoutEmailId = itemView.findViewById(R.id.layoutEmailId);

            image = (ImageView) itemView.findViewById(R.id.image);


        }

    }
}
