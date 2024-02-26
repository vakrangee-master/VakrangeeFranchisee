package in.vakrangee.franchisee.support;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDto;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.support.CategoryDto;
import in.vakrangee.supercore.franchisee.support.CustomSpinnerAdapter;
import in.vakrangee.supercore.franchisee.support.IssueTypeDto;
import in.vakrangee.supercore.franchisee.support.SubCategoryDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class SupportTicketActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final static String TAG = "SupportTicketActivity";

    @BindView(R.id.spinnerCategory)
    Spinner spinnerCategory;
    @BindView(R.id.spinnerSubCategory)
    Spinner spinnerSubCategory;
    @BindView(R.id.spinnerIssueType)
    Spinner spinnerIssueType;
    @BindView(R.id.editTextDescription)
    EditText editTextDescription;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.btnSubmitTicket)
    Button btnSubmitTicket;
    @BindView(R.id.btnAttachment)
    Button btnAttachment;
    @BindView(R.id.recyclerViewAttachments)
    RecyclerView recyclerViewAttachments;

    private CustomSpinnerAdapter customSpinnerAdapter;
    private CategoryDto selCategoryDto;
    private SubCategoryDto selSubCategoryDto;
    private IssueTypeDto selIssueTypeDto;
    private AsyncGetCatSubCatAndIssueType asyncGetCatSubCatAndIssueType = null;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private List<FileAttachmentDto> fileAttachmentList = new ArrayList<FileAttachmentDto>();
    private Uri picUri;                 // Picture URI
    private AttachmentListAdapter attachmentListAdapter;
    private PermissionHandler permissionHandler;
    Toolbar toolbar;
    private boolean IsBackVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_ticket);

        // Initialize all views
        ButterKnife.bind(this);
        permissionHandler = new PermissionHandler(this);
        Intent intent = getIntent();
        IsBackVisible = intent.getBooleanExtra("IsBackVisible", false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            mTitle.setText(R.string.name_support_ticket);

        }

        // Font
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        // Set Font
        btnClear.setTypeface(font);
        btnCancel.setTypeface(font);
        btnSubmitTicket.setTypeface(font);

        // Set Font Text
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnSubmitTicket.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        btnClear.setOnClickListener(SupportTicketActivity.this);
        btnCancel.setOnClickListener(this);
        btnSubmitTicket.setOnClickListener(this);
        btnAttachment.setOnClickListener(this);

        if (!InternetConnection.isNetworkAvailable(this)) {
            AlertDialogBoxInfo.alertDialogShow(this, getResources().getString(R.string.internetCheck));
            btnClear.setVisibility(View.GONE);
            btnSubmitTicket.setVisibility(View.GONE);
        } else {
            btnClear.setVisibility(View.VISIBLE);
            btnSubmitTicket.setVisibility(View.VISIBLE);
            //If the app has not the permission then asking for the permission
            permissionHandler.requestPermission(btnSubmitTicket, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        populateCategory();
                    }
                }
            });
        }
        setAttachmentAdapter(fileAttachmentList);
    }

    /**
     * Populate Category
     */
    public void populateCategory() {

        //Context context, String FROM, String categoryId, String subCategoryId, Callback callback
        asyncGetCatSubCatAndIssueType = new AsyncGetCatSubCatAndIssueType(this, Constants.SUPPORT_TICKET_CATEGORY, null, null, new AsyncGetCatSubCatAndIssueType.Callback() {
            @Override
            public void onResult(String result) {

                try {
                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process response
                    StringTokenizer tokens1 = new StringTokenizer(result, "|");
                    String key = tokens1.nextToken();
                    String response = tokens1.nextToken();

                    if (key.equalsIgnoreCase(Constants.OKAY_RESPONSE)) {

                        boolean IsValidJSON = CommonUtils.isJSONValid(response);
                        if (!IsValidJSON) {
                            AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, response);
                            return;
                        }

                        Gson gson = new GsonBuilder().create();
                        ArrayList<CategoryDto> categoryList = gson.fromJson(response, new TypeToken<ArrayList<CategoryDto>>() {
                        }.getType());
                        categoryList.add(0, new CategoryDto("0", getResources().getString(R.string.pleaseSelect)));

                        ArrayList<Object> list1 = new ArrayList<Object>(categoryList);
                        customSpinnerAdapter = new CustomSpinnerAdapter(SupportTicketActivity.this, list1);
                        spinnerCategory.setEnabled(true);
                        spinnerCategory.setAdapter(customSpinnerAdapter);
                        spinnerCategory.setSelection(0);
                        spinnerCategory.setOnItemSelectedListener(SupportTicketActivity.this);

                    } else if (key.equalsIgnoreCase(Constants.ERROR_RESPONSE)) {
                        AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetCatSubCatAndIssueType.execute("");
    }

    public void populateSubCategory(final CategoryDto categoryDto) {

        if (!InternetConnection.isNetworkAvailable(this)) {
            AlertDialogBoxInfo.alertDialogShow(this, getResources().getString(R.string.internetCheck));
            return;
        }

        asyncGetCatSubCatAndIssueType = new AsyncGetCatSubCatAndIssueType(this, Constants.SUPPORT_TICKET_SUB_CATEGORY, categoryDto.getId(), null, new AsyncGetCatSubCatAndIssueType.Callback() {
            @Override
            public void onResult(String result) {

                try {

                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process response
                    StringTokenizer tokens1 = new StringTokenizer(result, "|");
                    String key = tokens1.nextToken();
                    String response = tokens1.nextToken();

                    if (key.equalsIgnoreCase(Constants.OKAY_RESPONSE)) {

                        boolean IsValidJSON = CommonUtils.isJSONValid(response);
                        if (!IsValidJSON) {
                            AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, response);
                            return;
                        }

                        Gson gson = new GsonBuilder().create();
                        ArrayList<SubCategoryDto> subCategoryList = gson.fromJson(response, new TypeToken<ArrayList<SubCategoryDto>>() {
                        }.getType());
                        subCategoryList.add(0, new SubCategoryDto("0", getResources().getString(R.string.pleaseSelect)));

                        ArrayList<Object> list1 = new ArrayList<Object>(subCategoryList);
                        customSpinnerAdapter = new CustomSpinnerAdapter(SupportTicketActivity.this, list1);
                        spinnerSubCategory.setEnabled(true);
                        spinnerSubCategory.setAdapter(customSpinnerAdapter);
                        spinnerSubCategory.setSelection(0);
                        spinnerSubCategory.setOnItemSelectedListener(SupportTicketActivity.this);

                    } else if (key.equalsIgnoreCase(Constants.ERROR_RESPONSE)) {
                        AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetCatSubCatAndIssueType.execute("");
    }

    public void populateIssueType(CategoryDto categoryDto, SubCategoryDto subCategoryDto) {
        if (!InternetConnection.isNetworkAvailable(this)) {
            AlertDialogBoxInfo.alertDialogShow(this, getResources().getString(R.string.internetCheck));
            return;
        }

        asyncGetCatSubCatAndIssueType = new AsyncGetCatSubCatAndIssueType(this, Constants.SUPPORT_TICKET_ISSUE_TYPE, categoryDto.getId(), subCategoryDto.getId(), new AsyncGetCatSubCatAndIssueType.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process response
                    StringTokenizer tokens1 = new StringTokenizer(result, "|");
                    String key = tokens1.nextToken();
                    String response = tokens1.nextToken();

                    if (key.equalsIgnoreCase(Constants.OKAY_RESPONSE)) {

                        boolean IsValidJSON = CommonUtils.isJSONValid(response);
                        if (!IsValidJSON) {
                            AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, response);
                            return;
                        }

                        Gson gson = new GsonBuilder().create();
                        ArrayList<IssueTypeDto> issueTypeList = gson.fromJson(response, new TypeToken<ArrayList<IssueTypeDto>>() {
                        }.getType());
                        issueTypeList.add(0, new IssueTypeDto("0", getResources().getString(R.string.pleaseSelect)));

                        ArrayList<Object> list1 = new ArrayList<Object>(issueTypeList);
                        customSpinnerAdapter = new CustomSpinnerAdapter(SupportTicketActivity.this, list1);
                        spinnerIssueType.setEnabled(true);
                        spinnerIssueType.setAdapter(customSpinnerAdapter);
                        spinnerIssueType.setSelection(0);
                        spinnerIssueType.setOnItemSelectedListener(SupportTicketActivity.this);

                    } else if (key.equalsIgnoreCase(Constants.ERROR_RESPONSE)) {
                        AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetCatSubCatAndIssueType.execute("");
    }

    public void setAttachmentAdapter(final List<FileAttachmentDto> fileAttachmentList) {
        attachmentListAdapter = new AttachmentListAdapter(this, fileAttachmentList, new AttachmentListAdapter.IAttachmentRemove() {
            @Override
            public void removeAttachment(FileAttachmentDto fileAttachmentDto) {
                fileAttachmentList.remove(fileAttachmentDto);
                notifyAdapter();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewAttachments.setLayoutManager(mLayoutManager);
        recyclerViewAttachments.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAttachments.setAdapter(attachmentListAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnCancel:
                break;
            case R.id.btnClear:
                resetData();
                break;
            case R.id.btnSubmitTicket:
                submitSupportTicket();
                break;
            case R.id.btnAttachment:
                showAttachmentDialog();
                break;
        }
    }

    public void showAttachmentDialog() {
        fileAttachementDialog = new FileAttachmentDialog(this, new FileAttachmentDialog.IFileAttachmentClicks() {
            @Override
            public void cameraClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(btnSubmitTicket, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            picUri = Uri.fromFile(file); // create
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
                permissionHandler.requestPermission(btnSubmitTicket, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.needs_permission_storage_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            // This always works
                            Intent i = new Intent(SupportTicketActivity.this, FilePickerActivity.class);
                            // Set these depending on your use case. These are the defaults.
                            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                            // Configure initial directory by specifying a String.
                            // You could specify a String like "/storage/emulated/0/", but that can
                            // dangerous. Always use Android's API calls to get paths to the SD-card or
                            // internal memory.
                            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                            startActivityForResult(i, BROWSE_FOLDER_REQUEST);
                        }
                    }
                });

            }
        });
        fileAttachementDialog.setCancelable(false);
        fileAttachementDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();

    }

    public void resetData() {
        setErrorToSpinner(spinnerCategory, null, true);
        spinnerCategory.setSelection(0);
        spinnerSubCategory.setAdapter(null);
        spinnerIssueType.setAdapter(null);
        editTextDescription.setText("");
        editTextDescription.setError(null);
        selCategoryDto = null;
        selSubCategoryDto = null;
        selIssueTypeDto = null;
        fileAttachmentList.clear();
        notifyAdapter();

    }

    public void setErrorToSpinner(Spinner spinner, String msg, boolean IsReset) {
        LinearLayout layout = (LinearLayout) spinner.getSelectedView();
        TextView errorText = (TextView) layout.getChildAt(0);
        //TextView errorText = (TextView) spinner.getSelectedView();
        if (errorText == null) {
            AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
            return;
        }

        if (IsReset) {
            errorText.setError(null);
            errorText.setTextColor(Color.BLACK);//just to highlight that this is an error
        } else {
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(msg);
        }
    }

    public void submitSupportTicket() {
        int status = validateSupportTicketData();
        if (status == -1) {
            setErrorToSpinner(spinnerCategory, getResources().getString(R.string.pleaseSelect), false);

        } else if (status == -2) {
            setErrorToSpinner(spinnerSubCategory, getResources().getString(R.string.pleaseSelect), false);

        } else if (status == -3) {
            setErrorToSpinner(spinnerIssueType, getResources().getString(R.string.pleaseSelect), false);

        } else if (status == -4) {
            editTextDescription.setError(getResources().getString(R.string.enterDescription));

        } else {
            Toast.makeText(this, "Submit Succeddful.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int viewId = parent.getId();
        switch (viewId) {
            case R.id.spinnerCategory:
                if (position > 0) {
                    selCategoryDto = (CategoryDto) spinnerCategory.getItemAtPosition(position);
                    populateSubCategory(selCategoryDto);
                } else {
                    selCategoryDto = null;
                    selSubCategoryDto = null;
                    selIssueTypeDto = null;
                    spinnerSubCategory.setAdapter(null);
                    spinnerIssueType.setAdapter(null);
                }
                break;

            case R.id.spinnerSubCategory:
                if (position > 0) {
                    selSubCategoryDto = (SubCategoryDto) spinnerSubCategory.getItemAtPosition(position);
                    populateIssueType(selCategoryDto, selSubCategoryDto);
                } else {
                    selSubCategoryDto = null;
                    selIssueTypeDto = null;
                    spinnerIssueType.setAdapter(null);
                }
                break;

            case R.id.spinnerIssueType:
                if (position > 0) {
                    selIssueTypeDto = (IssueTypeDto) spinnerIssueType.getItemAtPosition(position);
                } else {
                    selIssueTypeDto = null;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No use.
    }

    public int validateSupportTicketData() {

        //STEP 1: Check if Category is selected
        if (selCategoryDto == null)
            return -1;

        //STEP 2: Check if SubCategory is selected
        if (selSubCategoryDto == null)
            return -2;

        //STEP 3: Check if SubCategory is selected
        if (selIssueTypeDto == null)
            return -3;

        //STEP 4: Check if Description is filled
        if (TextUtils.isEmpty(editTextDescription.getText().toString()))
            return -4;

        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap imageBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                File file = new File(picUri.toString());
                int status = prepareAndAddFileAttachmentDto(file, picUri);
                if (status == -1)
                    AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.file_already_exist));

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.Warning));
            }
        }
        if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(data);
            for (Uri uri : files) {
                File file = Utils.getFileForUri(uri);
                int status = prepareAndAddFileAttachmentDto(file, uri);
                if (status == -1)
                    AlertDialogBoxInfo.alertDialogShow(SupportTicketActivity.this, getResources().getString(R.string.file_already_exist));

            }
        }
    }

    public boolean IsFileAlreadyExist(File file) {
        for (FileAttachmentDto attachmentDto : fileAttachmentList) {
            if (attachmentDto.getFileName().equalsIgnoreCase(file.getName()))
                return true;
        }
        return false;
    }

    private int prepareAndAddFileAttachmentDto(File file, Uri uri) {

        String type = FileUtils.getFileExtension(this, uri);
        boolean IsFileExist = IsFileAlreadyExist(file);
        if (IsFileExist)
            return -1;

        FileAttachmentDto fileAttachmentDto = new FileAttachmentDto();
        fileAttachmentDto.setDirectory(file.isDirectory());
        fileAttachmentDto.setFile(file.isFile());
        fileAttachmentDto.setFileExtension(type);
        fileAttachmentDto.setFileName(file.getName());
        fileAttachmentDto.setFilePath(file.getPath());
        fileAttachmentList.add(fileAttachmentDto);
        notifyAdapter();

        Log.d(TAG, "Testing: File Name: " + file.getName() + " IsFile: " + file.isFile() + " IsDirectory: " + file.isDirectory() + " Type: " + type + " Path: " + file.getPath());
        return 0;
    }

    public void notifyAdapter() {
        attachmentListAdapter.notifyDataSetChanged();
    }

    public void goBack() {

        Intent intent = new Intent(SupportTicketActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        /*if (IsBackVisible) {
            Intent intent = new Intent(SupportTicketActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyncGetCatSubCatAndIssueType != null && !asyncGetCatSubCatAndIssueType.isCancelled()) {
            asyncGetCatSubCatAndIssueType.cancel(true);
            asyncGetCatSubCatAndIssueType = null;
        }
    }
}
