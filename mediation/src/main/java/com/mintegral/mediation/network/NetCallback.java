package com.mintegral.mediation.network;

/**
 * 网络请求回调
 *
 * @author hanliontien
 */
public interface NetCallback<T> {
    /**
     * 准备
     * 用于请求之前的loading等准备工作
     */
    void onPrepare();

    /**
     * 请求成功
     * @param response
     */
    void onSuccess(T response);

    /**
     * 请求失败
     */
    void onFail(Exception e);
}
