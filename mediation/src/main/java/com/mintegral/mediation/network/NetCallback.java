package com.mintegral.mediation.network;

/**
 * Callback for network request.
 *
 * @author hanliontien
 */
public interface NetCallback<T> {
    /**
     * prepare
     * Preparations such as loading before requests.
     */
    void onPrepare();

    /**
     * request success.
     * @param response
     */
    void onSuccess(T response);

    /**
     * request fail.
     */
    void onFail(Exception e);
}
