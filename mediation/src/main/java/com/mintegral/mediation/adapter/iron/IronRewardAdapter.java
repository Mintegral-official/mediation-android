package com.mintegral.mediation.adapter.iron;

import android.app.Activity;

import android.text.TextUtils;
import android.util.Log;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.ISDemandOnlyRewardedVideoListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

import com.mintegral.mediation.common.BaseLifecycleListener;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.adapter.BaseRewardAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;

import java.util.Map;

public class IronRewardAdapter extends BaseRewardAdapter {
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private MediationAdapterRewardListener mMediationAdapterRewardListener;

    private String appKey = "";
    // This is the instance id used inside ironSource SDK
//    private String mInstanceId = "0";
    // This is the placement name used inside ironSource SDK
    private String mPlacementName = null;
    // Indicates if IronSource RV adapter is in its first init flow
    private static boolean mIsFirstInitFlow = true;

    @Override
    public void init(Activity activity,String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras) {

        if(localExtras != null){
            Object ob = localExtras.get("local");
            if(ob instanceof String){
                //初始化reward video
                appKey = ob.toString();
                if (!TextUtils.isEmpty(appKey)) {
                    IronSource.init(activity, appKey, IronSource.AD_UNIT.REWARDED_VIDEO);
//                    if (mMediationAdapterInitListener != null) {
//                        mMediationAdapterInitListener.onInitSucceed();
//                    }
                    return;
                }
            }
        }
        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitFailed();

        }
    }


    @Override
    public LifecycleListener getLifecycleListener() {
        return mLifecycleListener;
    }

    private LifecycleListener mLifecycleListener = new BaseLifecycleListener() {
        @Override
        public void onPause( Activity activity) {
            super.onPause(activity);
            IronSource.onPause(activity);
        }

        @Override
        public void onResume( Activity activity) {
            super.onResume(activity);
            IronSource.onResume(activity);
        }
    };
    @Override
    public void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        mMediationAdapterInitListener = mediationAdapterInitListener;
        setRewardListenerToIronsource();
    }

    @Override
    public void show() {
        if (TextUtils.isEmpty(mPlacementName)) {
            IronSource.showRewardedVideo();
        } else {
            IronSource.showRewardedVideo( mPlacementName);
        }
    }

    private void setRewardListenerToIronsource(){
        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
            @Override
            public void onRewardedVideoAdOpened() {
                if(mMediationAdapterRewardListener != null){
                    mMediationAdapterRewardListener.showSucceed();
                }
            }

            @Override
            public void onRewardedVideoAdClosed() {
                if(mMediationAdapterRewardListener != null){
                    mMediationAdapterRewardListener.closed();
                }
            }

            @Override
            public void onRewardedVideoAvailabilityChanged(boolean b) {

                // Invoke only for first load, ignore for all others and rely on 'hasAdAvailable'
                if (mIsFirstInitFlow) {
                    if (mMediationAdapterInitListener != null) {
                        if (b) {
                            mMediationAdapterInitListener.onInitSucceed();
                        } else {

                            mMediationAdapterInitListener.onInitFailed();
                        }
                    }
                    if (mMediationAdapterRewardListener != null) {
                        if (b) {
                            mMediationAdapterRewardListener.loadSucceed();
                        } else {
                            mMediationAdapterRewardListener.loadFailed(MediationMTGErrorCode.UNSPECIFIED);
                        }
                    }
                    mIsFirstInitFlow = false;
                }

            }



            @Override
            public void onRewardedVideoAdRewarded(Placement placement) {
                if(mMediationAdapterRewardListener != null){
                    mMediationAdapterRewardListener.rewarded(placement.getRewardName(),placement.getRewardAmount());
                }
            }

            @Override
            public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
                if(mMediationAdapterRewardListener != null){
                    mMediationAdapterRewardListener.showFailed(getMIntergralErrorMessage(ironSourceError));
                }
            }

            @Override
            public void onRewardedVideoAdClicked(Placement placement) {
                if(mMediationAdapterRewardListener != null){
                    mMediationAdapterRewardListener.clicked(placement.toString());
                }
            }

            @Override
            public void onRewardedVideoAdEnded() {

            }

            @Override
            public void onRewardedVideoAdStarted() {

            }
        });
    }

    @Override
    public void setSDKRewardListener(final MediationAdapterRewardListener mediationAdapterRewardListener) {
        mMediationAdapterRewardListener = mediationAdapterRewardListener;
        setRewardListenerToIronsource();
    }

    @Override
    public boolean isReady() {
        return IronSource.isRewardedVideoAvailable();
    }

    @Override
    public void load() {
        if (!mIsFirstInitFlow) {
            if (mMediationAdapterRewardListener != null) {
                if (isReady()) {
                    mMediationAdapterRewardListener.loadSucceed();
                } else {
                    mMediationAdapterRewardListener.loadFailed(MediationMTGErrorCode.NETWORK_NO_FILL);
                }
            }
        }
    }


    private String getMIntergralErrorMessage(IronSourceError ironSourceError) {
        if (ironSourceError == null) {
            return MediationMTGErrorCode.INTERNAL_ERROR;
        }
        switch (ironSourceError.getErrorCode()) {
            case IronSourceError.ERROR_CODE_NO_CONFIGURATION_AVAILABLE:
            case IronSourceError.ERROR_CODE_KEY_NOT_SET:
            case IronSourceError.ERROR_CODE_INVALID_KEY_VALUE:
            case IronSourceError.ERROR_CODE_INIT_FAILED:
                return MediationMTGErrorCode.ADAPTER_CONFIGURATION_ERROR;
            case IronSourceError.ERROR_CODE_USING_CACHED_CONFIGURATION:
                return MediationMTGErrorCode.VIDEO_CACHE_ERROR;
            case IronSourceError.ERROR_CODE_NO_ADS_TO_SHOW:
                return MediationMTGErrorCode.NETWORK_NO_FILL;
            case IronSourceError.ERROR_CODE_GENERIC:
                return MediationMTGErrorCode.INTERNAL_ERROR;
            case IronSourceError.ERROR_NO_INTERNET_CONNECTION:
                return MediationMTGErrorCode.NO_CONNECTION;
            default:
                return MediationMTGErrorCode.UNSPECIFIED;
        }
    }
}
