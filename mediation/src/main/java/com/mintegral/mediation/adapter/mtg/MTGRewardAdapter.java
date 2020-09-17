package com.mintegral.mediation.adapter.mtg;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;


import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.adapter.BaseRewardAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MTGRewardVideoHandler;
import com.mintegral.msdk.out.RewardVideoListener;

import java.util.Map;

/**
 * MTGRewardAdapter
 *
 * Adapter for Mintegral rewardvideo ad.
 *
 * @author hanliontien
 */
public class MTGRewardAdapter extends BaseRewardAdapter {
    private static final String TAG = MTGRewardAdapter.class.getSimpleName();

    private MTGRewardVideoHandler mMTGRewardVideoHandler;
    private String appId;
    private String appKey;
    private String mUserId;
    private String mRewardId;
    private String mRewardUnitId;
    private String mPlacementId;
    private boolean isMute = false;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private MediationAdapterRewardListener mMediationAdapterRewardListener;

    @Override
    public void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if (activity == null || activity.isFinishing()) {
            Log.e(TAG, "Activity is null or finishing.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        if (localExtras == null || localExtras.isEmpty()) {
            Log.e(TAG, "Local parameters cannot be null.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        appId = (String) localExtras.get(CommonConst.KEY_APP_ID);
        appKey = (String) localExtras.get(CommonConst.KEY_APP_KEY);
        mUserId = (String) localExtras.get(CommonConst.KEY_USER_ID);
        mRewardId = (String) localExtras.get(CommonConst.KEY_REWARD_ID);
        mRewardUnitId = (String)localExtras.get(CommonConst.KEY_REWARD_UNIT_ID);
        mPlacementId = (String)localExtras.get(CommonConst.KEY_PLACEMENT_ID);

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(mRewardId) || TextUtils.isEmpty(mRewardUnitId)) {
            Log.e(TAG, "appId/appKey/rewarId/rewardUnitId cannot be null.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);
        sdk.init(map, activity);

        mMTGRewardVideoHandler = new MTGRewardVideoHandler(activity,mPlacementId, mRewardUnitId);
        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitSucceed();
        }
    }

    @Override
    public void setSDKRewardListener(MediationAdapterRewardListener mediationAdapterRewardListener) {
        mMediationAdapterRewardListener = mediationAdapterRewardListener;
    }

    @Override
    public void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        if (mediationAdapterInitListener == null) {
            Log.e(TAG, "MediationAdapterInitListener cannot be null.");
            return;
        }

        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    @Override
    public void show(String param) {
        if (mMTGRewardVideoHandler != null && mMTGRewardVideoHandler.isReady()) {
            if (TextUtils.isEmpty(mUserId)) {
                mMTGRewardVideoHandler.show(mRewardId);
            } else {
                mMTGRewardVideoHandler.show(mRewardId, mUserId);
            }
        } else {
            Log.e(TAG, "MTG RewardVideo not ready.");
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.loadFailed("MTG RewardVideo not ready.");
            }
        }
    }

    @Override
    public void load(String param) {
        mMTGRewardVideoHandler.setRewardVideoListener(mRewardVideoListener);
        mMTGRewardVideoHandler.load();
        mMTGRewardVideoHandler.playVideoMute(isMute ? MIntegralConstans.REWARD_VIDEO_PLAY_MUTE : MIntegralConstans.REWARD_VIDEO_PLAY_NOT_MUTE);
    }

    @Override
    public boolean isReady(String param) {
        return mMTGRewardVideoHandler.isReady();
    }

    @Override
    public LifecycleListener getLifecycleListener() {
        return null;
    }

    private RewardVideoListener mRewardVideoListener = new RewardVideoListener() {

        @Override
        public void onVideoLoadSuccess(String s, String s1) {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.loadSucceed();
            }
        }

        @Override
        public void onLoadSuccess(String s, String s1) {

        }

        @Override
        public void onVideoLoadFail(String s) {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.loadFailed(s);
            }
        }

        @Override
        public void onAdShow() {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.showSucceed();
            }
        }

        @Override
        public void onAdClose(boolean isCompleted, String s, float v) {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.closed();
                if (isCompleted) {
                    mMediationAdapterRewardListener.rewarded(s, Float.valueOf(v).intValue());
                }
            }
        }

        @Override
        public void onShowFail(String s) {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.showFailed(s);
            }
        }

        @Override
        public void onVideoAdClicked(String s, String s1) {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.clicked(s);
            }
        }

        @Override
        public void onVideoComplete(String s, String s1) {

        }

        @Override
        public void onEndcardShow(String s, String s1) {

        }

    };
}
