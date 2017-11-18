package com.digitalmoney.home.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;

/**
 * Created by shailesh on 18/11/17.
 */

public class WebViewActivity extends AppCompatActivity{

    Typeface typefaceBold;
    private ProgressBar progressBar;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        typefaceBold = Typeface.createFromAsset(getAssets(), Utils.TYPEFACE_PATH_BOLD);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView viewTitle = (TextView)toolbar.findViewById(R.id.tvTitleNotification);
        viewTitle.setTypeface(typefaceBold);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        Log.e("Uid", uid);

        if (!uid.equalsIgnoreCase("") && uid != null) {
            progressBar.setVisibility(View.VISIBLE);
            loadWebView(uid);
        }else {

            Toast.makeText(this,
                    getResources().getString(R.string.nothing_to_show),
                    Toast.LENGTH_SHORT).show();

            Snackbar.make(getWindow().getDecorView().getRootView(),
                    getResources().getString(R.string.nothing_to_show),
                    Snackbar.LENGTH_SHORT).show();
        }

    }

    private void loadWebView(String stringUrl) {

        WebView htmlWebView = (WebView)findViewById(R.id.webview);
        htmlWebView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        htmlWebView.loadUrl(stringUrl);
        progressBar.setVisibility(View.GONE);
    }



    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }
}
