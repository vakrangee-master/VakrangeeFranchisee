package in.vakrangee.franchisee.sitelayout.sitecompletion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.franchisee.sitelayout.R;

public class WorkCompletionCheckListAdapter extends RecyclerView.Adapter<WorkCompletionCheckListAdapter.MyViewHolder> {

    private static final String TAG = "WorkCompletionCheckListAdapter";
    private List<WorkCompletionCheckListDto> workCompletionCheckList;
    private Context context;

    public WorkCompletionCheckListAdapter(Context context, List<WorkCompletionCheckListDto> workCompletionCheckList) {
        this.context = context;
        this.workCompletionCheckList = workCompletionCheckList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtElementName, txtSubElementName;
        public RadioGroup radioGroupStatus;

        public MyViewHolder(View view) {
            super(view);
            txtElementName = view.findViewById(R.id.txtElementName);
            txtSubElementName = view.findViewById(R.id.txtSubElementName);
            radioGroupStatus = view.findViewById(R.id.radioGroupStatus);
        }
    }

    @Override
    public WorkCompletionCheckListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_completion_checklist2, parent, false);
        return new WorkCompletionCheckListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorkCompletionCheckListAdapter.MyViewHolder holder, final int position) {

        final WorkCompletionCheckListDto workCompletionCheckListDto = workCompletionCheckList.get(position);
        holder.txtElementName.setText(workCompletionCheckListDto.getElementName());
        holder.txtSubElementName.setText(workCompletionCheckListDto.getSubElementName());

        holder.radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked && checkedId == R.id.radioButtonElementYes) {
                    workCompletionCheckList.get(position).setStatus(1);

                } else if (isChecked && checkedId == R.id.radioButtonElementNo) {
                    workCompletionCheckList.get(position).setStatus(0);
                }
            }
        });

        holder.txtElementName.setTag(workCompletionCheckListDto);

    }

    public List<WorkCompletionCheckListDto> getWorkCompletionCheckList() {
        return workCompletionCheckList;
    }

    @Override
    public int getItemCount() {
        return workCompletionCheckList.size();
    }
}
