package com.mintegral.mediation.common.adapter;


import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;



/**
 * @author songjunjun
 */

public abstract class BaseRewardAdapter  extends BaseAdapter{

    /**
     * 设置激励视频相关的回调
     * @param mediationAdapterRewardListener 监听sdk激励视频结果
     */
    public abstract void setSDKRewardListener(MediationAdapterRewardListener mediationAdapterRewardListener);
}
