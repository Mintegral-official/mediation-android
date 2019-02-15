package com.mintegral.mediation.common.listener;

public interface MediationAdapterInitListener {
    /**
     * 初始化成功
     */
    void onInitSucceed();

    /**
     * 初始化失败
     */
    void onInitFailed();
}
