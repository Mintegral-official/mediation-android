package com.mintegral.mediation.network;

import android.content.Context;

/**
 * ExceptionHandler
 *
 * @author hanliontien
 */
public interface IExceptionHandler {
    /**
     * handle exception
     *
     * @param context
     * @param t
     */
    void handleException(Context context, Throwable t);
}
