package in.vakrangee.franchisee.workinprogress;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.model.BrandingElementDto;
import in.vakrangee.supercore.franchisee.support.WorkStatusDto;


public class ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.MyViewHolder> {

    private static final String TAG = "ItemCategoryAdapter";
    private List<BrandingElementDto> brandingElementList;
    private Context context;
    private ItemCategoryAdapter.IElementStatus iElementStatus;
    private CustomSpinnerAdapter customSpinnerAdapter;
    private static final String JSON_KEY_WORK_STATUS_DETAILS = "workStatusDetails";

    public ItemCategoryAdapter(Context context, List<BrandingElementDto> brandingElementList, ItemCategoryAdapter.IElementStatus iElementStatus) {
        this.context = context;
        this.brandingElementList = brandingElementList;
        this.iElementStatus = iElementStatus;
    }

    public interface IElementStatus {
        void getSelectedStatus(WorkStatusDto statusDto, int pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCategoryName;
        public Spinner spinnerCategoryStatus;

        public MyViewHolder(View view) {
            super(view);
            txtCategoryName = view.findViewById(R.id.txtCategoryName);
            spinnerCategoryStatus = view.findViewById(R.id.spinnerItemCategoryStatus);
        }
    }

    @Override
    public ItemCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ItemCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemCategoryAdapter.MyViewHolder holder, int position) {

        final BrandingElementDto brandingElementDto = brandingElementList.get(position);
        holder.txtCategoryName.setText(brandingElementDto.getName());
        holder.txtCategoryName.setTag(brandingElementDto);
        holder.spinnerCategoryStatus.setTag(brandingElementDto);

        //Long Text
        holder.txtCategoryName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                BrandingElementDto dto = (BrandingElementDto) view.getTag();
                Toast.makeText(context, dto.getName(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //Category Status
        String statusDetails = "{\"workStatusDetails\" : [{\"work_status_id\":\"1\", \"work_status_name\":\"Not Started\"},{\"work_status_id\":\"2\", \"work_status_name\":\"Work In Progress\"},{\"work_status_id\":\"3\", \"work_status_name\":\"Completed\"}]}";
        List<WorkStatusDto> workStatusList = getWorkStatusList(statusDetails);
        ArrayList<Object> list1 = new ArrayList<Object>(workStatusList);
        customSpinnerAdapter = new CustomSpinnerAdapter(context, list1);
        holder.spinnerCategoryStatus.setAdapter(customSpinnerAdapter);
        int selPos = getSelCategoryStatus(workStatusList);
        holder.spinnerCategoryStatus.setSelection(selPos);
        holder.spinnerCategoryStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    WorkStatusDto selDto = (WorkStatusDto) parent.getItemAtPosition(position);
                    iElementStatus.getSelectedStatus(selDto, position);
                    //franchiseeDetails.setWipMileStoneId(selDto.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public int getSelCategoryStatus(List<WorkStatusDto> list) {

       /* if (TextUtils.isEmpty(franchiseeDetails.getWipMileStoneId()))
            return 0;

        for (int i = 0; i < list.size(); i++) {
            if (franchiseeDetails.getWipMileStoneId().equalsIgnoreCase(list.get(i).getId()))
                return i;
        }*/
        return 0;
    }

    @Override
    public int getItemCount() {
        return brandingElementList.size();
    }

    private List<WorkStatusDto> getWorkStatusList(String workStatusDetails) {
        List<WorkStatusDto> workStatusList = new ArrayList<WorkStatusDto>();
        //Prepare WorkStatus list
        try {
            if (!TextUtils.isEmpty(workStatusDetails)) {

                JSONObject jsonObject = new JSONObject(workStatusDetails);
                JSONArray jsonArray = jsonObject.getJSONArray(JSON_KEY_WORK_STATUS_DETAILS);

                Gson gson = new GsonBuilder().create();
                workStatusList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WorkStatusDto>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workStatusList;
    }
}
