package com.mintegral.mediation.common.manager;

import android.app.Activity;
import android.os.Handler;
import android.view.ViewGroup;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.adapter.BaseBannerAdapter;
import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterBannerListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.utils.TimerHandler;
import com.mintegral.mediation.out.interceptor.DefaultBannerInterceptor;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author songjunjun
 */
public class MediationBannerManager extends BaseManager{

    private BaseInterceptor mInterceptor = new DefaultBannerInterceptor();
    private LinkedList<AdSource> adSources;
    private MediationAdapterBannerListener mMediationAdapterBannerListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private BaseBannerAdapter bannerAdapter;
    private WeakReference<Activity> activityWeakReference;
    private String mMediationUnitId;
    private AdSource currentAdSource;
    private boolean loadHadResult = false;
    private  Handler handler = new TimerHandler(this) ;
    private String currentLoadParam;
    private int index;

    @Override
    public String getCurrentLoadParam() {
        return currentLoadParam;
    }

    private MediationAdapterBannerListener mediationAdapterBannerListener = new MediationAdapterBannerListener() {
        @Override
        public void loadSucceed() {
            if(handler != null){
                handler.removeMessages(1);
            }
            if(mMediationAdapterBannerListener != null && !loadHadResult){
                mMediationAdapterBannerListener.loadSucceed();
                loadHadResult =true;

            }
        }

        @Override
        public void loadFailed(String msg) {
            bannerAdapter.setSDKBannerListener(null);
            if(handler != null){
                handler.removeMessages(1);
            }
            if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId))!= null){
                loadAndSetTimeOut(currentLoadParam);
            }else{
                loadFailedToUser(msg);
            }
        }

        @Override
        public void showSucceed() {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.showSucceed();
            }
            index = 0;
            loopNextAdapter(activityWeakReference.get(),mMediationUnitId);
        }

        @Override
        public void showFailed(String msg) {
            showFailedToUser(msg);
        }

        @Override
        public void clicked(String msg) {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.clicked(msg);
            }
        }

        @Override
        public void closed() {
            if(mMediationAdapterBannerListener != null){
                mMediationAdapterBannerListener.closed();
            }
        }

    };

    /**
     * 初始化sdk
     * @param activity
     * @param mediationUnitId
     * @param localParams
     */
    @Override
    public void init(Activity activity, String mediationUnitId, Map<String,Object> localParams){
        activityWeakReference = new WeakReference<Activity>(activity);
        mMediationUnitId = mediationUnitId;
        requestServiceSetting(activity,mMediationUnitId,localParams);
    }

    private void requestServiceSetting(Activity activity,String mediationUnitId,Map<String,Object> localParams){
        //TODO:当前版本不支持服务端下发配置
        if(mInterceptor != null){
            adSources = mInterceptor.onInterceptor(mediationUnitId,localParams,"");
        }
        if(adSources == null || adSources.size() == 0){
            callInitListener(false);
            return;
        }
        index = 0;
        bannerAdapter = loopNextAdapter(activity,mediationUnitId);

    }


    private void initAdapter(final Activity activity, final String mediationUnitId, final Map<String,Object> localParams, final Map<String,String> serviceParams, BaseBannerAdapter bannerAdapter){
        if(bannerAdapter != null){
            bannerAdapter.setSDKInitListener(new MediationAdapterInitListener() {
                @Override
                public void onInitSucceed() {
                    callInitListener(true);
                }

                @Override
                public void onInitFailed() {
                    //失败了，就看下一个优先级
                    if(loopNextAdapter(activity,mediationUnitId) == null){
                        callInitListener(false);
                    }
                }
            });
            bannerAdapter.init(activity,mediationUnitId,localParams,serviceParams);
        }else{
            callInitListener(false);
        }
    }

    private BaseBannerAdapter loopNextAdapter( Activity activity, String mediationUnitId){
        bannerAdapter = null;
        while (adSources != null && adSources.size() > 0 && index < adSources.size()){
            AdSource adSource = adSources.get(index);
            //再来获取的话，就是下一个优先级的
            index++;
            if(adSource != null){
                currentAdSource = adSource;
                bannerAdapter = newInstanceCurrentAdapter(adSource);

                //初始化当前的adapter
                initAdapter(activity,mediationUnitId,adSource.getLocalParams(),adSource.getServiceParams(),bannerAdapter);
                //如果已经有adapter了，就跳出
                if(bannerAdapter != null){
                    setAdapterBannerListener();
                    break;
                }

            }
        }
        return bannerAdapter;
    }

    private BaseBannerAdapter newInstanceCurrentAdapter(AdSource adSource){
        BaseBannerAdapter adapter = null;
        if(adSource != null){
            String clzStr = adSource.getTargetClass();
            try {
                Class clz = Class.forName(clzStr);
                Object object= clz.newInstance();
                if(object instanceof BaseBannerAdapter){
                    adapter = (BaseBannerAdapter)object;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return adapter;
    }


    /**
     * 设置拦截器，如果不设置或设置为null，将使用默认
     * @param interceptor
     */
    @Override
    public void setInterceptor(BaseInterceptor interceptor){
        if (interceptor != null) {
            mInterceptor = interceptor;
        }
    }

    /**
     * 设置激励视频的监听回调
     * @param mediationAdapterBannerListener
     */
    public void setMediationAdapterBannerListener(final MediationAdapterBannerListener mediationAdapterBannerListener){
        mMediationAdapterBannerListener = mediationAdapterBannerListener;
        setAdapterBannerListener();
    }

    private void setAdapterBannerListener(){
        if(bannerAdapter != null){
            bannerAdapter.setSDKBannerListener(mediationAdapterBannerListener);
        }
    }

    /**
     * 设置初始化的成功失败监听
     * @param mediationAdapterInitListener
     */
    @Override
    public void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener){
        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    /**
     * load方法，用于加载数据
     */
    @Override
    public void load(String param){
        currentLoadParam = param;
        loadHadResult = false;
        if(activityWeakReference == null || activityWeakReference.get() == null){
            loadFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);
            return;
        }
        if(bannerAdapter != null ){
            loadAndSetTimeOut(param);
        }else{
            if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId))!= null){
                loadAndSetTimeOut(param);
            }else{
                loadFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_INVALID);
            }
        }
    }

    /**
     * 展示广告
     */
    @Deprecated
    @Override
    public void show(String param){

    }

    @Override
    public void showBanner(String param, ViewGroup container) {
        if(activityWeakReference == null || activityWeakReference.get() == null){
            showFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);
            return;
        }
        if(bannerAdapter != null ){
            bannerAdapter.showBanner(container);
        }else{
            showFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_INVALID);
        }
    }

    /**
     * 是否满足展示条件
     * @return if can show reture true, or false
     */
    @Override
    public boolean isReady(String param){
        if(activityWeakReference == null || activityWeakReference.get() == null){
            return false;
        }
        if(bannerAdapter != null ){
            return bannerAdapter.isReady(param);
        }
        return false;
    }


    /**
     * 获取生命周期监听，在对应的生命周期调用
     * @return LifecycleListener
     */
    @Override
    public LifecycleListener getLifecycleListener(){
        if(bannerAdapter != null){
            return bannerAdapter.getLifecycleListener();
        }
        return null;
    }
    private void loadFailedToUser(String msg){
        if(mMediationAdapterBannerListener != null && !loadHadResult){
            mMediationAdapterBannerListener.loadFailed(msg);
            loadHadResult =true;
        }
    }

    private void showFailedToUser(String msg){
        if(mMediationAdapterBannerListener != null){
            mMediationAdapterBannerListener.showFailed(msg);
        }
    }

    @Override
    public boolean isLoadHadResult() {
        return loadHadResult;
    }

    @Override
    public void loadTimeout(String param){
        //清空上个adapter的监听
        if(bannerAdapter != null){
            bannerAdapter.setSDKBannerListener(null);
        }
        if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId))!= null){
            loadAndSetTimeOut(param);
        }else{
            loadFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_TIMEOUT);
        }

    }

    private void loadAndSetTimeOut(String param){
        if (bannerAdapter != null) {
            bannerAdapter.load(param);
            if (currentAdSource != null) {
                handler.sendEmptyMessageDelayed(1,currentAdSource.getTimeOut());
            }
        }
    }

    @Override
    void callInitListener(boolean succeed) {
        if(mMediationAdapterInitListener != null){
            if(succeed){
                mMediationAdapterInitListener.onInitSucceed();
            }else {
                mMediationAdapterInitListener.onInitFailed();
            }
            mMediationAdapterInitListener = null;
        }
    }

}
