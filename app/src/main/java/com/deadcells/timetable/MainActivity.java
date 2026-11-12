package com.deadcells.timetable;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;

    /** Progress is written synchronously to SharedPreferences, so it survives
     *  being swiped from recents, force-stop, or a reboot. */
    public class Store {
        private final SharedPreferences sp = getSharedPreferences("progress", MODE_PRIVATE);
        @JavascriptInterface public void save(String json) { sp.edit().putString("dc-tt", json).commit(); }
        @JavascriptInterface public String load() { return sp.getString("dc-tt", null); }
    }

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        web = new WebView(this);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        web.setBackgroundColor(0xFF0D0C14);
        web.setWebViewClient(new WebViewClient());
        web.addJavascriptInterface(new Store(), "AndroidStore");
        web.loadUrl("file:///android_asset/index.html");
    }

    @Override public void onBackPressed() { moveTaskToBack(true); }
    @Override protected void onPause() { super.onPause(); web.onPause(); }
    @Override protected void onResume() { super.onResume(); web.onResume(); }
}
