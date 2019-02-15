package com.mintegral.mediation.network;

import android.os.AsyncTask;
import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * 网络请求实现
 * @see AsyncTask
 *
 * @author hanliontien
 */
public class NetworkUtil<T> extends AsyncTask<BaseRequest<T>, Void, T> {
    private WeakReference<Activity> mActivitys;
    private NetCallback mNetCallback;

    public NetworkUtil(Activity activity, NetCallback netCallback) {
        this.mNetCallback = netCallback;
        this.mActivitys = new WeakReference<>(activity);
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
        if (isCancelled()) {
            return null;
        }

        try {
            if (requests != null && requests.length > 0) {
                return requests[0].request();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(T response) {
        super.onPostExecute(response);

        if (mActivitys.get() != null && !mActivitys.get().isFinishing()) {
            if(mNetCallback != null) {
                if (response != null) {
                    mNetCallback.onSuccess(response);
                } else {
                    mNetCallback.onFail();
                }
            }
        } else {
            cancel(true);
        }
    }
}

