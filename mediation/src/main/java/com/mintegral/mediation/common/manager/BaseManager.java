package com.mintegral.mediation.common.manager;

import android.app.Activity;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;

import java.util.Map;

public abstract class BaseManager {

    abstract void init(Activity activity, String mediationUnitId, Map<String,Object> localParams);
    abstract void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener);
    abstract void load();
    abstract boolean isReady();
    abstract void show();
    abstract LifecycleListener getLifecycleListener();
    public abstract void loadTimeout();
    public abstract boolean isLoadHadResult();
    abstract void setInterceptor(BaseInterceptor interceptor);
    abstract void callInitListener(boolean succeed);
}
