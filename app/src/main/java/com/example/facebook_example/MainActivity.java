package com.example.facebook_example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;



    LinearLayout mainsetLayout;
    ImageButton homeButton, friendsButton, messageButton, videoButton, noteButton, groupsButton, settingsButton, exitButton;

    MyViewClient myViewClient = new MyViewClient();
//    WebViewClient myViewClient = new WebViewClient();

    static String URL_home = "https://m.facebook.com/";
    static String URL_friends = "https://m.facebook.com/friends/";
    static String URL_messages = "https://m.facebook.com/messages/";
    static String URL_video = "https://m.facebook.com/watch/";
    static String URL_note = "https://m.facebook.com/notifications/";
    static String URL_groups = "https://m.facebook.com/groups/";
    static String URL_settings = "https://m.facebook.com/settings/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);
        homeButton = findViewById(R.id.homeButton);
        friendsButton = findViewById(R.id.friendsButton);
        messageButton = findViewById(R.id.messageButton);
        videoButton = findViewById(R.id.videoButton);
        noteButton = findViewById(R.id.noteButton);
        groupsButton = findViewById(R.id.groupsButton);
        settingsButton = findViewById(R.id.settingsButton);
        mainsetLayout = findViewById(R.id.mainsetLayout);
        exitButton = findViewById(R.id.exitButton);

        progressBar.setMax(100);

        webView.requestFocus();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(myViewClient);
        mainsetLayout.setVisibility(mainsetLayout.GONE);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setProgress(0);
                }
            }
                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    android.util.Log.e("WebView", consoleMessage.message());
                    return true;
                }
            });

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.loadUrl(URL_home);//////
        }
        try {
            homeButton.setOnClickListener(view -> webView.loadUrl(URL_home));
            friendsButton.setOnClickListener(view -> webView.loadUrl(URL_friends));
            videoButton.setOnClickListener(view -> webView.loadUrl(URL_video));
            messageButton.setOnClickListener(view -> webView.loadUrl(URL_messages));
            noteButton.setOnClickListener(view -> webView.loadUrl(URL_note));
            groupsButton.setOnClickListener(view -> webView.loadUrl(URL_groups));
            settingsButton.setOnClickListener(view -> {
                mainsetLayout.setVisibility(mainsetLayout.GONE);
                webView.loadUrl(URL_settings);
            });
            exitButton.setOnClickListener(view -> {
                clearCookiesAndCache(this);
                webView.reload();
            });
        } catch (Exception e) {
            Log.e("listener", String.valueOf(e));
        }
    }

    public class MyViewClient extends WebViewClient {
        // Переопределение метода загрузки страницы
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            CookieManager.getInstance().setAcceptCookie(true);
            return true;
        }

        // Переопределение метода окончания загрузки страницы
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            webView.postDelayed(new Runnable()
//            {
//                public void run(){
//                    webView.loadUrl("javascript:(function() { document.getElementById('mJewelNav').style.display='none';})()");
//                }
//            }, 1000);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            if (webView.getUrl().contains("settings")==false){
                mainsetLayout.setVisibility(mainsetLayout.VISIBLE);
            }
            super.onPageStarted(view, url, favicon);
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    public void clearCookiesAndCache(Context context){
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
        }
        else {
            cookieManager.removeAllCookie();
        }
    }
}