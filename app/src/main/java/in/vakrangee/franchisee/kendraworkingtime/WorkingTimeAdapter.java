package in.vakrangee.franchisee.kendraworkingtime;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class WorkingTimeAdapter extends RecyclerView.Adapter<WorkingTimeAdapter.MyViewHolder> {

    private static final String TAG = "WorkingTimeAdapter";
    public List<KendraWorkingDetailsDto> workingDetailsList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;
    private Activity activity;
    private int MAX_ROW_COUNT = 2;

    public WorkingTimeAdapter(Context context, Activity activity, List<KendraWorkingDetailsDto> workingDetailsList, RecyclerViewClickListener listener) {
        this.context = context;
        this.activity = activity;
        this.workingDetailsList = workingDetailsList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtDaysName;
        public LinearLayout layoutParent;
        public TableLayout tableLayout;
        public LinearLayout parentLinearlytAddButton;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);

            layoutParent = view.findViewById(R.id.layoutParent);
            txtDaysName = view.findViewById(R.id.txtDaysName);
            tableLayout = view.findViewById(R.id.tableLayoutWorkingTime);
            parentLinearlytAddButton = view.findViewById(R.id.parentLinearlytAddButton);

            mListener = listener;
            layoutParent.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public WorkingTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kendra_workingtime, parent, false);
        return new WorkingTimeAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(final WorkingTimeAdapter.MyViewHolder holder, int position) {

        final KendraWorkingDetailsDto kendraWorkingDetailsDto = workingDetailsList.get(position);

        holder.txtDaysName.setText(kendraWorkingDetailsDto.getDay());

        //Bind Table Layout data
        bindWorkingTime(position, kendraWorkingDetailsDto, holder.tableLayout);

        //Add row
        holder.parentLinearlytAddButton.setTag(kendraWorkingDetailsDto);
        holder.parentLinearlytAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KendraWorkingDetailsDto dto = (KendraWorkingDetailsDto) view.getTag();
                int size = dto.workingTimeList.size();
                if (size >= MAX_ROW_COUNT) {
                    Toast.makeText(context, "You cannot add more than " + MAX_ROW_COUNT + " rows.", Toast.LENGTH_SHORT).show();
                    return;
                }

                WorkingTimeDto workingTimeDto = new WorkingTimeDto();
                dto.workingTimeList.add(workingTimeDto);
                notifyDataSetChanged();
            }
        });

        holder.txtDaysName.setTag(kendraWorkingDetailsDto);
    }

    @Override
    public int getItemCount() {
        return workingDetailsList.size();
    }

    public void addRow(int position, final int rowPos, final TableLayout tableLayout, String difference, String openingTime, String closingTime) {

        TableRow tableRow = (TableRow) activity.getLayoutInflater().inflate(R.layout.row_spiners, null, false);

        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(0, 5, 0, 5);
        tableRow.setLayoutParams(tableRowParams);

        int interval = TextUtils.isEmpty(difference) ? 0 : Integer.parseInt(difference);
        List<String> timeSlotsList = getTimeSlots(interval);

        //Opening Time
        final Spinner spinnerOpeningTime = tableRow.findViewById(R.id.spinnerOpenTime);
        ArrayAdapter openningAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, timeSlotsList);
        openningAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpeningTime.setAdapter(openningAdapter);
        int openTimePos = getSelectedPos(timeSlotsList, openingTime);
        spinnerOpeningTime.setSelection(openTimePos);
        spinnerOpeningTime.setTag(position);
        spinnerOpeningTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedTime = (String) spinnerOpeningTime.getSelectedItem();
                    int pos = (int) parent.getTag();

                    KendraWorkingDetailsDto detailsDto = workingDetailsList.get(pos);
                    WorkingTimeDto timeDto = detailsDto.workingTimeList.get(rowPos);
                    timeDto.setOpeningTime(selectedTime);
                    detailsDto.workingTimeList.set(rowPos, timeDto);
                    workingDetailsList.set(pos, detailsDto);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Closing Time
        final Spinner spinnerClosingTime = tableRow.findViewById(R.id.spinnerCloseTime);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, timeSlotsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClosingTime.setAdapter(adapter);
        int closeTimePos = getSelectedPos(timeSlotsList, closingTime);
        spinnerClosingTime.setSelection(closeTimePos);
        spinnerClosingTime.setTag(position);
        spinnerClosingTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedClosingTime = (String) spinnerClosingTime.getSelectedItem();
                    int pos = (int) parent.getTag();

                    KendraWorkingDetailsDto detailsDto = workingDetailsList.get(pos);
                    WorkingTimeDto timeDto = detailsDto.workingTimeList.get(rowPos);
                    timeDto.setClosingTime(selectedClosingTime);
                    detailsDto.workingTimeList.set(rowPos, timeDto);
                    workingDetailsList.set(pos, detailsDto);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Remove Row
        LinearLayout layoutRemove = tableRow.findViewById(R.id.layoutRemove);
        layoutRemove.setTag(position);
        layoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();

                KendraWorkingDetailsDto detailsDto = workingDetailsList.get(pos);
                detailsDto.workingTimeList.remove(rowPos);
                workingDetailsList.set(pos, detailsDto);
                notifyDataSetChanged();
            }
        });

        if (rowPos == 0) {
            layoutRemove.setVisibility(View.INVISIBLE);

        } else {
            layoutRemove.setVisibility(View.VISIBLE);
        }

        tableLayout.addView(tableRow, tableRowParams);
    }

    private void bindWorkingTime(int pos, KendraWorkingDetailsDto detailsDto, TableLayout tableLayout) {

        tableLayout.removeAllViews();
        tableLayout.invalidate();

        if (detailsDto.workingTimeList == null || detailsDto.workingTimeList.size() == 0) {
            WorkingTimeDto workingTimeDto = new WorkingTimeDto();
            detailsDto.workingTimeList.add(workingTimeDto);
            workingDetailsList.set(pos, detailsDto);
        }

        for (int i = 0; i < detailsDto.workingTimeList.size(); i++) {
            addRow(pos, i, tableLayout, detailsDto.getInterval(), detailsDto.workingTimeList.get(i).getOpeningTime(), detailsDto.workingTimeList.get(i).getClosingTime());
        }
    }

    private List<String> getTimeSlots(int interval) {
        List<String> timeSlotsList = new ArrayList<>();

        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        int startDate = cal.get(Calendar.DATE);
        while (cal.get(Calendar.DATE) == startDate) {
            String slot = df.format(cal.getTime());
            timeSlotsList.add(slot);
            cal.add(Calendar.MINUTE, interval);
        }
        return timeSlotsList;
    }

    private int getSelectedPos(List<String> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue) || spinnerDtoList == null)
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i)))
                return i;
        }
        return 0;
    }

    public List<KendraWorkingDetailsDto> getFinalWorkingDetailsList() {
        return workingDetailsList;
    }
}
