package com.example.completionistguild.auth;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.completionistguild.R;
import com.example.completionistguild.auth.MyJavaScriptInterface;

public class AuthWindow extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_window);

        mWebView = findViewById(R.id.webView);

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        String webUrl = getIntent().getExtras().getString("url");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(checkurl(url));
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("10.0.2.2:3000/auth/steam")) {

                    mWebView.loadUrl("javascript:HtmlViewer.showHTML" +
                            "(document.getElementsByTagName('body')[0].innerHTML);");
                } else if(url.contains("10.0.2.2:3000/xbox/auth")) {
                    mWebView.loadUrl("javascript:HtmlViewer.showHTML" +
                            "(document.getElementsByTagName('body')[0].innerHTML);");
                }
            }
        });
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

        mWebView.loadUrl(checkurl(webUrl));
    }

    private String checkurl(String URL) {
        if (URL.contains("localhost:3000/steam/auth")) {

            URL = URL.replace("localhost:", "10.0.2.2:");

            mWebView.loadUrl(URL);
            return URL;
        } else if (URL.contains("localhost:3000/auth/steam")) {
            URL = URL.replace("localhost:", "10.0.2.2:");
            mWebView.loadUrl(URL);
        }else if (URL.contains("localhost:3000/xbox/auth")) {
            URL = URL.replace("localhost:", "10.0.2.2:");
            mWebView.loadUrl(URL);
        }
        return URL;

    }
}