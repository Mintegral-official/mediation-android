package com.mintegral.mediation.out.interceptor;

import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;

import java.util.LinkedList;
import java.util.Map;

public class DefaultInterstitialInterceptor extends BaseInterceptor {

    @Override
   public LinkedList<AdSource> onInterceptor(String unitId, Map<String,Object> localParams, String serviceParams){
        LinkedList<AdSource> linkedList = new LinkedList<>();
        if(localParams != null){
            AdSource adSource = new AdSource();
            adSource.setLocalParams(localParams.get("local").toString());
            adSource.setServiceParams(serviceParams);
            adSource.setTargetClass(localParams.get("targetClass").toString());
            adSource.setTimeOut(1000);
            linkedList.add(adSource);
        }
       return linkedList;
   }
}
