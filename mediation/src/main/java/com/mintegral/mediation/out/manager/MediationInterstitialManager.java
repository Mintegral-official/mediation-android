package com.mintegral.mediation.out.manager;

import android.app.Activity;

import android.os.Handler;
import android.os.Message;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.adapter.BaseInterstitialAdapter;
import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;

import com.mintegral.mediation.out.interceptor.DefaultInterstitialInterceptor;
import com.mintegral.mediation.out.interceptor.DefaultRewardInterceptor;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author songjunjun
 */
public class MediationInterstitialManager {

    private BaseInterceptor mInterceptor = new DefaultInterstitialInterceptor();
    private LinkedList<AdSource> adSources;
    private MediationAdapterInterstitialListener mMediationAdapterInterstitialListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private BaseInterstitialAdapter interstitialAdapter;
    private Map<String,Object> mLocalParams;
    private Map<String,String> mServiceParams;
    private WeakReference<Activity> activityWeakReference;
    private String mMediationUnitId;
    private AdSource currentAdSource;

    private boolean loadHadResult = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (!loadHadResult) {
                        loadTimeout();
                    }
                    break;
            }
        }
    };

    public void init(Activity activity, String mediationUnitId, Map<String,Object> localParams){
        activityWeakReference = new WeakReference<Activity>(activity);
        mMediationUnitId = mediationUnitId;
        requestServiceSetting(activity,mMediationUnitId,localParams);
    }

    private void requestServiceSetting(Activity activity,String mediationUnitId,Map<String,Object> localParams){
        mLocalParams = localParams;
        //TODO:当前版本不支持服务端下发配置
        mServiceParams = null;
        if(mInterceptor != null){
            adSources = mInterceptor.onInterceptor(mediationUnitId,localParams,"");
        }
        interstitialAdapter = loopNextAdapter(activity,mediationUnitId,localParams,null);

    }

    private BaseInterstitialAdapter loopNextAdapter( Activity activity, String mediationUnitId, Map<String,Object> localParams, Map<String,String> serviceParams){
        interstitialAdapter = null;
        while (adSources != null && adSources.size() > 0){
            AdSource adSource = adSources.poll();
            if(adSource != null){
                currentAdSource = adSource;
                interstitialAdapter = newInstanceCurrentAdapter(adSource);
                //初始化当前的adapter
                initAdapter(activity,mediationUnitId,localParams,serviceParams,interstitialAdapter);
                //如果已经有adapter了，就跳出
                if(interstitialAdapter != null){
                    setAdapterInterstitial();
                    break;
                }

            }
        }
        return interstitialAdapter;
    }

    private void setAdapterInterstitial(){
        if(interstitialAdapter != null){
            interstitialAdapter.setSDKInterstitial(new MediationAdapterInterstitialListener() {

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
                    if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId,mLocalParams,mServiceParams))!= null){

                        loadAndSetTimeOut();
                    }else{
                        loadFailedToUser(msg);
                    }
                }

                @Override
                public void showSucceed() {
                    if(mMediationAdapterInterstitialListener != null){
                        mMediationAdapterInterstitialListener.showSucceed();
                    }
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

            });
        }
    }

    private void initAdapter(final Activity activity, final String mediationUnitId, final Map<String,Object> localParams, final Map<String,String> serviceParams, BaseInterstitialAdapter rewardAdapter){
        if(rewardAdapter != null){
            rewardAdapter.setSDKInitListener(new MediationAdapterInitListener() {
                @Override
                public void onInitSucceed() {
                    if(mMediationAdapterInitListener != null){
                        mMediationAdapterInitListener.onInitSucceed();
                    }
                }

                @Override
                public void onInitFailed() {
                    if( loopNextAdapter(activity,mediationUnitId,localParams,serviceParams) == null){
                        if(mMediationAdapterInitListener != null){
                            mMediationAdapterInitListener.onInitFailed();
                        }
                    }
                }
            });
            rewardAdapter.init(activity,mediationUnitId,localParams,serviceParams);
        }else{
            if(mMediationAdapterInitListener != null){
                mMediationAdapterInitListener.onInitFailed();
            }
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

    public void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener){
        mMediationAdapterInitListener = mediationAdapterInitListener;
    }
    public void setMediationAdapterInterstitialListenerr(MediationAdapterInterstitialListener mediationAdapterInterstitialListenerr){
        mMediationAdapterInterstitialListener = mediationAdapterInterstitialListenerr;
        setAdapterInterstitial();
    }

    public void load(){

        loadHadResult = false;
        if(activityWeakReference == null || activityWeakReference.get() == null){
            loadFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);
            return;
        }
        if(interstitialAdapter != null ){
            loadAndSetTimeOut();
        }else{
            if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId,mLocalParams,mServiceParams))!= null){
                loadAndSetTimeOut();
            }else{
                loadFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_INVALID);
            }
        }
    }
    public boolean isReady(){
        if(activityWeakReference == null || activityWeakReference.get() == null){
            return false;
        }
        if(interstitialAdapter != null ){
            return interstitialAdapter.isReady();
        }
        return false;
    }

    public void show(){
        if(activityWeakReference == null || activityWeakReference.get() == null){
            showFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);

            return;
        }
        if(interstitialAdapter != null ){
            interstitialAdapter.show();
        }else{
            showFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_INVALID);
        }
    }

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


    private void loadTimeout(){
        //清空上个adapter的监听
        if(interstitialAdapter != null){
            interstitialAdapter.setSDKInterstitial(null);
        }

        if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId,mLocalParams,mServiceParams))!= null){
            loadAndSetTimeOut();
        }else{
            loadFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_TIMEOUT);
        }

    }

    private void loadAndSetTimeOut(){
        if (interstitialAdapter != null) {
            interstitialAdapter.load(activityWeakReference.get(),mLocalParams,mServiceParams);
            if (currentAdSource != null) {
                handler.sendEmptyMessageDelayed(1,currentAdSource.getTimeOut());
            }
        }
    }

}
