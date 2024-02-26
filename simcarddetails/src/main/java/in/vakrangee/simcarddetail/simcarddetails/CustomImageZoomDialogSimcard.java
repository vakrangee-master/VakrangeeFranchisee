package in.vakrangee.simcarddetail.simcarddetails;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import in.vakrangee.simcarddetail.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.ImageZoom;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class CustomImageZoomDialogSimcard extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomImageZoomDialogSimcard";
    private Context context;
    private TextView textViewImageTitle, txtTitle;
    private ImageZoom imgPreview;
    private LinearLayout parentLinearlytCaptureButton, parentLayout;
    private Button btnCancel, btnOK;
    private CustomImageZoomDialogSimcard.IImagePreviewDialogClicks iImagePreviewDialogClicks;
    private EditText editTextRemarks;
    private LinearLayout layoutRemarks;
    private Object object;
    private LinearLayout layoutImageTypeSelection;
    private Spinner spinnerImageType;
    private String brandingElements;
    private DeprecateHandler deprecateHandler;
    private int imageNameType;

    public interface IImagePreviewDialogClicks {
        public void capturePhotoClick();

        public void OkClick(Object object);
    }

    public CustomImageZoomDialogSimcard(@NonNull Context context, Object object, int imageNameType, CustomImageZoomDialogSimcard.IImagePreviewDialogClicks iImagePreviewDialogClicks) {
        super(context);
        this.context = context;
        this.object = object;
        this.imageNameType = imageNameType;
        this.iImagePreviewDialogClicks = iImagePreviewDialogClicks;
        deprecateHandler = new DeprecateHandler(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_image_preview_zoom);

        //Widgets
        txtTitle = findViewById(R.id.txtTitle);
        parentLayout = findViewById(R.id.parentLayout);
        textViewImageTitle = findViewById(R.id.textViewImageTitle);
        parentLinearlytCaptureButton = findViewById(R.id.parentLinearlytCaptureButton);
        editTextRemarks = findViewById(R.id.editTextRemarks);
        editTextRemarks.addTextChangedListener(new CustomImageZoomDialogSimcard.MyTextWatcher());
        CommonUtils.applyInputFilter(editTextRemarks, "\"~#^|$%&*!'");
        layoutImageTypeSelection = findViewById(R.id.layoutImageType);
        spinnerImageType = findViewById(R.id.spinnerImageType);

        imgPreview = (ImageZoom) findViewById(R.id.imageViewPreview);
        btnOK = findViewById(R.id.btnOK);
        btnCancel = findViewById(R.id.btnCancel);
        layoutRemarks = findViewById(R.id.layoutRemarks);

        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + context.getResources().getString(R.string.cancel)));
        btnOK.setTypeface(font);
        btnOK.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + context.getResources().getString(R.string.save)));

        //Listeners
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        parentLinearlytCaptureButton.setOnClickListener(this);

        CommonUtils.setDialog(context, parentLayout);
        refresh(object);

    }

    // Allow Change Photo Option
    public void allowChangePhoto(boolean isAllow) {
        editTextRemarks.setEnabled(isAllow);
        if (isAllow) {
            parentLinearlytCaptureButton.setVisibility(View.VISIBLE);
        } else {
            parentLinearlytCaptureButton.setVisibility(View.GONE);
        }
    }

    // Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
        }
    }

    // Allow Image Title visibility
    public void allowImageTitle(boolean isAllow) {
        if (isAllow) {
            textViewImageTitle.setVisibility(View.VISIBLE);
        } else {
            textViewImageTitle.setVisibility(View.INVISIBLE);
        }

    }

    // Allow Remarks
    public void allowRemarks(boolean isAllow) {
        if (isAllow) {
            layoutRemarks.setVisibility(View.VISIBLE);
        } else {
            layoutRemarks.setVisibility(View.GONE);
        }
    }

    // Allow To Save
    public void allowSaveOption(boolean isAllow) {
        if (isAllow) {
            btnOK.setVisibility(View.VISIBLE);
        } else {
            btnOK.setVisibility(View.GONE);
        }
    }

    //Allow to Select Image Type
    public void allowImageTypeSelection(boolean isAllow) {
        if (isAllow) {
            layoutImageTypeSelection.setVisibility(View.VISIBLE);
        } else {
            layoutImageTypeSelection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnOK) {

            boolean IsImageSelectionValildated = IsImageTypeValidated();
            if (IsImageSelectionValildated) {
                iImagePreviewDialogClicks.OkClick(object);
                dismiss();
            } else {
                setErrorToSpinner(spinnerImageType, "Please select Item Category.", false);
            }
        } else if (Id == R.id.btnCancel) {
            SimcardDetailsDto dto = ((SimcardDetailsDto) object);
            if (object != null) {
                if (imageNameType == 1) {
                    dto.setAtmSimcardCoverPhotoBase64(null);
                } else if (imageNameType == 2) {
                    dto.setAtmSimcardPhotoBase64(null);
                }
            }
            dismiss();

        } else if (Id == R.id.parentLinearlytCaptureButton) {
            iImagePreviewDialogClicks.capturePhotoClick();
            //dismiss();
        }
    }

    public void refresh(Object object) {
        this.object = object;
        String editTxt = editTextRemarks.getText().toString();
        if (object instanceof My_vakranggekendra_image) {
            My_vakranggekendra_image my_vakranggekendra_image = (My_vakranggekendra_image) object;
            imgPreview.setImageBitmap(my_vakranggekendra_image.getImage());
            textViewImageTitle.setText(my_vakranggekendra_image.getImgetype());
            String data = (!TextUtils.isEmpty(editTxt)) ? editTxt : my_vakranggekendra_image.getRemarks();
            editTextRemarks.setText(data);

        } else if (object instanceof ImageDto) {
            ImageDto imageDto = (ImageDto) object;
            if (imageDto.getBitmap() != null)
                imgPreview.setImageBitmap(imageDto.getBitmap());
            textViewImageTitle.setText(imageDto.getName());
            String data = (!TextUtils.isEmpty(editTxt)) ? editTxt : imageDto.getRemarks();
            editTextRemarks.setText(data);

        } else if (object instanceof SimcardDetailsDto) {
            if (imageNameType == 1) {
                String imageId = ((SimcardDetailsDto) object).getAtmSimCoverImageId();

                if (TextUtils.isEmpty(((SimcardDetailsDto) object).getAtmSimcardCoverPhotoBase64())) {
                    String imageUrl = Constants.DownloadImageUrl + imageId;
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .error(R.drawable.ic_camera_alt_black_72dp)
                                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true))
                            .into(imgPreview);
                } else {
                    Bitmap imageBitmap = CommonUtils.StringToBitMap(((SimcardDetailsDto) object).getAtmSimcardCoverPhotoBase64());
                    String imageHashMap = CommonUtils.getImageSalt(((SimcardDetailsDto) object).getAtmSimcardCoverPhotoBase64());
                    setImage(imageId, imgPreview, imageBitmap, imageHashMap, true);
                    imgPreview.setImageBitmap(imageBitmap);
                }
            } else if (imageNameType == 2) {
                String imageId = ((SimcardDetailsDto) object).getAtmSimImageId();

                if (TextUtils.isEmpty(((SimcardDetailsDto) object).getAtmSimcardPhotoBase64())) {
                    String imageUrl = Constants.DownloadImageUrl + imageId;
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .error(R.drawable.ic_camera_alt_black_72dp)
                                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true))
                            .into(imgPreview);
                } else {
                    Bitmap imageBitmap = CommonUtils.StringToBitMap(((SimcardDetailsDto) object).getAtmSimcardPhotoBase64());
                    String imageHashMap = CommonUtils.getImageSalt(((SimcardDetailsDto) object).getAtmSimcardPhotoBase64());
                    setImage(imageId, imgPreview, imageBitmap, imageHashMap, true);
                    imgPreview.setImageBitmap(imageBitmap);
                }
            }

            // textViewImageTitle.setText(((SimcardDetailsDto) object).getName());

        } else {
            textViewImageTitle.setText(object.toString());
        }
    }

    public void setImage(String imageId, final ImageView imageView, final Bitmap bitmap, final String imageHashMap, boolean IsChangedPhoto) {

        if (TextUtils.isEmpty(imageId) || imageId.equals("0") || IsChangedPhoto)
            Glide.with(context).asBitmap().load(bitmap).into(imageView);
        else {
            String imageUrl = Constants.DownloadImageUrl + imageId;

            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions().signature(new ObjectKey(imageHashMap)))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                        Glide.with(context).asBitmap().load(bitmap).into(imageView);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);


        }
    }

    private class MyTextWatcher implements TextWatcher {

        private MyTextWatcher() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
        }

        public void afterTextChanged(Editable s) {
            String data = s.toString().trim();

            if (object instanceof My_vakranggekendra_image) {
                ((My_vakranggekendra_image) object).setRemarks(data);

            } else if (object instanceof ImageDto) {
                ((ImageDto) object).setRemarks(data);

            }
        }
    }

    public boolean IsImageTypeValidated() {

        if (layoutImageTypeSelection.getVisibility() == View.GONE)
            return true;

        //Validate Image Type Selection
        if (object instanceof ImageDto) {
            ImageDto imageDto = (ImageDto) object;
            if (!TextUtils.isEmpty(imageDto.getBranding_element_Id()))
                return true;
        }
        return false;
    }

    public void setErrorToSpinner(Spinner spinner, String msg, boolean IsReset) {
        LinearLayout layout = (LinearLayout) spinner.getSelectedView();
        TextView errorText = (TextView) layout.getChildAt(0);
        if (errorText == null) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
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
}
