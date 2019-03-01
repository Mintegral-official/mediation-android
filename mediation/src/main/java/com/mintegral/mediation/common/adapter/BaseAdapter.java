package com.mintegral.mediation.common.adapter;

import android.app.Activity;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;

import java.util.Map;

public abstract class BaseAdapter {
    /**
     * init sdk
     * @param activity Ironsource request activity
     * @param localExtras local params
     * @param serverExtras service setting
     */
    public abstract void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras);

    /**
     * request ads
     */
    public abstract void load(String param);

    /**
     * show ads
     */
    public abstract void show(String param);

    /**
     * check ads state
     * @return if the ads can show reture true, or false
     */
    public abstract boolean isReady(String param);

    /**
     * set init state listener
     * @param mediationAdapterInitListener init state listener
     */
    public abstract void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener);
    /**
     * get adapter LifecycleListener for bind Lifecycle to adapter
     * @return LifecycleListener
     */
    public abstract LifecycleListener getLifecycleListener();

}
