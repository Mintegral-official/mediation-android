package com.mintegral.mediation.network;

/**
 * Reqest interface
 *
 * @author hanliontien
 */
public interface BaseRequest<T> {
    /**
     * Network Request Method
     * u need to implement it, such as HttpUrlConnection or Okhttp.
     * @return
     */
    T request() throws Exception;
}
