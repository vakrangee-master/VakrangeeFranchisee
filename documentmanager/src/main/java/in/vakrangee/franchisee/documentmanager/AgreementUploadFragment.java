package in.vakrangee.franchisee.documentmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.documentmanager.DocumentManagerConstants;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class AgreementUploadFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "AgreementUploadFragment";

    private View view;
    private Context context;

    private LinearLayout layoutStampFrankingDate;
    private TextView txtStampFrankingDateLbl;
    private TextView textViewStampFrankingDate;
    private LinearLayout layoutPageCount;
    private TextView txtPageCountLbl;
    private TextView txtUploadAgreementLbl;
    private EditText editTextPageCount;
    private LinearLayout layoutUploadAgreement;
    private LinearLayout btnUploadAgreement;
    private ImageView imgAgreementPDF;
    private LinearLayout layoutAgreementUploadDetail;
    private AgreementUploadDetailsDto agreementUploadDetailsDto;
    private DocumentManagerRepository documentManagerRepo;

    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date stampFrankingDate;
    private String strStampFrankingDate;
    private int selectedDateTimeId = 0;
    private static final int UPLOAD_ACTIVITY_REQUEST = 100;
    private PermissionHandler permissionHandler;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private CustomTrackingPDFdialog customTrackingPDFdialog;

    public AgreementUploadFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_agreement_upload, container, false);

        //Initialize data
        this.context = getContext();
        documentManagerRepo = new DocumentManagerRepository(context);
        permissionHandler = new PermissionHandler(getActivity());

        //References
        layoutStampFrankingDate = view.findViewById(R.id.layoutStampFrankingDate);
        txtStampFrankingDateLbl = view.findViewById(R.id.txtStampFrankingDateLbl);
        textViewStampFrankingDate = view.findViewById(R.id.textViewStampFrankingDate);
        layoutPageCount = view.findViewById(R.id.layoutPageCount);
        txtPageCountLbl = view.findViewById(R.id.txtPageCountLbl);
        txtUploadAgreementLbl = view.findViewById(R.id.txtUploadAgreementLbl);
        editTextPageCount = view.findViewById(R.id.editTextPageCount);
        layoutUploadAgreement = view.findViewById(R.id.layoutUploadAgreement);
        btnUploadAgreement = view.findViewById(R.id.btnUploadAgreement);
        imgAgreementPDF = view.findViewById(R.id.imgAgreementPDF);
        layoutAgreementUploadDetail = view.findViewById(R.id.layoutAgreementUploadDetail);

        btnUploadAgreement.setOnClickListener(this);
        textViewStampFrankingDate.setOnClickListener(this);
        imgAgreementPDF.setOnClickListener(this);

        setCompulsoryMarkLabel();

        return view;
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        if (Id == R.id.btnUploadAgreement) {

            if (IsPageCountEntered() != 0) {
                return;
            }

            popUpOptionUpload(v, DocumentManagerConstants.PDF);

        } else if (Id == R.id.textViewStampFrankingDate) {
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.imgAgreementPDF) {

            if (!TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementImageId())) {
                String url = Constants.DownloadPDFfileURL + agreementUploadDetailsDto.getAgreementImageId();
                openPDFDialog(url);
                return;
            }

            if (TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementFilePath())) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.file_not_exist));
                return;
            }

            //VIEW PDF
            ImagesToPDFUtils.viewPdf(context, new File(agreementUploadDetailsDto.getAgreementFilePath()));
        }
    }

    public void reload(String data) {

        //Reload Data
        if (TextUtils.isEmpty(data))
            agreementUploadDetailsDto = new AgreementUploadDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<AgreementUploadDetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<AgreementUploadDetailsDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    agreementUploadDetailsDto = applicationList.get(0);
                } else {
                    agreementUploadDetailsDto = new AgreementUploadDetailsDto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Bind Data
        bindData();

    }

    private void bindData() {

        //STEP 1: Stamp Paper/Franking Date
        if (TextUtils.isEmpty(agreementUploadDetailsDto.getStampFrankingDate())) {
            agreementUploadDetailsDto.setStampFrankingDate(dateFormatterYMD.format(new Date()));
        }
        String frankingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", agreementUploadDetailsDto.getStampFrankingDate());
        textViewStampFrankingDate.setText(frankingDate);

        //STEP 2: No Of Pages
        editTextPageCount.setText(agreementUploadDetailsDto.getNoOfPages());

        //STEP 3: Get Images
        agreementUploadDetailsDto.agreementImagesList = prepareAgreementImgsListFromExternalStorage();
        agreementUploadDetailsDto.setAgreementFilePath(getAlreadyExistingPdfPath(context));

        //PDF ImageView
        if (!TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementFilePath()) || !TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementImageId()))
            imgAgreementPDF.setVisibility(View.VISIBLE);
        else
            imgAgreementPDF.setVisibility(View.GONE);

        //Set No. Of Pages
        if (agreementUploadDetailsDto.agreementImagesList.size() > 0) {
            agreementUploadDetailsDto.setNoOfPages("" + agreementUploadDetailsDto.agreementImagesList.size());
            editTextPageCount.setText(agreementUploadDetailsDto.getNoOfPages());
        } else
            editTextPageCount.setText("");

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(agreementUploadDetailsDto.getIsEditable()) && agreementUploadDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutAgreementUploadDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewStampFrankingDate) {
            defaultDate = stampFrankingDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewStampFrankingDate) {
                            stampFrankingDate = datetime;
                            strStampFrankingDate = formateYMD;
                            agreementUploadDetailsDto.setStampFrankingDate(strStampFrankingDate);

                            String date = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", agreementUploadDetailsDto.getStampFrankingDate());
                            textViewStampFrankingDate.setText(date);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Set Min and Max Date
        long now = new Date().getTime() - 1000;

        Calendar c = Calendar.getInstance();
        c.set(1920, 0, 1);//Year,Month -1,Day

        Calendar curCal = Calendar.getInstance();
        int day = curCal.get(Calendar.DATE);
        int month = curCal.get(Calendar.MONTH);
        int year = curCal.get(Calendar.YEAR);
        curCal.set(year, month, day);

        if (selectedDateTimeId == R.id.textViewStampFrankingDate) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());
            //dateTimePickerDialog.setMaxDate(now);
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtStampFrankingDateLbl, txtPageCountLbl, txtUploadAgreementLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    public int IsAgreementDetailsValidated() {

        //STEP 1: Stamp Paper/Franking Date
        if (TextUtils.isEmpty(agreementUploadDetailsDto.getStampFrankingDate())) {
            Toast.makeText(context, "Please select Stamp Paper/Franking date.", Toast.LENGTH_LONG).show();
            textViewStampFrankingDate.setError("Please select Stamp Paper/Franking date.");
            return 15;
        }

        //STEP 2: No. Of Pages
        if (IsPageCountEntered() != 0) {
            return 1;
        }

        //STEP 3: Agreement Copy
        if (TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementImageId()) || agreementUploadDetailsDto.getAgreementImageId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementImageBase64())) {
                showMessage("Please add Agreement Copy.");
                return 1;
            }
        }

        return 0;
    }

    private int IsPageCountEntered() {

        //No. Of Pages
        agreementUploadDetailsDto.setNoOfPages(editTextPageCount.getText().toString().trim());
        if (TextUtils.isEmpty(agreementUploadDetailsDto.getNoOfPages()) || agreementUploadDetailsDto.getNoOfPages().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please enter Page Count.", Toast.LENGTH_LONG).show();
            editTextPageCount.setError("Please enter Page Count.");
            return 1;
        }
        return 0;
    }

    public AgreementUploadDetailsDto getAgreementDetailsDto() {

        if (agreementUploadDetailsDto == null)
            return null;

        agreementUploadDetailsDto.setNoOfPages(editTextPageCount.getText().toString());
        agreementUploadDetailsDto.setAgreementImagesList(new ArrayList<AgreementImageDto>());

        return agreementUploadDetailsDto;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
            try {

                if (data != null) {

                    int sync = data.getIntExtra("BIG_DATA:SYNC_CODE", -1);
                    AgreementUploadDetailsDto details_dto = (AgreementUploadDetailsDto) ResultIPC.get().getLargeData(sync);

                    this.agreementUploadDetailsDto = details_dto;
                    refreshPDFIcon();

                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        } else if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    uri = ImagesToPDFUtils.compressPdfFileSize(file, context);
                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data;

                    if (ext.equalsIgnoreCase(DocumentManagerConstants.PDF)) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                    }

                    setImageAndName(base64Data, uri);

                    refreshPDFIcon();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setImageAndName(String base64Data, Uri uri) {

        try {

            String ext = FileUtils.getFileExtension(context, uri);

            agreementUploadDetailsDto.setAgreementImageBase64(base64Data);
            agreementUploadDetailsDto.setAgreementImageExt(ext);
            agreementUploadDetailsDto.setAgreementFilePath(uri.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<AgreementImageDto> prepareAgreementImgsListFromExternalStorage() {
        List<AgreementImageDto> agreementImageList = new ArrayList<>();

        //Check EnquiryId exist
        String enquiryId = CommonUtils.getEnquiryId(context);
        if (TextUtils.isEmpty(enquiryId))
            return agreementImageList;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DocumentManagerConstants.DIR_NAME_AGREEMENT_UPLOAD + File.separator + enquiryId);

        //Check if Dir exists
        if (!mediaStorageDir.exists())
            return agreementImageList;

        //Prepare file
        File[] files = mediaStorageDir.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                //return Long.compare(f1.lastModified(), f2.lastModified());
                return f1.getName().compareTo(f2.getName());
            }
        });
        for (int i = 0; i < files.length; i++) {

            File file = files[i];
            Uri picUri = Uri.fromFile(file);
            String ext = FileUtils.getFileExtension(context, picUri);
            if (!TextUtils.isEmpty(ext) && !ext.equalsIgnoreCase(DocumentManagerConstants.PDF)) {

                Bitmap bitmap = ImageUtils.getBitmap(context.getContentResolver(), picUri, "", "", "");
                String base64Data = CommonUtils.convertBitmapToString(bitmap);

                AgreementImageDto agreementImageDto = new AgreementImageDto();

                int pgNo = documentManagerRepo.getPageNo(agreementImageList);
                agreementImageDto.setName(String.valueOf(pgNo));
                agreementImageDto.setBitmap(bitmap);
                agreementImageDto.setImgBase64(base64Data);
                agreementImageDto.setPath(picUri.toString());
                agreementImageDto.setExt(DocumentManagerConstants.JPG);
                agreementImageList.add(agreementImageDto);
            }
        }
        return agreementImageList;
    }

    private String getAlreadyExistingPdfPath(Context context) {

        String filePath = ImagesToPDFUtils.getPDFDestPath(context);
        File file = new File(filePath);
        if (file.exists())
            return filePath;

        return null;
    }

    //region Upload Options [Browse Pdf or Scan Images]
    public void popUpOptionUpload(View v, final String fileType) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.popup_browse_scan_option);
        Menu menu = popup.getMenu();

        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {

        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menuScanAgreement) {
                    scanAgreement();
                    return true;
                } else if (itemId == R.id.menuBrowseFile) {
                    browseFile(fileType);
                    return true;
                }
                return false;
            }
        });
        popup.show();

    }

    private void scanAgreement() {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    Intent intent = new Intent(context, ScanAgreementImagesActivity.class);

                    int sync = ResultIPC.get().setLargeData(agreementUploadDetailsDto);
                    intent.putExtra("BIG_DATA:SYNC_CODE", sync);
                    startActivityForResult(intent, UPLOAD_ACTIVITY_REQUEST);
                }
            }
        });
    }

    private void browseFile(final String fileType) {
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
    //endregion

    private void openPDFDialog(String pod) {

        if (customTrackingPDFdialog != null && customTrackingPDFdialog.isShowing()) {
            return;
        }
        customTrackingPDFdialog = new CustomTrackingPDFdialog(context, pod);
        customTrackingPDFdialog.show();
        customTrackingPDFdialog.setDialogTitle("Agreement");
        customTrackingPDFdialog.setCancelable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Date Time DIalog
        if (dateTimePickerDialog != null && dateTimePickerDialog.isShowing()) {
            dateTimePickerDialog.dismiss();
            dateTimePickerDialog = null;
        }

        //PDF DIalog
        if (customTrackingPDFdialog != null && customTrackingPDFdialog.isShowing()) {
            customTrackingPDFdialog.dismiss();
            customTrackingPDFdialog = null;
        }
    }

    private void refreshPDFIcon() {

        //Check if PDF Id exists
        if (!TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementImageId())) {
            imgAgreementPDF.setVisibility(View.VISIBLE);
            return;
        }

        if (!TextUtils.isEmpty(agreementUploadDetailsDto.getAgreementFilePath())) {
            imgAgreementPDF.setVisibility(View.VISIBLE);
            return;
        }

        imgAgreementPDF.setVisibility(View.GONE);

    }
}
