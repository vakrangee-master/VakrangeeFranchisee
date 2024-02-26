package in.vakrangee.franchisee.documentmanager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class CustomTrackingPDFdialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "CustomTrackingPDFdialog";
    private Context context;
    private Button btnClose;
    private TextView txtTitle;
    //private ImageView imgPreview;
    private String imageUrl;
    private WebView webViewPDF;

    private Typeface font;
    private ImageView imgPreview;
    private LinearLayout includeLayoutProgresstxt;

    public CustomTrackingPDFdialog(@NonNull Context context, String imageUrl) {
        super(context);
        this.context = context;
        this.imageUrl = imageUrl;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_tracking_pdf_view);

        init();

    }

    //region init
    private void init() {
        // imgPreview = findViewById(R.id.imageViewPreview);
        txtTitle = findViewById(R.id.txtTitle);
        webViewPDF = findViewById(R.id.webViewPDF);
        imgPreview = findViewById(R.id.imageViewPreview);
        includeLayoutProgresstxt = findViewById(R.id.includeLayoutProgresstxt);
        btnClose = findViewById(R.id.btnClose);


        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + " "));


        //Listeners
        btnClose.setOnClickListener(this);

        // set full screen dialog
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
        getWindow().setLayout(width, height);
        showImage(imageUrl);

    }
    //endregion

    //region  image to display
    private void showImage(String url) {
        if (url.endsWith(".PDF") || url.endsWith(".pdf")) {
            String loadPDF = "http://drive.google.com/viewerng/viewer?embedded=true&url=" + url;
            webViewPDF.getSettings().setJavaScriptEnabled(true);
            webViewPDF.getSettings().setLoadWithOverviewMode(true);
            webViewPDF.getSettings().setBuiltInZoomControls(true);
            webViewPDF.getSettings().setUseWideViewPort(false);

            webViewPDF.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, final String url) {
                    webViewPDF.setVisibility(View.VISIBLE);
                    imgPreview.setVisibility(View.GONE);
                    includeLayoutProgresstxt.setVisibility(View.GONE);
                }
            });
            webViewPDF.loadUrl(loadPDF);
        } else {
            Glide.with(context)
                    .load(url)
                    .apply(new RequestOptions()
                            .error(R.drawable.not_found)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .fitCenter()
                            .skipMemoryCache(true))
                    .into(imgPreview);
            webViewPDF.setVisibility(View.GONE);
            includeLayoutProgresstxt.setVisibility(View.GONE);
            imgPreview.setVisibility(View.VISIBLE);
        }
    }

    //endregion

    //region onClick button
    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.btnClose) {
            dismiss();
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


}
