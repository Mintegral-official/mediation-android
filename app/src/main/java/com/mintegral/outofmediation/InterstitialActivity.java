package com.mintegral.outofmediation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;
import com.mintegral.mediation.out.manager.MediaTionRewardManager;
import com.mintegral.mediation.out.manager.MediationInterstitialManager;

import java.util.HashMap;
import java.util.Map;

public class InterstitialActivity extends Activity implements View.OnClickListener {

    private Button showBtn,loadBtn,isReadyBtn;
    private MediationInterstitialManager manager;
    private LifecycleListener lifecycleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        initView();
        initListener();
        initManager();
    }

    private void initManager(){
        manager = new MediationInterstitialManager();
        manager.setMediationAdapterInitListener(new MediationAdapterInitListener() {
            @Override
            public void onInitSucceed() {
                Log.e("00","onInitSucceed");
            }

            @Override
            public void onInitFailed() {
                Log.e("00","onInitFailed");
            }
        });
        Map<String,Object> map = new HashMap<>();
        map.put("local","88f1a7f5");
        map.put("targetClass","com.mintegral.mediation.adapter.iron.IronInterstitialAdapter");

        /*map.put(CommonConst.KEY_APPID, "92762");
        map.put(CommonConst.KEY_APPKEY, "936dcbdd57fe235fd7cf61c2e93da3c4");
        map.put(CommonConst.KEY_INTERSTITIALUNITID, "35811");
        map.put(CommonConst.KEY_MUTE, false);
        map.put("targetClass", "com.mintegral.mediation.adapter.mtg.MTGInterstitialAdapter");*/

        manager.setMediationAdapterInterstitialListenerr(new MediationAdapterInterstitialListener() {
            @Override
            public void loadSucceed() {
                Log.e("----------","loadSucceed") ;
            }

            @Override
            public void loadFailed(String msg) {
                Log.e("----------","loadFailed:"+msg) ;
            }

            @Override
            public void showSucceed() {
                Log.e("----------","showSucceed:") ;
            }

            @Override
            public void showFailed(String msg) {
                Log.e("----------","showFailed:"+msg) ;
            }

            @Override
            public void clicked(String msg) {
                Log.e("----------","clicked:"+msg) ;
            }

            @Override
            public void closed() {
                Log.e("----------","closed:") ;
            }


        });
        manager.init(this,"9999",map);
        lifecycleListener = manager.getLifecycleListener();
    }
    private void initView(){
        showBtn = findViewById(R.id.reward_show);
        loadBtn = findViewById(R.id.reward_load);
        isReadyBtn = findViewById(R.id.reward_is_ready);
    }
    private void initListener(){
        setViewListener(showBtn);
        setViewListener(loadBtn);
        setViewListener(isReadyBtn);
    }

    private void setViewListener(View view){
        if(view != null){
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reward_load:
                manager.load();
                break;
            case R.id.reward_show:
                manager.show();
                break;
            case R.id.reward_is_ready:
               Log.e("----------","ready:"+manager.isReady()) ;
                break;
            default:
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(lifecycleListener != null){
            lifecycleListener.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lifecycleListener != null){
            lifecycleListener.onResume(this);
        }
    }
}
