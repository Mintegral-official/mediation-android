package com.mintegral.mediation.common.adapter;

import android.app.Activity;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;

import java.util.Map;

public abstract class BaseAdapter {
    /**
     * 初始化sdk
     * @param activity 方便给sdk传入context
     * @param localExtras 外部应用传进来的参数
     * @param serverExtras 从服务端获取的参数
     */
    public abstract void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras);

    /**
     * 请求广告
     */
    public abstract void load();

    /**
     * 展示广告
     */
    public abstract void show();

    /**
     * 查看是否可播放
     * @return 如果可以播放返回true，否则返回false
     */
    public abstract boolean isReady();

    /**
     * 设置初始化状态回调
     * @param mediationAdapterInitListener 监听sdk初始化结果
     */
    public abstract void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener);
    /**
     * 获取对应adapter的生命周期监听
     * @return
     */
    public abstract LifecycleListener getLifecycleListener();

}
