package com.mintegral.mediation.out.manager;

import android.app.Activity;
import android.os.Handler;

import android.os.Looper;
import android.os.Message;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.adapter.BaseRewardAdapter;
import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;
import com.mintegral.mediation.out.interceptor.DefaultRewardInterceptor;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author songjunjun
 */
public class MediationRewardManager {

    private BaseInterceptor mInterceptor = new DefaultRewardInterceptor();
    private LinkedList<AdSource> adSources;
    private MediationAdapterRewardListener mMediationAdapterRewardListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private BaseRewardAdapter rewardAdapter;
    private Map<String,Object> mLocalParams;
    private Map<String,String> mServiceParams;
    private WeakReference<Activity> activityWeakReference;
    private String mMediationUnitId;
    private AdSource currentAdSource;
    private boolean loadHadResult = false;
    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!loadHadResult) {
                        loadTimeout();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 初始化sdk
     * @param activity
     * @param mediationUnitId
     * @param localParams
     */
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
        if(adSources == null || adSources.size() == 0){
            if(mMediationAdapterInitListener != null){
                mMediationAdapterInitListener.onInitFailed();
                mMediationAdapterInitListener = null;
                return;
            }
        }
        rewardAdapter = loopNextAdapter(activity,mediationUnitId,localParams,null);

    }


    private void initAdapter(final Activity activity, final String mediationUnitId, final Map<String,Object> localParams, final Map<String,String> serviceParams, BaseRewardAdapter rewardAdapter){
        if(rewardAdapter != null){
            rewardAdapter.setSDKInitListener(new MediationAdapterInitListener() {
                @Override
                public void onInitSucceed() {
                    if(mMediationAdapterInitListener != null){
                        mMediationAdapterInitListener.onInitSucceed();
                        mMediationAdapterInitListener = null;
                    }
                }

                @Override
                public void onInitFailed() {
                    if( loopNextAdapter(activity,mediationUnitId,localParams,serviceParams) == null){
                        if(mMediationAdapterInitListener != null){
                            mMediationAdapterInitListener.onInitFailed();
                            mMediationAdapterInitListener = null;
                        }
                    }
                }
            });
            rewardAdapter.init(activity,mediationUnitId,localParams,serviceParams);
        }else{
            if(mMediationAdapterInitListener != null){
                mMediationAdapterInitListener.onInitFailed();
                mMediationAdapterInitListener = null;
            }
        }
    }

    private BaseRewardAdapter loopNextAdapter( Activity activity, String mediationUnitId, Map<String,Object> localParams, Map<String,String> serviceParams){
        rewardAdapter = null;
        while (adSources != null && adSources.size() > 0){
            AdSource adSource = adSources.poll();
            if(adSource != null){
                currentAdSource = adSource;
                rewardAdapter = newInstanceCurrentAdapter(adSource);
                //初始化当前的adapter
                initAdapter(activity,mediationUnitId,adSource.getLocalParams(),adSource.getServiceParams(),rewardAdapter);
                //如果已经有adapter了，就跳出
                if(rewardAdapter != null){
                    setAdapterRewardListener();
                    break;
                }

            }
        }
        return rewardAdapter;
    }

    private BaseRewardAdapter newInstanceCurrentAdapter(AdSource adSource){
        BaseRewardAdapter adapter = null;
        if(adSource != null){
            String clzStr = adSource.getTargetClass();
            try {
                Class clz = Class.forName(clzStr);
                Object object= clz.newInstance();
                if(object instanceof BaseRewardAdapter){
                    adapter = (BaseRewardAdapter)object;
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
    public void setInterceptor(BaseInterceptor interceptor){
        if (interceptor != null) {
            mInterceptor = interceptor;
        }
    }

    /**
     * 设置激励视频的监听回调
     * @param mediationAdapterRewardListener
     */
    public void setMediationAdapterRewardListener(final MediationAdapterRewardListener mediationAdapterRewardListener){
        mMediationAdapterRewardListener = mediationAdapterRewardListener;
        setAdapterRewardListener();
    }

    private void setAdapterRewardListener(){
        if(rewardAdapter != null){
            rewardAdapter.setSDKRewardListener(new MediationAdapterRewardListener() {
                @Override
                public void loadSucceed() {
                    if(handler != null){
                        handler.removeMessages(1);
                    }
                    if(mMediationAdapterRewardListener != null && !loadHadResult){
                        mMediationAdapterRewardListener.loadSucceed();
                        loadHadResult =true;

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
                    if(mMediationAdapterRewardListener != null){
                        mMediationAdapterRewardListener.showSucceed();
                    }
                }

                @Override
                public void showFailed(String msg) {
                    showFailedToUser(msg);
                }

                @Override
                public void clicked(String msg) {
                    if(mMediationAdapterRewardListener != null){
                        mMediationAdapterRewardListener.clicked(msg);
                    }
                }

                @Override
                public void closed() {
                    if(mMediationAdapterRewardListener != null){
                        mMediationAdapterRewardListener.closed();
                    }
                }

                @Override
                public void rewarded(String name, int amount) {
                    if(mMediationAdapterRewardListener != null){
                        mMediationAdapterRewardListener.rewarded(name, amount);
                    }
                }
            });
        }
    }

    /**
     * 设置初始化的成功失败监听
     * @param mediationAdapterInitListener
     */
    public void setMediationAdapterInitListener(MediationAdapterInitListener mediationAdapterInitListener){
        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    /**
     * load方法，用于加载数据
     */
    public void load(){

        loadHadResult = false;
        if(activityWeakReference == null || activityWeakReference.get() == null){
            loadFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);
            return;
        }
        if(rewardAdapter != null ){
            loadAndSetTimeOut();
        }else{
            if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId,mLocalParams,mServiceParams))!= null){
                loadAndSetTimeOut();
            }else{
                loadFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_INVALID);
            }
        }
    }

    /**
     * 展示广告
     */
    public void show(){
        if(activityWeakReference == null || activityWeakReference.get() == null){
            showFailedToUser(MediationMTGErrorCode.ACTIVITY_IS_NULL);
            return;
        }
        if(rewardAdapter != null ){
            rewardAdapter.show();
        }else{
            showFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_INVALID);
        }
    }


    /**
     * 是否满足展示条件
     * @return
     */
    public boolean isReady(){
        if(activityWeakReference == null || activityWeakReference.get() == null){
            return false;
        }
        if(rewardAdapter != null ){
            return rewardAdapter.isReady();
        }
        return false;
    }


    /**
     * 获取生命周期监听，在对应的生命周期调用
     * @return
     */
    public LifecycleListener getLifecycleListener(){
        if(rewardAdapter != null){
            return rewardAdapter.getLifecycleListener();
        }
        return null;
    }
    private void loadFailedToUser(String msg){
        if(mMediationAdapterRewardListener != null && !loadHadResult){
            mMediationAdapterRewardListener.loadFailed(msg);
            loadHadResult =true;
        }
    }

    private void showFailedToUser(String msg){
        if(mMediationAdapterRewardListener != null){
            mMediationAdapterRewardListener.showFailed(msg);
        }
    }



    private void loadTimeout(){
        //清空上个adapter的监听
        if(rewardAdapter != null){
            rewardAdapter.setSDKRewardListener(null);
        }

        if((loopNextAdapter(activityWeakReference.get(),mMediationUnitId,mLocalParams,mServiceParams))!= null){
            loadAndSetTimeOut();
        }else{
            loadFailedToUser(MediationMTGErrorCode.ADSOURCE_IS_TIMEOUT);
        }

    }

    private void loadAndSetTimeOut(){
        if (rewardAdapter != null) {
            rewardAdapter.load();
            if (currentAdSource != null) {
                handler.sendEmptyMessageDelayed(1,currentAdSource.getTimeOut());
            }
        }
    }

}
