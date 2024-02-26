package in.vakrangee.franchisee.atmtechlivechecklist;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.atmtechlivechecklist.adapter.ATMCheckListAdapter;
import in.vakrangee.franchisee.atmtechlivechecklist.asynctask.AsyncGetATMTechLiveCheckList;
import in.vakrangee.franchisee.atmtechlivechecklist.model.ATMTechLiveCheckListDto;
import in.vakrangee.franchisee.atmtechlivechecklist.model.OptionsDto;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.model.PhotoDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class ATMTechLiveCheckListFragment extends BaseFragment {

    private View view;
    private Context context;
    private RecyclerView recyclerViewCheckListDetails;
    private TextView txtNoDataMsg;
    private AsyncGetATMTechLiveCheckList asyncGetATMTechLiveCheckList = null;
    private List<ATMTechLiveCheckListDto> atmTechLiveCheckList;
    private ATMCheckListAdapter atmCheckListAdapter;
    private MaterialButton btnSubmit;
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private String mCurrentPhotoPath;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private String selectedImageType;
    private static final String PROOF_IMAGE = "Proof Image Preview";
    private PhotoDto selProofDto;
    private CustomAllImagePreviewDialog customImagePreviewDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final String EXT_JPG = "jpg";
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private int selectedPos;
    private ATMTechLiveCheckListDto selectedDto;

    public ATMTechLiveCheckListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_atm_techlive_checklist, container, false);

        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        recyclerViewCheckListDetails = view.findViewById(R.id.recyclerViewCheckListDetails);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);

                atmTechLiveCheckList = atmCheckListAdapter.getAtmTechLiveCheckList();
                if (atmTechLiveCheckList.size() > 0) {

                    int status = validateCheckList();
                    if (status != 0)
                        return;

                    //Save
                }

            }
        });
        return view;
    }

    public void reloadData() {
        asyncGetATMTechLiveCheckList = new AsyncGetATMTechLiveCheckList(context, new AsyncGetATMTechLiveCheckList.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncGetATMTechLiveCheckList.execute("");
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
            JSONArray atmCheckListDetailsJSON = jsonObject.getJSONArray("checklist");

            if (atmCheckListDetailsJSON == null || atmCheckListDetailsJSON.length() == 0) {
                return;
            }

            Gson gson = new GsonBuilder().create();
            atmTechLiveCheckList = gson.fromJson(atmCheckListDetailsJSON.toString(), new TypeToken<ArrayList<ATMTechLiveCheckListDto>>() {
            }.getType());

            if (atmTechLiveCheckList != null && atmTechLiveCheckList.size() > 0) {
                txtNoDataMsg.setVisibility(View.GONE);
                recyclerViewCheckListDetails.setVisibility(View.VISIBLE);
                atmCheckListAdapter = new ATMCheckListAdapter(context, atmTechLiveCheckList, new ATMCheckListAdapter.IclickListener() {
                    @Override
                    public void nextClickListener(int position) {

                    }

                    @Override
                    public void cameraClick(int position, ATMTechLiveCheckListDto dto) {
                        selProofDto = new PhotoDto();
                        selectedPos = position;
                        selectedDto = dto;
                        selectedImageType = PROOF_IMAGE;

                        if (TextUtils.isEmpty(selectedDto.getImg()) && TextUtils.isEmpty(selectedDto.getProofPicBase64())) {
                            startCamera(view);
                        } else {
                            Bitmap bitmap = CommonUtils.StringToBitMap(selectedDto.getProofPicBase64());
                            displayPreviewDialog(selectedDto.getProofPicBase64(), bitmap);
                        }
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewCheckListDetails.setLayoutManager(layoutManager);
                recyclerViewCheckListDetails.setItemAnimator(new DefaultItemAnimator());
                recyclerViewCheckListDetails.setAdapter(atmCheckListAdapter);

            } else {
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerViewCheckListDetails.setVisibility(View.GONE);
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
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                picUri = FileProvider.getUriForFile(context.getApplicationContext(), context.getApplicationContext().getPackageName() + ".provider", photoFile);
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
                    displayPreviewDialog(imageBase64, bitmapNew);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    private void displayPreviewDialog(String imageBase64, Bitmap bitmapNew) {

        PhotoDto imageDto = null;
        if (selectedImageType.equalsIgnoreCase(PROOF_IMAGE)) {

            selProofDto.setImageBase64(imageBase64);
            selProofDto.setBitmap(bitmapNew);
            selProofDto.setPhotoId(selectedDto.getImg());
            imageDto = selProofDto;
        }
        showImagePreviewDialog((Object) imageDto);
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
                    if (selectedImageType.equalsIgnoreCase(PROOF_IMAGE)) {
                        selProofDto.setChangedPhoto(true);
                    }

                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    //save data
                    PhotoDto photoDto = ((PhotoDto) object);
                    String base64 = photoDto.getImageBase64();
                    Bitmap bitmap = photoDto.getBitmap();

                    if (selectedImageType.equalsIgnoreCase(PROOF_IMAGE)) {
                        if (base64 != null || bitmap != null) {
                            selectedDto.setProofPicBase64(base64);
                            selectedDto.setProofExt(EXT_JPG);
                            atmTechLiveCheckList.set(selectedPos, selectedDto);
                            atmCheckListAdapter.notifyDataSetChanged();
                            //imgProofImage.setImageBitmap(bitmap);
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
    public void onDestroy() {
        super.onDestroy();

        if (asyncGetATMTechLiveCheckList != null && !asyncGetATMTechLiveCheckList.isCancelled()) {
            asyncGetATMTechLiveCheckList.cancel(true);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    private List<OptionsDto> prepareOptionsSelectedList(ATMTechLiveCheckListDto atmTechLiveCheckListDto) {
        List<OptionsDto> answersList = new ArrayList<OptionsDto>();
        atmTechLiveCheckListDto.optSelectedAnswersList = new ArrayList<>();
        for (Map.Entry m : atmTechLiveCheckListDto.selectedAnsList.entrySet()) {
            OptionsDto optionsDto = (OptionsDto) m.getValue();
            atmTechLiveCheckListDto.optSelectedAnswersList.add(optionsDto);
        }

        return answersList;
    }

    private int validateCheckList() {
        int status = 0;

        for (int i = 0; i < atmTechLiveCheckList.size(); i++) {
            ATMTechLiveCheckListDto quesDto = atmTechLiveCheckList.get(i);
            prepareOptionsSelectedList(quesDto);

            if (!TextUtils.isEmpty(quesDto.getIsMan()) && quesDto.getIsMan().equalsIgnoreCase("1")) {

                String isImgAvail = TextUtils.isEmpty(quesDto.getIsImg()) ? "0" : quesDto.getIsImg();
                int editText = 0;
                String type = TextUtils.isEmpty(quesDto.getCt()) ? ATMCheckListConstants.TYPE_TEXTBOX_TEXT : quesDto.getCt();
                switch (type) {

                    case ATMCheckListConstants.TYPE_CHECKBOX:
                    case ATMCheckListConstants.TYPE_RADIO_BUTTON:
                        status = quesDto.selectedAnsList.size() > 0 ? 0 : -1;
                        editText = 0;

                        //Image
                        if (status == 0 && isImgAvail.equalsIgnoreCase("1")) {
                            status = validateImage(quesDto);
                            editText = 4;
                        }
                        break;

                    case ATMCheckListConstants.TYPE_DROP_DOWN:
                        status = quesDto.selectedAnsList.size() > 0 ? 0 : -1;
                        editText = 2;

                        //Image
                        if (status == 0 && isImgAvail.equalsIgnoreCase("1")) {
                            status = validateImage(quesDto);
                            editText = 4;
                        }
                        break;

                    case ATMCheckListConstants.TYPE_TEXTBOX_INT:
                        status = (TextUtils.isEmpty(quesDto.getAnswerTxt()) || quesDto.getAnswerTxt().equalsIgnoreCase("0")) ? -1 : 0;
                        editText = 1;

                        //Image
                        if (status == 0 && isImgAvail.equalsIgnoreCase("1")) {
                            status = validateImage(quesDto);
                            editText = 4;
                        }
                        break;

                    default:
                        editText = 1;
                        status = (TextUtils.isEmpty(quesDto.getAnswerTxt()) || quesDto.getAnswerTxt().length() < 3) ? -1 : 0;

                        //Image
                        if (status == 0 && isImgAvail.equalsIgnoreCase("1")) {
                            status = validateImage(quesDto);
                            editText = 4;
                        }
                        break;
                }

                //Not Answered
                if (status != 0) {
                    TextView txtQuestion = recyclerViewCheckListDetails.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txtQuestionName);
                    EditText editTextAnswer = recyclerViewCheckListDetails.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editTextAnswer);
                    Spinner spinnerOptions = recyclerViewCheckListDetails.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.spinnerOptions);

                    String msg = editText == 1 ? "Please Input Correct Answer." : "Please select Correct Answer.";
                    if (editText == 1) {
                        editTextAnswer.setError(msg);

                    } else if (editText == 2) {             //Spinner
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        GUIUtils.setErrorToSpinner(spinnerOptions, msg, context);

                    } else if (editText == 4) {             //Image
                        String imgMsg = "Please add Proof Image for " + quesDto.getName();
                        showMessage(imgMsg);
                    } else {
                        txtQuestion.setError(msg);
                        showMessage(msg);
                    }
                    break;
                }
            }
        }
        return status;
    }

    private int validateImage(ATMTechLiveCheckListDto quesDto) {
        if (TextUtils.isEmpty(quesDto.getImg()) || quesDto.getImg().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(quesDto.getProofPicBase64())) {
                return 1;
            }
        }
        return 0;
    }
}
