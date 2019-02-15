package com.mintegral.mediation.common.adapter;


import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;


/**
 * @author songjunjun
 */
public abstract class BaseInterstitialAdapter extends BaseAdapter{

    /**
     * 设置激励视频相关的回调
     * @param mediationAdapterInterstitialListener 监听sdkinterstitial结果
     */
    public abstract void setSDKInterstitial(MediationAdapterInterstitialListener mediationAdapterInterstitialListener);

}
