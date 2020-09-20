package com.mintegral.mediation.adapter.iron;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.adapter.BaseBannerAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterBannerListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.msdk.out.BannerSize;

import java.lang.ref.WeakReference;
import java.util.Map;

public class IronBannerAdapter extends BaseBannerAdapter {
    private MediationAdapterBannerListener mMediationAdapterBannerListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private int mBannerSizeType = 1;
    private int mBannerSizeH,mBannerSizeW;
    private IronSourceBannerLayout ironBannerView;
    private String mAppKey;
    private boolean isReady;
    private  ISBannerSize isBannerSize = ISBannerSize.BANNER;
    private WeakReference<Activity> weakReference ;
    private BannerListener bannerListener = new BannerListener() {
        @Override
        public void onBannerAdLoaded() {
            isReady = true;
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.loadSucceed();
            }
        }

        @Override
        public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
            isReady = false;
            if(mMediationAdapterBannerListener != null){
                String msg = ironSourceError != null?ironSourceError.getErrorMessage():"unknown";
                mMediationAdapterBannerListener.loadFailed(msg);
            }
        }

        @Override
        public void onBannerAdClicked() {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.clicked("IronSource");
            }
        }

        @Override
        public void onBannerAdScreenPresented() {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.showSucceed();
            }
        }

        @Override
        public void onBannerAdScreenDismissed() {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.closed();
            }
        }

        @Override
        public void onBannerAdLeftApplication() {

        }
    };

    @Override
    public void setSDKBannerListener(MediationAdapterBannerListener mediationAdapterBannerListener) {
        mMediationAdapterBannerListener = mediationAdapterBannerListener;
    }


    @Override
    public void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if(localExtras != null && activity != null){
            weakReference = new WeakReference<>(activity);
            Object ob = localExtras.get("local");
            if(ob instanceof String){
                //初始化reward video
                mAppKey = ob.toString();
                if (!TextUtils.isEmpty(mAppKey)) {
                    IronSource.init(activity, mAppKey, IronSource.AD_UNIT.BANNER);
                    if (mMediationAdapterInitListener != null) {
                        mMediationAdapterInitListener.onInitSucceed();
                    }
                }
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

            switch (mBannerSizeType){
                case 1:
                    isBannerSize = ISBannerSize.LARGE;
                    break;
                case 2:
                    isBannerSize = ISBannerSize.RECTANGLE;
                    break;
                case 3:
                    isBannerSize = ISBannerSize.SMART;
                    break;
                case 5:
                    isBannerSize = new ISBannerSize(mBannerSizeW,mBannerSizeH);
                    break;
                case 4:
                default:
                    isBannerSize = ISBannerSize.BANNER;
            }
            return;
        }

        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitFailed();
        }
    }

    @Override
    public void load(String param) {
        IronSource.destroyBanner(ironBannerView);
        if (weakReference != null && weakReference.get() != null) {
            ironBannerView = IronSource.createBanner(weakReference.get(), isBannerSize);
            ironBannerView.setBannerListener(bannerListener);
            IronSource.loadBanner(ironBannerView);
        }else {
            if(bannerListener != null){
                bannerListener.onBannerAdLoadFailed(null);
            }
        }
    }

    @Override
    public void showBanner(ViewGroup bannerContainer) {
        if(bannerContainer != null){
            bannerContainer.removeAllViews();
            // add IronSourceBanner to your container
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            ironBannerView.setVisibility(View.VISIBLE);
            bannerContainer.addView(ironBannerView,0,layoutParams);
        }
    }

    /**
     * banner需要外界传一个view进来，该方法无法实现，在banner中废弃掉
     * @param param 参数
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
