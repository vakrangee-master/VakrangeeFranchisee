package in.vakrangee.franchisee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import in.vakrangee.supercore.franchisee.model.ODSpinner;

/**
 * Created by Nileshd on 1/9/2017.
 */
public class ODSpnAdapter extends ArrayAdapter<ODSpinner> {

    private Context context;
    private List<ODSpinner> ODSpinnerList;

    public ODSpnAdapter(Context context, int textViewResourceId,
                        List<ODSpinner> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.ODSpinnerList = values;
    }

    public int getCount() {
        return ODSpinnerList.size();
    }

    public ODSpinner getItem(int position) {
        return ODSpinnerList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(ODSpinnerList.get(position).getCity());
        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {


        TextView text = new TextView(context);

        DisplayMetrics metrics = parent.getResources().getDisplayMetrics();
        float dp = 30f;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);

        text.setHeight(pixels);


        text.setTextColor(Color.BLACK);
        text.setGravity(Gravity.CENTER);
        text.setText(ODSpinnerList.get(position).getCity());
        return text;
    }
}
