package com.mintegral.mediation.network;

import android.content.Context;
import android.os.AsyncTask;
import android.app.Activity;

import com.mintegral.mediation.common.CommonConst;

import java.lang.ref.WeakReference;

/**
 * Network request/response util
 * @see AsyncTask
 *
 * @author hanliontien
 */
public class NetworkUtil<T> extends AsyncTask<BaseRequest<T>, Void, T> {
    private WeakReference<Context> mContexts;
    private NetCallback mNetCallback;

    public NetworkUtil(Context context, NetCallback netCallback) {
        this.mNetCallback = netCallback;
        this.mContexts = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mNetCallback != null) {
            mNetCallback.onPrepare();
        }
    }

    @Override
    protected T doInBackground(BaseRequest<T>... requests) {
        if (isCancelled() || requests == null || requests.length == 0) {
            if(mNetCallback != null) {
                mNetCallback.onFail(new NetworkException(CommonConst.KEY_REQUEST_ERROR, CommonConst.REQUEST_ERROR_CONTENT));
            }
            return null;
        }

        try {
            return requests[0].request();
        } catch (Exception e) {
            if(mNetCallback != null) {
                mNetCallback.onFail(e);
            }
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(T response) {
        super.onPostExecute(response);

        if (mContexts.get() != null) {
            if (mContexts.get() instanceof Activity) {
                Activity activity = (Activity) mContexts.get();
                if (activity.isFinishing()) {
                    cancel(true);
                } else {
                    post(response);
                }
            } else {
                post(response);
            }
        } else {
            cancel(true);
        }
    }

    private void post(T response) {
        if(mNetCallback != null) {
            if (response != null) {
                mNetCallback.onSuccess(response);
            } else {
                mNetCallback.onFail(new NetworkException(CommonConst.KEY_RESPONSE_ERROR, CommonConst.RESPONSE_ERROR_CONTENT));
            }
        }
    }
}

