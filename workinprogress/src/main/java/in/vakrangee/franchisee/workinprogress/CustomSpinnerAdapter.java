package in.vakrangee.franchisee.workinprogress;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.vakrangee.supercore.franchisee.model.BrandingElementDto;
import in.vakrangee.supercore.franchisee.support.MileStoneDetailDto;
import in.vakrangee.supercore.franchisee.support.WorkStatusDto;

public class CustomSpinnerAdapter extends BaseAdapter {


    private static final String TAG = "CustomSpinnerAdapter";
    private Context context;
    private ArrayList<Object> objectList;

    public CustomSpinnerAdapter(Context context, ArrayList<Object> objectList) {
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
        ViewHolder holder = null;
        Object object = getItem(position);
        LayoutInflater minInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = minInflater.inflate(R.layout.custom_spinner_row, null);
            holder = new ViewHolder();

            //Binding views
            holder.txtValue = convertView.findViewById(R.id.txtName);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        //custom object types
        if (object instanceof MileStoneDetailDto) {
            holder.txtValue.setText(((MileStoneDetailDto) object).getName());

        } else if (object instanceof BrandingElementDto) {
            holder.txtValue.setText(((BrandingElementDto) object).getName());

        } else if (object instanceof WorkStatusDto) {
            holder.txtValue.setText(((WorkStatusDto) object).getName());

        } else {
            holder.txtValue.setText(object.toString());
        }
        return convertView;
    }
}
