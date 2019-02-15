package com.mintegral.mediation.common;

import android.app.Activity;
import android.support.annotation.NonNull;

public class BaseLifecycleListener implements LifecycleListener {
    @Override
    public void onCreate(@NonNull final Activity activity) {}

    @Override
    public void onStart(@NonNull final Activity activity) {}

    @Override
    public void onPause(@NonNull final Activity activity) {}

    @Override
    public void onResume(@NonNull final Activity activity) {}

    @Override
    public void onRestart(@NonNull final Activity activity) {}

    @Override
    public void onStop(@NonNull final Activity activity) {}

    @Override
    public void onDestroy(@NonNull final Activity activity) {}

    @Override
    public void onBackPressed(@NonNull final Activity activity) {}
}
