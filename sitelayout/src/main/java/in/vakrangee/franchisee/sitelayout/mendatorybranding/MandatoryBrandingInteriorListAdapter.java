package in.vakrangee.franchisee.sitelayout.mendatorybranding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.Hex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.model.MandatoryBrandingList;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.model.MandatoryImageList;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.commongui.CustomAttributeDialog;
import in.vakrangee.supercore.franchisee.model.SiteReadinessCheckListDto;
import in.vakrangee.supercore.franchisee.task.AsyncSiteReadinessGetAttributeDetail;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;

public class MandatoryBrandingInteriorListAdapter extends RecyclerView.Adapter<MandatoryBrandingInteriorListAdapter.MyViewHolder> {

    private static final String TAG = "CategoryListAdapter";
    //private List<SiteReadinessCategoryDto> categoryList;
    private List<MandatoryBrandingList> categoryList= new ArrayList<>();
    private List<MandatoryImageList> vkImageList = new ArrayList<>();

    //private List<SiteReadinessCheckListDto> siteCompletionCheckList;
    private Context context;
    private View itemView;
    private DeprecateHandler deprecateHandler;
    //private MandatoryInteriorCellAdapter interiorChecklistAdapter;
    private ISiteCheckHandler iSiteCheckHandler;
    private String DOWN_ARROW, UP_ARROW;
    private LinearLayout previousRecyclerView;
    private TextView textViewPrevious;
    private int refreshedPosition = -1;
    private String ATTRIBUTE_TYPE;
    private String imageByte;
    private boolean IsFranchisee = false;
    private CustomAttributeDialog customAttributeDialog = null;
    private AsyncSiteReadinessGetAttributeDetail asyncSiteReadinessGetAttributeDetail = null;
    private  Bitmap bitmap;
    public interface ISiteCheckHandler {
        void cameraClick(int parentPosition, int position, MandatoryBrandingList siteReadinessCheckListDto);
    }


    public MandatoryBrandingInteriorListAdapter(Context context, boolean IsFranchisee, String type, List<MandatoryBrandingList> categoryList,List<MandatoryImageList> vkImageList, ISiteCheckHandler iSiteCheckHandler) {
        this.context = context;
        this.IsFranchisee = IsFranchisee;
        this.ATTRIBUTE_TYPE = type;
        this.categoryList = categoryList;
        this.vkImageList = vkImageList;
        this.iSiteCheckHandler = iSiteCheckHandler;
        deprecateHandler = new DeprecateHandler(context);
        DOWN_ARROW = context.getResources().getString(R.string.fa_down_arrow);
        UP_ARROW = context.getResources().getString(R.string.fa_up_arrow);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCategoryLayoutIcon;
        public TextView txtCategoryName;
        public TextView txtCategoryStatus;
        public TextView txtCategoryLayoutArrow, tvChildTitle, tvChildSubTitle;
        public LinearLayout layoutItemContent;
        public LinearLayout layoutCategoryClick;
        public TextView etRemark;
        public CheckBox cbCompleted;
        public ImageView ivCapture, ivInfo, ivSuccessTick;

        public MyViewHolder(View view) {
            super(view);
            txtCategoryLayoutIcon = view.findViewById(R.id.txtCategoryLayoutIcon);
            layoutCategoryClick = view.findViewById(R.id.layoutCategoryClick);
            txtCategoryName = view.findViewById(R.id.txtCategoryLayoutTitle);
            txtCategoryStatus = view.findViewById(R.id.txtCategoryLayoutStatus);
            txtCategoryLayoutArrow = view.findViewById(R.id.txtCategoryLayoutArrow);
            layoutItemContent = view.findViewById(R.id.layoutItemContent);
            ivCapture = view.findViewById(R.id.ivCapture);
            cbCompleted = view.findViewById(R.id.cbCompleted);
            ivSuccessTick = view.findViewById(R.id.ivSuccessTick);
            ivInfo = view.findViewById(R.id.ivInfo);
            etRemark = view.findViewById(R.id.etRemark);
            tvChildTitle = view.findViewById(R.id.tvChildTitle);
            tvChildSubTitle = view.findViewById(R.id.tvChildSubTitle);

        }
    }

