package com.mintegral.mediation.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.mintegral.mediation.common.manager.BaseManager;

import java.lang.ref.WeakReference;

public class TimerHandler extends Handler {

    private WeakReference<BaseManager> managerWeakReference;

    public TimerHandler(BaseManager mediationRewardManager){
        super();
        managerWeakReference = new WeakReference<BaseManager>(mediationRewardManager);

    }

    public TimerHandler(Looper looper){
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:

                if (managerWeakReference != null) {
                    BaseManager mediationRewardManager = managerWeakReference.get();
                    if (mediationRewardManager != null) {
                        if (!mediationRewardManager.isLoadHadResult()) {
                            mediationRewardManager.loadTimeout();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
