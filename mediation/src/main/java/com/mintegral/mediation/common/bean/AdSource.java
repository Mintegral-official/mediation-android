package com.mintegral.mediation.common.bean;

import java.util.Map;

public class AdSource {
    /**
     * Maximum wait time of current AD source load
     */
    private long timeOut;

    /**
     * The full pathname of the class as the target adapter
     */
    private String targetClass;
    /**
     * Parameters passed into the adapter in the code
     */
    private Map<String,Object>  localParams;
    /**
     * Parameters passed into the adapter in the service
     */
    private Map<String,String> serviceParams;

    /**
     * get maximum wait time of current AD source load
     * @return maximum wait time
     */
    public long getTimeOut() {
        return timeOut;
    }

    /**
     * set maximum wait time of current AD source load
     * @param timeOut  maximum wait time
     */
    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * get the full pathname of the class as the target adapter
     * @return full pathname of the class
     */
    public String getTargetClass() {
        return targetClass;
    }

    /**
     * get the full pathname of the class as the target adapter
     * @return full pathname of the class
     */
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
