package in.vakrangee.franchisee.documentmanager.qualification;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.documentmanager.DocumentManagerActivity;
import in.vakrangee.franchisee.documentmanager.DocumentManagerRepository;
import in.vakrangee.franchisee.documentmanager.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.MonthYearPickerDialog;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class QualificationDetailsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "QualificationDetailsFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private QualificationDetailsDto qualificationDetailsDto;

    // View Objects
    private EditText editTextInstituteName;
    private TextView txtQualificationLbl, txtInstituteNameLbl, txtYearOfPassingLbl, txtQualificationCopyLbl;
    private TextView textViewYearOfPassing;
    private ImageView imgQualification;
    public CustomSearchableSpinner spinnerQualification;
    private LinearLayout layoutQualificationDetail;

    //img
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int QUALIFICATION_IMAGE_PHOTO = 1;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog;
    private List<CustomFranchiseeApplicationSpinnerDto> qualificationList;
    private DocumentManagerRepository documentManagerRepo;
    private GetAllSpinnerData getAllSpinnerData = null;
    private MonthYearPickerDialog monthYearPickerDialog = null;

    public QualificationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_qualification_details, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        documentManagerRepo = new DocumentManagerRepository(context);

        // Bind UI
        editTextInstituteName = view.findViewById(R.id.editTextInstituteName);
        txtQualificationLbl = view.findViewById(R.id.txtQualificationLbl);
        txtInstituteNameLbl = view.findViewById(R.id.txtInstituteNameLbl);
        txtYearOfPassingLbl = view.findViewById(R.id.txtYearOfPassingLbl);
        txtQualificationCopyLbl = view.findViewById(R.id.txtQualificationCopyLbl);
        textViewYearOfPassing = view.findViewById(R.id.textViewYearOfPassing);
        imgQualification = view.findViewById(R.id.imgQualification);
        spinnerQualification = view.findViewById(R.id.spinnerQualification);
        layoutQualificationDetail = view.findViewById(R.id.layoutQualificationDetail);

        imgQualification.setOnClickListener(this);
        textViewYearOfPassing.setOnClickListener(this);

        CommonUtils.InputFiletrWithMaxLength(editTextInstituteName, "~#^|$%&*!", 50);

        setCompulsoryMarkLabel();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    //region realod data
    public void reload(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            qualificationDetailsDto = new QualificationDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<QualificationDetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<QualificationDetailsDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    qualificationDetailsDto = applicationList.get(0);
                } else {
                    qualificationDetailsDto = new QualificationDetailsDto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");

    }
    //endregion

    //region Set Qualification Adapter
    private void setQualificationSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> qualificationList, Spinner spinnerQualification, String selQualificationId) {

        spinnerQualification.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, qualificationList));
        int pos = documentManagerRepo.getSelectedPos(qualificationList, selQualificationId);
        spinnerQualification.setSelection(pos);
        spinnerQualification.setOnItemSelectedListener(this);

    }
    //endregion

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerQualification) {
            if (position > 0) {
                spinnerQualification.setTitle("Select Qualification");
                spinnerQualification.requestFocus();
                CustomFranchiseeApplicationSpinnerDto stateDto = (CustomFranchiseeApplicationSpinnerDto) spinnerQualification.getItemAtPosition(position);
                if (!stateDto.getId().equals("0")) {
                    qualificationDetailsDto.setQualificationId(stateDto.getId());
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                qualificationDetailsDto.setQualificationId("0");

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //region bind data
    private void bindData() {

        //Qualification
        setQualificationSpinnerAdapter(qualificationList, spinnerQualification, qualificationDetailsDto.getQualificationId());

        //Institute Name
        editTextInstituteName.setText(qualificationDetailsDto.getInstituteName());

        //Year Of Passing
        if (!TextUtils.isEmpty(qualificationDetailsDto.getYearOfPassing())) {
            textViewYearOfPassing.setText(qualificationDetailsDto.getYearOfPassing());
        }

        //Qualification Scanned Copy
        boolean IsPDF = (qualificationDetailsDto.getQualificationScanExt() != null && qualificationDetailsDto.getQualificationScanExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .override(200, 200)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgQualification);

        } else {
            if (!TextUtils.isEmpty(qualificationDetailsDto.getQualificationScanId())) {
                String gstUrl = Constants.DownloadImageUrl + qualificationDetailsDto.getQualificationScanId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgQualification);
            }
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(qualificationDetailsDto.getIsEditable()) && qualificationDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutQualificationDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);

    }
    //endregion

    //region compulsaroy red mark
    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtQualificationLbl, txtInstituteNameLbl, txtYearOfPassingLbl, txtQualificationCopyLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }
    //endregion

    //region onclick image and datepicker
    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgQualification) {
            showImgOrPDF(v);
        } else if (Id == R.id.textViewYearOfPassing) {
            showMonthYearDialog();
        }
    }
    //endregion

    //region On Activity Result

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, "", "", "");
                File file = new File(picUri.getPath());
                String base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                setImageData(picUri, base64Data, bitmapNew);

            }
            if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 1) {
                        showMessage(context.getResources().getString(R.string.file_size_msg));
                        return;
                    }

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data;

                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                        Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgQualification);
                        qualificationDetailsDto.setQualificationScanBase64(base64Data);
                        qualificationDetailsDto.setQualificationScanExt(ext);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        bitmapNew = ImageUtils.getResizedBitmap(bitmapNew);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                        setImageData(uri, base64Data, bitmapNew);
                    }
                }
            }
            if (requestCode == SCANLIB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                picUri = uri;

                File file = new File(uri.getPath());

                //getActivity().getContentResolver().delete(uri, null, null);
                String base64Data = CommonUtils.convertBitmapToString(bitmap);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");

                setImageData(picUri, base64Data, bitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    private void setImageData(Uri imageUri, String imageBase64, Bitmap bitmapNew) {

        GSTINDTO imageDto = new GSTINDTO();
        imageDto.setUri(imageUri);
        imageDto.setBitmap(bitmapNew);
        imageDto.setChangedPhoto(true);
        imageDto.setGstImage(imageBase64);

        showImagePreviewDialog((Object) imageDto);
    }

    public void showAttachmentDialog(final View view) {
        fileAttachementDialog = new FileAttachmentDialog(context, new FileAttachmentDialog.IFileAttachmentClicks() {
            @Override
            public void cameraClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            FROM = QUALIFICATION_IMAGE_PHOTO;

                            String ext = TextUtils.isEmpty(qualificationDetailsDto.getQualificationScanExt()) ? "jpg" : qualificationDetailsDto.getQualificationScanExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(qualificationDetailsDto.getQualificationScanId()) ||
                                    qualificationDetailsDto.getQualificationScanId().equalsIgnoreCase("0"))
                                    && TextUtils.isEmpty(qualificationDetailsDto.getQualificationScanBase64())) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();

                            } else {
                                GSTINDTO previewDto = prepareDtoForPreview(FROM);
                                showImagePreviewDialog(previewDto);
                            }
                        }
                    }
                });
            }

            @Override
            public void folderClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.needs_permission_storage_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            // This always works
                            Intent i = new Intent(context, FilteredFilePickerActivity.class);
                            // Set these depending on your use case. These are the defaults.
                            i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                            i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                            i.putExtra(FilteredFilePickerActivity.EXTRA_MODE, FilteredFilePickerActivity.MODE_FILE);
                            FilteredFilePickerActivity.FILE_TYPE = "images/pdf";

                            // Configure initial directory by specifying a String.
                            // You could specify a String like "/storage/emulated/0/", but that can
                            // dangerous. Always use Android's API calls to get paths to the SD-card or
                            // internal memory.
                            i.putExtra(FilteredFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                            startActivityForResult(i, BROWSE_FOLDER_REQUEST);
                        }
                    }
                });

            }
        });
        fileAttachementDialog.setCancelable(false);
        fileAttachementDialog.show();
    }

    public int IsQualificationDetailsValidated() {

        //Qualification
        if (TextUtils.isEmpty(qualificationDetailsDto.getQualificationId()) || qualificationDetailsDto.getQualificationId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Qualification.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerQualification, "Please select Qualification.", context);
            return 1;
        }

        //Institute Name
        qualificationDetailsDto.setInstituteName(editTextInstituteName.getText().toString().trim());
        if (TextUtils.isEmpty(qualificationDetailsDto.getInstituteName())) {
            Toast.makeText(context, "Please enter Institute Name.", Toast.LENGTH_LONG).show();
            editTextInstituteName.setError("Please enter Institute Name.");
            return 1;
        }

        //Year Of Passing
        if (TextUtils.isEmpty(qualificationDetailsDto.getYearOfPassing())) {
            Toast.makeText(context, "Please select Year Of Passing.", Toast.LENGTH_LONG).show();
            textViewYearOfPassing.setError("Please select Year Of Passing.");
            return 1;
        }

        //Qualification Scan Copy
        if (TextUtils.isEmpty(qualificationDetailsDto.getQualificationScanId()) || qualificationDetailsDto.getQualificationScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(qualificationDetailsDto.getQualificationScanBase64())) {
                showMessage("Please add Qualification Certificate Scan Copy.");
                return 1;
            }
        }

        return 0;
    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent1 = new Intent(getActivity(), ScanActivity.class);
                    intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent1, SCANLIB_REQUEST_CODE);
                }
            }
        });
    }

    public QualificationDetailsDto getQualificationDetail() {

        if (qualificationDetailsDto == null)
            return null;

        qualificationDetailsDto.setInstituteName(editTextInstituteName.getText().toString());
        return qualificationDetailsDto;
    }

    private void refreshQualificationDetailsImg(GSTINDTO imageDto) {

        qualificationDetailsDto.setChangedPhoto(imageDto.isChangedPhoto());
        qualificationDetailsDto.setQualificationScanId(imageDto.getGstImageId());
        qualificationDetailsDto.setBitmap(imageDto.getBitmap());
        qualificationDetailsDto.setQualificationScanExt("jpg");
        qualificationDetailsDto.setQualificationScanBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(qualificationDetailsDto.isChangedPhoto());
        imageDto.setGstImageId(qualificationDetailsDto.getQualificationScanId());
        imageDto.setBitmap(qualificationDetailsDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(qualificationDetailsDto.getQualificationScanBase64());
        return imageDto;
    }

    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImageZoomDialogDM(context, object, new CustomImageZoomDialogDM.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startScanCamera();
                }

                @Override
                public void OkClick(Object object) {
                    GSTINDTO dto = ((GSTINDTO) object);

                    refreshQualificationDetailsImg(dto);

                    //Refresh Qualification Certificate Image
                    if (!TextUtils.isEmpty(qualificationDetailsDto.getQualificationScanBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(qualificationDetailsDto.getQualificationScanBase64());
                        if (bitmap != null)
                            imgQualification.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "Qualification Certificate Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(qualificationDetailsDto.getIsEditable()) &&
                    qualificationDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (qualificationDetailsDto.getQualificationScanExt() != null && qualificationDetailsDto.getQualificationScanExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(qualificationDetailsDto.getIsEditable()) && qualificationDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        if (IsEditable) {
            showAttachmentDialog(view);

        } else {

            //PDF - No Preview for PDF

            //Image
            if (!IsGSTImgPDF) {
                //GSTINDTO previewDto = prepareDtoForPreview(FROM);
                //showImagePreviewDialog(previewDto);
                return;
            }
        }
    }

    public void showMonthYearDialog() {

        if (monthYearPickerDialog != null && monthYearPickerDialog.isVisible()) {
            return;
        }

        monthYearPickerDialog = new MonthYearPickerDialog();
        monthYearPickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthString = new DateFormatSymbols().getMonths()[month];
                String monthConverted = "" + (month + 1);
                if (month < 10) {
                    monthConverted = "0" + monthConverted;
                }
                textViewYearOfPassing.setError(null);
                String getDateConvert = year + "-" + monthConverted + "-" + dayOfMonth;
                textViewYearOfPassing.setText("" + year);
                qualificationDetailsDto.setYearOfPassing("" + year);
            }
        });
        monthYearPickerDialog.show(getFragmentManager(), "MonthYearPickerDialog");
        monthYearPickerDialog.setMonthVisiblity(false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection connection = new Connection(context);
            String tmpVkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(context);
            String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

            qualificationList = documentManagerRepo.getQualificationList(vkIdOrEnquiryId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();

            //Bind Data
            bindData();
        }
    }
}
