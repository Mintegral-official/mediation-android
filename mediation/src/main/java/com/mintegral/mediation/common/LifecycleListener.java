package com.mintegral.mediation.common;

import android.app.Activity;

public interface LifecycleListener {
    void onCreate( Activity activity);
    void onStart( Activity activity);
    void onPause( Activity activity);
    void onResume( Activity activity);

    void onRestart( Activity activity);
    void onStop( Activity activity);
    void onDestroy( Activity activity);
    void onBackPressed( Activity activity);
}
