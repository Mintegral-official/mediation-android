package com.mintegral.mediation.out.handler;

import android.app.Activity;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;

import java.util.Map;

/**
 * @author songjunjun
 *
 */
public abstract class BaseHandler {
    /**
     * init manager then init adapters
     * @param activity context ,but ironsource need activity
     * @param localParams set params in code
     */
    public abstract void init(Activity activity, Map<String, Object> localParams);

    /**
     * set init state listener
     * @param mediationAdapterInitListener initListener
     */
    public abstract void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener);

    /**
     * it will load ads
     */
    public abstract void load(String param);

    /**
     * check ads state
     * @return if ads can show will return true,other false
     */
    public abstract boolean isReady(String param);

    /**
     * it will show ads
     */
    public abstract void show(String param);

    /**
     * get adapter's lifecycleListener
     * @return lifecycleListener
     */
    public abstract LifecycleListener getLifecycleListener();

    public abstract void setInterceptor(BaseInterceptor interceptor);
}
