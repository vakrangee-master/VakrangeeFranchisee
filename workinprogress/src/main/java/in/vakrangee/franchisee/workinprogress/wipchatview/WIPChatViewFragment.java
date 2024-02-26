package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shrikanthravi.chatview.data.Message;
import com.shrikanthravi.chatview.widget.ChatView;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.commongui.CustomFranchiseeApplicationSpinnerAdapter;
import in.vakrangee.supercore.franchisee.model.PhotoDto;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class WIPChatViewFragment extends Fragment {

    private static final String TAG = "WIPChatViewFragment";
    private View view;
    private Context context;
    private Logger logger;
    private ChatView chatView;
    public static int CAMERA_REQUEST = 12;
    private String USER_1 = "Me";
    private String selectedCat;
    private List<CustomFranchiseeApplicationSpinnerDto> elementsList;
    private List<CustomFranchiseeApplicationSpinnerDto> subElementsList;
    private AsyncGetWIPCategoryDetails asyncGetWIPCategoryDetails = null;
    private AsyncSaveWIPChatMessage asyncSaveWIPChatMessage = null;
    private AsyncGetWIPMessageWithPagination asyncGetWIPMessageWithPagination = null;
    private CustomFranchiseeApplicationSpinnerAdapter elementsAdapter;
    private CustomFranchiseeApplicationSpinnerDto selCategoryDto;
    private CustomFranchiseeApplicationSpinnerDto selSubCategoryDto;

    private List<Message> wipMessagesList;
    private String ELEMENT_TYPE_INTERIOR = "1";
    private int SIMPLE_MSG = 1;
    private int SIMPLE_IMAGE = 2;
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private String mCurrentPhotoPath;
    private CustomChatPreviewCaptionDialog customChatPreviewCaptionDialog;
    private Bitmap bitmapNew;
    private int CURRENT_PAGE_NO = 0;
    private boolean IsFilterOn = false;
    private String filterElementId, filterElementDetailId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_wip_chatview, container, false);

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        permissionHandler = new PermissionHandler(getActivity());

        // Font
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        //Widgets
        chatView = (ChatView) view.findViewById(R.id.chatView);
        chatView.setTextSize(16);

        //Send button click listerer
        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                if (TextUtils.isEmpty(body) || body.trim().length() == 0)
                    return;

                int status = validateCategory();
                if (status == 0) {

                    Message message = prepareSimpleMsg(body, SIMPLE_MSG);
                    saveChatMessage(message);
                }
            }
        });

        //Camera button click listener
        chatView.setOnClickCameraButtonListener(new ChatView.OnClickCameraButtonListener() {
            @Override
            public void onCameraButtonClicked() {

                int status = validateCategory();
                if (status == 0) {
                    //Start Camera
                    startCamera(view);
                }
            }
        });

        chatView.hideOtherOptions();

        setSwipeRefreshLayout();

        return view;
    }

    private void setSwipeRefreshLayout() {
        // Adding Listener
        chatView.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Get More Messages
                        getWIPMessageWithPagination();

                        // Stop animation (This will be after 3 seconds)
                        chatView.swipeContainer.setRefreshing(false);
                    }
                }, 500); // Delay in millis
            }
        });

        // Scheme colors for animation
        chatView.swipeContainer.setColorSchemeColors(
                getResources().getColor(R.color.vl_orange),
                getResources().getColor(R.color.vl_purple),
                getResources().getColor(R.color.vl_green)
        );
    }

    private Message prepareSimpleMsg(String body, int type) {

        Message message = null;
        try {
            Connection connection = new Connection(context);
            String vkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(context);
            String vkIdOrEnquiryId = TextUtils.isEmpty(vkId) ? enquiryId : vkId;

            message = new Message();
            message.setBody(body);
            message.setVkIdOrEnquiryId(vkIdOrEnquiryId);
            message.setCategoryId(selCategoryDto.getId());
            message.setCategory(selCategoryDto.getName());
            message.setSubCategoryId(selSubCategoryDto.getId());
            message.setSubCategory(selSubCategoryDto.getName());
            message.setElement_type(ELEMENT_TYPE_INTERIOR);
            message.setTime(getTime());
            message.setUserName(USER_1);

            if (type == SIMPLE_MSG) {
                message.setType(Message.RightSimpleMessage);
                message.setMessageType(Message.MessageType.RightSimpleMessage);

            } else if (type == SIMPLE_IMAGE) {

                message.setType(Message.RightSingleImage);
                message.setMessageType(Message.MessageType.RightSingleImage);

                picUri = Uri.parse(mCurrentPhotoPath);
                mCurrentPhotoPath = mCurrentPhotoPath.replace("file:/", "file://");
                Uri imageUri = Uri.parse(mCurrentPhotoPath);

                File file = new File(picUri.getPath());
                file = new ImageZipper(context).setQuality(50).compressToFile(file);
                bitmapNew = new ImageZipper(context).compressToBitmap(file);
                String base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                message.setElementImageBase64(base64Data);
                message.imageList.add(imageUri);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private String getTime() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String time = mdformat.format(calendar.getTime());
        return time;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 12: {

                //Image Capture result
                if (resultCode == RESULT_OK) {

                    Message message = prepareSimpleMsg(null, SIMPLE_IMAGE);
                    displayPreviewDialog(message);

                }
                break;
            }
        }
    }

    public void reloadData() {

        CURRENT_PAGE_NO = 0;

        //Get WIP Chat Messages
        getWIPChatMessages();

        //Get Category
        populateCategory();
    }

    public void refreshData(String elementId, String elementDetailId, boolean IsFilter) {
        this.filterElementId = elementId;
        this.filterElementDetailId = elementDetailId;
        this.IsFilterOn = IsFilter;

        CURRENT_PAGE_NO = 0;

        //Get WIP Chat Messages
        getWIPChatMessages();

    }

    public void getWIPChatMessages() {

        CURRENT_PAGE_NO++;
        asyncGetWIPMessageWithPagination = new AsyncGetWIPMessageWithPagination(context, String.valueOf(CURRENT_PAGE_NO), filterElementId, filterElementDetailId, IsFilterOn, new AsyncGetWIPMessageWithPagination.Callback() {
            public void onResult(String result) {
                processData(result);
            }
        });
        asyncGetWIPMessageWithPagination.execute("");
    }

    /**
     * Populate Category
     */
    public void populateCategory() {

        asyncGetWIPCategoryDetails = new AsyncGetWIPCategoryDetails(context, Constants.WIP_CHATVIEW_CATEGORY, null, new AsyncGetWIPCategoryDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process response
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new GsonBuilder().create();
                    elementsList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
                    }.getType());
                    elementsList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                    elementsAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, elementsList);
                    chatView.spinnerCategory.setEnabled(true);
                    chatView.spinnerCategory.setAdapter(elementsAdapter);
                    chatView.spinnerCategory.setSelection(0);
                    chatView.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                selCategoryDto = (CustomFranchiseeApplicationSpinnerDto) chatView.spinnerCategory.getItemAtPosition(position);
                                populateSubCategory(selCategoryDto.getId());
                            } else {
                                selCategoryDto = null;
                                selSubCategoryDto = null;
                                chatView.spinnerSubCategory.setAdapter(null);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetWIPCategoryDetails.execute("");
    }

    public void populateSubCategory(String categoryId) {

        if (!InternetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
            return;
        }

        asyncGetWIPCategoryDetails = new AsyncGetWIPCategoryDetails(context, Constants.WIP_CHATVIEW_SUB_CATEGORY, categoryId, new AsyncGetWIPCategoryDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process response
                    JSONArray jsonArray = new JSONArray(result);
                    Gson gson = new GsonBuilder().create();
                    subElementsList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
                    }.getType());
                    subElementsList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                    elementsAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, subElementsList);
                    chatView.spinnerSubCategory.setEnabled(true);
                    chatView.spinnerSubCategory.setAdapter(elementsAdapter);
                    chatView.spinnerSubCategory.setSelection(0);
                    chatView.spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                selSubCategoryDto = (CustomFranchiseeApplicationSpinnerDto) chatView.spinnerSubCategory.getItemAtPosition(position);
                                selectedCat = selSubCategoryDto.getName();
                            } else {
                                selSubCategoryDto = null;
                                selectedCat = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });

        asyncGetWIPCategoryDetails.execute("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncGetWIPCategoryDetails != null && !asyncGetWIPCategoryDetails.isCancelled()) {
            asyncGetWIPCategoryDetails.cancel(true);
            asyncGetWIPCategoryDetails = null;
        }

        if (asyncSaveWIPChatMessage != null && !asyncSaveWIPChatMessage.isCancelled()) {
            asyncSaveWIPChatMessage.cancel(true);
            asyncSaveWIPChatMessage = null;
        }

        if (asyncGetWIPMessageWithPagination != null && !asyncGetWIPMessageWithPagination.isCancelled()) {
            asyncGetWIPMessageWithPagination.cancel(true);
            asyncGetWIPMessageWithPagination = null;
        }

        //Image Preview DIalog
        if (customChatPreviewCaptionDialog != null) {
            customChatPreviewCaptionDialog.dismiss();
            customChatPreviewCaptionDialog = null;
        }
    }

    private int validateCategory() {

        //STEP 1: Check Category Selected
        if (selCategoryDto == null) {
            AlertDialogBoxInfo.alertDialogShow(context, "Please select Category first.");
            return -1;
        }

        if (selSubCategoryDto == null) {
            AlertDialogBoxInfo.alertDialogShow(context, "Please select Sub Category first.");
            return -2;
        }

        return 0;
    }

    private void processData(String result) {
        try {
            //Check if response if null or empty
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                return;
            }

            //Check response
            if (result.startsWith("ERROR|")) {

                StringTokenizer tokens = new StringTokenizer(result, "|");
                tokens.nextToken();     // Jump to next Token
                String errMsg = tokens.nextToken();

                if (!TextUtils.isEmpty(errMsg)) {
                    AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                }
                return;
            }

            //Process response
            if (result.startsWith("OKAY|")) {
                StringTokenizer st1 = new StringTokenizer(result, "|");
                String key = st1.nextToken();
                String data = st1.nextToken();

                if (TextUtils.isEmpty(data)) {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    return;
                }

                //Process Response
                chatView.clearMessages();
                JSONArray jsonArray = new JSONArray(data);
                Gson gson = new GsonBuilder().create();
                wipMessagesList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Message>>() {
                }.getType());

                for (int i = 0; i < wipMessagesList.size(); i++) {
                    Message message = wipMessagesList.get(i);

                    if (!TextUtils.isEmpty(message.getElementImageId())) {

                        String Url = Constants.DownloadImageUrl + message.getElementImageId();
                        Uri uri = Uri.parse(Url);

                        //For Image
                        message.imageList.add(uri);
                        wipMessagesList.set(i, message);
                    }
                    chatView.addMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    private void saveChatMessage(final Message message) {
        if (!InternetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
            return;
        }

        asyncSaveWIPChatMessage = new AsyncSaveWIPChatMessage(context, message, new AsyncSaveWIPChatMessage.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Check response
                    if (result.startsWith("ERROR|")) {

                        StringTokenizer tokens = new StringTokenizer(result, "|");
                        tokens.nextToken();     // Jump to next Token
                        String errMsg = tokens.nextToken();

                        if (!TextUtils.isEmpty(errMsg)) {
                            AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                        }
                        return;
                    }

                    //Process response
                    if (result.startsWith("OKAY|")) {

                        wipMessagesList.add(message);
                        chatView.addMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });

        asyncSaveWIPChatMessage.execute("");

    }

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
                }
            }
        });
    }

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

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                picUri = FileProvider.getUriForFile(getContext(), context.getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private void displayPreviewDialog(Message message) {

        PhotoDto imageDto = new PhotoDto();
        imageDto.setImageBase64(message.getElementImageBase64());
        imageDto.setBitmap(bitmapNew);
        imageDto.setPhotoId(message.getElementImageId());

        showImagePreviewDialog((Object) imageDto, message);
    }

    //region ShowImagePreviewDialog
    private void showImagePreviewDialog(Object object, final Message message) {
        String selectedCat = message.getCategory() + " > " + message.getSubCategory();

        if (customChatPreviewCaptionDialog != null && customChatPreviewCaptionDialog.isShowing()) {
            customChatPreviewCaptionDialog.refresh(object, selectedCat);
            return;
        }

        if (object != null) {
            customChatPreviewCaptionDialog = new CustomChatPreviewCaptionDialog(context, object, selectedCat, new CustomChatPreviewCaptionDialog.IImagePreviewDialogClicks() {

                @Override
                public void OkClick(Object object) {
                    //save data
                    PhotoDto photoDto = ((PhotoDto) object);
                    message.setBody(photoDto.getRemarks());
                    saveChatMessage(message);
                }
            });

            customChatPreviewCaptionDialog.show();
            customChatPreviewCaptionDialog.setCancelable(false);


        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    private void getWIPMessageWithPagination() {

        if (!InternetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
            return;
        }

        CURRENT_PAGE_NO++;
        asyncGetWIPMessageWithPagination = new AsyncGetWIPMessageWithPagination(context, String.valueOf(CURRENT_PAGE_NO), filterElementId, filterElementDetailId, IsFilterOn, new AsyncGetWIPMessageWithPagination.Callback() {
            @Override
            public void onResult(String result) {

                List<Message> olderMsgList = getOlderMessages(result);
                if (olderMsgList != null && olderMsgList.size() > 0) {
                    chatView.addOlderMessages(olderMsgList);
                }
            }
        });
        asyncGetWIPMessageWithPagination.execute("");
    }

    private List<Message> getOlderMessages(String result) {
        List<Message> wipMessagesList = new ArrayList<>();

        try {
            //Check if response if null or empty
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                return wipMessagesList;
            }

            //Check response
            if (result.startsWith("ERROR|")) {

                StringTokenizer tokens = new StringTokenizer(result, "|");
                tokens.nextToken();     // Jump to next Token
                String errMsg = tokens.nextToken();

                if (!TextUtils.isEmpty(errMsg)) {
                    AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                }
                return wipMessagesList;
            }

            //Process response
            if (result.startsWith("OKAY|")) {
                StringTokenizer st1 = new StringTokenizer(result, "|");
                String key = st1.nextToken();
                String data = st1.nextToken();

                if (TextUtils.isEmpty(data)) {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    return wipMessagesList;
                }

                //Process Response
                JSONArray jsonArray = new JSONArray(data);
                Gson gson = new GsonBuilder().create();
                wipMessagesList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Message>>() {
                }.getType());

                for (int i = 0; i < wipMessagesList.size(); i++) {
                    Message message = wipMessagesList.get(i);

                    if (!TextUtils.isEmpty(message.getElementImageId())) {

                        String Url = "https://vkmssit.vakrangee.in/ImageDisplayDatabase?type=A&image_id=" + message.getElementImageId();
                        Uri uri = Uri.parse(Url);

                        //For Image
                        List<Uri> uriList = new ArrayList<>();
                        uriList.add(uri);
                        message.setImageList(uriList);
                        wipMessagesList.set(i, message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wipMessagesList;
    }

}
