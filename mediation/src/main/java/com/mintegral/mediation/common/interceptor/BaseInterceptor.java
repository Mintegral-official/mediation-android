package com.mintegral.mediation.common.interceptor;

import com.mintegral.mediation.common.bean.AdSource;

import java.util.LinkedList;
import java.util.Map;

public abstract class BaseInterceptor {
    public abstract LinkedList<AdSource> onInterceptor(String unitId, Map<String,Object> localParams, String serviceParams);
}
