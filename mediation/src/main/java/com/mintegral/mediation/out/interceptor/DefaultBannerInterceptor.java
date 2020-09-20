package com.mintegral.mediation.out.interceptor;

import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;

import java.util.LinkedList;
import java.util.Map;

public class DefaultBannerInterceptor extends BaseInterceptor {

    @Override
   public LinkedList<AdSource> onInterceptor(String unitId, Map<String,Object> localParams, String serviceParams){
        LinkedList<AdSource> linkedList = new LinkedList<>();
        if(localParams != null){
            Object o = localParams.get("1");
            if(o instanceof AdSource){
                linkedList.add((AdSource) o);
            }
            Object o1 = localParams.get("2");
            if(o1 instanceof AdSource){
                linkedList.add((AdSource) o1);
            }
        }
       return linkedList;
   }
}
