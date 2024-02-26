package in.vakrangee.franchisee.loandocument;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.loandocument.adapter.AgreementImageAdapter;
import in.vakrangee.franchisee.loandocument.asynctask.AsyncSaveLoanDocumentDetails;
import in.vakrangee.franchisee.loandocument.drag_drop_utils.EditItemTouchHelperCallback;
import in.vakrangee.franchisee.loandocument.drag_drop_utils.OnStartDragListener;
import in.vakrangee.franchisee.loandocument.model.AgreementImageDto;
import in.vakrangee.franchisee.loandocument.model.LoanDocumentDto;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickLongClickListener;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class ScanLoanAgreementImagesActivity extends AppCompatActivity implements View.OnClickListener, OnStartDragListener {

    private static final String TAG = "ScanLoanAgreementImagesActivity";

    private Toolbar toolbar;
    private Typeface font;
    private Context context;

    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private RecyclerView recyclerViewAgreementImages;
    private LinearLayout fabUploadAgreementImg;
    private LinearLayout fabUpload;

    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private AgreementImageAdapter agreementImageAdapter;

    private LoanDocumentDto agreementUploadDetailsDto;
    private ProgressDialog progressDialog;

    private TextView txtNoDataMsg;
    private TextView txtPageCount;
    private LinearLayout layoutDragNDropOnOFF;
    private CardView cardViewPageCountLbl;
    private TextView txtDragONOFF;
    private LinearLayout layoutFooter;

    private ItemTouchHelper mItemTouchHelper;
    private boolean IsDragEnabled = false;
    private MenuItem moreMenuItem;
    private HashMap<String, AgreementImageDto> deleteImgList = new HashMap<String, AgreementImageDto>();
    private LoanDocumentRepository loanDocumentRepo;
    private String TOTAL_PAGE_COUNT = "10";
    private TextView txtSelectedTitle;
    private GetAllSpinnerData getAllSpinnerData = null;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private AsyncSaveLoanDocumentDetails asyncSaveLoanDocumentDetails = null;
    private int selectedPos;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_agreement_images);

        //Initialize data
        context = this;
        gpsTracker = new GPSTracker(context);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(this);
        loanDocumentRepo = new LoanDocumentRepository(context);

        //References
        txtSelectedTitle = findViewById(R.id.txtSelectedTitle);
        recyclerViewAgreementImages = findViewById(R.id.recyclerViewAgreementImages);
        fabUploadAgreementImg = findViewById(R.id.fabUploadAgreementImg);
        fabUpload = findViewById(R.id.fabUpload);
        txtNoDataMsg = findViewById(R.id.txtNoDataMsg);
        txtPageCount = findViewById(R.id.txtPageCount);
        layoutDragNDropOnOFF = findViewById(R.id.layoutDragNDropOnOFF);
        cardViewPageCountLbl = findViewById(R.id.cardViewPageCountLbl);
        txtDragONOFF = findViewById(R.id.txtDragONOFF);
        layoutFooter = findViewById(R.id.layoutFooter);

        //Widgets
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = "Loan Document";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        fabUploadAgreementImg.setOnClickListener(this);
        fabUpload.setOnClickListener(this);

        //Get Intent Data
        int sync = getIntent().getIntExtra("BIG_DATA:SYNC_CODE", -1);
        agreementUploadDetailsDto = (LoanDocumentDto) ResultIPC.get().getLargeData(sync);
        selectedPos = getIntent().getIntExtra("SELECTED_POS", -1);
        txtSelectedTitle.setText(agreementUploadDetailsDto.getName());

        reload(agreementUploadDetailsDto);

        //Enable/Disable Drag N Drop
        layoutDragNDropOnOFF.setOnClickListener(this);

        txtDragONOFF = findViewById(R.id.txtDragONOFF);
        setFontawesomeIcon(txtDragONOFF, deprecateHandler.getColor(R.color.white));

    }

    public void reload(LoanDocumentDto uploadDetailsDto) {

        //Reload Data
        if (uploadDetailsDto != null)
            this.agreementUploadDetailsDto = uploadDetailsDto;

        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");

    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);

        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 3: Get Images
            agreementUploadDetailsDto.agreementImagesList = prepareAgreementImgsListFromExternalStorage();
            agreementUploadDetailsDto.setAgreementFilePath(getAlreadyExistingPdfPath(context));

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

    private List<AgreementImageDto> prepareAgreementImgsListFromExternalStorage() {
        List<AgreementImageDto> agreementImageList = new ArrayList<>();

        //Check Id exist
        String Id = agreementUploadDetailsDto.getId();
        if (TextUtils.isEmpty(Id))
            return agreementImageList;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD + File.separator + Id);

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
            if (!TextUtils.isEmpty(ext) && !ext.equalsIgnoreCase(LoanDocumentConstants.PDF)) {

                Bitmap bitmap = ImageUtils.getBitmap(context.getContentResolver(), picUri, "", "", "");
                String base64Data = CommonUtils.convertBitmapToString(bitmap);

                AgreementImageDto agreementImageDto = new AgreementImageDto();

                int pgNo = loanDocumentRepo.getPageNo(agreementImageList);
                agreementImageDto.setName(String.valueOf(pgNo));
                agreementImageDto.setBitmap(bitmap);
                agreementImageDto.setImgBase64(base64Data);
                agreementImageDto.setPath(picUri.toString());
                agreementImageDto.setExt(LoanDocumentConstants.JPG);
                agreementImageList.add(agreementImageDto);
            }
        }
        return agreementImageList;
    }

    private String getAlreadyExistingPdfPath(Context context) {

        String filePath = ImagesToPDFUtils.getPDFDestPath(context, agreementUploadDetailsDto.getId());
        File file = new File(filePath);
        if (file.exists())
            return filePath;

        return null;
    }

    private void bindData() {

        agreementImageAdapter = new AgreementImageAdapter(context, agreementUploadDetailsDto.agreementImagesList, this, new RecyclerViewClickLongClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position != RecyclerView.NO_POSITION) {
                    AgreementImageDto imageDto = agreementUploadDetailsDto.agreementImagesList.get(position);
                    String path = new File(imageDto.getPath()).getPath();
                    path = path.replace("file:", "");
                    startEditingImage(path, position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (position != RecyclerView.NO_POSITION) {

                    if (!IsDragEnabled) {

                        hideShowDeleteOptionWithLabel(true);
                        agreementImageAdapter.enableDisableChkBoxNotify(true);
                    }
                }
            }
        }, new AgreementImageAdapter.IHandleChk() {
            @Override
            public void onCheckEvent(int pos, boolean IsChecked) {
                AgreementImageDto imgDto = agreementUploadDetailsDto.agreementImagesList.get(pos);
                if (IsChecked) {
                    deleteImgList.put(imgDto.getName(), imgDto);
                } else {
                    deleteImgList.remove(imgDto.getName());
                }
                updateLabel();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewAgreementImages.setLayoutManager(gridLayoutManager);

        //Enable/ Disable Drag N Drop feature on Recycler View
        disableEnableDragNDropOnRecyclerView(false);

        recyclerViewAgreementImages.setAdapter(agreementImageAdapter);

        if (agreementUploadDetailsDto.agreementImagesList.size() > 0) {
            fabUpload.setVisibility(View.VISIBLE);
            txtNoDataMsg.setVisibility(View.GONE);
            recyclerViewAgreementImages.setVisibility(View.VISIBLE);
        } else {
            fabUpload.setVisibility(View.GONE);
            txtNoDataMsg.setVisibility(View.VISIBLE);
            recyclerViewAgreementImages.setVisibility(View.GONE);
        }
        updatePageCountLbl();
    }

    private void disableEnableDragNDropOnRecyclerView(boolean IsEnable) {

        IsDragEnabled = IsEnable;

        //Attach Drag And Drop Touch Callback
        ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(agreementImageAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);

        if (IsEnable) {
            mItemTouchHelper.attachToRecyclerView(recyclerViewAgreementImages);
            setFontawesomeIcon(txtDragONOFF, deprecateHandler.getColor(R.color.orange_lite));
        } else {
            mItemTouchHelper.attachToRecyclerView(null);
            setFontawesomeIcon(txtDragONOFF, deprecateHandler.getColor(R.color.white));
        }
    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(fabUpload, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent1 = new Intent(ScanLoanAgreementImagesActivity.this, ScanActivity.class);
                    intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent1, SCANLIB_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == SCANLIB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                getLocationAndTimestamp();

                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                boolean IsEditMode = data.getBooleanExtra(ScanConstants.IS_EDIT_MODE, false);
                int selectedPos = data.getIntExtra(ScanConstants.SELECTED_POS, -1);

                //Perform
                int pgNo = getFinalPageNoUsingMode(IsEditMode, selectedPos);
                uri = performActions(uri, pgNo);

                //Check if actions failed
                if (uri == null) {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    return;
                }

                picUri = uri;

                File file = new File(uri.getPath());
                Bitmap bitmap = ImageUtils.getBitmap(context.getContentResolver(), picUri, latitude, longitude, currentTimestamp);

                String base64Data = CommonUtils.convertBitmapToString(bitmap);

                //Prepared Image Object
                prepareImageObject(pgNo, base64Data, bitmap, IsEditMode, selectedPos);

                //Delete Unnecessary files
                deleteAllFilesInPicturesFolder();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getFinalPageNoUsingMode(boolean IsEditMode, int selectedPos) {
        int pgNo = 0;
        if (IsEditMode) {
            String name = agreementUploadDetailsDto.agreementImagesList.get(selectedPos).getName();
            if (!TextUtils.isEmpty(name)) {
                pgNo = Integer.parseInt(name);
            }
        } else {
            pgNo = loanDocumentRepo.getPageNo(agreementUploadDetailsDto.agreementImagesList);
        }
        return pgNo;
    }

    public void prepareImageObject(int pgNo, String base64, Bitmap bitmap, boolean IsEditMode, int selectedPos) {
        try {
            String ext = LoanDocumentConstants.JPG;

            AgreementImageDto agreementImageDto;
            if (IsEditMode)
                agreementImageDto = agreementUploadDetailsDto.agreementImagesList.get(selectedPos);
            else
                agreementImageDto = new AgreementImageDto();

            //Set Data
            agreementImageDto.setName(String.valueOf(pgNo));
            agreementImageDto.setBitmap(bitmap);
            agreementImageDto.setImgBase64(base64);
            agreementImageDto.setPath(picUri.toString());
            agreementImageDto.setExt(ext);

            if (IsEditMode)
                agreementUploadDetailsDto.agreementImagesList.set(selectedPos, agreementImageDto);
            else
                agreementUploadDetailsDto.agreementImagesList.add(agreementImageDto);

            //Notify Adapter
            notifyAdapter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        if (Id == R.id.fabUploadAgreementImg) {
            startScanCamera();

        } else if (Id == R.id.fabUpload) {
            AnimationHanndler.bubbleAnimation(context, v);
            promptSaveDialog();

        } else if (Id == R.id.layoutDragNDropOnOFF) {

            boolean IsONOFF = IsDragEnabled ? false : true;
            disableEnableDragNDropOnRecyclerView(IsONOFF);
        }
    }

    private void setResultData(LoanDocumentDto dto, boolean isReload) {
        Intent intent = new Intent(ScanLoanAgreementImagesActivity.this, LoanDocumentFragment.class);

        int sync = ResultIPC.get().setLargeData(dto);
        intent.putExtra("BIG_DATA:SYNC_CODE", sync);
        intent.putExtra("PERFORM_RELOAD", isReload);
        intent.putExtra("SELECTED_POS", selectedPos);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {

        if (moreMenuItem != null && moreMenuItem.isVisible()) {
            resetDeletion();
            return;
        }

        if (agreementUploadDetailsDto.agreementImagesList.size() > 0) {
            promptBackDialog();
        } else {
            setResultData(agreementUploadDetailsDto, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        } else if (itemId == R.id.action_home_dashborad) {
            backPressed();
        } else if (itemId == R.id.action_more) {
            popUpSelectAllDelete(findViewById(R.id.action_more));
        }
        return true;
    }

    private void promptBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanLoanAgreementImagesActivity.this);
        builder.setMessage("Are you sure you want to go back?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResultData(agreementUploadDetailsDto, false);
                        dialog.cancel();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private Uri performActions(Uri uri1, int pgNo) {

        Uri uri = null;
        String Id = agreementUploadDetailsDto.getId(); //CommonUtils.getEnquiryId(ScanLoanAgreementImagesActivity.this);
        String fName = String.valueOf(pgNo);
        String path = CommonUtils.moveFile(uri1, Id, fName, LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD);

        try {
            int fileInKB = (int) (new File(path).length() / 1024);
            if (fileInKB > 50) {                //Compress if File size greater than 50 KB
                File compressFile = new ImageZipper(context).setQuality(50).compressToFile(new File(path));
                Uri newUri = Uri.fromFile(compressFile);
                path = CommonUtils.moveFile(newUri, Id, fName, LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check if Path is Null
        if (TextUtils.isEmpty(path))
            return uri;

        //Delete temp folder
        boolean status = false;
        File tempFile = new File(Environment.getExternalStorageDirectory() + "/temp/");
        if (tempFile.exists()) {
            status = CommonUtils.deleteDir(tempFile);
        }

        if (status)
            uri = Uri.fromFile(new File(path));
        return uri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);

        //Delete Menu
        moreMenuItem = menu.findItem(R.id.action_more);

        hideShowDeleteOptionWithLabel(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void deleteAllDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanLoanAgreementImagesActivity.this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAllAgreementData();
                        dialog.cancel();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteSelectedDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanLoanAgreementImagesActivity.this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteSelectedAgreementData();
                        dialog.cancel();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteAllAgreementData() {

        showProgressSpinner(this);

        //Check EnquiryId exist
        String enquiryId = agreementUploadDetailsDto.getId(); //CommonUtils.getEnquiryId(context);
        if (TextUtils.isEmpty(enquiryId)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            return;
        }

        File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD + File.separator + enquiryId);

        //Delete temp folder
        boolean status = false;
        if (tempFile.exists()) {
            status = CommonUtils.deleteDir(tempFile);
        }

        if (status) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.agreement_delete_success_msg));
            agreementUploadDetailsDto.agreementImagesList = new ArrayList<>();
            notifyAdapter();

        } else {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.agreement_failed_to_delete_msg));
        }

        dismissProgressSpinner();
    }

    private void deleteSelectedAgreementData() {

        showProgressSpinner(this);

        //Check EnquiryId exist
        String enquiryId = agreementUploadDetailsDto.getId(); //CommonUtils.getEnquiryId(context);
        if (TextUtils.isEmpty(enquiryId)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            return;
        }

        for (Map.Entry m : deleteImgList.entrySet()) {
            AgreementImageDto dto = (AgreementImageDto) m.getValue();

            File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD + File.separator + agreementUploadDetailsDto.getId() + File.separator + "IMG_" + dto.getName() + "." + dto.getExt());
            //File tempFile = new File(path);
            if (tempFile.exists()) {
                boolean status = CommonUtils.deleteFile(tempFile);
                if (status)
                    agreementUploadDetailsDto.agreementImagesList.remove(dto);
            }
        }

        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.selected_doc_delete_success_msg));

        deleteImgList = new HashMap<String, AgreementImageDto>();

        //Re-arrange Page No
        alterPageNoOfAllImages();
        notifyAdapter();

        dismissProgressSpinner();
    }

    public void showProgressSpinner(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void dismissProgressSpinner() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void notifyAdapter() {

        agreementImageAdapter.notifyDataSetChanged();
        if (agreementUploadDetailsDto.agreementImagesList.size() > 0) {
            recyclerViewAgreementImages.setVisibility(View.VISIBLE);
            fabUpload.setVisibility(View.VISIBLE);
            txtNoDataMsg.setVisibility(View.GONE);
        } else {
            fabUpload.setVisibility(View.GONE);
            txtNoDataMsg.setVisibility(View.VISIBLE);
            recyclerViewAgreementImages.setVisibility(View.GONE);
        }
        updatePageCountLbl();
    }

    private boolean deleteAllFilesInPicturesFolder() {
        try {

            String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES;
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                if (!files[i].getName().equalsIgnoreCase(LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD)) {

                    if (files[i].isFile())
                        files[i].delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String[] getImagesPath() {
        String[] images = new String[agreementUploadDetailsDto.agreementImagesList.size()];

        for (int i = 0; i < agreementUploadDetailsDto.agreementImagesList.size(); i++) {
            images[i] = agreementUploadDetailsDto.agreementImagesList.get(i).getPath();
        }
        return images;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (IsDragEnabled && mItemTouchHelper != null)
            mItemTouchHelper.startDrag(viewHolder);
    }

    private void startEditingImage(String path, int pos) {

        int preference = ScanConstants.OPEN_CAMERA;
        Intent intent1 = new Intent(ScanLoanAgreementImagesActivity.this, ScanActivity.class);
        intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        intent1.putExtra(ScanConstants.SELECTED_BITMAP, path);
        intent1.putExtra(ScanConstants.SELECTED_POS, pos);
        intent1.putExtra(ScanConstants.IS_EDIT_MODE, true);
        startActivityForResult(intent1, SCANLIB_REQUEST_CODE);
    }

    private void updatePageCountLbl() {
        txtPageCount.setText(agreementUploadDetailsDto.agreementImagesList.size() + " Of " + TOTAL_PAGE_COUNT);
    }

    public void setFontawesomeIcon(TextView textView, int color) {

        textView.setText(getResources().getString(R.string.fa_drag_drop));
        textView.setTextSize(25);
        textView.setTextColor(color);
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }

    public void popUpSelectAllDelete(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.popup_delete);
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
                if (itemId == R.id.menuDelete) {

                    if (deleteImgList.size() == 0)
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.no_document_to_delete_msg));
                    else {
                        String msg = getResources().getString(R.string.delete_selected_document_msg);
                        deleteSelectedDialog(msg);
                    }
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private void hideShowDeleteOptionWithLabel(boolean IsShow) {
        if (moreMenuItem != null) {
            moreMenuItem.setVisible(IsShow);

            updateLabel();
        }
    }

    private void updateLabel() {
        if (moreMenuItem != null && moreMenuItem.isVisible()) {

            if (getSupportActionBar() != null) {

                //Hide Page Count header and Footer
                cardViewPageCountLbl.setVisibility(View.GONE);
                layoutFooter.setVisibility(View.GONE);

                //Home Icon
                Drawable upArrow = getResources().getDrawable(R.drawable.ic_close_white_24dp);
                upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);

                //Title
                String suffix = deleteImgList.size() > 1 ? " items" : " item";
                String title = "Selected " + deleteImgList.size() + suffix;
                getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
            }
        }
    }

    private void resetDeletion() {
        if (moreMenuItem != null && moreMenuItem.isVisible()) {

            //Display Page Count header
            cardViewPageCountLbl.setVisibility(View.VISIBLE);
            layoutFooter.setVisibility(View.VISIBLE);

            //Hide Delete Item
            moreMenuItem.setVisible(false);

            //Title
            if (getSupportActionBar() != null) {

                //Home Icon
                Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
                upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);

                //Title

                String title = "Upload Agreement";
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));

            }

            //Clear Delete List
            deleteImgList = new HashMap<String, AgreementImageDto>();

            for (int i = 0; i < agreementUploadDetailsDto.agreementImagesList.size(); i++) {
                AgreementImageDto dto = agreementUploadDetailsDto.agreementImagesList.get(i);
                dto.setSelected(false);
                agreementUploadDetailsDto.agreementImagesList.set(i,dto);
            }

            //Hide Check Box
            agreementImageAdapter.enableDisableChkBoxNotify(false);
        }
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    private void prepareDataToSave() {
        try {

            if (agreementUploadDetailsDto.agreementImagesList.size() > 0) {
                //Convert Images to PDF
                String[] imgs = getImagesPath();
                boolean status = ImagesToPDFUtils.createPdf(imgs, ScanLoanAgreementImagesActivity.this, agreementUploadDetailsDto.getId());
                if (!status) {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.failed_pdf));
                    return;
                }

                String filePath = ImagesToPDFUtils.getPDFDestPath(ScanLoanAgreementImagesActivity.this, agreementUploadDetailsDto.getId());
                String base64Data = CommonUtils.encodeFileToBase64Binary(new File(filePath));
                agreementUploadDetailsDto.setLoanDocumentImgBase64(base64Data);
                agreementUploadDetailsDto.setLoanDocumentImgExt(LoanDocumentConstants.PDF);
                agreementUploadDetailsDto.setAgreementFilePath(filePath);
            } else {
                agreementUploadDetailsDto.setLoanDocumentImgBase64("");
                agreementUploadDetailsDto.setLoanDocumentImgExt("");
                agreementUploadDetailsDto.setAgreementFilePath("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImage() {

        prepareDataToSave();

        final LoanDocumentDto selectedDto = agreementUploadDetailsDto;
        selectedDto.agreementImagesList = new ArrayList<>();

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

                            String msg = agreementUploadDetailsDto.getName() + " Document uploaded successfully.";
                            showMessageWithFinish(msg, data);

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

    public void showMessageWithFinish(String msg, final String data) {
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
                            Gson gson = new Gson();
                            LoanDocumentDto dto = gson.fromJson(data, LoanDocumentDto.class);
                            setResultData(dto, true);

                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void promptSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanLoanAgreementImagesActivity.this);
        builder.setMessage("Are you sure you want to upload document?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveImage();
                        dialog.cancel();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void alterPageNoOfAllImages() {
        for (int i = 0; i < agreementUploadDetailsDto.agreementImagesList.size(); i++) {
            AgreementImageDto dto = agreementUploadDetailsDto.agreementImagesList.get(i);
            String name = "" + (i + 1);

            File sourceFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD + File.separator + agreementUploadDetailsDto.getId() + File.separator + "IMG_" + dto.getName() + "." + dto.getExt());

            File destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD + File.separator + agreementUploadDetailsDto.getId() + File.separator + "IMG_" + name + "." + dto.getExt());

            //Rename File
            if (sourceFile.renameTo(destFile)) {
                dto.setName(name);
                Uri picUri = Uri.fromFile(destFile);
                dto.setPath(picUri.toString());
                agreementUploadDetailsDto.agreementImagesList.set(i, dto);
            }
        }
    }
}
