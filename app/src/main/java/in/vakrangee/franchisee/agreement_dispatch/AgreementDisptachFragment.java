package in.vakrangee.franchisee.agreement_dispatch;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialog;
import in.vakrangee.supercore.franchisee.model.AgreementDispatchDto;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgreementDisptachFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private View view;
    private Context context;
    private static final String TAG = AgreementDisptachFragment.class.getSimpleName();
    private AgreementDispatchRepository agreementDispatchRepository;
    private AsyncgetAgreementSpinnerData asyncgetAgreementSpinnerData = null;
    private List<CustomFranchiseeApplicationSpinnerDto> agreementTypeList;
    private AgreementDispatchDto agreementDispatchDto;
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date incorporationDate;
    private String strIncorporationDate;
    private int selectedDateTimeId = 0;
    private String imgBase64;
    private Uri picUri;
    private Bitmap bitmap = null;
    private CustomImageZoomDialog customImagePreviewDialog;
    private PermissionHandler permissionHandler;
    private String mCurrentPhotoPath;
    private static final int CAMERA_PIC_REQUEST = 1;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;

    @BindView(R.id.radioGroupDelivery)
    RadioGroup radioGroupDelivery;
    @BindView(R.id.radioButtonCourier)
    RadioButton radioButtonCourier;
    @BindView(R.id.radioButtonHandDelivery)
    RadioButton radioButtonHandDelivery;
    //Hand Delivery Layout
    @BindView(R.id.mainLayoutHandDelivery)
    LinearLayout mainLayoutHandDelivery;
    @BindView(R.id.spinnerCourierType)
    CustomSearchableSpinner spinnerCourierType;
    @BindView(R.id.editTextCourierName)
    EditText editTextCourierName;
    @BindView(R.id.layoutStartDate)
    LinearLayout layoutStartDate;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.imgAgreementImage)
    ImageView imgAgreementImage;
    @BindView(R.id.editTextRemarks)
    EditText editTextRemarks;
    @BindView(R.id.editTextAwb)
    EditText editTextAwb;
    //Courier Layout
    @BindView(R.id.mainLayoutCourier)
    LinearLayout mainLayoutCourier;
    @BindView(R.id.editTextEmployeeID)
    EditText editTextEmployeeID;
    @BindView(R.id.editTextCourierRemarks)
    EditText editTextCourierRemarks;
    @BindView(R.id.textViewReceivedDate)
    TextView textViewReceivedDate;

    //complusory remarks
    @BindView(R.id.txtlayoutCourierNameLbl)
    TextView txtlayoutCourierNameLbl;
    @BindView(R.id.txtCourierDateLbl)
    TextView txtCourierDateLbl;
    @BindView(R.id.txtlayoutAwbLbl)
    TextView txtlayoutAwbLbl;
    @BindView(R.id.txtImagelayoutAgreementImage)
    TextView txtImagelayoutAgreementImage;
    @BindView(R.id.txtReceiveDateLbl)
    TextView txtReceiveDateLbl;
    @BindView(R.id.txtEmployeeIDLbl)
    TextView txtEmployeeIDLbl;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.btnClearSign)
    TextView btnClearSign;
    private DeprecateHandler deprecateHandler;
    private AsyncSaveAgreementDispatchDetails asyncSaveAgreementDispatchDetails;
    private Typeface font;


    public AgreementDisptachFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_agreement_disptach, container, false);

        ButterKnife.bind(this, view);
        this.context = getContext();

        agreementDispatchRepository = new AgreementDispatchRepository(context);
        agreementDispatchDto = new AgreementDispatchDto();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        layoutStartDate.setOnClickListener(this);

        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        btnSubmit.setTypeface(font);
        btnSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));
        btnSubmit.setOnClickListener(this);


        btnClearSign.setTypeface(font);
        btnClearSign.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " "));
        btnClearSign.setOnClickListener(this);

        textViewDate.setOnClickListener(this);
        imgAgreementImage.setOnClickListener(this);
        textViewReceivedDate.setOnClickListener(this);
        CommonUtils.InputFiletrWithMaxLength(editTextCourierName, "\"~#^|$%&*!'", 100);

        GUIUtils.CompulsoryMark(txtlayoutCourierNameLbl, "Courier Name ");
        GUIUtils.CompulsoryMark(txtCourierDateLbl, "Courier Date ");
        GUIUtils.CompulsoryMark(txtlayoutAwbLbl, "Airway Bill No (AWB) ");
        GUIUtils.CompulsoryMark(txtImagelayoutAgreementImage, "Airway Bill Image ");
        GUIUtils.CompulsoryMark(txtReceiveDateLbl, "Received Date ");
        GUIUtils.CompulsoryMark(txtEmployeeIDLbl, "Employee ID ");


        return view;
    }

    //region reload data
    public void reload(String result) {
        try {

            Type listType = new TypeToken<List<AgreementDispatchDto>>() {
            }.getType();

            if (result.startsWith("OKAY")) {
                result = result.replace("OKAY|", "");
                Gson gson = new Gson();


                List<AgreementDispatchDto> dispatchDtos = gson.fromJson(result, listType);

                if (dispatchDtos.size() != 0) {
                    agreementDispatchDto = dispatchDtos.get(0);

                    if (!TextUtils.isEmpty(dispatchDtos.get(0).getImage_id())) {
                        String imageUrl = Constants.DownloadImageUrl + agreementDispatchDto.getImage_id();
                        Glide.with(context)
                                .load(imageUrl)
                                .apply(new RequestOptions()
                                        .error(R.drawable.ic_camera_alt_black_72dp)
                                        .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true))
                                .into(imgAgreementImage);

                    } else {
                        Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(imgAgreementImage);
                    }
                    String remarks = TextUtils.isEmpty(agreementDispatchDto.getRemarks()) ? "" : agreementDispatchDto.getRemarks();
                    editTextRemarks.setText(remarks);
                    editTextAwb.setText(agreementDispatchDto.getAwb_no());

                    textViewDate.setText(agreementDispatchDto.getDate());
                    String setFormateDate = CommonUtils.getFormattedDate("dd-MM-yyyy", "yyyy-MM-dd", agreementDispatchDto.getDate());
                    agreementDispatchDto.setDate(setFormateDate);
                    //  textViewDate.setText(agreementDispatchDto.getDate());

                    editTextCourierName.setText(agreementDispatchDto.getCourier_name());


                }
            }


//        radioGroupDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radioButtonCourier) {
//                    mainLayoutHandDelivery.setVisibility(View.VISIBLE);
//                    mainLayoutCourier.setVisibility(View.GONE);
//                    agreementDispatchDto.setAgreementDispatchType("0");
//                } else if (checkedId == R.id.radioButtonHandDelivery) {
//                    Toast.makeText(context, "hand delivery", Toast.LENGTH_SHORT).show();
//                    mainLayoutHandDelivery.setVisibility(View.GONE);
//                    mainLayoutCourier.setVisibility(View.VISIBLE);
//                    agreementDispatchDto.setAgreementDispatchType("1");
//                }
//            }
//        });

            // asyncgetAgreementSpinnerData = new AsyncgetAgreementSpinnerData();
            //asyncgetAgreementSpinnerData.execute();

        } catch (Exception e) {
            e.getMessage();
        }
    }
    //endregion


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textViewDate:
                textViewDate.setError(null);
                selectedDateTimeId = id;
                showDateTimeDialogPicker();
                break;
            case R.id.btnSubmit:
                btnSubmitClick();
                break;
            case R.id.imgAgreementImage:
                imageCapture();
                break;
            case R.id.textViewReceivedDate:
                textViewReceivedDate.setError(null);
                selectedDateTimeId = id;
                showDateTimeDialogPicker();
                break;

        }

    }

    //region image capture
    private void imageCapture() {
        imgAgreementImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(imgBase64) && agreementDispatchDto.getImage_id() == null) {
                    startCamera(view);
                } else {
                    bitmap = CommonUtils.StringToBitMap(imgBase64);
                    showImagePreviewDialog(agreementDispatchDto);
                }
            }
        });

    }
    //endregion

    //region startCamera
    public void startCamera(View view) {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    try {
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   /* File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    picUri = Uri.fromFile(file); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                    i.putExtra("ImageId", picUri); // set the image file
                    startActivityForResult(i, CAMERA_PIC_REQUEST);*/
                }
            }
        });
    }
    //endregion

    //region dispatch Take Picture Intent
    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(); //CommonUtils.getOutputMediaFile(1);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //picUri = Uri.fromFile(createImageFile());
                picUri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }
    //endregion

    //region Create Image File
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    //endregion

    //region showImagePreviewDialog
    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImageZoomDialog(context, object, new CustomImageZoomDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    AgreementDispatchDto imageDto = ((AgreementDispatchDto) object);
                    imgBase64 = imageDto.getImage_base64();
                    if (!TextUtils.isEmpty(imgBase64)) {
                        bitmap = CommonUtils.StringToBitMap(imgBase64);
                        if (bitmap != null)
                            imgAgreementImage.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.setDialogTitle("Airway Bill Photo");
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Date Dialog picker
    private void showDateTimeDialogPicker() {
        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewDate) {
            try {
                // If date not available then default date
                // If date available then set that date
                //defaultDate = incorporationDate;
                if (!TextUtils.isEmpty(agreementDispatchDto.getDate()))
                    incorporationDate = dateFormatterYMD.parse(agreementDispatchDto.getDate());
                defaultDate = incorporationDate;
            } catch (Exception e) {
                e.printStackTrace();
            }
            // defaultDate = startDate;
        }

      /*  if (selectedDateTimeId == R.id.textViewReceivedDate) {
            try {
                if (!TextUtils.isEmpty(agreementDispatchDto.getDate()))
                    receivedDate = dateFormatter2.parse(agreementDispatchDto.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            defaultDate = receivedDate;
        }
*/
        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(context, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {

                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewDate) {
                            incorporationDate = datetime;
                            strIncorporationDate = formateYMD;
                            agreementDispatchDto.setDate(strIncorporationDate);

                            String DateOfIncorporation = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", agreementDispatchDto.getDate());
                            textViewDate.setText(DateOfIncorporation);

                        }


                       /* if (selectedDateTimeId == R.id.textViewReceivedDate) {
                            receivedDate = datetime;
                            strReceivedDate = formateYMD;
                            agreementDispatchDto.setDate(strReceivedDate);

                            //String rDate = kendraAcknowledgementDto.getReceivedDate();
                            String rDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", agreementDispatchDto.getDate());
                            //rDate = TextUtils.isEmpty(rDate) ? null : rDate.replace("am", "AM").replace("pm", "PM");
                            textViewReceivedDate.setText(rDate);
                        }*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Set Min and Max Date


        long now = new Date().getTime();
        int after4days = (1000 * 60 * 60 * 24 * 4);         //no Of Days
        //TODO: Get Last sixth month date from current date.
        Calendar c = Calendar.getInstance();
        c.set(2019, 0, 1);//Year,Month -1,Day
        if (selectedDateTimeId == R.id.textViewDate) {
            if (TextUtils.isEmpty(agreementDispatchDto.getMin_courier_date())) {
                dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            } else {
                dateTimePickerDialog.setMinDate(Long.parseLong(agreementDispatchDto.getMin_courier_date()));
            }
            //dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);

        }
      /*  if (selectedDateTimeId == R.id.textViewReceivedDate) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);

        }*/


        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();
    }
    //endregion

    //region button click - data save to server
    private void btnSubmitClick() {
        int valid = 0;

        if (mainLayoutHandDelivery.getVisibility() == View.VISIBLE) {
            valid = handDeliveryValidation();
        } else if (mainLayoutCourier.getVisibility() == View.VISIBLE) {
            valid = courierValidation();
        }


        if (valid == 0) {

            Gson gson = new Gson();
            String saveJsondata = null;
            if (mainLayoutCourier.getVisibility() == View.VISIBLE) {
                agreementDispatchDto.setEmployee_id(editTextEmployeeID.getText().toString());
                agreementDispatchDto.setRemarks(editTextCourierRemarks.getText().toString());
                agreementDispatchDto.setAgreementDispatchType("1");
                saveJsondata = gson.toJson(agreementDispatchDto, AgreementDispatchDto.class);
            } else if (mainLayoutHandDelivery.getVisibility() == View.VISIBLE) {
                agreementDispatchDto.setCourier_name(editTextCourierName.getText().toString());
                agreementDispatchDto.setAwb_no(editTextAwb.getText().toString());
                agreementDispatchDto.setRemarks(editTextRemarks.getText().toString());
                agreementDispatchDto.setAgreementDispatchType("0");


                saveJsondata = gson.toJson(agreementDispatchDto, AgreementDispatchDto.class);
            }

            //dispatchDtos = gson.fromJson(saveJsondata, listType);
            saveDataToServer(saveJsondata);
        }
    }
    //endregion

    //region courier validation
    private int courierValidation() {
        //STEP 1: Select Date
       /* if (TextUtils.isEmpty(agreementDispatchDto.getDate())) {
            textViewReceivedDate.setError("Please select Date.");
            return 1;
        }*/

        if (TextUtils.isEmpty(editTextEmployeeID.getText().toString())) {
            editTextEmployeeID.setError("Please enter Employee ID.");
            return 2;
        }


        /*if (TextUtils.isEmpty(editTextCourierRemarks.getText().toString())) {
            editTextCourierRemarks.setError("Please enter Remarks. ");
            return 3;
        }*/
        return 0;
    }
    //endregion

    //region asynctask  Agreement Dispatch Details data save to server
    private void saveDataToServer(String jsondata) {
        String type = null;
        //type COURIER = "2" , HAND_DELIVERY = "1"

        if (agreementDispatchDto.getAgreementDispatchType().equalsIgnoreCase("0")) {
            type = "2";
        } else if (agreementDispatchDto.getAgreementDispatchType().equalsIgnoreCase("1")) {
            type = "1";
        }

        asyncSaveAgreementDispatchDetails = new AsyncSaveAgreementDispatchDetails(context, type, jsondata, new AsyncSaveAgreementDispatchDetails.Callback() {
            @Override
            public void onResult(String result) {
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(context, result);
                    return;
                }
                if (result.startsWith("ERROR")) {
                    result = result.replace("ERROR|", "");
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, result);
                    }
                } else if (result.startsWith("OKAY")) {
                    reload(result);
                } else {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));

                }

            }
        });
        asyncSaveAgreementDispatchDetails.execute();

    }
    //endregion

    //region validation handon delivery
    private int handDeliveryValidation() {
        //STEP 1: Courier Name
        if (TextUtils.isEmpty(editTextCourierName.getText().toString())) {
            editTextCourierName.setError("Please enter handover name. ");
            return 1;
        }
        //STEP 2: Select Date
        if (TextUtils.isEmpty(agreementDispatchDto.getDate())) {
            textViewDate.setError("Please select Date.");
            return 2;
        }

        //STEP 3: AWB number
        if (TextUtils.isEmpty(editTextAwb.getText().toString())) {
            editTextAwb.setError("Please enter AWB number. ");
            return 3;
        }

        //STEP 4: Capture Image
        if (TextUtils.isEmpty(agreementDispatchDto.getImage_base64()) && agreementDispatchDto.getImage_id() == null) {
            //editTextAwb.setError("Please capture image. ");
            Toast.makeText(context, "Please capture image.", Toast.LENGTH_SHORT).show();
            return 4;
        }
       /* //STEP 5: Remarks
        if (TextUtils.isEmpty(editTextRemarks.getText().toString())) {
            editTextRemarks.setError("Please enter Remarks. ");
            return 5;
        }
*/
        return 0;

    }
    //endregion

    //region  Asyncget Service Spinner Data
    class AsyncgetAgreementSpinnerData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                agreementTypeList = agreementDispatchRepository.getAgreementTypeList();

            } catch (Exception e) {
                Log.e(TAG, "Failed to get AgreementDisptachFragment Data. ");
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                dismissProgressSpinner();
                bindServiceSpinner();
            } catch (Exception e) {
                e.getMessage();
                Log.e(TAG, "Failed to get AgreementDisptachFragment Data. " + s);
            }
        }
    }

    //endregion

    //region bind Spinner
    private void bindServiceSpinner() {
        spinner_focusablemode(spinnerCourierType);

        spinnerCourierType.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, agreementTypeList));
        int appPosterPos = agreementDispatchRepository.getSelectedPos(agreementTypeList, agreementDispatchDto.getAgreementDispatchType());
        spinnerCourierType.setSelection(appPosterPos);
        spinnerCourierType.setOnItemSelectedListener(this);

    }
    //endregions

    //region Spinner On Item Selected
    @OnItemSelected({R.id.spinnerCourierType})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        int Id = parent.getId();

        switch (Id) {
            case R.id.spinnerCourierType:
                if (position > -1) {
                    spinnerCourierType.setTitle("Select Courier Type");
                    spinnerCourierType.requestFocus();
                    CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerCourierType.getItemAtPosition(position);
                    if (!entityDto.getId().equals("-1")) {
                        agreementDispatchDto.setAgreementDispatchType(entityDto.getId());
                        if (entityDto.getId().equalsIgnoreCase("1")) {
                            mainLayoutHandDelivery.setVisibility(View.GONE);
                            mainLayoutCourier.setVisibility(View.VISIBLE);
                            agreementDispatchDto.setAgreementDispatchType(entityDto.getId());
                        } else if (entityDto.getId().equalsIgnoreCase("0")) {
                            mainLayoutHandDelivery.setVisibility(View.VISIBLE);
                            mainLayoutCourier.setVisibility(View.GONE);
                            agreementDispatchDto.setAgreementDispatchType(entityDto.getId());
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //region spinner  focusable mode
    private void spinner_focusablemode(CustomSearchableSpinner stateSpinner) {
        stateSpinner.setFocusable(true);
        stateSpinner.setFocusableInTouchMode(true);
    }
    //endregion


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncgetAgreementSpinnerData != null && !asyncgetAgreementSpinnerData.isCancelled()) {
            asyncgetAgreementSpinnerData.cancel(true);
        }
    }

    //endregion

    //region onActivityResult for camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(context.getContentResolver(), imageUri, latitude, longitude, currentTimestamp);
                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);
                String imageBase64 = ImageUtils.updateExifData(imageUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);
                if (!CommonUtils.isLandscapePhoto(imageUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(context, getString(R.string.landscape_mode_allowed));
                } else {

                    agreementDispatchDto.setImage_base64(imageBase64);
                    showImagePreviewDialog((Object) agreementDispatchDto);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    //endregion

    //region Location Time stamp
    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }
    //endregion


}
