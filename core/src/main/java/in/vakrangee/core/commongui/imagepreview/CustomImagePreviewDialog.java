package in.vakrangee.core.commongui.imagepreview;

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
import android.widget.AdapterView;
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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.imagegallery.ImageDto;
import in.vakrangee.core.gstdetails.GSTINDTO;
import in.vakrangee.core.model.BrandingElementDto;
import in.vakrangee.core.model.ElementAttributeDetailDto;
import in.vakrangee.core.model.GWRCheckListDto;
import in.vakrangee.core.model.My_vakranggekendra_image;
import in.vakrangee.core.model.PhotoDto;
import in.vakrangee.core.model.SiteReadinessCheckListDto;
import in.vakrangee.core.support.CustomSpinnerAdapter;
import in.vakrangee.core.utils.AlertDialogBoxInfo;
import in.vakrangee.core.utils.CommonUtils;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.DeprecateHandler;

public class CustomImagePreviewDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomImagePreviewDialog";
    private Context context;
    private TextView textViewImageTitle, txtTitle;
    private ImageView imgPreview, imgOverlay;
    private LinearLayout parentLinearlytCaptureButton, parentLayout;
    private Button btnCancel, btnOK, btnClose;
    private IImagePreviewDialogClicks iImagePreviewDialogClicks;
    private EditText editTextRemarks;
    private LinearLayout layoutRemarks;
    private Object object;
    private LinearLayout layoutImageTypeSelection;
    private Spinner spinnerImageType;
    private CustomSpinnerAdapter customSpinnerAdapter;
    private String brandingElements;
    private DeprecateHandler deprecateHandler;

    public interface IImagePreviewDialogClicks {
        public void capturePhotoClick();

        public void OkClick(Object object);
    }

    public CustomImagePreviewDialog(@NonNull Context context, Object object, IImagePreviewDialogClicks iImagePreviewDialogClicks) {
        super(context);
        this.context = context;
        this.object = object;
        this.iImagePreviewDialogClicks = iImagePreviewDialogClicks;
    }

    public CustomImagePreviewDialog(@NonNull Context context, Object object, String brandingElements, IImagePreviewDialogClicks iImagePreviewDialogClicks) {
        super(context);
        this.context = context;
        this.object = object;
        this.brandingElements = brandingElements;
        this.iImagePreviewDialogClicks = iImagePreviewDialogClicks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_image_preview);

        deprecateHandler = new DeprecateHandler(context);

        //Widgets
        txtTitle = findViewById(R.id.txtTitle);
        parentLayout = findViewById(R.id.parentLayout);
        textViewImageTitle = findViewById(R.id.textViewImageTitle);
        parentLinearlytCaptureButton = findViewById(R.id.parentLinearlytCaptureButton);
        editTextRemarks = findViewById(R.id.editTextRemarks);
        editTextRemarks.addTextChangedListener(new MyTextWatcher());
        CommonUtils.applyInputFilter(editTextRemarks, "\"~#^|$%&*!'");
        layoutImageTypeSelection = findViewById(R.id.layoutImageType);
        spinnerImageType = findViewById(R.id.spinnerImageType);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        imgPreview = findViewById(R.id.imageViewPreview);
        imgOverlay = findViewById(R.id.imgOverlay);
        btnOK = findViewById(R.id.btnOK);
        btnCancel = findViewById(R.id.btnCancel);
        layoutRemarks = findViewById(R.id.layoutRemarks);

        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + context.getResources().getString(R.string.close)));
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

    // Allow Image Rejected visibility
    public void allowImageRejected(boolean isAllow, String status) {
        if (isAllow) {
            imgOverlay.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(status) && status.equalsIgnoreCase(Constants.REJECTED_BY_FIELD_TEAM)) {
                imgOverlay.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected_by_be_de));
            } else {
                imgOverlay.setImageDrawable(deprecateHandler.getDrawable(R.drawable.rejected));
            }
        } else {
            imgOverlay.setVisibility(View.GONE);
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
            dismiss();

        } else if (Id == R.id.parentLinearlytCaptureButton) {
            iImagePreviewDialogClicks.capturePhotoClick();
            //dismiss();
        } else if (Id == R.id.btnClose) {
            dismiss();
        }
    }

    public void refresh(Object object) {
        this.object = object;
        String editTxt = editTextRemarks.getText().toString();
        if (object instanceof My_vakranggekendra_image) {
            My_vakranggekendra_image my_vakranggekendra_image = (My_vakranggekendra_image) object;

            boolean IsChangedPhoto = ((My_vakranggekendra_image) object).isChangedPhoto();
            String imageId = ((My_vakranggekendra_image) object).getImageId();

            setImage(imageId, imgPreview, my_vakranggekendra_image.getImage(), my_vakranggekendra_image.getImageHash(), IsChangedPhoto);
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
            reloadImageTypeSelectionSpinner();

        } else if (object instanceof PhotoDto) {
            PhotoDto photoDto = (PhotoDto) object;
           /* if (photoDto.getBitmap() != null)
                imgPreview.setImageBitmap(photoDto.getBitmap());
            textViewImageTitle.setText(photoDto.getName());
            String data = (!TextUtils.isEmpty(editTxt)) ? editTxt : photoDto.getRemarks();
            editTextRemarks.setText(data);*/

            String imageId = photoDto.getPhotoId();
            Bitmap imageBitmap = CommonUtils.StringToBitMap(photoDto.getImageBase64());
            String imageHashMap = CommonUtils.getImageSalt(photoDto.getImageBase64());
            setImage(imageId, imgPreview, imageBitmap, imageHashMap, photoDto.isChangedPhoto());

            imgPreview.setImageBitmap(photoDto.getBitmap());
            textViewImageTitle.setText("");
            String data = (!TextUtils.isEmpty(editTxt)) ? editTxt : photoDto.getRemarks();
            editTextRemarks.setText(data);

        } else if (object instanceof SiteReadinessCheckListDto) {
            String imageId = ((SiteReadinessCheckListDto) object).getImage_id();
            Bitmap imageBitmap = CommonUtils.StringToBitMap(((SiteReadinessCheckListDto) object).getElementImageBase64());
            String imageHashMap = CommonUtils.getImageSalt(((SiteReadinessCheckListDto) object).getElementImageBase64());
            setImage(imageId, imgPreview, imageBitmap, imageHashMap, ((SiteReadinessCheckListDto) object).isChangedPhoto());

            imgPreview.setImageBitmap(((SiteReadinessCheckListDto) object).getElementImgBitmap());
            textViewImageTitle.setText("");
            String data = (!TextUtils.isEmpty(editTxt)) ? editTxt : ((SiteReadinessCheckListDto) object).getElementImgRemarks();
            editTextRemarks.setText(data);

        } else if (object instanceof GWRCheckListDto) {
            String imageId = ((GWRCheckListDto) object).getImage_id();
            Bitmap imageBitmap = CommonUtils.StringToBitMap(((GWRCheckListDto) object).getElementImageBase64());
            String imageHashMap = CommonUtils.getImageSalt(((GWRCheckListDto) object).getElementImageBase64());
            setImage(imageId, imgPreview, imageBitmap, imageHashMap, ((GWRCheckListDto) object).isChangedPhoto());

            imgPreview.setImageBitmap(((GWRCheckListDto) object).getElementImgBitmap());
            textViewImageTitle.setText("");
            String data = (!TextUtils.isEmpty(editTxt)) ? editTxt : ((GWRCheckListDto) object).getElementImgRemarks();
            editTextRemarks.setText(data);

        } else if (object instanceof GSTINDTO) {
            String imageId = ((GSTINDTO) object).getGstImageId();
            Bitmap imageBitmap = CommonUtils.StringToBitMap(((GSTINDTO) object).getGstImage());
            String imageHashMap = CommonUtils.getImageSalt(((GSTINDTO) object).getGstImage());
            setImage(imageId, imgPreview, imageBitmap, imageHashMap, ((GSTINDTO) object).isChangedPhoto());

            imgPreview.setImageBitmap(((GSTINDTO) object).getBitmap());
            textViewImageTitle.setText(((GSTINDTO) object).getName());

        } else if (object instanceof ElementAttributeDetailDto) {
            String imageId = ((ElementAttributeDetailDto) object).getAttributeImageId();
            String imageUrl = Constants.DownloadImageUrl + imageId;

            Glide.with(context).load(imageUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imgPreview);
            textViewImageTitle.setText("");

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

            } else if (object instanceof SiteReadinessCheckListDto) {
                ((SiteReadinessCheckListDto) object).setElementImgRemarks(data);
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

    public void reloadImageTypeSelectionSpinner() {

        //Get Data
        if (object instanceof ImageDto) {
            ImageDto imageDto = (ImageDto) object;
            String selBrandingElementId = imageDto.getBranding_element_Id();
            String imageName = imageDto.getBranding_element_name();
            if (!TextUtils.isEmpty(imageName))
                textViewImageTitle.setText(imageName);

            ArrayList<BrandingElementDto> brandingElementList = new ArrayList<BrandingElementDto>();

            try {

                if (TextUtils.isEmpty(brandingElements)) {
                    brandingElementList.add(0, new BrandingElementDto("0", context.getResources().getString(R.string.pleaseSelect)));

                } else {
                    JSONObject jsonObject = new JSONObject(brandingElements);
                    JSONArray jsonArray = jsonObject.getJSONArray("designElements");

                    Gson gson = new GsonBuilder().create();
                    brandingElementList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BrandingElementDto>>() {
                    }.getType());
                    brandingElementList.add(0, new BrandingElementDto("0", context.getResources().getString(R.string.pleaseSelect)));
                }

                ArrayList<Object> list1 = new ArrayList<Object>(brandingElementList);
                customSpinnerAdapter = new CustomSpinnerAdapter(context, list1);
                spinnerImageType.setAdapter(customSpinnerAdapter);
                int selPos = getSelImageTypePos(brandingElementList, selBrandingElementId);
                spinnerImageType.setSelection(selPos);
                spinnerImageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            BrandingElementDto selDto = (BrandingElementDto) spinnerImageType.getItemAtPosition(position);
                            if (object instanceof ImageDto) {
                                ((ImageDto) object).setBranding_element_Id(selDto.getId());
                                ((ImageDto) object).setName(selDto.getName());
                                String imageName = selDto.getName();
                                if (!TextUtils.isEmpty(imageName))
                                    textViewImageTitle.setText(imageName);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

            } catch (Exception e) {
                e.toString();
            }
        }
    }

    public int getSelImageTypePos(List<BrandingElementDto> brandingElementList, String selBrandingElementId) {

        if (TextUtils.isEmpty(selBrandingElementId))
            return 0;

        for (int i = 0; i < brandingElementList.size(); i++) {
            if (selBrandingElementId.equalsIgnoreCase(brandingElementList.get(i).getId()))
                return i;
        }
        return 0;
    }

}
