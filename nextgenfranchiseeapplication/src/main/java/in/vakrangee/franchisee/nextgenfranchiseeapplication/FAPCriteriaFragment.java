package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import androidx.core.content.FileProvider;
import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class FAPCriteriaFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    //region References
    //line 1


    private FAPCriteriaDto criteriaDto;
    private int FROM = -1;
    private static final int BG_VERIFICATION_SCAN_COPY = 1;
    private String SEL_FILE_TYPE;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private List<CustomFranchiseeApplicationSpinnerDto> hearAboutUsList;
    private FranchiseeApplicationRepository fapRepo;
    private GetAllCrtiteriaSpinnerData getAllCrtiteriaSpinnerData = null;
    private boolean IsEditable = false;

    private RadioGroup radioGroupCriteriaLine11;
    private RadioButton radiobuttonCriteriaLine11;
    private RadioButton radiobuttonCriteriaLine12;
    private RadioGroup radioGroupCriteriaLine2;
    private RadioButton radiobuttonCriteriaLine21;
    private RadioButton radiobuttonCriteriaLine22;
    private RadioGroup radioGroupCriteriaLine3;
    private RadioButton radiobuttonCriteriaLine31;
    private RadioButton radiobuttonCriteriaLine32;
    private RadioGroup radioGroupCriteriaLine4;
    private RadioButton radiobuttonCriteriaLine41;
    private RadioButton radiobuttonCriteriaLine42;
    private RadioGroup radioGroupCriteriaLine5;
    private RadioButton radiobuttonCriteriaLine51;
    private RadioButton radiobuttonCriteriaLine52;
    private ImageView imgChoosePhotoBKVerification;
    private TextView txtChoosePhotoNameBKVerification;
    private Spinner spinnerAboutUs;
    private CheckBox checkBox_exit_application;
    private TextView txtChoosePhotoBKVerification;
    private TextView txtAboutUsLbl;
    private MaterialButton txtClickDownload;
    private LinearLayout layoutCriteriaParent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_fap_criteria, container, false);

        bindViewId(rootView);
        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        fapRepo = new FranchiseeApplicationRepository(context);
        ButterKnife.bind(this, rootView);

        TextView[] txtViewsForCompulsoryMark = {txtChoosePhotoBKVerification, txtAboutUsLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        txtClickDownload.setText(Html.fromHtml("<u><font color=\"#ffffff\">Click here to download Consent Form</font></u>"));

        return rootView;
    }

    private void bindViewId(View view) {
        radioGroupCriteriaLine11 = view.findViewById(R.id.radioGroupCriteriaLine11);
        radiobuttonCriteriaLine11 = view.findViewById(R.id.radiobuttonCriteriaLine11);
        radiobuttonCriteriaLine12 = view.findViewById(R.id.radiobuttonCriteriaLine12);
        radioGroupCriteriaLine2 = view.findViewById(R.id.radioGroupCriteriaLine2);
        radiobuttonCriteriaLine21 = view.findViewById(R.id.radiobuttonCriteriaLine21);
        radiobuttonCriteriaLine22 = view.findViewById(R.id.radiobuttonCriteriaLine22);
        radioGroupCriteriaLine3 = view.findViewById(R.id.radioGroupCriteriaLine3);
        radiobuttonCriteriaLine31 = view.findViewById(R.id.radiobuttonCriteriaLine31);
        radiobuttonCriteriaLine32 = view.findViewById(R.id.radiobuttonCriteriaLine32);
        radioGroupCriteriaLine4 = view.findViewById(R.id.radioGroupCriteriaLine4);
        radiobuttonCriteriaLine41 = view.findViewById(R.id.radiobuttonCriteriaLine41);
        radiobuttonCriteriaLine42 = view.findViewById(R.id.radiobuttonCriteriaLine42);
        radioGroupCriteriaLine5 = view.findViewById(R.id.radioGroupCriteriaLine5);
        radiobuttonCriteriaLine51 = view.findViewById(R.id.radiobuttonCriteriaLine51);
        radiobuttonCriteriaLine52 = view.findViewById(R.id.radiobuttonCriteriaLine52);
        imgChoosePhotoBKVerification = view.findViewById(R.id.imgChoosePhotoBKVerification);
        txtChoosePhotoNameBKVerification = view.findViewById(R.id.txtChoosePhotoNameBKVerification);
        spinnerAboutUs = view.findViewById(R.id.spinnerAboutUs);
        checkBox_exit_application = view.findViewById(R.id.checkBox_exit_application);
        txtChoosePhotoBKVerification = view.findViewById(R.id.txtChoosePhotoBKVerification);
        txtAboutUsLbl = view.findViewById(R.id.txtAboutUsLbl);
        txtClickDownload = view.findViewById(R.id.txtClickDownload);
        layoutCriteriaParent = view.findViewById(R.id.layoutCriteriaParent);

        imgChoosePhotoBKVerification.setOnClickListener(this);
        txtClickDownload.setOnClickListener(this);

        spinnerAboutUs.setOnItemSelectedListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.imgChoosePhotoBKVerification) {
            FROM = BG_VERIFICATION_SCAN_COPY;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.txtClickDownload) {
            displayConsentPdf();
        }
    }

    public void displayConsentPdf() {

        //STEP 1: Check Application No. Exists
        String appNo = ((NextGenFranchiseeApplicationActivity) getActivity()).getApplicationNo();
        if (TextUtils.isEmpty(appNo)) {
            showMessage("In order to download the background verification consent form kindly partial submit the form at least once with Applicant Name and Communication Address.");
            return;
        }

        String address = ((NextGenFranchiseeApplicationActivity) getActivity()).getAddress();
        String applicantName = ((NextGenFranchiseeApplicationActivity) getActivity()).getApplicantName();

        if (TextUtils.isEmpty(applicantName) || TextUtils.isEmpty(address)) {
            showMessage("Please enter Applicant Name and Address details.");
            return;
        }

        String url = Constants.URL_CONSENT_BASE_URL + context.getString(R.string.last_consent_pdf_url);
        url = url.replace("{name}", applicantName);
        url = url.replace("{date}", CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", CommonUtils.getCurrentDate()));
        url = url.replace("{address}", CommonUtils.toTitleCase(address));

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllCrtiteriaSpinnerData != null && !getAllCrtiteriaSpinnerData.isCancelled()) {
            getAllCrtiteriaSpinnerData.cancel(true);
        }
    }

    public int IsFranchiseeCriteriaValidated() {

        //STEP 1: Choose Photo
        if (TextUtils.isEmpty(criteriaDto.getBackgroundVerificationFileId()) || criteriaDto.getBackgroundVerificationFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(criteriaDto.getBgVerficationConsentBase64())) {
                showMessage("Please add Background Verification photo.");
                return 1;
            }
        }

        //STEP 2: AboutFrom
        /*if (TextUtils.isEmpty(criteriaDto.getAboutUsFrom())) {
            Toast.makeText(context, "Please select About Us.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerAboutUs, "Please select About Us.", context);
            return 2;
        }*/
        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, "", "", "");
                File file = new File(picUri.getPath());
                String base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                setImageAndName(fileName, base64Data, picUri);

            }
            if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 1) {
                        showMessage(getString(R.string.file_size_msg));
                        return;
                    }

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data;

                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                    }

                    String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                    setImageAndName(fileName, base64Data, uri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageAndName(String fileName, String base64Data, Uri uri) {
        try {
            Bitmap imageBitMap = null;
            boolean IsDrawable = false;
            String ext = FileUtils.getFileExtension(context, uri);
            if (ext.equalsIgnoreCase("pdf"))
                IsDrawable = true;
            else
                imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            setScanCopyData(IsDrawable, imageBitMap, fileName, base64Data, ext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String fileName, String base64, String ext) {

        switch (FROM) {

            case BG_VERIFICATION_SCAN_COPY:
                if (IsDrawable) {
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgChoosePhotoBKVerification);
                } else
                    Glide.with(context).asBitmap().load(bitmap).into(imgChoosePhotoBKVerification);

                txtChoosePhotoNameBKVerification.setVisibility(View.GONE);
                txtChoosePhotoNameBKVerification.setText(fileName);
                criteriaDto.setBgVerficationConsentBase64(base64);
                criteriaDto.setBgVerificationFileExt(ext);
                criteriaDto.setBgVerficationConsentFileName(fileName);
                break;

            default:
                break;

        }
    }

    public void showAttachmentDialog(final View view, final String fileType) {
        fileAttachementDialog = new FileAttachmentDialog(context, new FileAttachmentDialog.IFileAttachmentClicks() {
            @Override
            public void cameraClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //picUri = Uri.fromFile(file); // create
                            picUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                            i.putExtra("ImageId", picUri); // set the image file
                            startActivityForResult(i, CAMERA_PIC_REQUEST);
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
                            FilteredFilePickerActivity.FILE_TYPE = fileType;

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

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            criteriaDto = new FAPCriteriaDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                criteriaDto = gson.fromJson(data, FAPCriteriaDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllCrtiteriaSpinnerData = new GetAllCrtiteriaSpinnerData();
        getAllCrtiteriaSpinnerData.execute("");
    }

    private void bindCriteria() {

        //STEP 1: Criteria 1
        radioGroupCriteriaLine11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobuttonCriteriaLine11) {
                    criteriaDto.setIsAbleToDedecateFullTime("1");
                } else if (checkedId == R.id.radiobuttonCriteriaLine12) {
                    criteriaDto.setIsAbleToDedecateFullTime("0");
                }
            }
        });

        //STEP 2: Criteria 2
        radioGroupCriteriaLine2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobuttonCriteriaLine21) {
                    criteriaDto.setIsAffiliatedToPoliticalParty("1");
                } else if (checkedId == R.id.radiobuttonCriteriaLine22) {
                    criteriaDto.setIsAffiliatedToPoliticalParty("0");
                }
            }
        });

        //STEP 3: Criteria 3
        radioGroupCriteriaLine3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobuttonCriteriaLine31) {
                    criteriaDto.setIsFiledBankruptcyBefore("1");
                } else if (checkedId == R.id.radiobuttonCriteriaLine32) {
                    criteriaDto.setIsFiledBankruptcyBefore("0");
                }
            }
        });

        //STEP 4: Criteria 4
        radioGroupCriteriaLine4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobuttonCriteriaLine41) {
                    criteriaDto.setIsLegalProceeding("1");
                } else if (checkedId == R.id.radiobuttonCriteriaLine42) {
                    criteriaDto.setIsLegalProceeding("0");
                }
            }
        });

        //STEP 5: Criteria 5
        radioGroupCriteriaLine5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiobuttonCriteriaLine51) {
                    criteriaDto.setIsFIRFiled("1");
                } else if (checkedId == R.id.radiobuttonCriteriaLine52) {
                    criteriaDto.setIsFIRFiled("0");
                }
            }
        });
    }

    private void bindQuestions() {

        //STEP 3 : bind question 1
        if (!TextUtils.isEmpty(criteriaDto.getIsAbleToDedecateFullTime())) {
            switch (criteriaDto.getIsAbleToDedecateFullTime()) {
                case "1":
                    radioGroupCriteriaLine11.check(R.id.radiobuttonCriteriaLine11);
                    break;
                case "0":
                    radioGroupCriteriaLine11.check(R.id.radiobuttonCriteriaLine12);
                    break;
                default:
                    break;
            }
        }
        //STEP 4 : bind question 2
        if (!TextUtils.isEmpty(criteriaDto.getIsAffiliatedToPoliticalParty())) {
            switch (criteriaDto.getIsAffiliatedToPoliticalParty()) {
                case "1":
                    radioGroupCriteriaLine2.check(R.id.radiobuttonCriteriaLine21);
                    break;
                case "0":
                    radioGroupCriteriaLine2.check(R.id.radiobuttonCriteriaLine22);
                    break;
                default:
                    break;
            }
        }

        //STEP 5 : bind question 3
        if (!TextUtils.isEmpty(criteriaDto.getIsFiledBankruptcyBefore())) {
            switch (criteriaDto.getIsFiledBankruptcyBefore()) {
                case "1":
                    radioGroupCriteriaLine3.check(R.id.radiobuttonCriteriaLine31);
                    break;
                case "0":
                    radioGroupCriteriaLine3.check(R.id.radiobuttonCriteriaLine32);
                    break;
                default:
                    break;
            }
        }
        //STEP 6 : bind question 4
        if (!TextUtils.isEmpty(criteriaDto.getIsLegalProceeding())) {
            switch (criteriaDto.getIsLegalProceeding()) {
                case "1":
                    radioGroupCriteriaLine4.check(R.id.radiobuttonCriteriaLine41);
                    break;
                case "0":
                    radioGroupCriteriaLine4.check(R.id.radiobuttonCriteriaLine42);
                    break;
                default:
                    break;
            }
        }

        //STEP 6 : bind question 5
        if (!TextUtils.isEmpty(criteriaDto.getIsFIRFiled())) {
            switch (criteriaDto.getIsFIRFiled()) {
                case "1":
                    radioGroupCriteriaLine5.check(R.id.radiobuttonCriteriaLine51);
                    break;
                case "0":
                    radioGroupCriteriaLine5.check(R.id.radiobuttonCriteriaLine52);
                    break;
                default:
                    break;
            }
        }
    }

    private void bindSpinner() {

        bindCriteria();

        //STEP 2: Hear About Us
        CustomFranchiseeApplicationSpinnerAdapter aboutUsAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, hearAboutUsList);
        spinnerAboutUs.setAdapter(aboutUsAdapter);
        int entityPos = fapRepo.getSelectedPos(hearAboutUsList, criteriaDto.getAboutUsFrom());
        spinnerAboutUs.setSelection(entityPos);
        spinnerAboutUs.setOnItemSelectedListener(this);

        bindQuestions();

        //STEP 7: Background Verification Consent Image
        boolean IsBgVerifyPDF = ((criteriaDto.getBgVerificationFileExt() != null) && criteriaDto.getBgVerificationFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsBgVerifyPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).override(200, 200).into(imgChoosePhotoBKVerification);
        } else {
            if (!TextUtils.isEmpty(criteriaDto.getBackgroundVerificationFileId())) {
                String consentUrl = Constants.DownloadImageUrl + criteriaDto.getBackgroundVerificationFileId();
                Glide.with(context)
                        .load(consentUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgChoosePhotoBKVerification);
            }
        }

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutCriteriaParent, IsEditable);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerAboutUs && position >= 0) {
                CustomFranchiseeApplicationSpinnerDto aboutUsDto = (CustomFranchiseeApplicationSpinnerDto) spinnerAboutUs.getItemAtPosition(position);
                criteriaDto.setAboutUsFrom(aboutUsDto.getId());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    class GetAllCrtiteriaSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 2: Hear About Us
            hearAboutUsList = fapRepo.getHearAboutUsList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bindSpinner();
        }
    }

    public FAPCriteriaDto getCriteriaDto() {
        return criteriaDto;
    }

}
