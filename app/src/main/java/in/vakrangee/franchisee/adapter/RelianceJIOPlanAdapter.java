package in.vakrangee.franchisee.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.RelianceJIOTariffPlanDto;

/**
 * Created by Nileshd on 4/18/2016.
 */
public class RelianceJIOPlanAdapter extends BaseAdapter {

    private Context mContext;
    private List<RelianceJIOTariffPlanDto> mValues;

    public RelianceJIOPlanAdapter(Context context, List<RelianceJIOTariffPlanDto> objects) {
        //super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    private class ViewHolder {
        TextView relianceJIOPlanName;
        TextView relianceJIOPlanDescription;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public RelianceJIOTariffPlanDto getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelianceJIOPlanAdapter.ViewHolder holder = null;
        RelianceJIOTariffPlanDto relianceJIOTariffPlanDto = getItem(position);
        LayoutInflater minInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = minInflater.inflate(R.layout.reliance_jio_tariff_plans_spinner_row, null);
            holder = new RelianceJIOPlanAdapter.ViewHolder();

            //Binding views
            holder.relianceJIOPlanName = (TextView) convertView.findViewById(R.id.relianceJIOPlanName);
            holder.relianceJIOPlanDescription = (TextView) convertView.findViewById(R.id.relianceJIOPlanDescription);

            convertView.setTag(holder);
        } else
            holder = (RelianceJIOPlanAdapter.ViewHolder) convertView.getTag();

        holder.relianceJIOPlanName.setText(relianceJIOTariffPlanDto.getReliance_jio_tariff_plan_name());
        if (TextUtils.isEmpty(relianceJIOTariffPlanDto.getReliance_jio_tariff_plan_description())) {
            holder.relianceJIOPlanDescription.setVisibility(View.GONE);
        } else {
            holder.relianceJIOPlanDescription.setVisibility(View.VISIBLE);
            holder.relianceJIOPlanDescription.setText(relianceJIOTariffPlanDto.getReliance_jio_tariff_plan_description());
        }


        return convertView;
    }
}
