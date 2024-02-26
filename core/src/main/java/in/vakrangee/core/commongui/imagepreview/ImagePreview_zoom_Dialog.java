package in.vakrangee.core.commongui.imagepreview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import in.vakrangee.core.R;
import in.vakrangee.core.utils.Constants;

public class ImagePreview_zoom_Dialog extends Dialog {
    private Context context;
    private String imageID, imageTitle;
    private Button btnClose;
    private TextView txtDialogTitle;
    private ImageView fullscreenImageView;
    private Bitmap bitmap;
    private String Signature;

    public ImagePreview_zoom_Dialog(Context context, String id, String imageTitle) {
        super(context);
        this.context = context;
        this.imageID = id;
        this.imageTitle = imageTitle;
        this.Signature = String.valueOf(System.currentTimeMillis());
    }

    public ImagePreview_zoom_Dialog(Context context, String id, String imageTitle, String Signature) {
        super(context);
        this.context = context;
        this.imageID = id;
        this.imageTitle = imageTitle;
        this.Signature = Signature;
    }

    public ImagePreview_zoom_Dialog(Context context, Bitmap bitmap, String imageTitle) {
        super(context);
        this.context = context;
        this.bitmap = bitmap;
        this.imageTitle = imageTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //  getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.blackTransparent)));
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.dialog_fullscreen_dialog);
        fullscreenImageView = (ImageView) findViewById(R.id.fullscreen);

        txtDialogTitle = findViewById(R.id.txtDialogTitle);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));

        //set Data from Constructor
        txtDialogTitle.setText(imageTitle);
        if (imageTitle.equals("Sample Equipment Package Image")) {

            Glide.with(context)
                    .load(imageID)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))

                    .into(fullscreenImageView);



      /*  Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .error(R.drawable.ic_camera_alt_black_72dp))
                    .load(imageID)
                    .apply(new RequestOptions().signature(new ObjectKey(Signature)))
                    .into(fullscreenImageView);*/

        } else if (!TextUtils.isEmpty(imageID) && !imageID.equals("0")) {
            String ImageURL = Constants.DownloadImageUrl + imageID;
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .error(R.drawable.ic_camera_alt_black_72dp))
                    .load(ImageURL)
                    .apply(new RequestOptions().signature(new ObjectKey(Signature)))
                    /*.apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))*/
                    .into(fullscreenImageView);
        } else {
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .error(R.drawable.ic_camera_alt_black_72dp))
                    .asBitmap().load(bitmap)
                    .apply(new RequestOptions().signature(new ObjectKey(Signature)))
                    /*.apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))*/
                    .into(fullscreenImageView);

        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // perform setOnZoomInClickListener event on ZoomControls


    }

    @Override
    public void onStart() {
        super.onStart();
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels);
        getWindow().setLayout(width, height);
    }
}
