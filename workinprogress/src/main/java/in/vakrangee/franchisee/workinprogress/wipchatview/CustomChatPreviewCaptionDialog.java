package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.franchisee.workinprogress.SiteReadinessCheckListDto;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.model.PhotoDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class CustomChatPreviewCaptionDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomChatPreviewCaptionDialog";
    private Context context;
    private ImageView imgPreview;
    private LinearLayout parentLayout;
    private Button btnClose, btnCancel, btnOK;
    private CustomChatPreviewCaptionDialog.IImagePreviewDialogClicks iImagePreviewDialogClicks;
    private EditText editTextRemarks;
    private Object object;
    private DeprecateHandler deprecateHandler;
    private TextView txtSelCalSubCat;
    private String selectedCat;

    public interface IImagePreviewDialogClicks {

        public void OkClick(Object object);
    }

    public CustomChatPreviewCaptionDialog(@NonNull Context context, Object object, String selectedCat, CustomChatPreviewCaptionDialog.IImagePreviewDialogClicks iImagePreviewDialogClicks) {
        super(context);
        this.context = context;
        this.object = object;
        this.selectedCat = selectedCat;
        this.iImagePreviewDialogClicks = iImagePreviewDialogClicks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_chat_preview_with_caption);

        deprecateHandler = new DeprecateHandler(context);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        editTextRemarks = findViewById(R.id.editTextRemarks);
        editTextRemarks.addTextChangedListener(new CustomChatPreviewCaptionDialog.MyTextWatcher());
        CommonUtils.applyInputFilter(editTextRemarks, "\"~#^|$%&*!'");
        txtSelCalSubCat = findViewById(R.id.txtSelCalSubCat);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        btnOK = findViewById(R.id.btnOK);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + context.getResources().getString(R.string.close)));
        btnOK.setTypeface(font);
        btnOK.setText(new SpannableStringBuilder(new String(new char[]{0xf1d8}) + "  Send"));

        //Listeners
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        imgPreview = findViewById(R.id.imageViewPreview);
        CommonUtils.setDialog(context, parentLayout);
        refresh(object, selectedCat);

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnOK) {
            iImagePreviewDialogClicks.OkClick(object);
            dismiss();

        } else if (Id == R.id.btnClose) {
            dismiss();

        } else if (Id == R.id.btnCancel) {
            dismiss();
        }
    }

    public void refresh(Object object, String selectedCat) {
        this.object = object;
        this.selectedCat = selectedCat;

        String editTxt = editTextRemarks.getText().toString();
        if (object instanceof PhotoDto) {
            PhotoDto photoDto = (PhotoDto) object;

            String imageId = photoDto.getPhotoId();
            Bitmap imageBitmap = CommonUtils.StringToBitMap(photoDto.getImageBase64());
            String imageHashMap = CommonUtils.getImageSalt(photoDto.getImageBase64());
            setImage(imageId, imgPreview, imageBitmap, imageHashMap, photoDto.isChangedPhoto());

            imgPreview.setImageBitmap(photoDto.getBitmap());
            String data = (!TextUtils.isEmpty(editTxt)) ? editTxt : photoDto.getRemarks();
            editTextRemarks.setText(data);

            //Set Seleected Category/SubCategory
            txtSelCalSubCat.setText(selectedCat);

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

            } else if (object instanceof SiteReadinessCheckListDto) {
                ((SiteReadinessCheckListDto) object).setElementImgRemarks(data);

            } else if (object instanceof PhotoDto) {
                ((PhotoDto) object).setRemarks(data);

            }
        }
    }

}
