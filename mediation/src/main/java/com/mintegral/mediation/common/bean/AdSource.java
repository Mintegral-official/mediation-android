package com.mintegral.mediation.common.bean;

public class AdSource {
    private long timeOut;
    private String targetClass;
    private String localParams;
    private String serviceParams;

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

    public String getLocalParams() {
        return localParams;
    }

    public void setLocalParams(String localParams) {
        this.localParams = localParams;
    }

    public String getServiceParams() {
        return serviceParams;
    }

    public void setServiceParams(String serviceParams) {
        this.serviceParams = serviceParams;
    }
}
