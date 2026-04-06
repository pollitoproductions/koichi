package com.pollitoproductions.koichi;

import android.webkit.JavascriptInterface;
import android.util.Log;

/**
 * JavaScript interface to communicate between WebView and Android native code.
 * Methods here can be called from JavaScript using: Android.methodName()
 */
public class WebViewInterface {
    private static final String TAG = "WebViewInterface";
    private MainActivity activity;

    public WebViewInterface(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * Called from JavaScript when the user wants to watch a rewarded ad to unlock night mode.
     * Usage in JS: Android.showRewardedAd()
     */
    @JavascriptInterface
    public void showRewardedAd() {
        Log.d(TAG, "showRewardedAd called from JavaScript");
        if (activity != null) {
            activity.showRewardedAd();
        }
    }
}
