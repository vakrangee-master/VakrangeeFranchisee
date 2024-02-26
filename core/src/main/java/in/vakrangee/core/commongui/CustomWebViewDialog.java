package in.vakrangee.core.commongui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import in.vakrangee.core.R;
import in.vakrangee.core.utils.FranchiseeApplicationRepository;

public class CustomWebViewDialog extends Dialog {

    private static final String TAG = "CustomWebViewDialog";
    private Context context;
    private WebView webView;
    private ImageView imgCancel;
    private ProgressBar progressBar, progressBar_webview;
    private Button btnAgree;
    private FranchiseeApplicationRepository fapRepo;
    private String data;
    private TextView txtTitle;
    private IOnClick iOnClick;

    public interface IOnClick {
        void onAgreeClick();

        void onCancelClick();
    }

    public CustomWebViewDialog(@NonNull Context context, IOnClick iOnClick) {
        super(context);
        this.context = context;
        this.iOnClick = iOnClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom_webview);
        init();
    }

    //region init
    private void init() {
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        fapRepo = new FranchiseeApplicationRepository(context);
        imgCancel = findViewById(R.id.imgCancel);
        progressBar = findViewById(R.id.progressBar);
        progressBar_webview = findViewById(R.id.progressBar_webview);
        webView = findViewById(R.id.webViewDialog);
        btnAgree = findViewById(R.id.btnAgree);
        txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setText(R.string.disclaimer);
        btnAgree.setTypeface(font);
        btnAgree.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + " " + "I Agree"));

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                iOnClick.onCancelClick();
            }
        });
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                iOnClick.onAgreeClick();
            }
        });

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
        getWindow().setLayout(width, height);
        GetDataFormURL getDataFormURL = new GetDataFormURL();
        getDataFormURL.execute("");
    }
    //endregion

    //region load data in web view
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void loadWebPage(String data) {
        //  webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setPadding(0, 0, 0, 0);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //Webview Client
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "Testing: URLS: " + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

        });

        //WebView Chrome Client
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                progressBar.setProgress(progress);
                progressBar_webview.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    progressBar_webview.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar_webview.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);

                }
            }
        });

        webView.loadData(data, "text/html", "UTF-8");

    }
    //endregion

    //region load data from URL
    class GetDataFormURL extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1:Address Proof Type
            data = fapRepo.getDisclaimer();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dismissProgressSpinner();

            loadWebPage(data);

        }
    }
    //endregion

}
