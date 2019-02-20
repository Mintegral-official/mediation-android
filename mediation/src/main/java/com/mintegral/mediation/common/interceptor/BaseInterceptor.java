package com.mintegral.mediation.common.interceptor;

import com.mintegral.mediation.common.bean.AdSource;

import java.util.LinkedList;
import java.util.Map;

public abstract class BaseInterceptor {
    /**
     * it will prase localparams and service ,build adsource ,and return adapters queue. if you need interrupt the progress, you can throw a exception
     * @param unitId mediation unit id
     * @param localParams local params
     * @param serviceParams service setting string
     * @return adapters queue
     */
    public abstract LinkedList<AdSource> onInterceptor(String unitId, Map<String,Object> localParams, String serviceParams);
}
