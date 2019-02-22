package com.mintegral.outofmediation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;
import com.mintegral.mediation.out.handler.MediationInterstitialHandler;


import java.util.HashMap;
import java.util.Map;

public class InterstitialActivity extends Activity implements View.OnClickListener {

    private Button showBtn,loadBtn,isReadyBtn,initBtn;
    private MediationInterstitialHandler mediationInterstitialHandler;
    private LifecycleListener lifecycleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        initView();
        initListener();

    }

    private void initManager(){
        mediationInterstitialHandler = new MediationInterstitialHandler();
        mediationInterstitialHandler.setMediationAdapterInitListener(new MediationAdapterInitListener() {
            @Override
            public void onInitSucceed() {
                Toast.makeText(InterstitialActivity.this,"onInitSucceed",Toast.LENGTH_LONG).show();
                Log.e("interstitial","onInitSucceed");
            }

            @Override
            public void onInitFailed() {
                Toast.makeText(InterstitialActivity.this,"onInitFailed",Toast.LENGTH_LONG).show();
                Log.e("interstitial","onInitFailed");
            }
        });

        Map<String,Object> paramsMap = new HashMap<>();
        //IronSource
        AdSource adSource = new AdSource();
        Map<String,Object> ironsourceMap = new HashMap<>();
        ironsourceMap.put("local","88f1a7f5");
        adSource.setLocalParams(ironsourceMap);
        adSource.setTargetClass("com.mintegral.mediation.adapter.iron.IronInterstitialAdapter");
        adSource.setTimeOut(10000);
        paramsMap.put("2",adSource);

        //MTG
        Map<String,Object> map = new HashMap<>();
        AdSource mtgAdSource = new AdSource();
        map.put(CommonConst.KEY_APPID, "92762");
        map.put(CommonConst.KEY_APPKEY, "936dcbdd57fe235fd7cf61c2e93da3c4");
        map.put(CommonConst.KEY_INTERSTITIALUNITID, "35811");
        map.put(CommonConst.KEY_MUTE, false);
        mtgAdSource.setLocalParams(map);
        mtgAdSource.setTargetClass("com.mintegral.mediation.adapter.mtg.MTGInterstitialAdapter");
        mtgAdSource.setTimeOut(10000);
        paramsMap.put("1",mtgAdSource);


        mediationInterstitialHandler.setMediationAdapterInterstitialListener(new MediationAdapterInterstitialListener() {
            @Override
            public void loadSucceed() {
                Toast.makeText(InterstitialActivity.this,"loadSucceed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void loadFailed(String msg) {
                Toast.makeText(InterstitialActivity.this,"loadFailed:"+msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void showSucceed() {
                Toast.makeText(InterstitialActivity.this,"showSucceed:",Toast.LENGTH_LONG).show();
            }

            @Override
            public void showFailed(String msg) {
                Toast.makeText(InterstitialActivity.this,"showFailed:"+msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void clicked(String msg) {
                Toast.makeText(InterstitialActivity.this,"clicked:"+msg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void closed() {
                Toast.makeText(InterstitialActivity.this,"closed:",Toast.LENGTH_LONG).show();
            }

        });
        mediationInterstitialHandler.init(this,paramsMap);
        lifecycleListener = mediationInterstitialHandler.getLifecycleListener();
    }
    private void initView(){
        initBtn = findViewById(R.id.reward_init);
        showBtn = findViewById(R.id.reward_show);
        loadBtn = findViewById(R.id.reward_load);
        isReadyBtn = findViewById(R.id.reward_is_ready);
    }
    private void initListener(){
        setViewListener(initBtn);
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
            case R.id.reward_init:
                initManager();
                break;
            case R.id.reward_load:
                if (mediationInterstitialHandler != null) {
                    mediationInterstitialHandler.load();
                }
                break;
            case R.id.reward_show:
                if (mediationInterstitialHandler != null) {
                    mediationInterstitialHandler.show();
                }
                break;
            case R.id.reward_is_ready:
                if (mediationInterstitialHandler != null) {
                    Toast.makeText(InterstitialActivity.this,"ready:"+mediationInterstitialHandler.isReady(),Toast.LENGTH_LONG).show();
                }
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
