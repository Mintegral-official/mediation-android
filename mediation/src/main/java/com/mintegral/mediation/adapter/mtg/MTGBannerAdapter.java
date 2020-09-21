package com.mintegral.mediation.adapter.mtg;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.adapter.BaseBannerAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterBannerListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.BannerAdListener;
import com.mintegral.msdk.out.BannerSize;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MTGBannerView;

import java.util.Map;

public class MTGBannerAdapter extends BaseBannerAdapter {

    private String TAG = getClass().getSimpleName();
    private String mAppId;
    private String mAppKey;
    private String mUnitId;
    private String mPlacementId;
    private int mBannerSizeType = 1;
    private int mBannerSizeH,mBannerSizeW;
    private int mBannerRefreshTime;
    private MediationAdapterBannerListener mMediationAdapterBannerListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private MTGBannerView mtgBannerView;
    private boolean isReady = false;

    private BannerAdListener bannerAdListener = new BannerAdListener() {
        @Override
        public void onLoadFailed(String s) {
            isReady = false;
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.loadFailed(s);
            }
        }

        @Override
        public void onLoadSuccessed() {
            isReady = true;
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.loadSucceed();
            }
        }

        @Override
        public void onLogImpression() {
            isReady = false;
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.showSucceed();
            }
        }

        @Override
        public void onClick() {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.clicked("");
            }
        }

        @Override
        public void onLeaveApp() {

        }

        @Override
        public void showFullScreen() {

        }

        @Override
        public void closeFullScreen() {

        }

        @Override
        public void onCloseBanner() {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.closed();
            }
        }
    };



    @Override
    public void setSDKBannerListener(MediationAdapterBannerListener mediationAdapterBannerListener) {
        mMediationAdapterBannerListener = mediationAdapterBannerListener;
    }

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

        mAppId = (String) localExtras.get(CommonConst.KEY_APP_ID);
        mAppKey = (String) localExtras.get(CommonConst.KEY_APP_KEY);
        mPlacementId = (String) localExtras.get(CommonConst.KEY_PLACEMENT_ID);
        mUnitId = (String) localExtras.get(CommonConst.KEY_BANNER_UNIT_ID);
        if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mAppKey) || TextUtils.isEmpty(mUnitId)) {
            Log.e(TAG, "appId/appKey/mUnitId cannot be null.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        Map<String, String> map = sdk.getMTGConfigurationMap(mAppId, mAppKey);
        sdk.init(map, activity);

        Object refreshTimeObj = localExtras.get(CommonConst.KEY_BANNER_REFRESH_TIME);
        if(refreshTimeObj instanceof Integer){
            mBannerRefreshTime = (int) refreshTimeObj;
        }
        Object typeObj = localExtras.get(CommonConst.KEY_BANNER_SIZE_TYPE);
        if(typeObj instanceof Integer){
            mBannerSizeType = (Integer) typeObj;
        }
        if (mBannerSizeType == BannerSize.DEV_SET_TYPE) {
            Object hObj =  localExtras.get(CommonConst.KEY_BANNER_SIZE_H);
            if (hObj instanceof Integer) {
                mBannerSizeH = (Integer)hObj;
            }
            Object wObj = localExtras.get(CommonConst.KEY_BANNER_SIZE_W);
            if (wObj instanceof Integer) {
                mBannerSizeW = (Integer) wObj;
            }
        }


        mtgBannerView = new MTGBannerView(activity);
        mtgBannerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mtgBannerView.init(new BannerSize(mBannerSizeType,mBannerSizeW,mBannerSizeH),mPlacementId,mUnitId);
        mtgBannerView.setRefreshTime(mBannerRefreshTime);
        mtgBannerView.setBannerAdListener(bannerAdListener);
        Object allowSkip = localExtras.get(CommonConst.KEY_BANNER_ALLOW_SKIP);
        if(allowSkip instanceof Boolean){
            mtgBannerView.setAllowShowCloseBtn((boolean)allowSkip);
        }
        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitSucceed();
        }
    }

    @Override
    public void load(String param) {
        if(mtgBannerView != null){
            mtgBannerView.load();
        }else {
            if(bannerAdListener != null){
                bannerAdListener.onLoadFailed("mtgBannerView is null");
            }
        }
    }

    @Override
    public void showBanner(ViewGroup bannerContainer) {
        if(bannerContainer != null && isReady){
            bannerContainer.removeAllViews();
            bannerContainer.addView(mtgBannerView);
        }else {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.showFailed("bannerContainer is null or banner not ready");
            }
        }
    }


    /**
     * 无法传入view，在banner中不用
     * @param param 无效
     */
    @Deprecated
    @Override
    public void show(String param) {
    }

    @Override
    public boolean isReady(String param) {
        return isReady;
    }

    @Override
    public void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    @Override
    public LifecycleListener getLifecycleListener() {
        return null;
    }
}
