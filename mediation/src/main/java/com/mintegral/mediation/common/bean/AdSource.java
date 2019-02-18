package com.mintegral.mediation.common.bean;

import java.util.Map;

public class AdSource {
    private long timeOut;
    private String targetClass;
    private Map<String,Object>  localParams;
    private Map<String,String> serviceParams;

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public Map<String, Object> getLocalParams() {
        return localParams;
    }

    public void setLocalParams(Map<String, Object> localParams) {
        this.localParams = localParams;
    }

    public Map<String, String> getServiceParams() {
        return serviceParams;
    }

    public void setServiceParams(Map<String, String> serviceParams) {
        this.serviceParams = serviceParams;
    }
}
