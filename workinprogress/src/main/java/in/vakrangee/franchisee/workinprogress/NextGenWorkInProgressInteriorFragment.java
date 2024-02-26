package in.vakrangee.franchisee.workinprogress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class NextGenWorkInProgressInteriorFragment extends BaseFragment implements View.OnClickListener, WIPCategoryListAdapter.ISiteCheckHandler {

    private static final String TAG = "NextGenWorkInProgressInteriorFragment";
    private Context context;
    private FranchiseeDetails franchiseeDetails;
    private boolean IsEditable;
    private View view;
    private Logger logger;
    private GPSTracker gpsTracker;
    private Connection connection;
    private PermissionHandler permissionHandler;
    private WIPCategoryListAdapter wipCategoryListAdapter;
    private List<WIPInteriorCategoryDto> wipCategoryList;
    private int selectedDateTimeId = 0;
    private DateTimePickerDialog dateTimePickerDialog;


    //region Work In Progress
    private LinearLayout layoutWorkStartDate, layoutExpectedDate;
    private TextView textViewLastVisit;
    private Spinner spinnerWorkStatusType;
    private LinearLayout layoutWorkStatusType;
    private LinearLayout layoutWIPDetails;
    private TextView txtExpectedDateLbl;
    private EditText editTextRemarks;
    private RadioGroup radioGroupWorkOnTrack;
    private TextView txtVisitDate, textViewWorkStartDate, txtExpectedDate, textViewDelayInDays;
    private Date visitDate, workStartDate, expectedDate;
    private String strVisitDate, strWorkStartDate, strExpectedDate;
    // Commencement Min Date
    private String strCommencementMinDate;
    private Date commencementMinDate;

    private DateFormat wipDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US); //new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US);
    private DateFormat wipDateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat wipYearDateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private DateFormat wipDateFormatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    //endregion

    private Button btnCancel;
    private Button btnClear;
    private Button btnSubmitInterior;
    private RecyclerView recyclerViewWIPInteriorCategoryList;

    private NextGenWorkInProgressInteriorFragment() {
    }

    public NextGenWorkInProgressInteriorFragment(FranchiseeDetails franchiseeDetails, boolean IsEditable) {
        this.franchiseeDetails = franchiseeDetails;
        this.IsEditable = IsEditable;
    }

    public interface IImagesHandler {
        public void updateImagesList(List<ImageDto> imageDtoList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_wip_interior, container, false);
        final Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        bindViewId(view);
        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        gpsTracker = new GPSTracker(context);
        connection = new Connection(context);
        permissionHandler = new PermissionHandler(getActivity());
        ButterKnife.bind(this, view);

        btnClear.setTypeface(font);
        btnCancel.setTypeface(font);
        btnSubmitInterior.setTypeface(font);

        // Set Font Text
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnSubmitInterior.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmitInterior.setOnClickListener(this);

        //TODO @V: Interior Category List
        String data = "[{\"sub_cat\":[{\"element_detail_image\":\"\",\"completed\":1,\"mandatory\":1,\"vakrangee_kendra_design_verification_id\":14,\"vakrangee_kendra_design_verification_history_id\":118,\"is_correction\":0,\"element_description\":\"Ceiling with LED Lights  \",\"element_detail_id\":40,\"element_name\":\"Ceiling with LED Lights\",\"activity_conducted_by\":0,\"remarks\":\"led.,\",\"activity_status\":0,\"element_detail_image_id\":6713}],\"element_id\":2,\"element_description\":\"Ceiling\",\"element_name\":\"Ceiling\"},{\"sub_cat\":[{\"is_correction\":0,\"element_description\":\"Concealed Wiring  \",\"completed\":0,\"element_detail_id\":41,\"element_name\":\"Concealed Wiring\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Point for Digital Signage  \",\"completed\":0,\"element_detail_id\":42,\"element_name\":\"Point for Digital Signage\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"UPS and ATM Points  \",\"completed\":0,\"element_detail_id\":43,\"element_name\":\"UPS and ATM Points\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"CCTV point  \",\"completed\":0,\"element_detail_id\":44,\"element_name\":\"CCTV point\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Main Distribution Board\",\"completed\":0,\"element_detail_id\":74,\"element_name\":\"Main Distribution Board\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Counter Switches Below The Table\",\"completed\":0,\"element_detail_id\":75,\"element_name\":\"Counter Switches Below The Table\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0}],\"element_id\":3,\"element_description\":\"Electrical\",\"element_name\":\"Electrical\"},{\"sub_cat\":[{\"is_correction\":0,\"element_description\":\"AC-Fan-Cooler\",\"completed\":0,\"element_detail_id\":59,\"element_name\":\"AC-Fan-Cooler\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Water Cooler\",\"completed\":0,\"element_detail_id\":60,\"element_name\":\"Water Cooler\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Dust Bins - Qty 6\",\"completed\":0,\"element_detail_id\":61,\"element_name\":\"Dust Bins - Qty 6\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Inverter or Generator (IF applicable)\",\"completed\":0,\"element_detail_id\":62,\"element_name\":\"Inverter - Generator (IF Applicable)\",\"mandatory\":0,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Fire Extinguisher\",\"completed\":0,\"element_detail_id\":63,\"element_name\":\"Fire Extinguisher\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Softboard\",\"completed\":0,\"element_detail_id\":64,\"element_name\":\"Softboard\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0}],\"element_id\":6,\"element_description\":\"Fixtures\",\"element_name\":\"Fixtures\"},{\"sub_cat\":[{\"is_correction\":0,\"element_description\":\"Flooring  \",\"completed\":0,\"element_detail_id\":39,\"element_name\":\"Flooring\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0}],\"element_id\":1,\"element_description\":\"Flooring\",\"element_name\":\"Flooring\"},{\"sub_cat\":[{\"is_correction\":0,\"element_description\":\"Bank Counter Table with Chairs - Front View\",\"completed\":0,\"element_detail_id\":52,\"element_name\":\"Bank Counter Table with Chairs - Front View\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Bank Counter Table with Chairs - Back View\",\"completed\":0,\"element_detail_id\":53,\"element_name\":\"Bank Counter Table with Chairs - Back View\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Other services counter table with chairs - Front View\",\"completed\":0,\"element_detail_id\":54,\"element_name\":\"Other services counter table with chairs - Front View\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Other services counter table with chairs - Back View\",\"completed\":0,\"element_detail_id\":55,\"element_name\":\"Other services counter table with chairs - Back View\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Waiting Bench\",\"completed\":0,\"element_detail_id\":56,\"element_name\":\"Waiting Bench\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Storage Cabinet \",\"completed\":0,\"element_detail_id\":57,\"element_name\":\"Storage Cabinet \",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Store Room -  Storage shelf on wall\",\"completed\":0,\"element_detail_id\":58,\"element_name\":\"Store Room - Storage shelf on wall\",\"mandatory\":0,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Main Entrance Glass Door & Fixed Glass\",\"completed\":0,\"element_detail_id\":65,\"element_name\":\"Main Entrance Glass Door & Fixed Glass\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0}],\"element_id\":5,\"element_description\":\"Furniture\",\"element_name\":\"Furniture\"},{\"sub_cat\":[{\"is_correction\":0,\"element_description\":\"Left Wall  \",\"completed\":0,\"element_detail_id\":45,\"element_name\":\"Left Wall\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Right Wall\",\"completed\":0,\"element_detail_id\":47,\"element_name\":\"Right Wall\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Back Wall\",\"completed\":0,\"element_detail_id\":48,\"element_name\":\"Back Wall\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Front Wall\",\"completed\":0,\"element_detail_id\":49,\"element_name\":\"Front Wall\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Outside Wall (IF Applicable)\",\"completed\":0,\"element_detail_id\":50,\"element_name\":\"Outside Wall (IF Applicable)\",\"mandatory\":0,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Rolling Shutter\",\"completed\":0,\"element_detail_id\":51,\"element_name\":\"Rolling Shutter\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0}],\"element_id\":4,\"element_description\":\"Painting\",\"element_name\":\"Painting\"}]";
        String interiorJsonArray = data;    //franchiseeDetails.getDesignElements();
        Gson gson = new GsonBuilder().create();
        wipCategoryList = gson.fromJson(interiorJsonArray.toString(), new TypeToken<ArrayList<WIPInteriorCategoryDto>>() {
        }.getType());

        wipCategoryListAdapter = new WIPCategoryListAdapter(context, getActivity(), true, Constants.SITE_READINESS_ATTRIBUTE_DESIGN_TYPE, wipCategoryList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewWIPInteriorCategoryList.setLayoutManager(layoutManager);
        recyclerViewWIPInteriorCategoryList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWIPInteriorCategoryList.setAdapter(wipCategoryListAdapter);

        reloadWIPDetails(view);

        return view;
    }

    private void bindViewId(View view) {
        //region Reference
        Button btnCancel = view.findViewById(R.id.btnWIPInteriorCancel);
        Button btnClear = view.findViewById(R.id.btnWIPInteriorClear);
        Button btnSubmitInterior = view.findViewById(R.id.btnWIPInteriorSubmit);
        RecyclerView recyclerViewWIPInteriorCategoryList = view.findViewById(R.id.recyclerViewWIPInteriorCategoryList);
        //endregion
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void cameraClick(int parentPosition, int position, WIPCheckListDto siteReadinessCheckListDto) {

    }

    public void reloadWIPDetails(View view) {

        //Widgets
        layoutWorkStartDate = view.findViewById(R.id.layoutStartDate);
        textViewWorkStartDate = view.findViewById(R.id.textViewWorkStartDate);
        textViewLastVisit = view.findViewById(R.id.textViewLastVisit);
        radioGroupWorkOnTrack = view.findViewById(R.id.radioGroupWorkOnTrack);
        layoutExpectedDate = view.findViewById(R.id.layoutExpectedDate);
        txtExpectedDateLbl = view.findViewById(R.id.txtExpectedDateLbl);
        txtExpectedDate = view.findViewById(R.id.textViewExpectedDate);
        txtVisitDate = view.findViewById(R.id.textViewVisitDate);
        editTextRemarks = view.findViewById(R.id.editTextRemarks);
        textViewDelayInDays = view.findViewById(R.id.textViewDelayInDays);
        CommonUtils.applyInputFilter(editTextRemarks, "\"~#^|$%&*!'");
        //region Hide Work Status Type
        layoutWorkStatusType = view.findViewById(R.id.layoutWorkStatusType);
        spinnerWorkStatusType = view.findViewById(R.id.spinnerWorkStatusType);
        //endregion

        //STEP 1: Work Start Date


        //STEP 2: Last Updated On
        String lastVisit = "";
        if ((!TextUtils.isEmpty(franchiseeDetails.getWipLastVisitedDateTime())) && (!TextUtils.isEmpty(franchiseeDetails.getWipLastVisitedBy())))
            lastVisit = franchiseeDetails.getWipLastVisitedDateTime();
        else
            lastVisit = getString(R.string.not_visited_yet);

        textViewLastVisit.setText(Html.fromHtml(lastVisit));

        reloadWIPData();
        layoutWorkStartDate.setVisibility(View.VISIBLE);
        layoutExpectedDate.setVisibility(View.VISIBLE);
        // Add Listener to Buttons
        textViewWorkStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewWorkStartDate.setError(null);
                selectedDateTimeId = view.getId();
                showDateTimeDialogPicker();
            }
        });
        txtExpectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(strWorkStartDate)) {
                    selectedDateTimeId = view.getId();
                    showDateTimeDialogPicker();
                } else {
                    Toast.makeText(getActivity(), "Please Select Work Start Date.", Toast.LENGTH_LONG).show();
                    textViewWorkStartDate.setError("Select Work Start Date.");
                }
            }
        });
        radioGroupWorkOnTrack.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb != null && (checkedId > -1)) {
                    String text = rb.getText().toString();
                    if (text.equalsIgnoreCase(getString(R.string.no_txt))) {
                        franchiseeDetails.setWipWorkOnTrack(0);
                        //layoutExpectedDate.setVisibility(View.VISIBLE);
                        txtExpectedDateLbl.setText("Revised Expected Date Of Completion");
                    } else {
                        franchiseeDetails.setWipWorkOnTrack(1);
                        //layoutExpectedDate.setVisibility(View.GONE);
                        strExpectedDate = null;
                        expectedDate = null;
                        txtExpectedDate.setText("");
                        franchiseeDetails.setWipRevisedExpectedDateOfCompletion(null);
                        txtExpectedDateLbl.setText("Expected Date Of Completion");
                    }
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    public void reloadWIPData() {

        if (franchiseeDetails == null)
            return;

        try {

            //Set Minimum Commencement Date
            strCommencementMinDate = franchiseeDetails.getCommencementMinDate();
            Log.e(TAG, "Commencement Min Date : " + strCommencementMinDate);
            commencementMinDate = wipDateFormatter3.parse(strCommencementMinDate);

            // Set Visited and Expected Date
            if (TextUtils.isEmpty(franchiseeDetails.getWipVisitDateTime())) {
                Date datetime = new Date();
                String formatedDate = wipDateFormatter.format(datetime);
                visitDate = datetime;
                strVisitDate = formatedDate;
                txtVisitDate.setText(wipDateFormatter2.format(visitDate));
                franchiseeDetails.setWipVisitDateTime(wipDateFormatter.format(new Date()));
            } else {
                strVisitDate = franchiseeDetails.getWipVisitDateTime();
                if (!TextUtils.isEmpty(strVisitDate)) {
                    visitDate = wipDateFormatter.parse(strVisitDate);
                    txtVisitDate.setText(wipDateFormatter2.format(visitDate));
                }
            }

            // Set Start and Estimated End Date
            strWorkStartDate = franchiseeDetails.getCommencementStartDate();
            if (!TextUtils.isEmpty(strWorkStartDate)) {
                workStartDate = wipDateFormatter3.parse(strWorkStartDate);
                textViewWorkStartDate.setText(CommonUtils.getFormattedDate("yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy", strWorkStartDate)); //dateFormatter2.format(startDate));
            }

            if (!TextUtils.isEmpty(franchiseeDetails.getNextgenSiteWorkCommencementId()))
                textViewWorkStartDate.setEnabled(false);
            else
                textViewWorkStartDate.setEnabled(true);

            strExpectedDate = franchiseeDetails.getWipRevisedExpectedDateOfCompletion();
            if (!TextUtils.isEmpty(strExpectedDate)) {
                expectedDate = wipDateFormatter.parse(strExpectedDate);
                txtExpectedDate.setText(wipDateFormatter2.format(expectedDate));
            }

            editTextRemarks.setText(franchiseeDetails.getWipFieldForceRemarks());

            //Work On Track
            if (franchiseeDetails.getWipWorkOnTrack() == 0 && !TextUtils.isEmpty(franchiseeDetails.getWipRevisedExpectedDateOfCompletion())) {
                radioGroupWorkOnTrack.check(R.id.radioButtonWOTNo);
                franchiseeDetails.setWipWorkOnTrack(0);
                txtExpectedDateLbl.setText("Revised Expected Date Of Completion");

            } else {
                radioGroupWorkOnTrack.check(R.id.radioButtonWOTYes);
                franchiseeDetails.setWipWorkOnTrack(1);
                txtExpectedDateLbl.setText("Expected Date Of Completion");
            }

            //TODO:@V: Set Delay In days
            textViewDelayInDays.setText("2 days");

            //Edit mode or not
            if (!IsEditable) {
                GUIUtils.setViewAndChildrenEnabled(layoutWIPDetails, false);
            }

        } catch (Exception pe) {
            pe.printStackTrace();
        }
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewWorkStartDate) {
            defaultDate = workStartDate;
        } else if (selectedDateTimeId == R.id.textViewExpectedDate) {
            defaultDate = expectedDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = wipDateFormatter2.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formatedDate);

                        if (selectedDateTimeId == R.id.textViewWorkStartDate) {
                            workStartDate = datetime;
                            strWorkStartDate = formatedDate;
                            franchiseeDetails.setCommencementStartDate(defaultFormattedDateTime);

                        } else if (selectedDateTimeId == R.id.textViewExpectedDate) {
                            expectedDate = datetime;
                            strExpectedDate = formatedDate;
                            String properFormatedDate = CommonUtils.getFormattedDate("yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy", defaultFormattedDateTime);
                            franchiseeDetails.setWipRevisedExpectedDateOfCompletion(properFormatedDate);

                            if (TextUtils.isEmpty(franchiseeDetails.getNextgenSiteWorkCommencementId())) {
                                franchiseeDetails.setCommencementEstimatedEndDate(defaultFormattedDateTime);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");

        // Feb 2018  Days Back To allow.
        if (selectedDateTimeId == R.id.textViewStartDate) {

            Calendar cal = Calendar.getInstance();
            //cal.setTime(new Date());
            //cal.add(Calendar.MONTH, -5); // current date to past 5 month date display
            cal.set(Calendar.YEAR, 2018);
            cal.set(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            dateTimePickerDialog.setMinDate(cal.getTimeInMillis());

        } else if (selectedDateTimeId == R.id.textViewWorkStartDate) {
            if (commencementMinDate == null) {
                dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            } else {
                dateTimePickerDialog.setMinDate(commencementMinDate.getTime()); // Received from Server.
            }
        } else if (selectedDateTimeId == R.id.textViewExpectedDate && !TextUtils.isEmpty(strWorkStartDate)) {
            dateTimePickerDialog.setMinDate(workStartDate.getTime());
        }
        dateTimePickerDialog.show();

    }

}
