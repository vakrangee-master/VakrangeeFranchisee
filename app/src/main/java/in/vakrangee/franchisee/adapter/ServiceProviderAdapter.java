package in.vakrangee.franchisee.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import in.vakrangee.supercore.franchisee.model.ServiceProvider;

/**
 * Created by Nileshd on 4/18/2016.
 */
public class ServiceProviderAdapter extends ArrayAdapter<ServiceProvider> {
    private Context mContext;
    private List<ServiceProvider> mValues;

    public ServiceProviderAdapter(Context context,
                                  int textViewResourceId, List<ServiceProvider> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }


    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public ServiceProvider getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView textview = (TextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);

        textview.setText(mValues.get(position).getServiceDescription());

        DisplayMetrics metrics = parent.getResources().getDisplayMetrics();
        float dp = 40f;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);

        textview.setHeight(pixels);
        textview.setTextSize(14);

        return textview;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView textview = (TextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);

        textview.setText(mValues.get(position).getServiceDescription());

        DisplayMetrics metrics = parent.getResources().getDisplayMetrics();
        float dp = 40f;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);

        textview.setHeight(pixels);
        textview.setTextSize(14);
        return textview;


        //return label;
    }


}
