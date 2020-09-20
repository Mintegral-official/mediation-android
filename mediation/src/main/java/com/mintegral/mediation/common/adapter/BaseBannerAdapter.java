package com.mintegral.mediation.common.adapter;


import android.view.ViewGroup;

import com.mintegral.mediation.common.listener.MediationAdapterBannerListener;



/**
 * @author songjunjun
 */

public abstract class BaseBannerAdapter extends BaseAdapter{

    /**
     * 设置banner相关的回调
     * @param mediationAdapterBannerListener 监听sdk banner
     */
    public abstract void setSDKBannerListener(MediationAdapterBannerListener mediationAdapterBannerListener);

    public abstract void showBanner(ViewGroup bannerContainer);
}
