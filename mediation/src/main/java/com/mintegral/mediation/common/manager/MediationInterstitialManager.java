package com.mintegral.mediation.common.manager;

import android.app.Activity;

import android.os.Handler;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.adapter.BaseInterstitialAdapter;
import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;

import com.mintegral.mediation.common.utils.TimerHandler;
import com.mintegral.mediation.out.interceptor.DefaultInterstitialInterceptor;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author songjunjun
 */
public class MediationInterstitialManager extends BaseManager{

    private BaseInterceptor mInterceptor = new DefaultInterstitialInterceptor();
    private LinkedList<AdSource> adSources;
    /**
     * 外部传入的监听，用于对外回调数据
     */
    private MediationAdapterInterstitialListener mMediationAdapterInterstitialListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private BaseInterstitialAdapter interstitialAdapter;
    private WeakReference<Activity> activityWeakReference;
    private String mMediationUnitId;
    private AdSource currentAdSource;
    private String currentLoadParam;
    private int index;
    /**
     * 内部使用，在从adapter返回的时候，可以做一些操作
     */
    private MediationAdapterInterstitialListener mediationAdapterInterstitialListener = new MediationAdapterInterstitialListener() {

        @Override
        public void loadSucceed() {

            if(handler != null){
                handler.removeMessages(1);
            }
            if(mMediationAdapterInterstitialListener != null && !loadHadResult){
                mMediationAdapterInterstitialListener.loadSucceed();
                loadHadResult = true;
            }
        }

        @Override
        public void loadFailed(String msg) {
            if(handler != null){
                handler.removeMessages(1);
            }
            interstitialAdapter.setSDKInterstitial(null);
            if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId))!= null){
                loadAndSetTimeOut(currentLoadParam);
            }else{
                loadFailedToUser(msg);
            }
        }

        @Override
        public void showSucceed() {
            if(mMediationAdapterInterstitialListener != null){
                mMediationAdapterInterstitialListener.showSucceed();
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
            if(mMediationAdapterInterstitialListener != null){
                mMediationAdapterInterstitialListener.clicked(msg);
            }
        }

        @Override
        public void closed() {
            if(mMediationAdapterInterstitialListener != null){
                mMediationAdapterInterstitialListener.closed();
            }
        }

    };

    @Override
    public String getCurrentLoadParam() {
        return currentLoadParam;
    }

    private boolean loadHadResult = false;
    private Handler handler = new TimerHandler(this);


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
        interstitialAdapter = loopNextAdapter(activity,mediationUnitId);

    }

    private BaseInterstitialAdapter loopNextAdapter( Activity activity, String mediationUnitId){
        interstitialAdapter = null;
        while (adSources != null && adSources.size() > 0&& index < adSources.size()){
            AdSource adSource = adSources.get(index);
            index++;
            if(adSource != null){
                currentAdSource = adSource;
                interstitialAdapter = newInstanceCurrentAdapter(adSource);
                //初始化当前的adapter
                initAdapter(activity,mediationUnitId,adSource.getLocalParams(),adSource.getServiceParams(),interstitialAdapter);
                //如果已经有adapter了，就跳出
                if(interstitialAdapter != null){
                    setAdapterInterstitial();
                    break;
                }

            }
        }
        return interstitialAdapter;
    }

    /**
     * 对adapter设置监听
     */
    private void setAdapterInterstitial(){
        if(interstitialAdapter != null){
            interstitialAdapter.setSDKInterstitial(mediationAdapterInterstitialListener);
        }
    }

    private void initAdapter(final Activity activity, final String mediationUnitId, final Map<String,Object> localParams, final Map<String,String> serviceParams, BaseInterstitialAdapter interstitialAdapter){
        if(interstitialAdapter != null){
            interstitialAdapter.setSDKInitListener(new MediationAdapterInitListener() {
                @Override
                public void onInitSucceed() {
                    callInitListener(true);
                }

                @Override
                public void onInitFailed() {
                    if( loopNextAdapter(activity,mediationUnitId) == null){
                        callInitListener(false);
                    }
                }
            });
            interstitialAdapter.init(activity,mediationUnitId,localParams,serviceParams);
        }else{
            callInitListener(false);
        }
    }

    private BaseInterstitialAdapter newInstanceCurrentAdapter(AdSource adSource){
        BaseInterstitialAdapter adapter = null;
        if(adSource != null){
            String clzStr = adSource.getTargetClass();
            try {
                Class clz = Class.forName(clzStr);
                Object object= clz.newInstance();
                if(object instanceof BaseInterstitialAdapter){
                    adapter = (BaseInterstitialAdapter)object;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return adapter;
    }

    /**
     * 设置初始化监听
     * @param mediationAdapterInitListener
     */
    @Override
    public void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener){
        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    /**
     * 设置Interstitial相关监听
     * @param mediationAdapterInterstitialListener
     */
    public void setMediationAdapterInterstitialListener(MediationAdapterInterstitialListener mediationAdapterInterstitialListener){
        mMediationAdapterInterstitialListener = mediationAdapterInterstitialListener;
        setAdapterInterstitial();
    }

    /**
     * 加载数据
     */
    @Override
    public void load(String param){
        loadHadResult = false;
        currentLoadParam=param;
        if(activityWeakReference == null || activityWeakReference.get() == null){
            loadFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);
            return;
        }
        if(interstitialAdapter != null ){
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
     * 判断是否可以展示
     * @return if can show reture true, or false
     */
    @Override
    public boolean isReady(String param){
        if(activityWeakReference == null || activityWeakReference.get() == null){
            return false;
        }
        if(interstitialAdapter != null ){
            return interstitialAdapter.isReady(param);
        }
        return false;
    }

    /**
     * 展示广告
     */
    @Override
    public void show(String param){
        if(activityWeakReference == null || activityWeakReference.get() == null){
            showFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);
            return;
        }
        if(interstitialAdapter != null ){
            setAdapterInterstitial();
            interstitialAdapter.show(param);
        }else{
            showFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_INVALID);
        }
    }

    /**
     * 获取activity的生命周期的监听
     * @return LifecycleListener
     */
    @Override
    public LifecycleListener getLifecycleListener(){
        if(interstitialAdapter != null){
            return interstitialAdapter.getLifecycleListener();
        }
        return null;
    }
    private void loadFailedToUser(String msg){
        if(mMediationAdapterInterstitialListener != null && !loadHadResult){
            mMediationAdapterInterstitialListener.loadFailed(msg);
            loadHadResult = true;
        }
    }

    private void showFailedToUser(String msg){
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.showFailed(msg);
        }
    }

    @Override
    public boolean isLoadHadResult() {
        return loadHadResult;
    }

    @Override
    public void loadTimeout(String param){
        //清空上个adapter的监听
        if(interstitialAdapter != null){
            interstitialAdapter.setSDKInterstitial(null);
        }

        if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId))!= null){
            loadAndSetTimeOut(param);
        }else{
            loadFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_TIMEOUT);
        }

    }

    private void loadAndSetTimeOut(String param){
        if (interstitialAdapter != null) {
            setAdapterInterstitial();
            interstitialAdapter.load(param);
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
