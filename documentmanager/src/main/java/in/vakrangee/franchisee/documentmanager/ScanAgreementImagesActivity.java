package in.vakrangee.franchisee.documentmanager;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.utils.drag_drop_utils.EditItemTouchHelperCallback;
import in.vakrangee.franchisee.utils.drag_drop_utils.OnStartDragListener;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickLongClickListener;
import in.vakrangee.supercore.franchisee.documentmanager.DocumentManagerConstants;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class ScanAgreementImagesActivity extends AppCompatActivity implements View.OnClickListener, OnStartDragListener {

    private static final String TAG = "ScanAgreementImagesActivity";

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

    private AgreementUploadDetailsDto agreementUploadDetailsDto;
    private DocumentManagerRepository doumentManagerRepo;
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
    private List<AgreementImageDto> deleteImgList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_agreement_images);

        //Initialize data
        context = this;
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(this);
        doumentManagerRepo = new DocumentManagerRepository(this);

        //References
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
            String title = "Upload Agreement";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        fabUploadAgreementImg.setOnClickListener(this);
        fabUpload.setOnClickListener(this);

        //Get Intent Data
        int sync = getIntent().getIntExtra("BIG_DATA:SYNC_CODE", -1);
        agreementUploadDetailsDto = (AgreementUploadDetailsDto) ResultIPC.get().getLargeData(sync);

        //agreementUploadDetailsDto = (AgreementUploadDetailsDto) getIntent().getSerializableExtra("AGREEMENT_DETAILS_DTO");
        reload(agreementUploadDetailsDto);

        //Enable/Disable Drag N Drop
        layoutDragNDropOnOFF.setOnClickListener(this);

        txtDragONOFF = findViewById(R.id.txtDragONOFF);
        setFontawesomeIcon(txtDragONOFF, deprecateHandler.getColor(R.color.white));

    }

    public void reload(AgreementUploadDetailsDto uploadDetailsDto) {

        //Reload Data
        if (uploadDetailsDto != null)
            this.agreementUploadDetailsDto = uploadDetailsDto;

        //Bind Data
        bindData();
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
                if (IsChecked)
                    deleteImgList.add(agreementUploadDetailsDto.agreementImagesList.get(pos));
                else
                    deleteImgList.remove(agreementUploadDetailsDto.agreementImagesList.get(pos));

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
                    Intent intent1 = new Intent(ScanAgreementImagesActivity.this, ScanActivity.class);
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

                Bitmap bitmap = ImageUtils.getBitmap(getContentResolver(), uri, "", "", "");
                picUri = uri;

                File file = new File(uri.getPath());

                String base64Data = CommonUtils.convertBitmapToString(bitmap);
                //String fileName = URLDecoder.decode(file.getName(), "UTF-8");

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
            pgNo = doumentManagerRepo.getPageNo(agreementUploadDetailsDto.agreementImagesList);
        }
        return pgNo;
    }

    public void prepareImageObject(int pgNo, String base64, Bitmap bitmap, boolean IsEditMode, int selectedPos) {
        try {
            String ext = DocumentManagerConstants.JPG;

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
            setResultData();

        } else if (Id == R.id.layoutDragNDropOnOFF) {

            boolean IsONOFF = IsDragEnabled ? false : true;
            disableEnableDragNDropOnRecyclerView(IsONOFF);
        }
    }

    private void setResultData() {

        try {

            if (agreementUploadDetailsDto.agreementImagesList.size() > 0) {
                //Convert Images to PDF
                String[] imgs = getImagesPath();
                boolean status = ImagesToPDFUtils.createPdf(imgs, ScanAgreementImagesActivity.this);
                if (!status) {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.failed_pdf));
                    return;
                }

                String filePath = ImagesToPDFUtils.getPDFDestPath(ScanAgreementImagesActivity.this);
                String base64Data = CommonUtils.encodeFileToBase64Binary(new File(filePath));
                agreementUploadDetailsDto.setAgreementImageBase64(base64Data);
                agreementUploadDetailsDto.setAgreementImageExt(DocumentManagerConstants.PDF);
                agreementUploadDetailsDto.setAgreementFilePath(filePath);
            } else {
                agreementUploadDetailsDto.setAgreementImageBase64("");
                agreementUploadDetailsDto.setAgreementImageExt("");
                agreementUploadDetailsDto.setAgreementFilePath("");
            }

            Intent intent = new Intent(ScanAgreementImagesActivity.this, AgreementUploadFragment.class);
            int sync = ResultIPC.get().setLargeData(agreementUploadDetailsDto);
            intent.putExtra("BIG_DATA:SYNC_CODE", sync);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            //finish();
            setResultData();
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
            popUpSelectAllDelete(item.getActionView());
        }
        return true;
    }

    private void promptBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanAgreementImagesActivity.this);
        builder.setMessage(getResources().getString(R.string.agreement_back_msg))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        setResultData();
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
        String enquiryId = CommonUtils.getEnquiryId(ScanAgreementImagesActivity.this);
        String fName = String.valueOf(pgNo);
        String path = CommonUtils.moveFile(uri1, enquiryId, fName, DocumentManagerConstants.DIR_NAME_AGREEMENT_UPLOAD);

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

    private void deleteDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanAgreementImagesActivity.this);
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

    private void deleteAllAgreementData() {

        showProgressSpinner(this);

        //Check EnquiryId exist
        String enquiryId = CommonUtils.getEnquiryId(context);
        if (TextUtils.isEmpty(enquiryId)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            return;
        }

        File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DocumentManagerConstants.DIR_NAME_AGREEMENT_UPLOAD + File.separator + enquiryId);

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
                if (!files[i].getName().equalsIgnoreCase(DocumentManagerConstants.DIR_NAME_AGREEMENT_UPLOAD)) {

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
        Intent intent1 = new Intent(ScanAgreementImagesActivity.this, ScanActivity.class);
        intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        intent1.putExtra(ScanConstants.SELECTED_BITMAP, path);
        intent1.putExtra(ScanConstants.SELECTED_POS, pos);
        intent1.putExtra(ScanConstants.IS_EDIT_MODE, true);
        startActivityForResult(intent1, SCANLIB_REQUEST_CODE);
    }

    private void updatePageCountLbl() {
        txtPageCount.setText(agreementUploadDetailsDto.agreementImagesList.size() + " Of " + agreementUploadDetailsDto.getModelWisePageCount());
    }

    public void setFontawesomeIcon(TextView textView, int color) {

        textView.setText(getResources().getString(R.string.fa_drag_drop));
        textView.setTextSize(25);
        textView.setTextColor(color);
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }

    //region Select All and Delete Options
    public void popUpSelectAllDelete(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.popup_selectall_delete);
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
                if (itemId == R.id.menuSelectAll) {
                    Toast.makeText(ScanAgreementImagesActivity.this, "Select All.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menuDelete) {
                    Toast.makeText(ScanAgreementImagesActivity.this, "Delete.", Toast.LENGTH_SHORT).show();

                       /* if (agreementUploadDetailsDto.agreementImagesList.size() == 0)
                            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.no_agreement_to_delete_msg));
                        else {
                            String msg = getResources().getString(R.string.delete_agreement_msg);
                            deleteDialog(msg);
                        }*/
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
            deleteImgList = new ArrayList<>();

            //Hide Check Box
            agreementImageAdapter.enableDisableChkBoxNotify(false);
        }
    }

}
