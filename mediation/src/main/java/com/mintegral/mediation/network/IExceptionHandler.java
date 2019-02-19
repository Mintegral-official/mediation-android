package com.mintegral.mediation.network;

import android.app.Activity;

public interface IExceptionHandler {
    void handleException(Activity activity, Throwable t);
}
