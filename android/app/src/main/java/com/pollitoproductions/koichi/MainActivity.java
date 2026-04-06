package com.pollitoproductions.koichi;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String GAME_URL = "https://pollitoproductions.github.io/koichi/index.html";
    private static final String GAME_HOST = "pollitoproductions.github.io";
    private static final String REWARDED_AD_UNIT_ID = "ca-app-pub-7858482153655813/4188505133";
    
    private WebView webView;
    private RewardedAd rewardedAd;
    private boolean isLoadingAd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this);
        
        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        // Enable immersive mode
        enableImmersiveMode();
        
        // Create and configure WebView
        webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        
        // Add JavaScript interface for Android callbacks
        webView.addJavascriptInterface(new WebViewInterface(this), "Android");
        
        // Keep navigation inside deployed game host
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (GAME_HOST.equalsIgnoreCase(uri.getHost())) {
                    return false;
                }
                view.loadUrl(GAME_URL);
                return true;
            }
        });

        // Load deployed Koi Chi game page
        webView.loadUrl(GAME_URL);
        
        // Preload a rewarded ad
        loadRewardedAd();
        
        setContentView(webView);
    }

    /**
     * Load a rewarded ad from AdMob
     */
    private void loadRewardedAd() {
        if (isLoadingAd) {
            Log.d(TAG, "Ad is already loading");
            return;
        }

        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        isLoadingAd = true;

        RewardedAd.load(this, REWARDED_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(RewardedAd ad) {
                rewardedAd = ad;
                isLoadingAd = false;
                Log.d(TAG, "Rewarded ad loaded successfully");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                rewardedAd = null;
                isLoadingAd = false;
                Log.w(TAG, "Failed to load rewarded ad: " + loadAdError.getMessage());
            }
        });
    }

    /**
     * Show the rewarded ad to the user
     * Called from JavaScript when user taps the night mode button
     */
    public void showRewardedAd() {
        if (rewardedAd != null) {
            Log.d(TAG, "Showing rewarded ad");
            rewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(com.google.android.gms.ads.rewarded.RewardItem reward) {
                    Log.d(TAG, "User earned reward: " + reward.getType() + " - " + reward.getAmount());
                    // Notify JavaScript that reward was earned
                    enableNightModeInWebView();
                }
            });
            // Preload next ad after showing
            loadRewardedAd();
        } else {
            Log.w(TAG, "Rewarded ad not ready yet");
            // Reload ad if not ready
            loadRewardedAd();
        }
    }

    /**
     * Call JavaScript function to enable night mode in the game
     */
    private void enableNightModeInWebView() {
        if (webView != null) {
            webView.post(() -> {
                webView.evaluateJavascript("enableNightMode();", null);
                Log.d(TAG, "Called enableNightMode() in JavaScript");
            });
        }
    }

    private void enableImmersiveMode() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            enableImmersiveMode();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause JavaScript execution when app goes to background
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume JavaScript execution
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
        enableImmersiveMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up WebView
        if (webView != null) {
            webView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
