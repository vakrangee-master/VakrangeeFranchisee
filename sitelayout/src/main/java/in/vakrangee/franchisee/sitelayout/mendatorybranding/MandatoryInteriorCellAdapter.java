package in.vakrangee.franchisee.sitelayout.mendatorybranding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.commongui.CustomAttributeDialog;
import in.vakrangee.supercore.franchisee.commongui.CustomCorrectionRemarksDialog;
import in.vakrangee.supercore.franchisee.model.SiteReadinessCheckListDto;
import in.vakrangee.supercore.franchisee.task.AsyncSiteReadinessGetAttributeDetail;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.EditTextWatcher;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;

public class MandatoryInteriorCellAdapter extends RecyclerView.Adapter<MandatoryInteriorCellAdapter.MyViewHolder> {

    private static final String TAG = "SiteReadinessInteriorCellAdapter";
    private List<SiteReadinessCheckListDto> siteCompletionCheckList;
    private Context context;
    private MandatoryInteriorCellAdapter.ISiteCheckHandler iSiteCheckHandler;
    private View itemView;
    private DeprecateHandler deprecateHandler;
    private int parentPosition;
    private AsyncSiteReadinessGetAttributeDetail asyncSiteReadinessGetAttributeDetail = null;
    private String ATTRIBUTE_TYPE;
    private CustomAttributeDialog customAttributeDialog = null;
    private CustomCorrectionRemarksDialog customCorrectionRemarksDialog = null;
    private boolean IsFranchisee = false;

    public interface ISiteCheckHandler {
        public void cameraClick(int parentPosition, int position, SiteReadinessCheckListDto siteReadinessCheckListDto);
    }

    public MandatoryInteriorCellAdapter(Context context, boolean IsFranchisee, String type, int position, List<SiteReadinessCheckListDto> siteCompletionCheckList, MandatoryInteriorCellAdapter.ISiteCheckHandler iSiteCheckHandler) {
        this.context = context;
        this.IsFranchisee = IsFranchisee;
        this.ATTRIBUTE_TYPE = type;
        this.parentPosition = position;
        this.siteCompletionCheckList = siteCompletionCheckList;
        this.iSiteCheckHandler = iSiteCheckHandler;
        deprecateHandler = new DeprecateHandler(context);
 }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtElementName, txtSubElementName, txtRemarks, txtWarningIcon, txtWarningDetail;
        public ImageView imgElement, imgOverlay;
        public CheckBox checkBoxCompleted;
        public EditText editTextRemarks, editTextLength, editTextWidth;
        public LinearLayout layoutParent;
        public LinearLayout layoutMainSignBoard, layoutWarning;
        public Spinner spinnerLengthFeet, spinnerLengthInch, spinnerWidthFeet, spinnerWidthInch;
        public TableRow tableRowCompleted;
        public LinearLayout layoutStatus;
        public RadioGroup radioGroupStatus;
        public RadioButton radioButtonStatusVerified, radioButtonStatusRejected;

