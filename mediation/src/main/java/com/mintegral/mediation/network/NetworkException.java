package com.mintegral.mediation.network;

/**
 * NetworkException
 *
 * @author hanliontien
 */
public class NetworkException extends Exception {
    public int errorCode;
    public String message;

    public NetworkException(int errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }
}
