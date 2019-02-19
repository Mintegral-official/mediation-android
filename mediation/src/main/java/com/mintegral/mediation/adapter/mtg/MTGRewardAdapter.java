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
 * 适用于mtg reward ad显示的adapter
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
    private boolean isMute = false;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private MediationAdapterRewardListener mMediationAdapterRewardListener;

    @Override
    public void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if (activity == null || activity.isFinishing()) {
            Log.e(TAG, "Activity is null or finishing.");
            return;
        }

        if (localExtras == null || localExtras.isEmpty()) {
            Log.e(TAG, "Local parameters cannot be null.");
            return;
        }

        appId = (String) localExtras.get(CommonConst.KEY_APPID);
        appKey = (String) localExtras.get(CommonConst.KEY_APPKEY);
        mUserId = (String) localExtras.get(CommonConst.KEY_USERID);
        mRewardId = (String) localExtras.get(CommonConst.KEY_REWARDID);
        mRewardUnitId = (String)localExtras.get(CommonConst.KEY_REWARDUNITID);

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(mUserId) || TextUtils.isEmpty(mRewardId) || TextUtils.isEmpty(mRewardUnitId)) {
            Log.e(TAG, "appId/appKey/userId/rewarId/rewardUnitId cannot be null.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        MIntegralConstans.DEBUG = true;
        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);
        sdk.init(map, activity.getApplication());

        mMTGRewardVideoHandler = new MTGRewardVideoHandler(activity, mRewardUnitId);
        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitSucceed();
        }
    }

    @Override
    public void setSDKRewardListener(MediationAdapterRewardListener mediationAdapterRewardListener) {
        if (mediationAdapterRewardListener == null) {
            Log.e(TAG, "MediationAdapterRewardListener cannot be null.");
            return;
        }
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
    public void show() {
        if (mMTGRewardVideoHandler != null && mMTGRewardVideoHandler.isReady()) {
            mMTGRewardVideoHandler.show(mRewardId, mUserId);
        } else {
            Log.e(TAG, "MTG RewardVideo not ready.");
        }
    }

    @Override
    public void load(Activity activity, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        mMTGRewardVideoHandler.setRewardVideoListener(mRewardVideoListener);
        mMTGRewardVideoHandler.load();
        mMTGRewardVideoHandler.playVideoMute(isMute ? MIntegralConstans.REWARD_VIDEO_PLAY_MUTE : MIntegralConstans.REWARD_VIDEO_PLAY_NOT_MUTE);
    }

    @Override
    public boolean isReady() {
        return mMTGRewardVideoHandler.isReady();
    }

    @Override
    public LifecycleListener getLifecycleListener() {
        return null;
    }

    private RewardVideoListener mRewardVideoListener = new RewardVideoListener() {
        @Override
        public void onLoadSuccess(String s) {}

        @Override
        public void onVideoLoadSuccess(String s) {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.loadSucceed();
            }
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
        public void onVideoAdClicked(String s) {
            if (mMediationAdapterRewardListener != null) {
                mMediationAdapterRewardListener.clicked(s);
            }
        }
    };
}
