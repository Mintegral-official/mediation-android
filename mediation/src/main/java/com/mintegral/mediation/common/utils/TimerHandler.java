package com.mintegral.mediation.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.mintegral.mediation.common.manager.BaseManager;

import java.lang.ref.WeakReference;

public class TimerHandler extends Handler {

    private WeakReference<BaseManager> managerWeakReference;

    public TimerHandler(BaseManager mediationManager){
        super();
        managerWeakReference = new WeakReference<BaseManager>(mediationManager);

    }

    public TimerHandler(Looper looper){
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:

                if (managerWeakReference != null) {
                    BaseManager mediationManager = managerWeakReference.get();
                    if (mediationManager != null) {
                        if (!mediationManager.isLoadHadResult()) {
                            mediationManager.loadTimeout(mediationManager.getCurrentLoadParam());
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