    @Override
    public MandatoryBrandingInteriorListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interior_mandatory, parent, false);
        return new MandatoryBrandingInteriorListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MandatoryBrandingInteriorListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MandatoryBrandingList dto = categoryList.get(position);
        // holder.txtCategoryName.setText(dto.kendra_sub_category_name);
        holder.layoutItemContent.setTag(position);
        if (!TextUtils.isEmpty(dto.kendra_sub_category_name)) {
            GUIUtils.CompulsoryMark(holder.txtCategoryName, dto.kendra_sub_category_name + " ");
        } else {
            holder.tvChildTitle.setText(dto.kendra_sub_category_name);
        }
        //Check list Recycler view
       /* interiorChecklistAdapter = new MandatoryInteriorCellAdapter(context, IsFranchisee, ATTRIBUTE_TYPE, position, dto.checkList, new MandatoryInteriorCellAdapter.ISiteCheckHandler() {
            @Override
            public void cameraClick(int parentPosition, int position, SiteReadinessCheckListDto siteReadinessCheckListDto) {
                iSiteCheckHandler.cameraClick(parentPosition, position, siteReadinessCheckListDto);
            }
        });*/

        if ((vkImageList != null && !(vkImageList.size() == 0))) {
            if (vkImageList.get(position).getimgBytes() != null && !vkImageList.get(position).getimgBytes().isEmpty()) {
                String allImages = vkImageList.get(position).getimgBytes();
                if (allImages != null || !allImages.isEmpty()) {
                    bitmap = CommonUtils.StringToBitMap(allImages);
                    holder.ivCapture.setImageBitmap(bitmap);
                } else {
                    holder.ivCapture.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp));
                }

            }
        } else {
            holder.ivCapture.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp));
        }

        if (position == refreshedPosition) {
            holder.layoutItemContent.setVisibility(View.VISIBLE);
            setFontawesomeIcon(holder.txtCategoryLayoutArrow, UP_ARROW);

            textViewPrevious = holder.txtCategoryLayoutArrow;
            previousRecyclerView = holder.layoutItemContent;
        } else {
            holder.layoutItemContent.setVisibility(View.GONE);
            setFontawesomeIcon(holder.txtCategoryLayoutArrow, DOWN_ARROW);
        }



        //Change color of status
        /*if (!TextUtils.isEmpty(dto.getStatus())) {
            switch (dto.getStatus()) {
                case "0":
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.orange));
                    break;
                case "1":
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.ired));
                    break;
                case "2":
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.green));
                    break;

                default:
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.orange));
                    break;
            }
        } else {*/
        holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.orange));
        // }

        //TExtView Click
        holder.layoutCategoryClick.setTag(position);
        holder.layoutCategoryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                ViewGroup topParent = (ViewGroup) view.getParent();
                LinearLayout layoutItemContent = topParent.findViewById(R.id.layoutItemContent);
                TextView txtArrow = view.findViewById(R.id.txtCategoryLayoutArrow);

                if (layoutItemContent.getVisibility() == View.VISIBLE) {
                    layoutItemContent.setVisibility(View.GONE);
                    setFontawesomeIcon(txtArrow, DOWN_ARROW);

                } else {
                    layoutItemContent.setVisibility(View.VISIBLE);
                    setFontawesomeIcon(txtArrow, UP_ARROW);

                    collapseOtherViews(pos, holder.txtCategoryLayoutArrow, holder.layoutItemContent);
                }

            }
        });
        holder.txtCategoryName.setTag(position);
        holder.ivCapture.setTag(position);
    /*   *//* imageByte = dto.getImageByte();
         bitmap = CommonUtils.StringToBitMap(imageByte);
        holder.ivCapture.setImageBitmap(bitmap)*//*;*/
        //Remarks
