package com.mintegral.mediation.out.handler;

import android.app.Activity;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;
import com.mintegral.mediation.common.manager.MediationInterstitialManager;
import com.mintegral.mediation.common.manager.MediationRewardManager;

import java.util.Map;

public class MediationRewardVideoHandler extends BaseHandler{

    private MediationRewardManager mMediationRewardManager;
    private MediationAdapterRewardListener mMediationAdapterRewardListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    /**
     * 后续版本使用，标记在当前聚合平台的广告位
     */
    private String mediationUnitId = "";

    public MediationRewardVideoHandler(){
        mMediationRewardManager = new MediationRewardManager();
    }
    @Override
    public void init(Activity activity,  Map<String, Object> localParams) {
        try {
            mMediationRewardManager.init(activity,mediationUnitId,localParams);
        } catch (Exception e) {
            if(mMediationAdapterInitListener != null){
                mMediationAdapterInitListener.onInitFailed();
            }
        }
    }

    @Override
    public void load() {
        try {
            if(mMediationRewardManager != null){
                mMediationRewardManager.load();
            }else{
                if(mMediationAdapterRewardListener != null){
                    mMediationAdapterRewardListener.loadFailed(MediationMTGErrorCode.UNSPECIFIED);
                }
            }
        } catch (Exception e) {
            if(mMediationAdapterRewardListener != null){
                mMediationAdapterRewardListener.loadFailed(MediationMTGErrorCode.UNSPECIFIED);
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            if(mMediationRewardManager != null){
                return mMediationRewardManager.isReady();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void show() {
        try {
            if(mMediationRewardManager != null){
                mMediationRewardManager.show();
            }else{
                if(mMediationAdapterRewardListener != null){
                    mMediationAdapterRewardListener.showFailed(MediationMTGErrorCode.UNSPECIFIED);
                }
            }
        } catch (Exception e) {
            if(mMediationAdapterRewardListener != null){
                mMediationAdapterRewardListener.showFailed(MediationMTGErrorCode.UNSPECIFIED);
            }
        }
    }

    @Override
    public void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        try {
            mMediationAdapterInitListener = mediationAdapterInitListener;
            if(mMediationRewardManager != null){
                mMediationRewardManager.setMediationAdapterInitListener(mediationAdapterInitListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LifecycleListener getLifecycleListener() {
        try {
            if(mMediationRewardManager != null){
                return mMediationRewardManager.getLifecycleListener();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置Interstitial相关监听
     * @param mediationAdapterRewardListener
     */
    public void setMediationAdapterRewardListener(MediationAdapterRewardListener mediationAdapterRewardListener){
        try {
            mMediationAdapterRewardListener = mediationAdapterRewardListener;
            if(mMediationRewardManager != null){
                mMediationRewardManager.setMediationAdapterRewardListener(mediationAdapterRewardListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
