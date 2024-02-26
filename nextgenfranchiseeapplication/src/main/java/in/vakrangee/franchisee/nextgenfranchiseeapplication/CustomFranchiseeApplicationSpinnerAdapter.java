package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;

public class CustomFranchiseeApplicationSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<CustomFranchiseeApplicationSpinnerDto> objectList;

    public CustomFranchiseeApplicationSpinnerAdapter(Context context, List<CustomFranchiseeApplicationSpinnerDto> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView txtValue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomFranchiseeApplicationSpinnerAdapter.ViewHolder holder = null;
        CustomFranchiseeApplicationSpinnerDto object = (CustomFranchiseeApplicationSpinnerDto) getItem(position);
        LayoutInflater minInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = minInflater.inflate(R.layout.custom_spinner_row, null);
            holder = new CustomFranchiseeApplicationSpinnerAdapter.ViewHolder();

            //Binding views
            holder.txtValue = convertView.findViewById(R.id.txtName);

            convertView.setTag(holder);
        } else
            holder = (CustomFranchiseeApplicationSpinnerAdapter.ViewHolder) convertView.getTag();

        String value = object.getName();
        holder.txtValue.setText(value);
        return convertView;
    }

    public void notify(List<CustomFranchiseeApplicationSpinnerDto> list) {
        if (objectList != null) {
            objectList.clear();
            objectList.addAll(list);

        } else {
            objectList = list;
        }


        notifyDataSetChanged();
    }
}