//        CommonUtils.applyInputFilter(holder.etRemark, "\"~#^|$%&*!'");
//        holder.etRemark.setText(siteCompletionCheckListDto.getRemarks());
//        holder.etRemark.addTextChangedListener(new MyTextWatcher(itemView));
//        holder.etRemark.setTag(siteCompletionCheckListDto);

        //Element Info
        holder.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MandatoryBrandingList checkListDto = (MandatoryBrandingList) view.getTag();
                //  displayElementDetail(checkListDto.getKendra_sub_category_name(), ATTRIBUTE_TYPE, checkListDto.getNextgen_site_work_kendra_sub_category_id());

            }
        });


        holder.ivCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
               // if (!dto.isChecked()) {
                    iSiteCheckHandler.cameraClick(position, pos, dto);
              /*  } else {
                    String msg = "";
                    if (IsFranchisee)
                        msg = context.getResources().getString(R.string.alert_msg_please_select_completed);
                    else
                        msg = context.getResources().getString(R.string.alert_msg_please_select_verified_rejected);
                    AlertDialogBoxInfo.alertDialogShow(context, msg);
                }*/
            }
        });

        if (vkImageList != null && !(vkImageList.size() == 0)) {
            if (vkImageList.get(position).getStatus().equals("1")) {
                holder.etRemark.setText("Approve");
                holder.ivCapture.setClickable(false);
                holder.ivSuccessTick.setVisibility(View.VISIBLE);

            } else if (vkImageList.get(position).getStatus().equals("2")) {
                holder.etRemark.setText("Reject");
                holder.ivCapture.setClickable(false);
                holder.ivSuccessTick.setVisibility(View.GONE);

            } else if (vkImageList.get(position).getStatus().equals("3")) {
                holder.etRemark.setText("Hold");
                holder.ivCapture.setClickable(false);
                holder.ivSuccessTick.setVisibility(View.GONE);

            } else if (vkImageList.get(position).getStatus().equals("4")) {
                holder.etRemark.setText("Send Back for correction");
                holder.ivCapture.setClickable(true);
                holder.ivSuccessTick.setVisibility(View.GONE);

            } else if (vkImageList.get(position).getStatus().equals("5")) {
                holder.etRemark.setText("Re Submitted by Franchise");
                holder.ivCapture.setClickable(false);
                holder.ivSuccessTick.setVisibility(View.GONE);

            } else if (vkImageList.get(position).getStatus().equals("6")) {
                holder.etRemark.setText("Re Approve");
                holder.ivCapture.setClickable(false);
                holder.ivSuccessTick.setVisibility(View.VISIBLE);

            } else if (vkImageList.get(position).getStatus().equals("0")) {
                holder.etRemark.setText("Submitted by Franchise");
                holder.ivCapture.setClickable(true);
                holder.ivSuccessTick.setVisibility(View.GONE);

            }
        }
    }

    public void collapseOtherViews(int pos, TextView txtCategoryLayoutArrow, LinearLayout recyclerView) {
        if (previousRecyclerView != null && textViewPrevious != null) {
            int prevPos = (int) previousRecyclerView.getTag();
            if (prevPos != pos) {
                previousRecyclerView.setVisibility(View.GONE);
                setFontawesomeIcon(textViewPrevious, DOWN_ARROW);
                textViewPrevious = txtCategoryLayoutArrow;
                previousRecyclerView = recyclerView;
            }
        }
    }

    public void updateRefreshedPosition(int pos) {
        refreshedPosition = pos;
    }

    public void GetImageClick(String imageBytes) {
        imageByte = imageBytes;
      //  setImageClick(imageByte);
    }

    public void disableOtherViews(int selectedPos) {
        int size = getItemCount();
        for (int i = 0; i < size; i++) {
            getItemId(selectedPos);

        }
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setFontawesomeIcon(TextView textView, String icon) {
        textView.setText(icon);
        textView.setTextSize(25);
        textView.setTextColor(deprecateHandler.getColor(R.color.white));
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
        }

        public void afterTextChanged(Editable s) {
            String data = s.toString().trim();
            EditText editText = (EditText) view.findViewById(R.id.editTextRemarks);
            SiteReadinessCheckListDto obj = (SiteReadinessCheckListDto) editText.getTag();
            obj.setRemarks(data);
        }
    }

    public void displayElementDetail(final String elementName, String elementType, String elementDetailId) {

        asyncSiteReadinessGetAttributeDetail = new AsyncSiteReadinessGetAttributeDetail(context, elementType, elementDetailId, new AsyncSiteReadinessGetAttributeDetail.Callback() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process Result
                    Log.e(TAG, "Result : " + result);
                    StringTokenizer st1 = new StringTokenizer(result, "|");
                    String key = st1.nextToken();
                    String data = st1.nextToken();

                    //TODO: Comment below line
                    //key = "OKAY";
                    //data = "[ {\"attr_name\":\"Length\",\"attr_value\":\"Blue\",\"attr_img_id\":6716,\"uom\":\"cm\"},{\"attr_name\":\"Width\",\"attr_value\":\"Red\",\"attr_img_id\":6716,\"uom\":\"cm\"}]";

                    if (key.equalsIgnoreCase("OKAY")) {
                        showElementAttributeDialog(elementName, data);

                    } else {
                        Toast toast = Toast.makeText(context, "No Information Found.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSiteReadinessGetAttributeDetail.execute("");
    }

    public void showElementAttributeDialog(String elementName, String data) {
        if (customAttributeDialog != null && customAttributeDialog.isShowing()) {
            customAttributeDialog.setDialogTitle(elementName);
            customAttributeDialog.refresh(data);
            return;
        }

        if (data != null) {
            customAttributeDialog = new CustomAttributeDialog(context, data);
            customAttributeDialog.show();
            customAttributeDialog.setCancelable(false);
            customAttributeDialog.setDialogTitle(elementName);

        } else {
            Toast toast = Toast.makeText(context, "No Information Found.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

}
