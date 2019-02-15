package com.mintegral.mediation.adapter.iron;

import android.app.Activity;
import android.text.TextUtils;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.ISDemandOnlyRewardedVideoListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.mintegral.mediation.common.adapter.BaseRewardAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;

import java.util.Map;

public class IronRewardAdapter extends BaseRewardAdapter {
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private MediationAdapterRewardListener mMediationAdapterRewardListener;

    private String appKey = "";
    // This is the instance id used inside ironSource SDK
    private String mInstanceId = "0";
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
                IronSource.initISDemandOnly(activity, appKey, IronSource.AD_UNIT.REWARDED_VIDEO);
                if (mMediationAdapterInitListener != null) {
                    mMediationAdapterInitListener.onInitSucceed();

                }
                return;
            }

        }
        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitFailed();

        }


    }

    @Override
    public void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    @Override
    public void show() {
        if (TextUtils.isEmpty(mPlacementName)) {
            IronSource.showISDemandOnlyRewardedVideo(mInstanceId);
        } else {
            IronSource.showISDemandOnlyRewardedVideo(mInstanceId, mPlacementName);
        }
    }

    @Override
    public void setSDKRewardListener(final MediationAdapterRewardListener mediationAdapterRewardListener) {
        mMediationAdapterRewardListener = mediationAdapterRewardListener;
        IronSource.setISDemandOnlyRewardedVideoListener(new ISDemandOnlyRewardedVideoListener() {
            @Override
            public void onRewardedVideoAdOpened(String instanceId) {
                if(mediationAdapterRewardListener != null){
                    mediationAdapterRewardListener.showSucceed();
                }
            }

            @Override
            public void onRewardedVideoAdClosed(String instanceId) {
                if(mediationAdapterRewardListener != null){
                    mediationAdapterRewardListener.closed();
                }
            }

            @Override
            public void onRewardedVideoAvailabilityChanged(String instanceId,boolean b) {

                // Invoke only for first load, ignore for all others and rely on 'hasAdAvailable'
                if (mIsFirstInitFlow) {
                    if (mediationAdapterRewardListener != null) {
                        if (b) {
                            mediationAdapterRewardListener.loadSucceed();
                        } else {
                            mediationAdapterRewardListener.loadFailed("");
                        }
                    }
                    mIsFirstInitFlow = false;
                }

            }



            @Override
            public void onRewardedVideoAdRewarded(String instanceId,Placement placement) {
                if(mediationAdapterRewardListener != null){
                    mediationAdapterRewardListener.rewarded(placement.getRewardName(),placement.getRewardAmount());
                }
            }

            @Override
            public void onRewardedVideoAdShowFailed(String instanceId,IronSourceError ironSourceError) {
                if(mediationAdapterRewardListener != null){
                    mediationAdapterRewardListener.showFailed(ironSourceError.toString());
                }
            }

            @Override
            public void onRewardedVideoAdClicked(String s, Placement placement) {
                if(mediationAdapterRewardListener != null){
                    mediationAdapterRewardListener.clicked(placement.toString());
                }
            }

        });
    }

    @Override
    public boolean isReady() {
        return IronSource.isISDemandOnlyRewardedVideoAvailable(mInstanceId);
    }

    @Override
    public void load(Activity activity, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if (!mIsFirstInitFlow) {
            if (mMediationAdapterRewardListener != null) {
                if (isReady()) {
                    mMediationAdapterRewardListener.loadSucceed();
                } else {
                    mMediationAdapterRewardListener.loadFailed("");
                }
            }
        }
    }
}
