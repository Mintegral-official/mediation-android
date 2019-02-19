package com.mintegral.outofmediation.network;

import android.app.Activity;

import com.mintegral.mediation.network.IExceptionHandler;
import com.mintegral.mediation.network.NetworkException;

/**
 * 网络请求Exception处理实现
 */
public class ExceptionHandlerImpl implements IExceptionHandler {
    @Override
    public void handleException(Activity activity, Throwable t) {
        if (t instanceof NetworkException){
            NetworkException networkException = (NetworkException) t;
            switch (networkException.errorCode) {
                case 400:
                    break;

                case 401:
                    break;
            }
        }
    }
}
