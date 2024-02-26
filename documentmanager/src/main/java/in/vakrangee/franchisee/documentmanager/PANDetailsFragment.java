package in.vakrangee.franchisee.documentmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class PANDetailsFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "PANDetailsFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private TextView txtPANNumberLbl;
    private EditText editTextPANNumber1;
    private EditText editTextPANNumber2;
    private EditText editTextNameAsPerPANCard;
    private TextView txtPanCardScanCopyLbl;
    private ImageView imgPanScanCopy;
    private LinearLayout layoutPANCardDetail;

    private boolean edittext_listener = true;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;

    private static final int PAN_CARD_SCAN_COPY = 1;
    private PANCardDetailsDto panCardDetailsDto;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private CustomImageZoomDialogDM customImagePreviewDialog;

    public PANDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_pan_details, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());

        //References
        txtPANNumberLbl = view.findViewById(R.id.txtPANNumberLbl);
        editTextPANNumber1 = view.findViewById(R.id.editTextPANNumber1);
        editTextPANNumber2 = view.findViewById(R.id.editTextPANNumber2);
        editTextNameAsPerPANCard = view.findViewById(R.id.editTextNameAsPerPANCard);
        txtPanCardScanCopyLbl = view.findViewById(R.id.txtPanCardScanCopyLbl);
        imgPanScanCopy = view.findViewById(R.id.imgPanScanCopyDoc);
        layoutPANCardDetail = view.findViewById(R.id.layoutPANCardDetail);

        imgPanScanCopy.setOnClickListener(this);

        setCompulsoryMarkLabel();

        CommonUtils.InputFiletrWithMaxLength(editTextNameAsPerPANCard, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber1, "~#^|$%&*!", 10);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber2, "~#^|$%&*!", 10);

        TextChangedListenerPANcard();

        return view;
    }

    public void reload(String data) {

        //Reload Data
        if (TextUtils.isEmpty(data))
            panCardDetailsDto = new PANCardDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<PANCardDetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<PANCardDetailsDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    panCardDetailsDto = applicationList.get(0);
                } else {
                    panCardDetailsDto = new PANCardDetailsDto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Bind Data
        bindData();
    }

    private void bindData() {

        //STEP 1: PAN Card Holder Name
        editTextNameAsPerPANCard.setText(panCardDetailsDto.getNameAsPanCard());

        //STEP 2.1: PAN Card Number
        editTextPANNumber1.setText(panCardDetailsDto.getPanNumber());

        //STEP 2.2: Confirm PAN Card Number
        editTextPANNumber2.setText(panCardDetailsDto.getPanNumber());

        //STEP 3: PAN Card Scan Copy
        boolean IsGSTImgPDF = (panCardDetailsDto.getPanCardScanExt() != null && panCardDetailsDto.getPanCardScanExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsGSTImgPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgPanScanCopy);

        } else {
            if (!TextUtils.isEmpty(panCardDetailsDto.getPanCardScanId())) {
                String gstUrl = Constants.DownloadImageUrl + panCardDetailsDto.getPanCardScanId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgPanScanCopy);
            }
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(panCardDetailsDto.getIsEditable()) && panCardDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutPANCardDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);
    }

    //region TextChangedListener PAN Card
    private void TextChangedListenerPANcard() {

        editTextPANNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextPANNumber1.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int len = editTextPANNumber1.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextPANNumber1.getText().toString().contains(" ")) {
                    editTextPANNumber1.setText(editTextPANNumber1.getText().toString().replaceAll(" ", ""));
                    editTextPANNumber1.setSelection(editTextPANNumber1.getText().length());
                }


                if (!CommonUtils.PANCardValid(String.valueOf(editable.toString()))) {
                    edittext_listener = true;
                } else {
                    edittext_listener = false;
                }

                //set color base on
                if (edittext_listener) {
                    editTextPANNumber1.setTextColor(Color.parseColor("#FF0000"));
                    editTextPANNumber1.setError("Please enter a valid PAN number.");
                } else {
                    editTextPANNumber1.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber1.setError(null);

                    String strPass1 = editTextPANNumber1.getText().toString();
                    String strPass2 = editTextPANNumber2.getText().toString();

                    if (editTextPANNumber1.length() < 1) {
                    } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                        editTextPANNumber1.setError("Not Match");
                        editTextPANNumber1.setTextColor(Color.parseColor("#000000"));
                    } else {
                        editTextPANNumber2.setTextColor(Color.parseColor("#468847"));
                        editTextPANNumber2.setError(null);
                        editTextPANNumber1.setTextColor(Color.parseColor("#468847"));
                        editTextPANNumber1.setError(null);
                    }
                }
            }
        });

        editTextPANNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextPANNumber2.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (start != 10) {
                    editTextPANNumber2.setTextColor(Color.parseColor("#000000"));
                    editTextPANNumber2.setError("Please enter a valid PAN number");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextPANNumber2.getText().toString().contains(" ")) {
                    editTextPANNumber2.setText(editTextPANNumber2.getText().toString().replaceAll(" ", ""));
                    editTextPANNumber2.setSelection(editTextPANNumber2.getText().length());
                }

                String strPass1 = editTextPANNumber1.getText().toString();
                String strPass2 = editTextPANNumber2.getText().toString();

                if (editTextPANNumber2.length() < 1) {
                } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                    editTextPANNumber2.setError("Not Match");
                    editTextPANNumber2.setTextColor(Color.parseColor("#000000"));
                } else {
                    editTextPANNumber2.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber2.setError(null);
                    editTextPANNumber1.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber1.setError(null);

                }
            }
        });


    }
    //endregion

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtPANNumberLbl, txtPanCardScanCopyLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgPanScanCopyDoc) {
            showImgOrPDF(view);
        }
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
                    String base64Data = CommonUtils.encodeFileToBase64Binary(file);

                    panCardDetailsDto.setPanCardScanBase64(base64Data);
                    panCardDetailsDto.setPanCardScanExt(ext);
                    panCardDetailsDto.setChangedPhoto(true);

                    Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgPanScanCopy);

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

    private void setImageData(Uri imageUri, String imageBase64, Bitmap bitmapNew) {

        GSTINDTO imageDto = new GSTINDTO();
        imageDto.setUri(imageUri);
        imageDto.setBitmap(bitmapNew);
        imageDto.setChangedPhoto(true);
        imageDto.setGstImage(imageBase64);

        showImagePreviewDialog((Object) imageDto);
    }

    public void setImageAndName(String base64Data, Uri uri) {
        try {
            Bitmap imageBitMap = null;
            boolean IsDrawable = false;
            String ext = "jpg";     //FileUtils.getFileExtension(context, uri);
            if (ext.equalsIgnoreCase("pdf"))
                IsDrawable = true;
            else
                imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            setScanCopyData(IsDrawable, imageBitMap, base64Data, ext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String base64, String ext) {

        switch (FROM) {

            case PAN_CARD_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPanScanCopy);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgPanScanCopy);
                }

                panCardDetailsDto.setPanCardScanBase64(base64);
                panCardDetailsDto.setPanCardScanExt(ext);
                break;

            default:
                break;

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
                            FROM = PAN_CARD_SCAN_COPY;

                            String ext = TextUtils.isEmpty(panCardDetailsDto.getPanCardScanExt()) ? "jpg" : panCardDetailsDto.getPanCardScanExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(panCardDetailsDto.getPanCardScanId()) || panCardDetailsDto.getPanCardScanId().equalsIgnoreCase("0")) && TextUtils.isEmpty(panCardDetailsDto.getPanCardScanBase64())) {
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
                            FilteredFilePickerActivity.FILE_TYPE = "pdf";

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

    public int IsPANDetailsValidated() {

        //STEP 1.1: PAN Number
        panCardDetailsDto.setPanNumber(editTextPANNumber1.getText().toString().trim());
        if (TextUtils.isEmpty(panCardDetailsDto.getPanNumber()) || !editTextPANNumber1.getText().toString().trim().matches(CommonUtils.pancardpattern)) {
            Toast.makeText(context, "Please enter PAN Number.", Toast.LENGTH_LONG).show();
            editTextPANNumber1.setError("Please enter PAN Number.");
            return 1;
        }

        //STEP 1.2: Confirm PAN Number
        if (TextUtils.isEmpty(panCardDetailsDto.getPanNumber()) || !editTextPANNumber2.getText().toString().trim().matches(CommonUtils.pancardpattern)) {
            Toast.makeText(context, "Please enter Confirm PAN Number.", Toast.LENGTH_LONG).show();
            editTextPANNumber2.setError("Please enter Confirm PAN Number.");
            return 1;
        }

        //STEP 2: Name as per PAN Card
        panCardDetailsDto.setNameAsPanCard(editTextNameAsPerPANCard.getText().toString().trim());
        /*if (TextUtils.isEmpty(panCardDetailsDto.getNameAsPanCard())) {
            Toast.makeText(context, "Please enter Name as mentioned on PAN Card.", Toast.LENGTH_LONG).show();
            editTextNameAsPerPANCard.setError("Please enter Name as mentioned on PAN Card.");
            return 1;
        }*/

        //STEP 3: PAN Card Scan Copy
        if (TextUtils.isEmpty(panCardDetailsDto.getPanCardScanId()) || panCardDetailsDto.getPanCardScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(panCardDetailsDto.getPanCardScanBase64())) {
                showMessage("Please add PAN Card Scan Copy.");
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

    public PANCardDetailsDto getPANCardDetailsDto() {

        if (panCardDetailsDto == null)
            return null;

        panCardDetailsDto.setNameAsPanCard(editTextNameAsPerPANCard.getText().toString());
        panCardDetailsDto.setPanNumber(editTextPANNumber1.getText().toString());

        return panCardDetailsDto;
    }

    private void refreshGSTDetailsImg(GSTINDTO imageDto) {

        panCardDetailsDto.setChangedPhoto(imageDto.isChangedPhoto());
        panCardDetailsDto.setPanCardScanId(imageDto.getGstImageId());
        panCardDetailsDto.setBitmap(imageDto.getBitmap());
        panCardDetailsDto.setPanCardScanExt("jpg");
        panCardDetailsDto.setPanCardScanBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(panCardDetailsDto.isChangedPhoto());
        imageDto.setGstImageId(panCardDetailsDto.getPanCardScanId());
        imageDto.setBitmap(panCardDetailsDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(panCardDetailsDto.getPanCardScanBase64());
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

                    refreshGSTDetailsImg(dto);

                    //Refresh PAN Card Image
                    if (!TextUtils.isEmpty(panCardDetailsDto.getPanCardScanBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(panCardDetailsDto.getPanCardScanBase64());
                        if (bitmap != null)
                            imgPanScanCopy.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "PAN Card Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(panCardDetailsDto.getIsEditable()) && panCardDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (panCardDetailsDto.getPanCardScanExt() != null && panCardDetailsDto.getPanCardScanExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(panCardDetailsDto.getIsEditable()) && panCardDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

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
}
