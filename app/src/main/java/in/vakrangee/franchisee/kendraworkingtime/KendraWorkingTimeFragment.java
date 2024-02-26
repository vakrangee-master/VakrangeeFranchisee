package in.vakrangee.franchisee.kendraworkingtime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

@SuppressLint("ValidFragment")
public class KendraWorkingTimeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "KendraWorkingTimeFragment";
    private View view;
    private Context context;
    private GPSTracker gpsTracker;
    private InternetConnection internetConnection;
    private TextView txtNoDataMsg;
    private LinearLayout layoutProgress;
    private Button btnSubmitWorkingTime;
    private RecyclerView recyclerViewWorkingTime;
    private List<KendraWorkingDetailsDto> workingDetailsList = new ArrayList<>();
    private WorkingTimeAdapter workingTimeAdapter;

    public KendraWorkingTimeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_workingtime, container, false);
        final Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        //Initialize data
        this.context = getContext();
        gpsTracker = new GPSTracker(context);
        internetConnection = new InternetConnection(context);
        ButterKnife.bind(this, view);

        layoutProgress = (LinearLayout) view.findViewById(R.id.layoutProgress);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);
        recyclerViewWorkingTime = view.findViewById(R.id.recycler_view_working_time);
        btnSubmitWorkingTime = view.findViewById(R.id.btnSubmitWorkingTime);
        btnSubmitWorkingTime.setTypeface(font);

        // Set Font Text
        btnSubmitWorkingTime.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        btnSubmitWorkingTime.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnSubmitWorkingTime) {
            workingDetailsList = workingTimeAdapter.getFinalWorkingDetailsList();

            Gson gson = new Gson();
            String data = gson.toJson(workingDetailsList, new TypeToken<ArrayList<KendraWorkingDetailsDto>>() {
            }.getType());
            Log.e(TAG, "Testing: Final Kendra Details: " + data);

        }
    }

    public void processData(String data) {

        //data = "[{\"day\":\"Monday\",\"interval\":\"15\",\"working_time\":[{\"opening_time\":\"00:15\",\"closing_time\":\"12:45\"},{\"opening_time\":\"02:00\",\"closing_time\":\"06:00\"}]},{\"day\":\"Tuesday\",\"interval\":\"30\",\"working_time\":[{\"opening_time\":\"00:15\",\"closing_time\":\"12:45\"}]}]";
        data = "[{\"day\":\"Monday\",\"interval\":\"15\",\"working_time\":[{\"opening_time\":\"00:15\",\"closing_time\":\"12:45\"},{\"opening_time\":\"00:15\",\"closing_time\":\"12:45\"},{\"opening_time\":\"00:15\",\"closing_time\":\"12:45\"},{\"opening_time\":\"02:00\",\"closing_time\":\"06:00\"}]},{\"day\":\"Tuesday\",\"interval\":\"30\",\"working_time\":[{\"opening_time\":\"04:00\",\"closing_time\":\"05:30\"}]},{\"day\":\"Wednesday\",\"interval\":\"45\"},{\"day\":\"Thursday\",\"interval\":\"45\",\"working_time\":[{\"opening_time\":\"04:45\",\"closing_time\":\"07:45\"}]},{\"day\":\"Friday\",\"interval\":\"15\"},{\"day\":\"Saturday\",\"interval\":\"30\",\"working_time\":[{\"opening_time\":\"00:00\",\"closing_time\":\"12:30\"},{\"opening_time\":\"12:00\",\"closing_time\":\"13:00\"},{\"opening_time\":\"00:30\",\"closing_time\":\"12:30\"}]},{\"day\":\"Sunday\",\"interval\":\"30\",\"working_time\":[{\"opening_time\":\"00:00\",\"closing_time\":\"12:30\"},{\"opening_time\":\"12:00\",\"closing_time\":\"13:00\"},{\"opening_time\":\"00:30\",\"closing_time\":\"12:30\"},{\"opening_time\":\"13:30\",\"closing_time\":\"15:30\"}]}]";

        if (TextUtils.isEmpty(data)) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            txtNoDataMsg.setVisibility(View.VISIBLE);
            layoutProgress.setVisibility(View.GONE);
            recyclerViewWorkingTime.setVisibility(View.GONE);
            return;
        }

        //Process Response
        txtNoDataMsg.setVisibility(View.GONE);
        recyclerViewWorkingTime.setVisibility(View.VISIBLE);
        reloadKendraWorkingTimeDetails(data);
    }

    private void reloadKendraWorkingTimeDetails(String data) {

        Gson gson = new GsonBuilder().create();
        workingDetailsList = gson.fromJson(data, new TypeToken<ArrayList<KendraWorkingDetailsDto>>() {
        }.getType());

        workingTimeAdapter = new WorkingTimeAdapter(context, getActivity(), workingDetailsList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewWorkingTime.setLayoutManager(layoutManager);
        recyclerViewWorkingTime.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWorkingTime.setAdapter(workingTimeAdapter);
        layoutProgress.setVisibility(View.GONE);
    }


}
