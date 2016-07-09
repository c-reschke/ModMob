package com.crescoop.modmob.interfaces;

public interface WriteHoldingRegisterListner {

    void serviceSuccess(int result);

    void serviceFailure(Exception exception);
    void serviceUpdate(String msgHex);
}
