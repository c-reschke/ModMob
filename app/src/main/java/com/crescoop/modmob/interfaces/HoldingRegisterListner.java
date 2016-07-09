package com.crescoop.modmob.interfaces;

public interface HoldingRegisterListner {

    void serviceSuccess(int result);

    void serviceFailure(Exception exception);
    void serviceUpdate(String msgHex);
}
