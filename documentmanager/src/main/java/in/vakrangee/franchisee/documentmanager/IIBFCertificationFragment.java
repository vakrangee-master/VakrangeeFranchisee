package in.vakrangee.franchisee.documentmanager;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class IIBFCertificationFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "IIBFCertificationFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;
    private IIBFCertificationDto iibfCertificationDto;
    private EditText editTextMembershipNumber, editTextCertificateNumber;
    private ImageView imgIIBFCerificationImg;
    private TextView textViewDateOfExaminiation, textBCACode;
    private TextView txtMembershipNumberLbl, txtCertificateNumberLbl, txtDateOfExaminiationLbl, txtIIBFCopyLbl;
    private LinearLayout layoutIIBFCertificationDetail;

    //Date
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date iibfApplicationDate;
    private String strIIBFApplicationDate;
    private int selectedDateTimeId = 0;
    //img
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int IIBF_CERTIFICATION_IMAGE_PHOTO = 1;
    private FileAttachmentDialog fileAttachementDialog;

    public IIBFCertificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_iibfcertification, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());

        //References
        txtMembershipNumberLbl = view.findViewById(R.id.txtMembershipNumberLbl);
        txtCertificateNumberLbl = view.findViewById(R.id.txtCertificateNumberLbl);
        txtDateOfExaminiationLbl = view.findViewById(R.id.txtDateOfExaminiationLbl);
        txtIIBFCopyLbl = view.findViewById(R.id.txtIIBFCopyLbl);
        textBCACode = view.findViewById(R.id.textBCACode);
        textViewDateOfExaminiation = view.findViewById(R.id.textViewDateOfExaminiation);
        editTextMembershipNumber = view.findViewById(R.id.editTextMembershipNumber);
        editTextCertificateNumber = view.findViewById(R.id.editTextCertificateNumber);
        imgIIBFCerificationImg = view.findViewById(R.id.imgIIBFCerificationImg);
        layoutIIBFCertificationDetail = view.findViewById(R.id.layoutIIBFCertificationDetail);

        imgIIBFCerificationImg.setOnClickListener(this);
        textViewDateOfExaminiation.setOnClickListener(this);

        setCompulsoryMarkLabel();

        CommonUtils.InputFiletrWithMaxLength(editTextMembershipNumber, "~#^|$%&*!", 10);
        CommonUtils.InputFiletrWithMaxLength(editTextCertificateNumber, "~#^|$%&*!", 10);

        return view;
    }

    //region realod data
    public void reload(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            iibfCertificationDto = new IIBFCertificationDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<IIBFCertificationDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<IIBFCertificationDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    iibfCertificationDto = applicationList.get(0);
                } else {
                    iibfCertificationDto = new IIBFCertificationDto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Bind Data
        bindData();
    }
    //endregion

    //region bind data
    private void bindData() {
        //STEP 1:BCA Code
        textBCACode.setText(iibfCertificationDto.getBcaCode());

        //STEP 2:Membership Number
        editTextMembershipNumber.setText(iibfCertificationDto.getMembershipNumber());

        //STEP 3:Certificate Number
        editTextCertificateNumber.setText(iibfCertificationDto.getCertificateNumber());

        //STEP 4:date of examination
        if (!TextUtils.isEmpty(iibfCertificationDto.getDateOfExamination())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", iibfCertificationDto.getDateOfExamination());
            textViewDateOfExaminiation.setText(formattedDate);
        }

        //STEP 5: IIBF Certificate Scanned Copy
        if (!TextUtils.isEmpty(iibfCertificationDto.getIibfCertificateScanId())) {
            String gstUrl = Constants.DownloadImageUrl + iibfCertificationDto.getIibfCertificateScanId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgIIBFCerificationImg);
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(iibfCertificationDto.getIsEditable()) && iibfCertificationDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutIIBFCertificationDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);

    }
    //endregion

    //region compulsaroy red mark
    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtMembershipNumberLbl, txtCertificateNumberLbl, txtDateOfExaminiationLbl, txtIIBFCopyLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }
    //endregion

    //region is validation data
    public int IsIIBFValidation() {

        //Step 1: Membership Number/Registration Number
        iibfCertificationDto.setMembershipNumber(editTextMembershipNumber.getText().toString().trim());
        if (TextUtils.isEmpty(iibfCertificationDto.getMembershipNumber())) {
            Toast.makeText(context, "Please enter Membership Number.", Toast.LENGTH_LONG).show();
            editTextMembershipNumber.setError("Please enter  Membership Number.");
            return 1;
        }

        //length
        int mobLen = iibfCertificationDto.getMembershipNumber().length();
        if (mobLen < 8) {
            Toast.makeText(context, "Please enter 8 character Membership Number.", Toast.LENGTH_LONG).show();
            editTextMembershipNumber.setError("Please enter 8 character Membership Number.");
            return 1;
        }

        //Step 2: Certificate Number
        iibfCertificationDto.setCertificateNumber(editTextCertificateNumber.getText().toString().trim());
        if (TextUtils.isEmpty(iibfCertificationDto.getCertificateNumber())) {
            Toast.makeText(context, "Please enter Certificate Number.", Toast.LENGTH_LONG).show();
            editTextCertificateNumber.setError("Please enter Certificate Number.");
            return 2;
        }

        //STEP 3: Date of examination
        if (TextUtils.isEmpty(iibfCertificationDto.getDateOfExamination())) {
            Toast.makeText(context, "Please select Date of examination.", Toast.LENGTH_LONG).show();
            textViewDateOfExaminiation.setError("Please select Date of examination.");
            return 3;
        }

        //STEP 4: IIBF Certificate Scanned Copy
        if (TextUtils.isEmpty(iibfCertificationDto.getIibfCertificateScanId()) || iibfCertificationDto.getIibfCertificateScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(iibfCertificationDto.getIibfCertificateScanBase64())) {
                showMessage("Please add IIBF Certificate Scanned Copy.");
                return 1;
            }
        }

        return 0;
    }
    //endregion

    //region onclick image and datepicker
    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgIIBFCerificationImg) {
            FROM = IIBF_CERTIFICATION_IMAGE_PHOTO;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(v, SEL_FILE_TYPE);
        } else if (Id == R.id.textViewDateOfExaminiation) {
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);
        }
    }
    //endregion

    //region show image dialog
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
                            FilteredFilePickerActivity.FILE_TYPE = "images";
                            // Configure initial directory by specifying a String.
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
    //endregion

    //region date picker dialog open
    private void showDateTimeDialogPicker(final View view) {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewDateOfExaminiation) {
            defaultDate = iibfApplicationDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(context, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    //Toast.makeText(context, "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewDateOfExaminiation) {
                            iibfApplicationDate = datetime;
                            strIIBFApplicationDate = formateYMD;
                            iibfCertificationDto.setDateOfExamination(strIIBFApplicationDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", iibfCertificationDto.getDateOfExamination());
                            textViewDateOfExaminiation.setText(DOB);
                            textViewDateOfExaminiation.setError(null);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Last 1 year from Today
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        long end = calendar.getTimeInMillis();//Set Min and Max Date

        //Today's Date
        long now = new Date().getTime() - 1000;

        if (selectedDateTimeId == R.id.textViewDateOfExaminiation) {
            dateTimePickerDialog.setMinDate(end);
            dateTimePickerDialog.setMaxDate(now);

        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }
    //endregion

    //region on Activity result image set
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
    //endregion

    //region set image name and set data
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

            case IIBF_CERTIFICATION_IMAGE_PHOTO:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(imgIIBFCerificationImg);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgIIBFCerificationImg);
                }

                iibfCertificationDto.setIibfCertificateScanBase64(base64);
                iibfCertificationDto.setIibfCertificateScanExt(ext);
                break;

            default:
                break;

        }
    }
    //endregion

    public IIBFCertificationDto getIIBFCertificationDto() {

        if (iibfCertificationDto == null)
            return null;

        iibfCertificationDto.setMembershipNumber(editTextMembershipNumber.getText().toString());
        iibfCertificationDto.setCertificateNumber(editTextCertificateNumber.getText().toString());

        return iibfCertificationDto;
    }
}
