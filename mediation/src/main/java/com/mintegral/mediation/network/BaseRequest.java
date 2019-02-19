package com.mintegral.mediation.network;

/**
 * Reqest接口
 *
 * @author hanliontien
 */
public interface BaseRequest<T> {
    /**
     * 网络请求方法
     * 需自行实现，如HttpUrlConnection或者Okhttp
     * @return
     */
    T request() throws Exception;
}
