package com.mintegral.outofmediation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.listener.MediationAdapterBannerListener;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.out.handler.MediationBannerHandler;

import java.util.HashMap;
import java.util.Map;

public class BannerActivity extends Activity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();

    private Button showBtn, loadBtn, isReadyBtn, initBtn;
    private RelativeLayout adContainer;

    private MediationBannerHandler mediationBannerHandler;
    private LifecycleListener lifecycleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baner);
        initView();
        initListener();

    }

    private void initManager() {

        mediationBannerHandler = new MediationBannerHandler();

        mediationBannerHandler.setMediationAdapterInitListener(new MediationAdapterInitListener() {
            @Override
            public void onInitSucceed() {
                Toast.makeText(BannerActivity.this, "onInitSucceed", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onInitSucceed");
            }

            @Override
            public void onInitFailed() {
                Toast.makeText(BannerActivity.this, "onInitFailed", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onInitFailed");
            }
        });
        Map<String, Object> paramsMap = new HashMap<>();
        //IronSource
        final AdSource adSource = new AdSource();
        Map<String,Object> ironsourceMap = new HashMap<>();
        ironsourceMap.put("local","85460dcd");
        ironsourceMap.put(CommonConst.KEY_BANNER_SIZE_TYPE,1);
        adSource.setLocalParams(ironsourceMap);
        adSource.setTargetClass("com.mintegral.mediation.adapter.iron.IronBannerAdapter");
        adSource.setTimeOut(100000);
        paramsMap.put("2",adSource);
//        map.put("local","88f1a7f5");
//        map.put("targetClass","com.mintegral.mediation.adapter.iron.IronRewardAdapter");

        //MTG
        Map<String,Object> map = new HashMap<>();
        AdSource mtgAdSource = new AdSource();
        map.put(CommonConst.KEY_APP_ID, "118690");
        map.put(CommonConst.KEY_APP_KEY, "7c22942b749fe6a6e361b675e96b3ee9");
        map.put(CommonConst.KEY_BANNER_UNIT_ID, "146879");
        map.put(CommonConst.KEY_BANNER_REFRESH_TIME,10);
        map.put(CommonConst.KEY_BANNER_SIZE_TYPE,1);
        map.put(CommonConst.KEY_BANNER_ALLOW_SKIP,false);
        mtgAdSource.setLocalParams(map);
        mtgAdSource.setTargetClass("com.mintegral.mediation.adapter.mtg.MTGBannerAdapter");
        mtgAdSource.setTimeOut(20000);
        paramsMap.put("1",mtgAdSource);



        mediationBannerHandler.setMediationAdapterBannerListener(new MediationAdapterBannerListener() {
            @Override
            public void loadSucceed() {
                Toast.makeText(BannerActivity.this, "loadSucceed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void loadFailed(String msg) {
                Toast.makeText(BannerActivity.this, "loadFailed:" + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void showSucceed() {
                Toast.makeText(BannerActivity.this, "showSucceed:", Toast.LENGTH_LONG).show();
            }

            @Override
            public void showFailed(String msg) {
                Toast.makeText(BannerActivity.this, "showFailed:" + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void clicked(String msg) {
                Toast.makeText(BannerActivity.this, "clicked:" + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void closed() {
                Toast.makeText(BannerActivity.this, "closed:", Toast.LENGTH_LONG).show();
            }

        });
        mediationBannerHandler.init(this, paramsMap);
        lifecycleListener = mediationBannerHandler.getLifecycleListener();
    }

    private void initView() {
        showBtn = findViewById(R.id.banner_show);
        loadBtn = findViewById(R.id.banner_load);
        isReadyBtn = findViewById(R.id.banner_is_ready);
        initBtn = findViewById(R.id.banner_init);
        adContainer = findViewById(R.id.banner_ad_container);
    }

    private void initListener() {
        setViewListener(showBtn);
        setViewListener(loadBtn);
        setViewListener(isReadyBtn);
        setViewListener(initBtn);
    }

    private void setViewListener(View view) {
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner_init:
                initManager();
                break;
            case R.id.banner_load:
                if (mediationBannerHandler != null) {
                    mediationBannerHandler.load("");
                }
                break;
            case R.id.banner_show:
                if (mediationBannerHandler != null) {
                    mediationBannerHandler.showBanner("",adContainer);
                }
                break;
            case R.id.banner_is_ready:
                if (mediationBannerHandler != null) {
                    Toast.makeText(BannerActivity.this, "ready:" + mediationBannerHandler.isReady(""), Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lifecycleListener != null) {
            lifecycleListener.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lifecycleListener != null) {
            lifecycleListener.onResume(this);
        }
    }
}
