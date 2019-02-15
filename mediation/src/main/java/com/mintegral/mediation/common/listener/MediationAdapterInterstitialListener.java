package com.mintegral.mediation.common.listener;

/**
 * @author songjunjun
 */
public interface MediationAdapterInterstitialListener {

    void loadSucceed();
    void loadFailed(String msg);
    void showSucceed();
    void showFailed(String msg);
    void clicked(String msg);
    void closed();
}
