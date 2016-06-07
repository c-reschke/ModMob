package com.crescoop.modmob.interfaces;

import com.crescoop.modmob.modbus.ModbusConfiguration;

public interface InterfaceSwap {

    void start(ModbusConfiguration modbusConfiguration);
    void stop();
    boolean isRunning();

}
