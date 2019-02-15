package com.mintegral.mediation.adapter.mtg;

import android.app.Activity;


import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.adapter.BaseRewardAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;

import java.util.Map;

public class MTGRewardAdapter extends BaseRewardAdapter {

    @Override
    public void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras) {

    }

    @Override
    public void setSDKRewardListener(MediationAdapterRewardListener mediationAdapterRewardListener) {

    }

    @Override
    public void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener) {

    }

    @Override
    public void show() {

    }

    @Override
    public void load(Activity activity, Map<String, Object> localExtras, Map<String, String> serverExtras) {

    }

    @Override
    public boolean isReady() {
        return false;
    }


    @Override
    public LifecycleListener getLifecycleListener() {
        return null;
    }
}
