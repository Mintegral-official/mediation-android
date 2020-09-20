package com.mintegral.mediation.common.manager;

import android.app.Activity;
import android.view.ViewGroup;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;

import java.util.Map;

public abstract class BaseManager {

    abstract void init(Activity activity, String mediationUnitId, Map<String,Object> localParams);
    abstract void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener);
    abstract void load(String param);
    abstract boolean isReady(String param);
    abstract void show(String param);
    public void showBanner(String param, ViewGroup container){};
    abstract LifecycleListener getLifecycleListener();
    public abstract void loadTimeout(String param);
    public abstract boolean isLoadHadResult();
    abstract void setInterceptor(BaseInterceptor interceptor);
    abstract void callInitListener(boolean succeed);
    public abstract String getCurrentLoadParam();
}
