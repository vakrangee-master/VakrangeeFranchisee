package in.vakrangee.franchisee.activity;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import in.vakrangee.franchisee.R;



public class WebviewActivity  extends AppCompatActivity {
    String mUrl;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView mywebview = (WebView) findViewById(R.id.webView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             mUrl = extras.getString("path");
        }
        mywebview.loadUrl(mUrl);
    }
}