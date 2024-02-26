package in.vakrangee.franchisee.workinprogress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageGalleryFragment;
import in.vakrangee.supercore.franchisee.ifc.IImagesHandler;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.support.MileStoneDetailDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;

@SuppressLint("ValidFragment")
public class NextGenWorkInProgressFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NextGenWorkInProgressFragment";

    private Context context;
    private Button btnClear, btnCancel, btnSubmitWorkInProgress;
    private EditText editTextRemarks;
    private FranchiseeDetails franchiseeDetails;
    private ImageGalleryFragment fragmentImageGallery;
    private List<ImageDto> imagesList = new ArrayList<ImageDto>();
    private RadioGroup radioGroupWorkOnTrack;
    private TextView txtVisitDate, textViewStartDate, txtExpectedDate;
    private int selectedDateTimeId = 0;
    private Date visitDate, startDate, expectedDate;
    private String strVisitDate, strStartDate, strExpectedDate;
    private DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US); //new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US);
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat yearDateFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private DateFormat dateFormatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    View view;
    private LinearLayout layoutStartDate, layoutExpectedDate;
    private Logger logger;
    private AsyncUpdateWorkInProgress asyncUpdateWorkInProgress;
    LinearLayout layoutFooter;
    private boolean IsEditable;
    private TextView NextGenFranchiseeApplicationNo, VKID, FranchiseeName;
    private static final String JSON_KEY_WIP_IMAGES = "wipImages";
    private LinearLayout layoutWIPDetails;
    private ImageCorrectionAdapter imageCorrectionAdapter;
    private RecyclerView recyclerViewCorrection;
    private LinearLayout layoutrecyclerCorrection;
    private ImageView profilepic;
    private TextView txtAddress;

    private TextView textViewLastVisit;
    private GPSTracker gpsTracker;
    private Spinner spinnerWorkStatusType;
    private LinearLayout layoutWorkStatusType;
    private CustomSpinnerAdapter customSpinnerAdapter;

    private TextView txtExpectedDateLbl;

    // Commencement Min Date
    private String strCommencementMinDate;
    private Date commencementMinDate;

    private NextGenWorkInProgressFragment() {
    }

    public NextGenWorkInProgressFragment(FranchiseeDetails franchiseeDetails, boolean IsEditable) {
        this.franchiseeDetails = franchiseeDetails;
        this.IsEditable = IsEditable;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.next_gen_work_in_progress, container, false);

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        gpsTracker = new GPSTracker(context);

        // Font
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        //Widgets
        layoutWorkStatusType = view.findViewById(R.id.layoutWorkStatusType);
        spinnerWorkStatusType = view.findViewById(R.id.spinnerWorkStatusType);
        layoutrecyclerCorrection = view.findViewById(R.id.layoutrecyclerCorrection);
        recyclerViewCorrection = view.findViewById(R.id.recyclerViewCorrection);
        layoutWIPDetails = view.findViewById(R.id.layoutWIPDetails);
        editTextRemarks = view.findViewById(R.id.editTextRemarks);
        CommonUtils.applyInputFilter(editTextRemarks, "\"~#^|$%&*!'");
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubmitWorkInProgress = view.findViewById(R.id.btnSubmitWIP);
        btnClear = view.findViewById(R.id.btnClear);
        layoutStartDate = view.findViewById(R.id.layoutStartDate);
        layoutExpectedDate = view.findViewById(R.id.layoutExpectedDate);
        layoutFooter = view.findViewById(R.id.layoutFooter);
        radioGroupWorkOnTrack = view.findViewById(R.id.radioGroupWorkOnTrack);
        txtVisitDate = view.findViewById(R.id.textViewVisitDate);
        txtExpectedDateLbl = view.findViewById(R.id.txtExpectedDateLbl);
        textViewStartDate = view.findViewById(R.id.textViewStartDate);
        txtExpectedDate = view.findViewById(R.id.textViewExpectedDate);
        fragmentImageGallery = (ImageGalleryFragment) getChildFragmentManager().findFragmentById(R.id.fragmentImages);
        NextGenFranchiseeApplicationNo = (TextView) view.findViewById(R.id.frachicessno);
        VKID = (TextView) view.findViewById(R.id.vkid_name);
        FranchiseeName = (TextView) view.findViewById(R.id.frachicessname);
        profilepic = (ImageView) view.findViewById(R.id.profilepic);
        txtAddress = view.findViewById(R.id.address);
        textViewLastVisit = view.findViewById(R.id.textViewLastVisit);

        if (franchiseeDetails.getFranchiseePicFile() != null) {
            Bitmap img = CommonUtils.StringToBitMap(franchiseeDetails.getFranchiseePicFile());
            profilepic.setImageBitmap(img);
        }

        NextGenFranchiseeApplicationNo.setText(franchiseeDetails.getNextGenFranchiseeApplicationNo() == null ? "UNKNOWN" : franchiseeDetails.getNextGenFranchiseeApplicationNo());
        VKID.setText(franchiseeDetails.getVKID());
        FranchiseeName.setText(franchiseeDetails.getFranchiseeName());
        txtAddress.setText(franchiseeDetails.getVAddress());

        //Set Last Visited Date
        String lastVisit = "";
        if ((!TextUtils.isEmpty(franchiseeDetails.getWipLastVisitedDateTime())) && (!TextUtils.isEmpty(franchiseeDetails.getWipLastVisitedBy())))
            //lastVisit = "<b>On:&nbsp; </b>" + franchiseeDetails.getWipLastVisitedDateTime() + "<br/> <b>By:&nbsp; </b>" + franchiseeDetails.getWipLastVisitedBy();
            lastVisit = franchiseeDetails.getWipLastVisitedDateTime();
        else
            lastVisit = getString(R.string.not_visited_yet);

        textViewLastVisit.setText(Html.fromHtml(lastVisit));

        reloadData();
        reloadWorkStatustypeSpinner();
        layoutStartDate.setVisibility(View.VISIBLE);
        layoutExpectedDate.setVisibility(View.VISIBLE);
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

        // Set Font
        btnClear.setTypeface(font);
        btnCancel.setTypeface(font);
        btnSubmitWorkInProgress.setTypeface(font);

        // Set Font Text
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnSubmitWorkInProgress.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        textViewStartDate.setOnClickListener(this);
        txtExpectedDate.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmitWorkInProgress.setOnClickListener(this);

        logger.writeError(TAG, "Testing: Before formatting: VisitDate: " + franchiseeDetails.getWipVisitDateTime() + " Expected Date: " + franchiseeDetails.getWipRevisedExpectedDateOfCompletion());

        return view;
    }

    public void reloadWorkStatustypeSpinner() {
        ArrayList<MileStoneDetailDto> workStatusList = new ArrayList<MileStoneDetailDto>();

        try {
            String workStatusType = franchiseeDetails.getWipMileStoneDetail();
            if (TextUtils.isEmpty(workStatusType)) {
                workStatusList.add(0, new MileStoneDetailDto("0", getResources().getString(R.string.unscheduled)));

            } else {
                JSONObject jsonObject = new JSONObject(workStatusType);
                JSONArray jsonArray = jsonObject.getJSONArray("mileStoneDetail");

                Gson gson = new GsonBuilder().create();
                workStatusList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<MileStoneDetailDto>>() {
                }.getType());
                workStatusList.add(0, new MileStoneDetailDto("0", getResources().getString(R.string.unscheduled)));
            }

            ArrayList<Object> list1 = new ArrayList<Object>(workStatusList);
            customSpinnerAdapter = new CustomSpinnerAdapter(context, list1);
            spinnerWorkStatusType.setAdapter(customSpinnerAdapter);
            int selPos = getSelWorkStatustypePos(workStatusList);
            spinnerWorkStatusType.setSelection(selPos);
            spinnerWorkStatusType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0) {
                        MileStoneDetailDto selDto = (MileStoneDetailDto) spinnerWorkStatusType.getItemAtPosition(position);
                        franchiseeDetails.setWipMileStoneId(selDto.getId());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        } catch (Exception e) {
            e.toString();
        }
    }

    public int getSelWorkStatustypePos(List<MileStoneDetailDto> mileStoneDetailList) {

        if (TextUtils.isEmpty(franchiseeDetails.getWipMileStoneId()))
            return 0;

        for (int i = 0; i < mileStoneDetailList.size(); i++) {
            if (franchiseeDetails.getWipMileStoneId().equalsIgnoreCase(mileStoneDetailList.get(i).getId()))
                return i;
        }
        return 0;
    }

    public List<MileStoneDetailDto> filterSelWorkStatusTypeList(List<MileStoneDetailDto> mileStoneDetailList) {
        List<MileStoneDetailDto> filteredList = new ArrayList<MileStoneDetailDto>();

        //Work Status Type
        filteredList.addAll(mileStoneDetailList);
        if (TextUtils.isEmpty(franchiseeDetails.getWipMileStoneId()))
            return filteredList;

        for (MileStoneDetailDto dto : mileStoneDetailList) {
            if (dto.getId().equalsIgnoreCase(franchiseeDetails.getWipMileStoneId())) {
                filteredList.remove(dto);
            }
        }
        return filteredList;
    }

    public boolean IsValidLocation() {
        if (gpsTracker.canGetLocation()) {
            if (TextUtils.isEmpty(franchiseeDetails.getLatitude()) || TextUtils.isEmpty(franchiseeDetails.getLongitude()) || franchiseeDetails.getWipLocationRange() == 0)
                return true;

            int distance = (int) gpsTracker.getDistance(Double.parseDouble(franchiseeDetails.getLatitude()), Double.parseDouble(franchiseeDetails.getLongitude()));
            if (distance <= franchiseeDetails.getWipLocationRange())
                return true;
        }
        return false;
    }

    public void reloadImageFragment() {
        Bundle args = new Bundle();
        args.putSerializable(Constants.INTENT_KEY_IMAGE_GALLERY_LIST, (Serializable) imagesList);
        args.putString(Constants.INTENT_KEY_TYPE, Constants.RECYCLER_TYPE_HORIZONTAL);
        args.putString(Constants.INTENT_KEY_TITLE, context.getString(R.string.wip_images));
        args.putString("BrandingElements", franchiseeDetails.getBranding_element_details());
        args.putBoolean(Constants.INTENT_KEY_PORTRAIT_ALLOWED, true);
        args.putInt(Constants.INTENT_VALUE_MAX_IMAGES_COUNT, 10);
        fragmentImageGallery.refresh(args, new IImagesHandler() {
            @Override
            public void updateImagesList(List<ImageDto> imageDtoList) {
                imagesList = imageDtoList;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.btnSubmitWIP) {
            submitWorkInProgressDetails();
        } else if (Id == R.id.btnClear) {
            resetData();
        } else if (Id == R.id.btnCancel) {
            cancelWorkInProgress();
        } else if (Id == R.id.textViewStartDate) {
            textViewStartDate.setError(null);
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();
        } else if (Id == R.id.textViewExpectedDate) {

            if (!TextUtils.isEmpty(strStartDate)) {
                selectedDateTimeId = Id;
                showDateTimeDialogPicker();
            } else {
                Toast.makeText(getActivity(), "Please Select Start Date.", Toast.LENGTH_LONG).show();
                textViewStartDate.setError("Select Start Date.");
            }
          /*
            if (!TextUtils.isEmpty(strVisitDate)) {
                selectedDateTimeId = Id;
                showDateTimeDialogPicker();
            } else {
                txtExpectedDate.setError(getResources().getString(R.string.select_visit_date));
            }*/
        }
    }

    //region Show Schedule Visit DateTime Picker Dialog
    private DateTimePickerDialog dateTimePickerDialog;

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewVisitDate) {
            defaultDate = visitDate;
        } else if (selectedDateTimeId == R.id.textViewExpectedDate) {
            defaultDate = expectedDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter.format(datetime);

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setError(null);
                        textViewDateTime.setText(formatedDate);

                        if (selectedDateTimeId == R.id.textViewVisitDate) {
                            visitDate = datetime;
                            strVisitDate = formatedDate;
                            franchiseeDetails.setWipVisitDateTime(defaultFormattedDateTime);

                        } else if (selectedDateTimeId == R.id.textViewStartDate) {
                            startDate = datetime;
                            strStartDate = formatedDate;
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
                    logger.writeError(TAG, "DateTimePickerDialog: Error: " + e.toString());
                }
            }
        });
        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");

        // 7 Days Back To allow.
        if (selectedDateTimeId == R.id.textViewVisitDate) {
            dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000

        } else if (selectedDateTimeId == R.id.textViewStartDate) {
            /*if(TextUtils.isEmpty(strStartDate)) {
                dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            }
            else {
                Date newDate = new Date(startDate.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
                dateTimePickerDialog.setMinDate(newDate.getTime());
            }*/
            if (commencementMinDate == null) {
                dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            } else {
                dateTimePickerDialog.setMinDate(commencementMinDate.getTime()); // Received from Server.
            }
        }
        /*else if (selectedDateTimeId == R.id.textViewExpectedDate && !TextUtils.isEmpty(strVisitDate)) {
            dateTimePickerDialog.setMinDate(visitDate.getTime());
        }*/
        else if (selectedDateTimeId == R.id.textViewExpectedDate && !TextUtils.isEmpty(strStartDate)) {
            dateTimePickerDialog.setMinDate(startDate.getTime());
        }
        dateTimePickerDialog.show();

    }
    //endregion

    public void resetData() {
        //Images
        imagesList.clear();
        fragmentImageGallery.notifyAdapter();

        //Field Force Remarks
        editTextRemarks.setText("");
        editTextRemarks.setError(null);
        franchiseeDetails.setWipFieldForceRemarks(null);

        // WIP Visit date
        /*strVisitDate = null;
        visitDate = null;
        txtVisitDate.setText("");
        franchiseeDetails.setWipVisitDateTime(null);*/

        // WIP Start Date
        strStartDate = null;
        startDate = null;
        textViewStartDate.setText("");
        franchiseeDetails.setCommencementStartDate(null);

        // WIP Expected Date
        strExpectedDate = null;
        expectedDate = null;
        txtExpectedDate.setText("");
        franchiseeDetails.setWipRevisedExpectedDateOfCompletion(null);

        //Work On Track
        franchiseeDetails.setWipWorkOnTrack(1);

        // DateTime View Id
        selectedDateTimeId = 0;
    }

    public int validateWorkInProgressData() {

        // Validate Location distance range
        if (!IsValidLocation())
            return -101;

        // Check Images size
        if (imagesList.size() == 0)
            return -1;

        // Check Start is available or not.
        if (TextUtils.isEmpty(franchiseeDetails.getCommencementStartDate()))
            return -5;

        // Check Expected Date
        if (TextUtils.isEmpty(franchiseeDetails.getWipRevisedExpectedDateOfCompletion()))
            return -3;

        if (expectedDate.before(startDate)) {
            return -6;
        }

        // Check Work On Track
        if (franchiseeDetails.getWipWorkOnTrack() == 0) {     //1 = YES, 0 = NO
            // Check if Remarks is filled
            if (TextUtils.isEmpty(editTextRemarks.getText().toString()))
                return -4;
        }

        return 0;
    }

    public void submitWorkInProgressDetails() {
        int status = validateWorkInProgressData();

        if (status == -101) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));

        } else if (status == -1) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.please_add_images));

        } else if (status == -2) {
            txtVisitDate.setError(getResources().getString(R.string.select_visit_date));

        } else if (status == -3) {
            txtExpectedDate.setError(getResources().getString(R.string.select_expected_date));

        } else if (status == -4) {
            editTextRemarks.setError(getResources().getString(R.string.enterRemarks));

        } else if (status == -5) {
            txtExpectedDate.setError(getResources().getString(R.string.select_start_date));
        } else if (status == -6) {
            AlertDialogBoxInfo.alertDialogShow(context, getString(R.string.alert_msg_work_date_validation));
        } else {
            franchiseeDetails.setWipFieldForceRemarks(editTextRemarks.getText().toString());

            //Format Visit Date and Revised Expected DOB
            String visitDate = franchiseeDetails.getWipVisitDateTime();
            String expectedDate = franchiseeDetails.getWipRevisedExpectedDateOfCompletion();

            if (!TextUtils.isEmpty(visitDate))
                franchiseeDetails.setWipVisitDateTime(CommonUtils.getFormattedDate("dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", visitDate));

            if (!TextUtils.isEmpty(expectedDate))
                franchiseeDetails.setWipRevisedExpectedDateOfCompletion(CommonUtils.getFormattedDate("dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", expectedDate));

            logger.writeError(TAG, "Testing: After formatting: VisitDate: " + franchiseeDetails.getWipVisitDateTime() + " Expected Date: " + franchiseeDetails.getWipRevisedExpectedDateOfCompletion());

            try {
                Gson gson = new Gson();
                String data = gson.toJson(imagesList, new TypeToken<ArrayList<ImageDto>>() {
                }.getType());
                franchiseeDetails.setWipImages(data);
                String jsonData = JSONUtils.toString(franchiseeDetails);

                asyncUpdateWorkInProgress = new AsyncUpdateWorkInProgress(getActivity(), new AsyncUpdateWorkInProgress.Callback() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResult(String result) {
                        try {
                            if (TextUtils.isEmpty(result)) {
                                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                                return;
                            }

                            Log.e(TAG, "Result : " + result);
                            if (result.startsWith("OKAY")) {
                                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.alert_msg_work_in_progress_success));
                                // Hide Layout Footer [Cancel, Clear and Submit Button]
                                disableViews();

                            } else {
                                AlertDialogBoxInfo.showOkDialog(getActivity(), getResources().getString(R.string.alert_msg_work_in_progress_fail));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        }
                    }
                });
                asyncUpdateWorkInProgress.execute(jsonData);

            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "Error: " + e.toString());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncUpdateWorkInProgress != null && !asyncUpdateWorkInProgress.isCancelled()) {
            asyncUpdateWorkInProgress.cancel(true);
        }
    }

    public void cancelWorkInProgress() {
        getActivity().finish();
        ((NextGenWorkInProgressActivity) getActivity()).viewPager.setCurrentItem(0, true);
    }

    @SuppressLint("LongLogTag")
    public void reloadData() {

        if (franchiseeDetails == null)
            return;

        try {

            //Set Minimum Commencement Date
            strCommencementMinDate = franchiseeDetails.getCommencementMinDate();
            Log.e(TAG, "Commencement Min Date : " + strCommencementMinDate);
            commencementMinDate = dateFormatter3.parse(strCommencementMinDate);

            // Set Visited and Expected Date
            if (TextUtils.isEmpty(franchiseeDetails.getWipVisitDateTime())) {
                setCurrentDateIntoVisitDate();
            } else {
                strVisitDate = franchiseeDetails.getWipVisitDateTime();
                if (!TextUtils.isEmpty(strVisitDate)) {
                    visitDate = dateFormatter.parse(strVisitDate);
                    txtVisitDate.setText(dateFormatter2.format(visitDate));
                }
            }

            // Set Start and Estimated End Date
            strStartDate = franchiseeDetails.getCommencementStartDate();
            if (!TextUtils.isEmpty(strStartDate)) {
                startDate = dateFormatter3.parse(strStartDate);
                textViewStartDate.setText(CommonUtils.getFormattedDate("yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy", strStartDate)); //dateFormatter2.format(startDate));
            }

            if (!TextUtils.isEmpty(franchiseeDetails.getNextgenSiteWorkCommencementId()))
                textViewStartDate.setEnabled(false);
            else
                textViewStartDate.setEnabled(true);

            strExpectedDate = franchiseeDetails.getWipRevisedExpectedDateOfCompletion();
            if (!TextUtils.isEmpty(strExpectedDate)) {
                expectedDate = dateFormatter.parse(strExpectedDate);
                txtExpectedDate.setText(dateFormatter2.format(expectedDate));
            }

            editTextRemarks.setText(franchiseeDetails.getWipFieldForceRemarks());

            //Images List
            if (!TextUtils.isEmpty(franchiseeDetails.getWipImages()))
                imagesList = getAlreadyExistImagesList();

            reloadImageFragment();

            //Work On Track
            if (franchiseeDetails.getWipWorkOnTrack() == 0 && !TextUtils.isEmpty(franchiseeDetails.getWipRevisedExpectedDateOfCompletion())) {
                radioGroupWorkOnTrack.check(R.id.radioButtonNo);
                franchiseeDetails.setWipWorkOnTrack(0);
                //layoutExpectedDate.setVisibility(View.VISIBLE);
                txtExpectedDateLbl.setText("Revised Expected Date Of Completion");

            } else {
                radioGroupWorkOnTrack.check(R.id.radioButtonYes);
                franchiseeDetails.setWipWorkOnTrack(1);
                //layoutExpectedDate.setVisibility(View.GONE);
                txtExpectedDateLbl.setText("Expected Date Of Completion");
            }

            //Edit mode or not
            if (!IsEditable) {
                disableViews();
            }

            //Correction Adapter
            setCorrectionAdapter();

        } catch (Exception pe) {
            pe.toString();
            logger.writeError(TAG, "reloadData: Error: " + pe.toString());
        }
    }

    public void setCurrentDateIntoVisitDate() {

        Date datetime = new Date();
        String formatedDate = dateFormatter.format(datetime);
        visitDate = datetime;
        strVisitDate = formatedDate;
        txtVisitDate.setText(dateFormatter2.format(visitDate));
        franchiseeDetails.setWipVisitDateTime(dateFormatter.format(new Date()));

    }

    /**
     * Prepare already existing Images list
     *
     * @return
     */
    private List<ImageDto> getAlreadyExistImagesList() {

        try {
            JSONObject jsonObject = new JSONObject(franchiseeDetails.getWipImages());
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_KEY_WIP_IMAGES);
            Gson gson = new GsonBuilder().create();

            for (int i = 0; i < jsonArray.length(); i++) {

                ImageDto imageDto = gson.fromJson(jsonArray.getJSONObject(i).toString(), ImageDto.class);
                String imageBase64 = imageDto.getImageBase64();
                imageDto.setBitmap(CommonUtils.StringToBitMap(imageBase64));
                imageDto.setName("Photo " + (i + 1));
                imagesList.add(imageDto);

                //imagesList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ImageDto>>() {}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "getAlreadyExistImagesList: Error: " + e.toString());
        }
        return imagesList;
    }

    private void disableViews() {
        layoutFooter.setVisibility(View.GONE);
        GUIUtils.setViewAndChildrenEnabled(layoutWIPDetails, false);
        fragmentImageGallery.disableViews();
    }

    public void setCorrectionAdapter() {

        //imagesList = getCorrectionImageList();

        List<ImageDto> filteredList = filteredWithVLRemarks();
        if (filteredList.size() == 0) {
            layoutrecyclerCorrection.setVisibility(View.GONE);
        } else {
            layoutrecyclerCorrection.setVisibility(View.VISIBLE);

            imageCorrectionAdapter = new ImageCorrectionAdapter(context, filteredList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerViewCorrection.setLayoutManager(mLayoutManager);
            recyclerViewCorrection.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCorrection.setAdapter(imageCorrectionAdapter);
            recyclerViewCorrection.setNestedScrollingEnabled(false);
        }
    }

    public List<ImageDto> filteredWithVLRemarks() {
        List<ImageDto> filteredList = new ArrayList<ImageDto>();

        for (ImageDto dto : imagesList) {
            if (!TextUtils.isEmpty(dto.getVl_remarks())) {
                filteredList.add(dto);
            }
        }
        return filteredList;
    }

    /**
     * TODO: Remove after testing
     */
   /* public List<ImageDto> getCorrectionImageList() {
        ImageDto dto = imagesList.get(0);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.forntimage);
        dto.setBitmap(icon);
        dto.setVl_remarks("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        imagesList.set(0, dto);

        ImageDto dto1 = imagesList.get(2);
        dto1.setBitmap(icon);
        dto1.setVl_remarks("Image not proper");
        imagesList.set(2, dto1);
        return imagesList;
    }*/
}
