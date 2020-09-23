package com.mintegral.mediation.out.handler;

import android.app.Activity;
import android.view.ViewGroup;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterBannerListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.manager.MediationBannerManager;

import java.util.Map;

public class MediationBannerHandler extends BaseHandler{

    private MediationBannerManager mMediationBannerManager;
    private MediationAdapterBannerListener mMediationAdapterBannerListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    /**
     * 后续版本使用，标记在当前聚合平台的广告位
     */
    private String mediationUnitId = "";

    public MediationBannerHandler(){
        mMediationBannerManager = new MediationBannerManager();
    }
    @Override
    public void init(Activity activity,  Map<String, Object> localParams) {
        try {
            mMediationBannerManager.init(activity,mediationUnitId,localParams);
        } catch (Exception e) {
            if(mMediationAdapterInitListener != null){
                mMediationAdapterInitListener.onInitFailed();
            }
        }
    }

    @Override
    public void load(String param) {
        try {
            if(mMediationBannerManager != null){
                mMediationBannerManager.load(param);
            }else{
                if(mMediationAdapterBannerListener != null){
                    mMediationAdapterBannerListener.loadFailed(MediationMTGErrorCode.UNSPECIFIED);
                }
            }
        } catch (Exception e) {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.loadFailed(MediationMTGErrorCode.UNSPECIFIED);
            }
        }
    }

    @Override
    public boolean isReady(String param) {
        try {
            if(mMediationBannerManager != null){
                return mMediationBannerManager.isReady(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void showBanner(String param, ViewGroup container) {
        try {
            if(mMediationBannerManager != null && container != null){
                mMediationBannerManager.showBanner(param,container);
            }else{
                if(mMediationAdapterBannerListener != null){
                    mMediationAdapterBannerListener.showFailed(MediationMTGErrorCode.UNSPECIFIED);
                }
            }
        } catch (Exception e) {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.showFailed(MediationMTGErrorCode.UNSPECIFIED);
            }
        }
    }

    @Deprecated
    @Override
    public void show(String param) {

    }

    @Override
    public void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        try {
            mMediationAdapterInitListener = mediationAdapterInitListener;
            if(mMediationBannerManager != null){
                mMediationBannerManager.setMediationAdapterInitListener(mediationAdapterInitListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LifecycleListener getLifecycleListener() {
        try {
            if(mMediationBannerManager != null){
                return mMediationBannerManager.getLifecycleListener();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置Interstitial相关监听
     * @param mediationAdapterBannerListener
     */
    public void setMediationAdapterBannerListener(MediationAdapterBannerListener mediationAdapterBannerListener){
        try {
            mMediationAdapterBannerListener = mediationAdapterBannerListener;
            if(mMediationBannerManager != null){
                mMediationBannerManager.setMediationAdapterBannerListener(mediationAdapterBannerListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setInterceptor(BaseInterceptor interceptor) {
        try {
            if(mMediationBannerManager != null){
                mMediationBannerManager.setInterceptor(interceptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
