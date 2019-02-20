package com.mintegral.mediation.out.handler;

import android.app.Activity;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;

import java.util.Map;


public abstract class BaseHandler {
    public abstract void init(Activity activity, Map<String,Object> localParams);
    public abstract void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener);
    public abstract void load();
    public abstract boolean isReady();
    public abstract void show();
    public abstract LifecycleListener getLifecycleListener();
}
