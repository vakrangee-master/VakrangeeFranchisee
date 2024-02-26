package in.vakrangee.franchisee.workinprogress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.supercore.franchisee.commongui.CustomAttributeDialog;
import in.vakrangee.supercore.franchisee.task.AsyncSiteReadinessGetAttributeDetail;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;

public class WIPInteriorCellStatusAdapter extends RecyclerView.Adapter<WIPInteriorCellStatusAdapter.MyViewHolder> {

    private static final String TAG = "WIPInteriorCellStatusAdapter";
    private List<WIPCheckListDto> siteCompletionCheckList;
    private Context context;
    private WIPInteriorCellStatusAdapter.ISiteCheckHandler iSiteCheckHandler;
    private View itemView;
    private DeprecateHandler deprecateHandler;
    private AsyncSiteReadinessGetAttributeDetail asyncSiteReadinessGetAttributeDetail = null;
    private String ATTRIBUTE_TYPE;
    private CustomAttributeDialog customAttributeDialog = null;
    private boolean IsFranchisee = false;
    private Activity activity;

    public interface ISiteCheckHandler {
        public void cameraClick(int parentPosition, int position, WIPCheckListDto siteReadinessCheckListDto);
    }

    public WIPInteriorCellStatusAdapter(Context context, Activity activity, boolean IsFranchisee, String type, int position, List<WIPCheckListDto> siteCompletionCheckList, WIPInteriorCellStatusAdapter.ISiteCheckHandler iSiteCheckHandler) {
        this.context = context;
        this.activity = activity;
        this.IsFranchisee = IsFranchisee;
        this.ATTRIBUTE_TYPE = type;
        this.siteCompletionCheckList = siteCompletionCheckList;
        this.iSiteCheckHandler = iSiteCheckHandler;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout layoutAddStatus;
        public TextView txtElementName, txtSubElementName;
        public LinearLayout layoutCellDetails;
        public TextView txtMsg;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            txtElementName = view.findViewById(R.id.txtElementName);
            txtSubElementName = view.findViewById(R.id.txtSubElementName);
            layoutAddStatus = view.findViewById(R.id.layoutAddStatus);
            layoutCellDetails = view.findViewById(R.id.layoutCellDetails);
            txtMsg = view.findViewById(R.id.txtMsg);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }

    @Override
    public WIPInteriorCellStatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wip_interior_status, parent, false);
        return new WIPInteriorCellStatusAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WIPInteriorCellStatusAdapter.MyViewHolder holder, int position) {

        final WIPCheckListDto siteCompletionCheckListDto = siteCompletionCheckList.get(position);

        String mandatory = siteCompletionCheckListDto.getMandatory();
        if (!TextUtils.isEmpty(mandatory) && mandatory.equalsIgnoreCase("1")) {
            GUIUtils.CompulsoryMark(holder.txtElementName, siteCompletionCheckListDto.getElementName() + " ");
        } else {
            holder.txtElementName.setText(siteCompletionCheckListDto.getElementName());
        }
        holder.txtSubElementName.setText(siteCompletionCheckListDto.getDesc());

        //Element Info
        holder.txtElementName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WIPCheckListDto checkListDto = (WIPCheckListDto) view.getTag();
                displayElementDetail(checkListDto.getElementName(), ATTRIBUTE_TYPE, checkListDto.getId());
            }
        });

        //Add Status
        holder.layoutAddStatus.setTag(position);
        holder.layoutAddStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                WIPCheckListDto dto = siteCompletionCheckList.get(pos);
                processStatusData(dto, new WIPStatusDto());
            }
        });

        //Bind Data
        String jsonData = "[{\"date\":\"2018-11-11\",\"status\":\"WIP\",\"photos\":\"1\"},{\"date\":\"2018-11-13\",\"status\":\"WIP\",\"photos\":\"2\"},{\"date\":\"2018-12-01\",\"status\":\"WIP\",\"photos\":\"3\"},{\"date\":\"2018-12-01\",\"status\":\"WIP\",\"photos\":\"0\"}]";
        Gson gson = new GsonBuilder().create();
        List<WIPStatusDto> statusDtoList = gson.fromJson(jsonData, new TypeToken<ArrayList<WIPStatusDto>>() {
        }.getType());
        if (position == 1) {
            statusDtoList = null;
        }

        if (position == 3) {
            statusDtoList.remove(1);
            statusDtoList.remove(0);
        }

        if (statusDtoList == null || statusDtoList.size() == 0) {
            holder.txtMsg.setVisibility(View.VISIBLE);
            holder.layoutCellDetails.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            holder.txtMsg.setVisibility(View.GONE);
            holder.layoutCellDetails.setVisibility(View.VISIBLE);
            inflateStatusView(statusDtoList, holder.layoutCellDetails, siteCompletionCheckListDto, holder.progressBar);
        }

        if (position == 2) {
            holder.txtMsg.setVisibility(View.GONE);
            holder.layoutCellDetails.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
        }

        holder.txtElementName.setTag(siteCompletionCheckListDto);
    }

    private void processStatusData(WIPCheckListDto dto, WIPStatusDto statusDto) {
        Intent intent = new Intent(context, WIPImagePreviewActivity.class);
        intent.putExtra("WIPCheckListDto", (Serializable) dto);
        intent.putExtra("WIPStatusDto", (Serializable) statusDto);
        activity.startActivity(intent);
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

    @Override
    public int getItemCount() {
        return siteCompletionCheckList.size();
    }

    public void inflateStatusView(List<WIPStatusDto> statusList, LinearLayout linearLayout, final WIPCheckListDto checkDto, ProgressBar progressBar) {

        if (linearLayout == null)
            return;

        linearLayout.invalidate();
        linearLayout.removeAllViews();
        for (int i = 0; i < statusList.size(); i++) {
            WIPStatusDto statusDto = statusList.get(i);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.item_wip_status, null);

            TextView txtDate = myView.findViewById(R.id.txtDate);
            TextView txtStatus = myView.findViewById(R.id.txtStatus);
            TextView txtPhotosCount = myView.findViewById(R.id.txtPhotosCount);
            txtDate.setText(statusDto.getDate());
            txtStatus.setText(statusDto.getStatus());
            String count = TextUtils.isEmpty(statusDto.getPhotosCount()) ? "0" : statusDto.getPhotosCount();
            txtPhotosCount.setText("+" + count);

            myView.setTag(statusDto);
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WIPStatusDto statusDto = (WIPStatusDto) view.getTag();
                    processStatusData(checkDto, statusDto);
                }
            });

            linearLayout.addView(myView);
        }
        progressBar.setVisibility(View.GONE);
    }
}