        public MyViewHolder(View view) {
            super(view);
            txtElementName = view.findViewById(R.id.txtElementName);
            txtSubElementName = view.findViewById(R.id.txtSubElementName);
            imgElement = view.findViewById(R.id.imgElementImage);
            checkBoxCompleted = view.findViewById(R.id.checkboxCompleted);
            editTextRemarks = view.findViewById(R.id.editTextRemarks);
            layoutParent = view.findViewById(R.id.layoutParent);
            layoutMainSignBoard = view.findViewById(R.id.layoutMainSignBoard);
            editTextLength = view.findViewById(R.id.editTextLength);
            editTextWidth = view.findViewById(R.id.editTextWidth);
            spinnerLengthFeet = view.findViewById(R.id.spinnerLengthFeet);
            spinnerLengthInch = view.findViewById(R.id.spinnerLengthInch);
            spinnerWidthFeet = view.findViewById(R.id.spinnerWidthFeet);
            spinnerWidthInch = view.findViewById(R.id.spinnerWidthInch);
            txtWarningIcon = view.findViewById(R.id.txtWarningIcon);
            layoutWarning = view.findViewById(R.id.layoutWarning);
            imgOverlay = view.findViewById(R.id.imgOverlay);
            tableRowCompleted = view.findViewById(R.id.tableRowCompleted);
            layoutStatus = view.findViewById(R.id.layoutStatus);
            radioGroupStatus = view.findViewById(R.id.radioGroupStatus);
            radioButtonStatusVerified = view.findViewById(R.id.radioButtonStatusVerified);
            radioButtonStatusRejected = view.findViewById(R.id.radioButtonStatusRejected);
            txtWarningDetail = view.findViewById(R.id.txtWarningDetail);
        }
    }

    @Override
    public MandatoryInteriorCellAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interior_cell, parent, false);
        return new MandatoryInteriorCellAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MandatoryInteriorCellAdapter.MyViewHolder holder, int position) {

        final SiteReadinessCheckListDto siteCompletionCheckListDto = siteCompletionCheckList.get(position);

        String mandatory = siteCompletionCheckListDto.getMandatory();
        if (!TextUtils.isEmpty(mandatory) && mandatory.equalsIgnoreCase("1")) {
            GUIUtils.CompulsoryMark(holder.txtElementName, siteCompletionCheckListDto.getElementName() + " ");
        } else {
            holder.txtElementName.setText(siteCompletionCheckListDto.getElementName());
        }
        holder.txtSubElementName.setText(siteCompletionCheckListDto.getDesc());

        //Remarks
        CommonUtils.applyInputFilter(holder.editTextRemarks, "\"~#^|$%&*!'");
        holder.editTextRemarks.setText(siteCompletionCheckListDto.getRemarks());
        holder.editTextRemarks.addTextChangedListener(new MandatoryInteriorCellAdapter.MyTextWatcher(itemView));
        holder.editTextRemarks.setTag(siteCompletionCheckListDto);

        //Element Info
        holder.txtElementName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteReadinessCheckListDto checkListDto = (SiteReadinessCheckListDto) view.getTag();
                displayElementDetail(checkListDto.getElementName(), ATTRIBUTE_TYPE, checkListDto.getId());

            }
        });

        //Image Element
        holder.imgElement.setTag(position);
        String imageBase64 = siteCompletionCheckListDto.getElementImageBase64();
        if (imageBase64 != null) {
            Bitmap bitmap = CommonUtils.StringToBitMap(imageBase64);
            holder.imgElement.setImageBitmap(bitmap);

        } else {
            holder.imgElement.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp));
        }

      /*  if (!TextUtils.isEmpty(siteCompletionCheckListDto.getImage_id())) {
            String gstUrl = Constants.DownloadImageUrl + siteCompletionCheckListDto.getImage_id();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(holder.imgElement);
        } else {
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(holder.imgElement);
        }*/

        //Set Click Listener to Camera
        holder.imgElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                SiteReadinessCheckListDto dto = siteCompletionCheckList.get(pos);
                if (!TextUtils.isEmpty(dto.getCompleted()) && (dto.getCompleted().equalsIgnoreCase("1") || dto.getCompleted().equalsIgnoreCase("2"))) {
                    iSiteCheckHandler.cameraClick(parentPosition, pos, dto);
                } else {
                    String msg = "";
                    if (IsFranchisee)
                        msg = context.getResources().getString(R.string.alert_msg_please_select_completed);
                    else
                        msg = context.getResources().getString(R.string.alert_msg_please_select_verified_rejected);
                    AlertDialogBoxInfo.alertDialogShow(context, msg);
                }
            }
        });

        //Main Signboard Visibility
        if (siteCompletionCheckListDto.getElementName().equalsIgnoreCase("Main Signboard Frame")) {
            holder.layoutMainSignBoard.setVisibility(View.VISIBLE);
        } else {
            holder.layoutMainSignBoard.setVisibility(View.GONE);
        }
        //when focusable true , if item click in spinner
        spinner_focusablemode(holder.spinnerLengthFeet);
        spinner_focusablemode(holder.spinnerLengthInch);
        spinner_focusablemode(holder.spinnerWidthFeet);
        spinner_focusablemode(holder.spinnerWidthInch);



        //Length
        holder.editTextLength.setText(siteCompletionCheckListDto.getLength());
        holder.editTextLength.addTextChangedListener(new EditTextWatcher(new EditTextWatcher.IEditextData() {
            @Override
            public void getEditTextData(String data) {
                siteCompletionCheckListDto.setLength(data);
            }
        }));
        holder.editTextLength.setTag(siteCompletionCheckListDto);

        //Width
        holder.editTextWidth.setText(siteCompletionCheckListDto.getWidth());
        holder.editTextWidth.addTextChangedListener(new EditTextWatcher(new EditTextWatcher.IEditextData() {
            @Override
            public void getEditTextData(String data) {
                siteCompletionCheckListDto.setWidth(data);
            }
        }));
        holder.editTextWidth.setTag(siteCompletionCheckListDto);

        //Completed Status
        if (TextUtils.isEmpty(siteCompletionCheckListDto.getCompleted()) || siteCompletionCheckListDto.getCompleted().equalsIgnoreCase("0")) {
            holder.checkBoxCompleted.setChecked(false);
        } else {
            holder.checkBoxCompleted.setChecked(true);
        }

        //Checkbox Completed
        holder.checkBoxCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                if (IsChecked) {
                    siteCompletionCheckListDto.setCompleted("1");

                } else {
                    siteCompletionCheckListDto.setCompleted("0");
                }
            }
        });

        //Disable Edit
        if (!TextUtils.isEmpty(siteCompletionCheckListDto.getStatus()) && (siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.APPROVED_BY_RM) || siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.ON_HOLD_BY_RM))) {
            disableViews(holder.layoutParent, holder.checkBoxCompleted, holder.layoutStatus, false);
            //GUIUtils.setViewAndChildrenEnabled(holder.layoutParent, false);
        } else {
            disableViews(holder.layoutParent, holder.checkBoxCompleted, holder.layoutStatus, true);
            //GUIUtils.setViewAndChildrenEnabled(holder.layoutParent, true);
        }

        //Visible Warning layout
        if (!TextUtils.isEmpty(siteCompletionCheckListDto.getStatus()) && (siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.APPROVED_BY_RM) || siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.SEND_BACK_FOR_CORRECTION_BY_RM)
                || siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.ON_HOLD_BY_RM) || siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.REJECTED_BY_FIELD_TEAM))) {
            holder.layoutWarning.setVisibility(View.VISIBLE);
            if (siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.APPROVED_BY_RM)) {
                holder.layoutWarning.setEnabled(true);
                holder.txtWarningIcon.setText(context.getString(R.string.fa_circle_check));
                holder.txtWarningIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                holder.txtWarningIcon.setTextColor(deprecateHandler.getColor(R.color.green));

            } else if (siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.ON_HOLD_BY_RM)) {
                holder.layoutWarning.setEnabled(true);
                holder.txtWarningIcon.setText(context.getString(R.string.fa_circle_question));
                holder.txtWarningIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                holder.txtWarningIcon.setTextColor(deprecateHandler.getColor(R.color.orange));

            } else {
                holder.layoutWarning.setEnabled(true);
                holder.txtWarningIcon.setTextColor(deprecateHandler.getColor(R.color.ired));
                //holder.txtWarningIcon.setText(context.getString(R.string.fa_warning));
                holder.txtWarningIcon.setText(context.getString(R.string.fa_circle_cross));
                holder.txtWarningIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
            }
        } else {
            holder.layoutWarning.setVisibility(View.GONE);
        }

        //Display Rejected Stamp Image
        boolean IsRejected = false;
        if (!TextUtils.isEmpty(siteCompletionCheckListDto.getStatus()) && (siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.SEND_BACK_FOR_CORRECTION_BY_RM) || siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.REJECTED_BY_FIELD_TEAM)))
            IsRejected = true;

        if (IsRejected && !siteCompletionCheckListDto.isChangedPhoto()) {
            holder.imgOverlay.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(siteCompletionCheckListDto.getStatus()) && siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.REJECTED_BY_FIELD_TEAM)) {
                holder.imgOverlay.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected_by_be_de));
            } else {
                holder.imgOverlay.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected));
            }

        } else {
            holder.imgOverlay.setVisibility(View.GONE);
        }

        if (siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.APPROVED_BY_RM)) {
            holder.txtWarningDetail.setVisibility(View.GONE);

        } else if (siteCompletionCheckListDto.getStatus().equalsIgnoreCase(Constants.ON_HOLD_BY_RM)) {
            holder.txtWarningDetail.setVisibility(View.GONE);

        } else {
            holder.txtWarningDetail.setVisibility(View.VISIBLE);
            holder.txtWarningDetail.setText(siteCompletionCheckListDto.getVlRemarks());
        }

        holder.layoutWarning.setTag(siteCompletionCheckListDto);
        holder.layoutWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteReadinessCheckListDto checkListDto = (SiteReadinessCheckListDto) view.getTag();
                if (checkListDto.getStatus().equalsIgnoreCase(Constants.APPROVED_BY_RM)) {
                    Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();

                } else if (checkListDto.getStatus().equalsIgnoreCase(Constants.ON_HOLD_BY_RM)) {
                    Toast.makeText(context, "On Hold", Toast.LENGTH_SHORT).show();

                } else {
                    //holder.txtWarningDetail.setVisibility(View.VISIBLE);
                    //holder.txtWarningDetail.setText(checkListDto.getVlRemarks());
                    //showRemarksDialog(checkListDto.getElementName(), checkListDto.getVlRemarks());
                }
            }
        });

        //Set Completed Layout
        if (IsFranchisee) {
            holder.tableRowCompleted.setVisibility(View.VISIBLE);
            holder.layoutStatus.setVisibility(View.GONE);
        } else {
            holder.tableRowCompleted.setVisibility(View.GONE);
            holder.layoutStatus.setVisibility(View.VISIBLE);
        }

        //Call Received
        holder.radioGroupStatus.setTag(siteCompletionCheckListDto);
        holder.radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SiteReadinessCheckListDto checkListDto = (SiteReadinessCheckListDto) group.getTag();
                if (checkedId == R.id.radioButtonStatusVerified) {
                    checkListDto.setCompleted("1");
                    setRejectedImageByStatus(checkListDto, holder.imgOverlay);
                } else if (checkedId == R.id.radioButtonStatusRejected) {
                    checkListDto.setCompleted("2");
                    setRejectedImageByStatus(checkListDto, holder.imgOverlay);
                }
            }
        });
        if (siteCompletionCheckListDto.getCompleted() != null && !IsFranchisee) {
            if (siteCompletionCheckListDto.getCompleted().equals("1")) {
                RadioButton radioButton = holder.radioGroupStatus.findViewById(R.id.radioButtonStatusVerified);
                radioButton.setChecked(true);

            } else if (siteCompletionCheckListDto.getCompleted().equals("2")) {
                RadioButton radioButton = holder.radioGroupStatus.findViewById(R.id.radioButtonStatusRejected);
                radioButton.setChecked(true);
            }
        }
        holder.txtElementName.setTag(siteCompletionCheckListDto);
    }

    private void setRejectedImageByStatus(SiteReadinessCheckListDto checkListDto, ImageView imgView) {
        if (TextUtils.isEmpty(checkListDto.getCompleted()) || TextUtils.isEmpty(checkListDto.getElementImageBase64())) {
            imgView.setVisibility(View.GONE);
            return;
        }

        //Verified
        if (checkListDto.getCompleted().equalsIgnoreCase("1") && checkListDto.getStatus().equalsIgnoreCase(Constants.SEND_BACK_FOR_CORRECTION_BY_RM)) {
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected));

        } else if (checkListDto.getCompleted().equalsIgnoreCase("2") && checkListDto.getStatus().equalsIgnoreCase(Constants.SEND_BACK_FOR_CORRECTION_BY_RM)) {
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected));

        } else if (checkListDto.getCompleted().equalsIgnoreCase("2") && checkListDto.getStatus().equalsIgnoreCase(Constants.REJECTED_BY_FIELD_TEAM)) {
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected_by_be_de));

        } else if (checkListDto.getCompleted().equalsIgnoreCase("2")) {
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected_by_be_de));

        } else {
            imgView.setVisibility(View.GONE);
        }
    }

    private void spinner_focusablemode(Spinner stateSpinner) {
        stateSpinner.setFocusable(true);
        stateSpinner.setFocusableInTouchMode(true);
    }

    @Override
    public int getItemCount() {
        return siteCompletionCheckList.size();
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

    //Set Values to spinner
    private int getIndexText(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private int feetAndInchesToInches(int feet, int inch) {
        int inches = (feet * 12) + inch;
        return inches;
    }

    private String inchesTofeetAndInches(String inches) {
        int feet = Integer.valueOf(inches) / 12;
        int leftover = Integer.valueOf(inches) % 12;
        return feet + "|" + leftover;
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

    public void showRemarksDialog(String elementName, String data) {
        if (customCorrectionRemarksDialog != null && customCorrectionRemarksDialog.isShowing()) {
            customCorrectionRemarksDialog.setDialogTitle(elementName);
            customCorrectionRemarksDialog.refresh(data);
            return;
        }

        if (data != null) {
            customCorrectionRemarksDialog = new CustomCorrectionRemarksDialog(context, data);
            customCorrectionRemarksDialog.show();
            customCorrectionRemarksDialog.setCancelable(false);
            customCorrectionRemarksDialog.setDialogTitle(elementName);

        } else {
            Toast toast = Toast.makeText(context, "No Information Found.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void disableViews(LinearLayout layoutParent, CheckBox checkBox, LinearLayout layoutStatus, boolean IsEnabled) {
        GUIUtils.setViewAndChildrenEnabled(layoutParent, IsEnabled);
        checkBox.setEnabled(IsEnabled);
        layoutStatus.setEnabled(IsEnabled);
    }
}
