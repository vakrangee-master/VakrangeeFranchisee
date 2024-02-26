package in.vakrangee.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.InputStream;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.animation.AnimationHanndler;
import in.vakrangee.core.commongui.imagepreview.ImageZoom;
import pl.droidsonroids.gif.GifImageView;

public class CustomFaceDetectionImageDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "CustomFaceDetectionImageDialog";
    private Context context;
    private DeprecateHandler deprecateHandler;
    private IImageFaceDetectionDialogClicks iImagePreviewDialogClicks;
    private Button btnClose;
    private ImageZoom imgPreview;
    private LinearLayout parentLayout;
    private TextView textViewImageTitle, txtTitle;
    private ImageView imgCaptureImage;
    private Object object;
    private GifImageView imgGif;
    private LinearLayout layoutSave, layoutSelfie;
    private String imgName;
    private FaceDetector detector;

    public interface IImageFaceDetectionDialogClicks {
        public void capturePhotoClick();

        public void OkClick(Object object);

        public void cancelClick();
    }

    public CustomFaceDetectionImageDialog(@NonNull Context context, Object object, String imgName, IImageFaceDetectionDialogClicks iImagePreviewDialogClicks) {
        super(context);
        this.context = context;
        this.object = object;
        this.iImagePreviewDialogClicks = iImagePreviewDialogClicks;
        deprecateHandler = new DeprecateHandler(context);
        this.imgName = imgName;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_facedetection_image);

        parentLayout = findViewById(R.id.parentLayout);
        imgPreview = (ImageZoom) findViewById(R.id.imageViewPreview);
        btnClose = findViewById(R.id.btnClose);
        txtTitle = findViewById(R.id.txtTitle);
        textViewImageTitle = findViewById(R.id.textViewImageTitle);
        imgCaptureImage = findViewById(R.id.imgCaptureImage);
        imgGif = findViewById(R.id.imgGif);
        layoutSave = findViewById(R.id.layoutSave);
        layoutSelfie = findViewById(R.id.layoutSelfie);
        //Widgets
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));

        layoutSave.setOnClickListener(this);
        layoutSelfie.setOnClickListener(this);
        btnClose.setOnClickListener(this);


        //face detector object
        detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();


        CommonUtils.setDialog(context, parentLayout);
        refresh(object);

        // set full screen dialog
        /*int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.80);
        getWindow().setLayout(width, height);*/


    }

    public void refresh(Object object) {
        this.object = object;  // FaceDetectionImageDto
        if (object instanceof FaceDetectionImageDto) {

            //Check Bitmap exist
            Bitmap imageBitmap = CommonUtils.StringToBitMap(((FaceDetectionImageDto) object).getImage_base64());
            if (imageBitmap != null) {
                imgGif.setVisibility(View.GONE);
                imgPreview.setVisibility(View.VISIBLE);
                imgPreview.setImageBitmap(imageBitmap);
                return;
            }

            //Set Gif using Key Name
            switch (imgName) {

                case "KendraVerificationUpdation":
                    setGifDrawable(R.drawable.selfieimg);
                    break;

                default:
                    setGifDrawable(R.drawable.selfieimg);
                    break;

            }
        }

    }

    private void setGifDrawable(int drawable) {
        try {
            imgGif.setBackground(deprecateHandler.getDrawable(drawable));
            //GifDrawable gifFromResource = new GifDrawable(context.getResources(), drawable);
            //imgGif.setBackground(gifFromResource);
            imgPreview.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.layoutSave) {
            AnimationHanndler.bubbleAnimation(context, view);
            iImagePreviewDialogClicks.OkClick(object);
        } else if (Id == R.id.layoutSelfie) {
            AnimationHanndler.bubbleAnimation(context, view);
            iImagePreviewDialogClicks.capturePhotoClick();
        } else if (Id == R.id.btnClose) {
            iImagePreviewDialogClicks.cancelClick();
            dismiss();
        }
    }

    // Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
        }
    }

    // Allow Image Title visibility
    public void allowImageTitle(boolean isAllow, String text) {
        if (isAllow) {
            textViewImageTitle.setText(text);
            textViewImageTitle.setVisibility(View.VISIBLE);
        } else {
            textViewImageTitle.setVisibility(View.INVISIBLE);
        }

    }

    //region get face count using face detection
    public int faceCount(Context context, Uri imageUri) {
        int faceCount = 0;
        Bitmap rotatedBitmap = null;
        try {
            Bitmap decodeBitmapUri = CommonUtils.decodeBitmapUri(context, imageUri);
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            ExifInterface exif = new ExifInterface(inputStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            rotatedBitmap = CommonUtils.rotateBitmap(decodeBitmapUri, orientation);
            faceCount = faceDetectionCount(context, detector, rotatedBitmap);
        } catch (Exception e) {
            e.getMessage();
        }
        return faceCount;
    }

    //face detection count find
    public int faceDetectionCount(Context context, FaceDetector detector, Bitmap bitmap) {
        int faceCount = 0;
        if (detector.isOperational() && bitmap != null) {
            Bitmap editBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), bitmap.getConfig());
            float scale = context.getResources().getDisplayMetrics().density;
            // Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Paint paint = new Paint();
            paint.setColor(Color.rgb(255, 61, 61));
            paint.setTextSize((int) (14 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3f);
            Canvas canvas = new Canvas(editBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editBitmap).build();
            SparseArray<Face> faces = detector.detect(frame);
            if (faces.size() == 0) {
                faceCount = 0;
            } else {
                faceCount = faces.size();
            }
        }
        return faceCount;
    }

}
