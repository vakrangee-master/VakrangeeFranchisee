package in.vakrangee.franchisee.commongui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.ImageGIFDto;
import in.vakrangee.supercore.franchisee.model.GWREventPhotoDto;

public class CustomGIFImageDialog extends Dialog implements android.view.View.OnClickListener {
    private static final String TAG = "CustomGIFImageDialog";
    private Object object;
    private Context context;
    private Button btnClose, btnRefreshGIF;
    private TextView txtTitle;
    private ImageView imgPreview;
    private LinearLayout layoutFooter;
    private Button btnCancel;
    private boolean cameraVisible = false;
    private ImageView imgCapture;
    private Typeface font;
    private CustomGIFImageDialog.onCameraClickGIf onCameraClickGIf;

    public CustomGIFImageDialog(@NonNull Context context, Object object, boolean cameraVisible, CustomGIFImageDialog.onCameraClickGIf onCameraClickGIf) {
        super(context);
        this.context = context;
        this.object = object;
        this.cameraVisible = cameraVisible;
        this.onCameraClickGIf = onCameraClickGIf;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_gif_image_preview);

        init();
    }

    //region init
    private void init() {
        imgCapture = findViewById(R.id.imgCapture);
        imgPreview = findViewById(R.id.imageViewPreview);
        txtTitle = findViewById(R.id.txtTitle);
        layoutFooter = findViewById(R.id.layoutFooter);


        btnCancel = findViewById(R.id.btnCancel);
        btnClose = findViewById(R.id.btnClose);
        btnRefreshGIF = findViewById(R.id.btnRefreshGIF);

        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + context.getResources().getString(R.string.cancel)));

        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + " "));

        btnRefreshGIF.setTypeface(font);
        btnRefreshGIF.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " "));


        //Listeners
        layoutFooter.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        btnRefreshGIF.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        imgCapture.setOnClickListener(this);
        gifImage(object); // dto to object

        // set full screen dialog
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
        getWindow().setLayout(width, height);

    }
    //endregion

    //region GIF image object to display data
    private void gifImage(Object object) {
        if (!cameraVisible) {
            imgCapture.setVisibility(View.GONE);
        }
        if (object instanceof ImageGIFDto) {
            ImageGIFDto imageGIFDto = (ImageGIFDto) object;
            setDialogTitle(imageGIFDto.getTitle());
            //R.drawable.frontimagemarker
            Glide.with(context).load(imageGIFDto.getImageViewId())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (resource instanceof GifDrawable) {
                                ((GifDrawable) resource).setLoopCount(GifDrawable.LOOP_FOREVER);
                            }
                            return false;
                        }
                    })
                    .into(imgPreview);
        } else if (object instanceof GWREventPhotoDto) {
            GWREventPhotoDto gwrEventPhotoDto = (GWREventPhotoDto) object;
            setDialogTitle(gwrEventPhotoDto.getGuinnessEventPhotoTypeName());
            //R.drawable.frontimagemarker
            Glide.with(context).load(R.drawable.frontimagemarker)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (resource instanceof GifDrawable) {
                                ((GifDrawable) resource).setLoopCount(2);
                            }
                            return false;
                        }
                    })
                    .into(imgPreview);
        }

    }
    //endregion

    //region onClick button
    @Override
    public void onClick(View view) {
        int Id = view.getId();
        switch (Id) {
            case R.id.btnClose:
                dismiss();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnRefreshGIF:
                gifImage(object);
                break;
            case R.id.imgCapture:
                onCameraClickGIf.onGIFCameraClick();
                dismiss();
                break;
        }
    }
    //endregion

    //region Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
        }
    }
    //endregion

    public interface onCameraClickGIf {
        public void onGIFCameraClick();
    }

}
