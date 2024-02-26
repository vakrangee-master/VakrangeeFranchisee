package in.vakrangee.franchisee.loandocument;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.loandocument.adapter.LoanDocumentAdapter;
import in.vakrangee.franchisee.loandocument.asynctask.AsyncGetAllLoanDocumentDetails;
import in.vakrangee.franchisee.loandocument.asynctask.AsyncSaveLoanDocumentDetails;
import in.vakrangee.franchisee.loandocument.model.LoanDocumentDto;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.model.PhotoDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class LoanDocumentFragment extends BaseFragment {

    private Context context;
    private View view;
    private AsyncGetAllLoanDocumentDetails asyncGetAllLoanDocumentDetails = null;
    private AsyncSaveLoanDocumentDetails asyncSaveLoanDocumentDetails = null;
    private RecyclerView recyclerViewDocumentDetails;
    private TextView txtNoDataMsg;
    private List<LoanDocumentDto> loanDocumentList;
    private LoanDocumentAdapter loanDocumentAdapter;
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private String mCurrentPhotoPath;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private String selectedImageType;
    private static final String PROOF_IMAGE = "Proof Image Preview";
    private CustomAllImagePreviewDialog customImagePreviewDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final String EXT_JPG = "jpg";
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private LoanDocumentDto selectedDto;
    private int selectedPos;
    private FileAttachmentDialog fileAttachementDialog = null;
    private String SEL_FILE_TYPE = "images/pdf";
    private static final int SCANLIB_REQUEST_CODE = 99;
    private static final int UPLOAD_ACTIVITY_REQUEST = 200;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    public LoanDocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_loan_document, container, false);

        this.context = getContext();
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);

        recyclerViewDocumentDetails = view.findViewById(R.id.recyclerViewDocumentDetails);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);

        return view;
    }

    public void reloadData() {
        asyncGetAllLoanDocumentDetails = new AsyncGetAllLoanDocumentDetails(context, new AsyncGetAllLoanDocumentDetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncGetAllLoanDocumentDetails.execute("");
    }

    private void processResult(String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            if (result.startsWith("ERROR")) {
                String msg = result.replace("ERROR|", "");
                msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return;
            }

            if (result.startsWith("OKAY")) {
                //Handle Response
                String data = result.replace("OKAY|", "");
                if (TextUtils.isEmpty(data))
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                else {
                    refreshDetails(data);
                }
            } else {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
        }
    }

    private void refreshDetails(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("loan_document_list");

            if (jsonArray == null || jsonArray.length() == 0) {
                return;
            }

            Gson gson = new GsonBuilder().create();
            loanDocumentList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<LoanDocumentDto>>() {
            }.getType());

            if (loanDocumentList != null && loanDocumentList.size() > 0) {
                txtNoDataMsg.setVisibility(View.GONE);
                recyclerViewDocumentDetails.setVisibility(View.VISIBLE);
                loanDocumentAdapter = new LoanDocumentAdapter(context, loanDocumentList, new LoanDocumentAdapter.IClickHandler() {
                    @Override
                    public void cameraClick(int position, LoanDocumentDto documentDto) {
                        selectedPos = position;
                        selectedDto = documentDto;

                        //Ext
                        if (TextUtils.isEmpty(selectedDto.getLoanDocumentImgExt())) {
                            showAttachmentDialog(view);
                            return;
                        }

                        boolean IsPDF = (!TextUtils.isEmpty(selectedDto.getLoanDocumentImgExt()) && selectedDto.getLoanDocumentImgExt().contains("pdf")) ? true : false;

                        //PDF
                        if (IsPDF) {
                            showAttachmentDialog(view);
                            return;
                        }

                        //Image
                        if (TextUtils.isEmpty(selectedDto.getLoanDocumentImgBase64()) && TextUtils.isEmpty(selectedDto.getLoanDocumentImgId())) {
                            showAttachmentDialog(view);
                            return;
                        }

                        //Preview Image
                        PhotoDto photoDto = new PhotoDto();
                        photoDto.setPhotoId(selectedDto.getLoanDocumentImgId());
                        photoDto.setImageBase64(selectedDto.getLoanDocumentImgBase64());
                        if (TextUtils.isEmpty(selectedDto.getLoanDocumentImgBase64()))
                            photoDto.setBitmap(CommonUtils.StringToBitMap(selectedDto.getLoanDocumentImgBase64()));
                        showImagePreviewDialog((Object) photoDto);

                    }

                    @Override
                    public void onUploadClick(View v, int position) {
                        selectedPos = position;
                        selectedDto = loanDocumentList.get(position);

                        popUpOptionUpload(v);
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewDocumentDetails.setLayoutManager(layoutManager);
                recyclerViewDocumentDetails.setItemAnimator(new DefaultItemAnimator());
                recyclerViewDocumentDetails.setAdapter(loanDocumentAdapter);

            } else {
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerViewDocumentDetails.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region startCamera
    public void startCamera(View view) {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestMultiplePermission(view, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    try {
                        int preference = ScanConstants.OPEN_CAMERA;
                        Intent intent1 = new Intent(context, ScanActivity.class);
                        intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                        startActivityForResult(intent1, SCANLIB_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                getLocationAndTimestamp();
                // File file = new File(picUri.getPath());
                File file = Utils.getFileForUri(picUri);
                file = new ImageZipper(context).setQuality(50).compressToFile(file);
                Bitmap bitmapNew = new ImageZipper(context).compressToBitmapWithExif(file, latitude, longitude, currentTimestamp);
                //Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, "", "", "");

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
                    int actFileSize = TextUtils.isEmpty(selectedDto.getFileSize()) ? 0 : Integer.parseInt(selectedDto.getFileSize());
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > actFileSize) {
                        String msg = context.getResources().getString(R.string.act_file_size_msg) + " " + actFileSize + " MB.";
                        showMessage(msg);
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
            if (requestCode == SCANLIB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                picUri = uri;

                File file = new File(uri.getPath());
                file = new ImageZipper(context).setQuality(50).compressToFile(file);
                bitmap = new ImageZipper(context).compressToBitmapWithExif(file, latitude, longitude, currentTimestamp);

                String base64Data = CommonUtils.convertBitmapToString(bitmap);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");

                setImageAndName(fileName, base64Data, picUri);

            }
            if (requestCode == UPLOAD_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    int sync = data.getIntExtra("BIG_DATA:SYNC_CODE", -1);
                    boolean IsReload = data.getBooleanExtra("PERFORM_RELOAD", false);

                    if (IsReload) {
                        reloadData();
                        /*LoanDocumentDto details_dto = (LoanDocumentDto) ResultIPC.get().getLargeData(sync);
                        this.selectedPos = data.getIntExtra("SELECTED_POS", -1);
                        this.selectedDto = details_dto;
                        loanDocumentList.set(selectedPos, selectedDto);
                        loanDocumentAdapter.notifyDataSetChanged();*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageAndName(String fileName, String base64Data, Uri uri) {
        try {
            String ext = FileUtils.getFileExtension(context, uri);
            selectedDto.setLoanDocumentImgBase64(base64Data);
            selectedDto.setLoanDocumentImgExt(ext);

            //Service Call
            saveImage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region showImagePreviewDialog
    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomAllImagePreviewDialog(context, object, new CustomAllImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    customImagePreviewDialog.dismiss();
                    showAttachmentDialog(view);
                }

                @Override
                public void OkClick(Object object) {
                    //save data
                    PhotoDto photoDto = ((PhotoDto) object);
                    String base64 = photoDto.getImageBase64();
                    Bitmap bitmap = photoDto.getBitmap();

                    if (selectedImageType.equalsIgnoreCase(PROOF_IMAGE)) {
                        if (base64 != null || bitmap != null) {
                            selectedDto.setLoanDocumentImgBase64(base64);
                            selectedDto.setLoanDocumentImgExt(EXT_JPG);
                            loanDocumentList.set(selectedPos, selectedDto);
                            loanDocumentAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.setDialogTitle(selectedImageType);
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncGetAllLoanDocumentDetails != null && !asyncGetAllLoanDocumentDetails.isCancelled()) {
            asyncGetAllLoanDocumentDetails.cancel(true);
        }

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
                            startCamera(view);
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
                            FilteredFilePickerActivity.FILE_TYPE = SEL_FILE_TYPE;

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

    private void saveImage() {
        asyncSaveLoanDocumentDetails = new AsyncSaveLoanDocumentDetails(context, selectedDto, new AsyncSaveLoanDocumentDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("ERROR")) {
                        String msg = result.replace("ERROR|", "");
                        msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                        AlertDialogBoxInfo.alertDialogShow(context, msg);
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        String data = result.replace("OKAY|", "");
                        if (TextUtils.isEmpty(data))
                            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        else {
                            String msg = selectedDto.getName() + " Document uploaded successfully.";
                            showMessageWithReload(msg);
                        }
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveLoanDocumentDetails.execute("");
    }

    public void popUpOptionUpload(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.popup_scan);
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

        boolean isBrowseShown = (!TextUtils.isEmpty(selectedDto.getIsBrowse()) && selectedDto.getIsBrowse().equalsIgnoreCase("1")) ? true : false;
        MenuItem browseItem = menu.findItem(R.id.menuBrowseFile);
        if (isBrowseShown)
            browseItem.setVisible(true);
        else
            browseItem.setVisible(false);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menuScanAgreement) {
                    scanAgreement();
                    return true;
                } else if (itemId == R.id.menuBrowseFile) {
                    browseFile();
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
                    Intent intent = new Intent(context, ScanLoanAgreementImagesActivity.class);

                    int sync = ResultIPC.get().setLargeData(selectedDto);
                    intent.putExtra("BIG_DATA:SYNC_CODE", sync);
                    intent.putExtra("SELECTED_POS", selectedPos);
                    startActivityForResult(intent, UPLOAD_ACTIVITY_REQUEST);
                }
            }
        });
    }

    private void browseFile() {
        final String fileType = selectedDto.getBrowseFileType();

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

                    i.putExtra(FilteredFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                    startActivityForResult(i, BROWSE_FOLDER_REQUEST);
                }
            }
        });
    }

    public void showMessageWithReload(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            reloadData();

                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }


}
