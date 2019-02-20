package com.mintegral.mediation.common;

import android.app.Activity;


public class BaseLifecycleListener implements LifecycleListener {
    @Override
    public void onCreate( final Activity activity) {}

    @Override
    public void onStart( final Activity activity) {}

    @Override
    public void onPause( final Activity activity) {}

    @Override
    public void onResume(final Activity activity) {}

    @Override
    public void onRestart( final Activity activity) {}

    @Override
    public void onStop( final Activity activity) {}

    @Override
    public void onDestroy( final Activity activity) {}

    @Override
    public void onBackPressed( final Activity activity) {}
}
